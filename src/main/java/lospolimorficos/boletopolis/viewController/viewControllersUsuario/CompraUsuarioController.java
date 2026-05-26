package lospolimorficos.boletopolis.viewController.viewControllersUsuario;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.CompraController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.services.GestorSesion;
import lospolimorficos.boletopolis.services.ServicioAlerta;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;
import lospolimorficos.boletopolis.viewController.viewControllersCompartidos.ServiciosAdicionalesController;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompraUsuarioController {

    @FXML
    private Label lblEvento;
    @FXML
    private AnchorPane panelMapa;
    @FXML
    private Label lblResumen;
    @FXML
    private ComboBox<MetodoPago> cbMetodosPago;
    @FXML
    private CheckBox chkNotificaciones;
    @FXML
    private TableView<Zona> tablaAforo;
    @FXML
    private TableColumn<Zona, String> colZonaNombre;
    @FXML
    private TableColumn<Zona, Integer> colOcupacion;
    @FXML
    private TableColumn<Zona, Integer> colZonaCapacidad;

    private Evento eventoSeleccionado;
    private Cliente clienteActual;
    private ServicioDibujoRecinto servicioDibujo;
    private final CompraController compraController = new CompraController();
    private final Map<Asiento, Zona> zonaAsientoMap = new HashMap<>();

    @FXML
    public void initialize() {
        if (GestorSesion.getInstancia().getUsuarioActual() instanceof Cliente cliente) {
            clienteActual = cliente;
            cbMetodosPago.getItems().addAll(cliente.getMetodosPago());
            if (!cbMetodosPago.getItems().isEmpty()) {
                cbMetodosPago.getSelectionModel().selectFirst();
            }
        }
        inicializarTabla();
    }

    private void inicializarTabla() {
        colZonaNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colOcupacion.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().calcularOcupacion()).asObject());
        colZonaCapacidad.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCapacidad()).asObject());
    }

    public void setEvento(Evento evento) {
        this.eventoSeleccionado = evento;
        lblEvento.setText("Comprando entradas para: " + evento.getNombre());

        this.servicioDibujo = new ServicioDibujoRecinto(panelMapa);
        this.servicioDibujo.setStrategy(new CompraInteraccionStrategy());
        this.servicioDibujo.setInteractivo(true);

        this.servicioDibujo.setOnAsientoChanged(() -> {
            actualizarResumen();
            tablaAforo.refresh();
        });

        cargarZonas();
        renderizarMapa();
    }

    private void cargarZonas() {
        if (eventoSeleccionado != null && eventoSeleccionado.getRecinto() != null) {
            tablaAforo.setItems(FXCollections.observableArrayList(eventoSeleccionado.getRecinto().getZonas()));
        }
    }

    private void renderizarMapa() {
        if (eventoSeleccionado == null || eventoSeleccionado.getRecinto() == null) return;

        zonaAsientoMap.clear();
        for (Zona zona : eventoSeleccionado.getRecinto().getZonas()) {
            for (Asiento asiento : zona.getAsientos()) {
                zonaAsientoMap.put(asiento, zona);
            }
        }

        Platform.runLater(() -> {
            servicioDibujo.renderizar(eventoSeleccionado.getRecinto().getEscenario(), eventoSeleccionado.getRecinto().getZonas());
        });
    }

    private void actualizarResumen() {
        List<Asiento> seleccionados = servicioDibujo.getAsientosSeleccionados();
        double total = 0;
        for (Asiento a : seleccionados) {
            total += zonaAsientoMap.get(a).getPrecioZona();
        }
        lblResumen.setText(String.format("Asientos: %d | Total: $%.2f", seleccionados.size(), total));
    }

    @FXML
    private void finalizarCompra() {
        List<Asiento> seleccionados = servicioDibujo.getAsientosSeleccionados();
        MetodoPago metodoPago = cbMetodosPago.getValue();

        if (seleccionados.isEmpty()) {
            ServicioAlerta.mostrarAlertaError("Debe seleccionar al menos un asiento.");
            return;
        }

        if (metodoPago == null) {
            ServicioAlerta.mostrarAlertaError("Debe seleccionar un método de pago.");
            return;
        }

        Compra compra = compraController.realizarCompra(clienteActual, eventoSeleccionado, seleccionados, zonaAsientoMap, metodoPago);

        if (compra != null && compraController.registrarCompra(compra)) {
            if (chkNotificaciones.isSelected()) {
                eventoSeleccionado.agregarObservador(clienteActual);
            }

            try {
                // 1. Cargar la vista
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/ServiciosAdicionalesView.fxml"));
                Parent root = loader.load();

                // 2. Obtener el controlador reutilizable
                ServiciosAdicionalesController serviciosController = loader.getController();

                // 3. Crear el Stage de manera Modal
                Stage ventanaServicios = new Stage();
                ventanaServicios.initModality(Modality.APPLICATION_MODAL);
                ventanaServicios.setTitle("Personaliza tu Experiencia - Servicios Adicionales");
                ventanaServicios.setScene(new Scene(root));

                // 4. Pasar los datos y definir el Runnable (lo que pasará al dar "Confirmar")
                serviciosController.setCompra(compra, () -> {
                    // Este código se ejecuta cuando el usuario da clic en "Confirmar Servicios"
                    // Aquí puedes actualizar la base de datos o el archivo de la compra si es necesario
                    System.out.println("Servicios adicionales actualizados para la compra.");

                    // Opcional: Mostrar ahora sí una alerta de éxito final con el total definitivo
                    mostrarResumenEntradas(compra);
                });

                // 5. Bloquear la pantalla principal hasta que cierre esta
                ventanaServicios.showAndWait();

            } catch (IOException e) {
                System.err.println("Error al cargar la vista de servicios adicionales: " + e.getMessage());
                e.printStackTrace();
            }
            // -----------------------------------------------------------

            // Limpiamos selección y refrescamos componentes principales
            servicioDibujo.limpiarSeleccion();
            tablaAforo.refresh();
            actualizarResumen();
        }
    }

    private void mostrarResumenEntradas(Compra compra) {
        // 1. Crear el contenedor secundario (Stage)
        Stage ventanaRecibo = new Stage();
        ventanaRecibo.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana de atrás
        ventanaRecibo.setTitle("Boletópolis - Recibo de Compra");
        ventanaRecibo.setResizable(false);

        // 2. Armar el texto del recibo
        StringBuilder sb = new StringBuilder();
        sb.append("¡Gracias por tu compra, ").append(clienteActual.getNombre()).append("!\n\n");
        sb.append("Detalle del Evento:\n");
        sb.append("--------------------------------------------------\n");
        sb.append("Evento: ").append(compra.getEvento().getNombre()).append("\n");
        sb.append("Fecha de Compra: ").append(compra.getFechaCompra()).append("\n\n");
        sb.append("Tus Entradas:\n");

        for (Entrada entrada : compra.getEntradas()) {
            sb.append("• Zona: ").append(entrada.getZona().getNombre())
                    .append(" | Asiento: ").append(entrada.getAsiento().getIdAsiento())
                    .append(" | Valor: $").append(String.format("%.2f", entrada.getPrecioFinal()))
                    .append("\n");
        }
        sb.append("--------------------------------------------------\n");
        sb.append("Total Pagado: $").append(String.format("%.2f", compra.getTotalCompra()));

        // 3. Crear componentes visuales del Stage transitorio
        Label lblTitulo = new Label("Comprobante electrónico de entrada");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #163A36;");

        TextArea txtRecibo = new TextArea(sb.toString());
        txtRecibo.setEditable(false);
        txtRecibo.setWrapText(true);
        txtRecibo.setPrefSize(400, 250);
        txtRecibo.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px;");

        Button btnCerrar = new Button("Entendido / Cerrar");
        btnCerrar.setStyle("-fx-background-color: #163A36; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16 8 16; -fx-background-radius: 6;");
        btnCerrar.setOnAction(e -> ventanaRecibo.close());

        // 4. Layout y Escena
        VBox layoutRecibo = new VBox(15);
        layoutRecibo.setPadding(new Insets(20));
        layoutRecibo.setAlignment(Pos.CENTER);
        layoutRecibo.setStyle("-fx-background-color: #FFFFFF;");
        layoutRecibo.getChildren().addAll(lblTitulo, txtRecibo, btnCerrar);

        Scene escena = new Scene(layoutRecibo);
        ventanaRecibo.setScene(escena);

        // 5. Mostrar y esperar a que el usuario interactúe
        ventanaRecibo.showAndWait();
    }
}