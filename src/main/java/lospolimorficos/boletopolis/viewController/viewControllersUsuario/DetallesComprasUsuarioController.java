package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.ReporteUsuarioFacadeController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.services.GestorSesion;

import java.time.LocalDate;
import java.util.List;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

/**
 * Controlador del historial de compras del usuario.
 *
 * Permite:
 * - Consultar historial
 * - Filtrar compras
 * - Exportar reportes PDF/CSV
 * - Ver detalles de una compra
 */
public class DetallesComprasUsuarioController {

    // =========================
    // FILTROS
    // =========================

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private ComboBox<Evento> cbEventos;

    @FXML
    private ComboBox<EstadoCompra> cbEstado;

    // =========================
    // TABLA
    // =========================

    @FXML
    private TableView<Compra> tablaCompras;

    @FXML
    private TableColumn<Compra, LocalDate> colFecha;

    @FXML
    private TableColumn<Compra, String> colEvento;

    @FXML
    private TableColumn<Compra, Integer> colCantidad;

    @FXML
    private TableColumn<Compra, String> colTotal;

    @FXML
    private TableColumn<Compra, String> colMetodoPago;

    @FXML
    private TableColumn<Compra, EstadoCompra> colEstado;

    // =========================

    private Cliente clienteActual;

    private final EventoRepositorio eventoRepositorio =
            EventoRepositorio.getInstancia();

    private ReporteUsuarioFacadeController reporteController;

    @FXML
    public void initialize() {

        // =========================
        // CLIENTE LOGUEADO
        // =========================

        if (GestorSesion.getInstancia()
                .getUsuarioActual() instanceof Cliente cliente) {

            clienteActual = cliente;
        }

        reporteController =
                new ReporteUsuarioFacadeController();

        inicializarTabla();

        cargarCombos();

        cargarCompras();

        configurarClickTabla();
    }

    /**
     * Configura columnas de la tabla.
     */
    private void inicializarTabla() {

        colFecha.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(
                        cell.getValue()
                                .getFechaCompra()
                                .toLocalDate()
                )
        );

