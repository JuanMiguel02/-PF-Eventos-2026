package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import lospolimorficos.boletopolis.controller.CompraController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.services.GestorSesion;
import lospolimorficos.boletopolis.services.ServicioAlerta;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;

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
    private Label lblTituloEntradas;

    @FXML
    private TextArea txtResumenEntradas;

    // =========================
    // TABLA AFORO
    // =========================

    @FXML
    private TableView<Zona> tablaAforo;

    @FXML
    private TableColumn<Zona, String> colZonaNombre;

    @FXML
    private TableColumn<Zona, Integer> colOcupacion;

    @FXML
    private TableColumn<Zona, Integer> colZonaCapacidad;

    // =========================

    private Evento eventoSeleccionado;

    private Cliente clienteActual;

    private ServicioDibujoRecinto servicioDibujo;

    private final CompraController compraController =
            new CompraController();

    private final Map<Asiento, Zona> zonaAsientoMap =
            new HashMap<>();

    @FXML
    public void initialize() {

        if (GestorSesion.getInstancia().getUsuarioActual() instanceof Cliente cliente) {

            clienteActual = cliente;

            cbMetodosPago.getItems().addAll(
                    cliente.getMetodosPago()
            );

            if (!cbMetodosPago.getItems().isEmpty()) {

                cbMetodosPago.getSelectionModel().selectFirst();
            }
        }

        servicioDibujo = new ServicioDibujoRecinto(panelMapa);

        servicioDibujo.setStrategy(
                new CompraInteraccionStrategy()
        );

        servicioDibujo.setInteractivo(true);

        servicioDibujo.setOnAsientoChanged(
                this::actualizarResumen
        );

        inicializarTabla();
    }

    /**
     * Inicializa la tabla de aforo.
     */
    private void inicializarTabla() {

        colZonaNombre.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        cell.getValue().getNombre()
                )
        );

        colOcupacion.setCellValueFactory(
                cell -> new SimpleIntegerProperty(
                        cell.getValue().calcularOcupacion()
                ).asObject()
        );

        colZonaCapacidad.setCellValueFactory(
                cell -> new SimpleIntegerProperty(
                        cell.getValue().getCapacidad()
                ).asObject()
        );
    }

    /**
     * Configura el evento.
     */
    public void setEvento(Evento evento) {

        this.eventoSeleccionado = evento;

        lblEvento.setText(
                "Comprando entradas para: "
                        + evento.getNombre()
        );

        renderizarMapa();

        cargarTablaAforo();
    }

    /**
     * Carga las zonas en la tabla.
     */
    private void cargarTablaAforo() {

        if (eventoSeleccionado == null ||
                eventoSeleccionado.getRecinto() == null) {
            return;
        }

        tablaAforo.getItems().clear();

        tablaAforo.getItems().addAll(
                eventoSeleccionado
                        .getRecinto()
                        .getZonas()
        );
    }

    /**
     * Renderiza el mapa del recinto.
     */
    private void renderizarMapa() {

        if (eventoSeleccionado == null ||
                eventoSeleccionado.getRecinto() == null) {
            return;
        }

        zonaAsientoMap.clear();

        for (Zona zona : eventoSeleccionado
                .getRecinto()
                .getZonas()) {

            for (Asiento asiento : zona.getAsientos()) {

                zonaAsientoMap.put(asiento, zona);
            }
        }

        Platform.runLater(() -> {

            servicioDibujo.renderizar(
                    eventoSeleccionado
                            .getRecinto()
                            .getEscenario(),

                    eventoSeleccionado
                            .getRecinto()
                            .getZonas()
            );
        });
    }

    /**
     * Actualiza resumen de compra.
     */
    private void actualizarResumen() {

        List<Asiento> seleccionados =
                servicioDibujo.getAsientosSeleccionados();

        double total = 0;

        for (Asiento asiento : seleccionados) {

            total += zonaAsientoMap
                    .get(asiento)
                    .getPrecioZona();
        }

        lblResumen.setText(
                String.format(
                        "Asientos: %d | Total: $%.2f",
                        seleccionados.size(),
                        total
                )
        );

        // Refrescar ocupación
        tablaAforo.refresh();
    }

    /**
     * Finaliza la compra.
     */
    @FXML
    private void finalizarCompra() {

        List<Asiento> seleccionados =
                servicioDibujo.getAsientosSeleccionados();

        MetodoPago metodoPago =
                cbMetodosPago.getValue();

        if (seleccionados.isEmpty()) {

            ServicioAlerta.mostrarAlertaError(
                    "Debe seleccionar al menos un asiento."
            );

            return;
        }

        if (metodoPago == null) {

            ServicioAlerta.mostrarAlertaError(
                    "Debe seleccionar un método de pago."
            );

            return;
        }

        Compra compra = compraController.realizarCompra(
                clienteActual,
                eventoSeleccionado,
                seleccionados,
                zonaAsientoMap,
                metodoPago
        );

        if (compra != null &&
                compraController.registrarCompra(compra)) {

            // Suscripción a notificaciones
            if (chkNotificaciones.isSelected()) {

                eventoSeleccionado.agregarObservador(
                        clienteActual
                );
            }

            ServicioAlerta.mostrarAlerta(
                    "Éxito",
                    "Compra realizada correctamente.",
                    Alert.AlertType.INFORMATION
            );

            mostrarResumenEntradas(compra);

            // Actualizar tabla
            tablaAforo.refresh();

        } else {

            ServicioAlerta.mostrarAlertaError(
                    "No se pudo completar la compra. Verifique su saldo."
            );
        }
    }

    /**
     * Muestra resumen de entradas.
     */
    private void mostrarResumenEntradas(Compra compra) {

        StringBuilder sb = new StringBuilder();

        sb.append("Resumen de Compra:\n");

        sb.append("Evento: ")
                .append(compra.getEvento().getNombre())
                .append("\n");

        sb.append("Fecha: ")
                .append(compra.getFechaCompra())
                .append("\n\n");

        sb.append("Entradas:\n");

        for (Entrada entrada : compra.getEntradas()) {

            sb.append("- Zona: ")
                    .append(entrada.getZona().getNombre())

                    .append(" | Asiento: ")
                    .append(entrada.getAsiento().getIdAsiento())

                    .append(" | Precio: $")
                    .append(String.format(
                            "%.2f",
                            entrada.getPrecioFinal()
                    ))

                    .append("\n");
        }

        sb.append("\nTotal Pagado: $")
                .append(String.format(
                        "%.2f",
                        compra.getTotalCompra()
                ));

        txtResumenEntradas.setText(
                sb.toString()
        );

        txtResumenEntradas.setVisible(true);

        lblTituloEntradas.setVisible(true);
    }
}