        colEvento.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue()
                                .getEvento()
                                .getNombre()
                )
        );

        colCantidad.setCellValueFactory(cell ->
                new SimpleIntegerProperty(
                        cell.getValue()
                                .getEntradas()
                                .size()
                ).asObject()
        );

        colTotal.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        "$" + String.format(
                                "%.2f",
                                cell.getValue()
                                        .getTotalCompra()
                        )
                )
        );

        colMetodoPago.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue()
                                .getPago()
                                .getMetodoPago()
                                .toString()
                )
        );

        colEstado.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(
                        cell.getValue()
                                .getEstadoCompra()
                )
        );
    }

    /**
     * Configura doble click sobre tabla.
     */
    private void configurarClickTabla() {

        tablaCompras.setRowFactory(tv -> {

            TableRow<Compra> row =
                    new TableRow<>();

            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2
                        && !row.isEmpty()) {

                    Compra compra =
                            row.getItem();

                    mostrarDetallesCompra(compra);
                }
            });

            return row;
        });
    }

    /**
     * Muestra ventana emergente
     * con detalles de la compra.
     */
    private void mostrarDetallesCompra(
            Compra compra
    ) {

        Stage ventana =
                new Stage();

        ventana.initModality(
                Modality.APPLICATION_MODAL
        );

        ventana.setTitle(
                "Detalles de la Compra"
        );

        VBox root = new VBox(12);

        root.setPadding(
                new Insets(20)
        );

        root.setStyle(
                "-fx-background-color: white;"
        );

        // =========================
        // INFORMACIÓN GENERAL
        // =========================

        Label lblTitulo =
                new Label(
                        "Detalles de la Compra"
                );

        lblTitulo.setStyle(
                "-fx-font-size: 20;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #163A36;"
        );

        Label lblIdPago =
                new Label(
                        "Id Pago: "
                                + compra.getPago().getIdPago().toString()
                );

        Label lblEvento =
                new Label(
                        "Evento: "
                                + compra.getEvento()
                                .getNombre()
                );

        Label lblFecha =
                new Label(
                        "Fecha: "
                                + compra.getFechaCompra()
                );

        Label lblEstado =
                new Label(
                        "Estado: "
                                + compra.getEstadoCompra()
                );

        Label lblMetodoPago =
                new Label(
                        "Método de Pago: "
                                + compra.getPago()
                                .getMetodoPago()
                );

        Label lblTotal =
                new Label(
                        "Total Pagado: $"
                                + String.format(
                                "%.2f",
                                compra.getTotalCompra()
                        )
                );

        // =========================
        // ENTRADAS
        // =========================

        Label lblEntradas =
                new Label(
                        "Entradas Compradas:"
                );

        lblEntradas.setStyle(
                "-fx-font-weight: bold;"
        );

        TextArea txtEntradas =
                new TextArea();

        txtEntradas.setEditable(false);

        txtEntradas.setPrefHeight(250);

        StringBuilder sb =
                new StringBuilder();

        int contador = 1;

        for (Entrada entrada :
                compra.getEntradas()) {

            sb.append("Entrada #")
                    .append(contador++)
                    .append("\n");

            sb.append("Zona: ")
                    .append(
                            entrada.getZona()
                                    .getNombre()
                    )
                    .append("\n");

            sb.append("Asiento: ")
                    .append(
                            entrada.getAsiento()
                                    .getIdAsiento()
                    )
                    .append("\n");

            sb.append("Precio: $")
                    .append(
                            String.format(
                                    "%.2f",
                                    entrada.getPrecioFinal()
                            )
                    )
                    .append("\n");

            sb.append("--------------------------------\n");
        }

        txtEntradas.setText(
                sb.toString()
        );

        // =========================
        // BOTÓN CERRAR
        // =========================

        Button btnCerrar =
                new Button("Cerrar");

        btnCerrar.setStyle(
                "-fx-background-color: #2E7D5A;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        btnCerrar.setOnAction(
                e -> ventana.close()
        );

        // =========================

        root.getChildren().addAll(
                lblTitulo,
                lblIdPago,
                lblEvento,
                lblFecha,
                lblEstado,
                lblMetodoPago,
                lblTotal,
                new Separator(),
                lblEntradas,
                txtEntradas,
                btnCerrar
        );

        Scene scene =
                new Scene(root, 500, 600);

        ventana.setScene(scene);

        ventana.showAndWait();
    }

    /**
     * Carga eventos y estados.
     */
    private void cargarCombos() {

        cbEventos.getItems().addAll(
                eventoRepositorio.getEventos()
        );

        cbEstado.getItems().addAll(
                EstadoCompra.values()
        );
    }

    /**
     * Carga compras del usuario.
     */
    private void cargarCompras() {

        if (clienteActual == null) {
            return;
        }

        tablaCompras.getItems().clear();

        List<Compra> compras =
                clienteActual.getCompras();

        tablaCompras.getItems().addAll(
                compras
        );
    }

    /**
     * Filtrar compras.
     */
    @FXML
    private void filtrarCompras() {

        if (clienteActual == null) {
            return;
        }

        LocalDate fechaInicio =
                dpFechaInicio.getValue();

        LocalDate fechaFin =
                dpFechaFin.getValue();

        Evento evento =
                cbEventos.getValue();

        EstadoCompra estado =
                cbEstado.getValue();

        tablaCompras.getItems().clear();

        List<Compra> compras =
                clienteActual.getCompras();

        for (Compra compra : compras) {

            boolean cumple = true;

            // FILTRO FECHA

            if (fechaInicio != null &&
                    fechaFin != null) {

                LocalDate fechaCompra =
                        compra.getFechaCompra()
                                .toLocalDate();

                if (fechaCompra.isBefore(fechaInicio)
                        || fechaCompra.isAfter(fechaFin)) {

                    cumple = false;
                }
            }

            // FILTRO EVENTO

            if (evento != null &&
                    !compra.getEvento()
                            .equals(evento)) {

                cumple = false;
            }

            // FILTRO ESTADO

            if (estado != null &&
                    compra.getEstadoCompra()
                            != estado) {

                cumple = false;
            }

            if (cumple) {

                tablaCompras.getItems()
                        .add(compra);
            }
        }

        if (tablaCompras.getItems()
                .isEmpty()) {

            mostrarAlerta(
                    "Sin resultados",
                    "No se encontraron compras con esos filtros.",
                    Alert.AlertType.INFORMATION
            );
        }
    }

    /**
     * Limpia filtros.
     */
    @FXML
    private void limpiarFiltros() {

        dpFechaInicio.setValue(null);

        dpFechaFin.setValue(null);

        cbEventos.getSelectionModel()
                .clearSelection();

        cbEstado.getSelectionModel()
                .clearSelection();

        cargarCompras();
    }

    /**
     * Descargar CSV.
     */
    @FXML
    private void descargarCSV() {

        if (clienteActual == null) {

            mostrarAlertaError(
                    "No hay un cliente autenticado."
            );

            return;
        }

        String ruta =
                reporteController.exportarCSV(
                        clienteActual,
                        dpFechaInicio.getValue(),
                        dpFechaFin.getValue(),
                        cbEventos.getValue(),
                        cbEstado.getValue()
                );

        mostrarAlerta(
                "Reporte generado",
                "Reporte CSV exportado en:\n" + ruta,
                Alert.AlertType.INFORMATION
        );
    }

    /**
     * Descargar PDF.
     */
    @FXML
    private void descargarPDF() {

        if (clienteActual == null) {

            mostrarAlertaError(
                    "No hay un cliente autenticado."
            );

            return;
        }

        String ruta =
                reporteController.exportarPDF(
                        clienteActual,
                        dpFechaInicio.getValue(),
                        dpFechaFin.getValue(),
                        cbEventos.getValue(),
                        cbEstado.getValue()
                );

        mostrarAlerta(
                "Reporte generado",
                "Reporte PDF exportado en:\n" + ruta,
                Alert.AlertType.INFORMATION
        );
    }
}