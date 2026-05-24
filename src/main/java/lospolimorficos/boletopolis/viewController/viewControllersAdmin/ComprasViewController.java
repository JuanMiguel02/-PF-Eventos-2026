package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.CompraController;
import lospolimorficos.boletopolis.models.Compra;

import java.io.IOException;
import java.util.List;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class ComprasViewController {

    @FXML
    private TextField txtBusqueda;
    @FXML
    private TableView<Compra> tblCompras;
    @FXML
    private TableColumn<Compra, String> colId;
    @FXML
    private TableColumn<Compra, String> colCliente;
    @FXML
    private TableColumn<Compra, String> colEvento;
    @FXML
    private TableColumn<Compra, String> colReembolsable;
    @FXML
    private TableColumn<Compra, String> colEstado;
    @FXML
    private TableColumn<Compra, String> colFecha;
    @FXML
    private TableColumn<Compra, String> colTotal;
    @FXML
    private Button btnVerDetalles;
    @FXML
    private Button btnReembolsar;

    private final CompraController compraController = new CompraController();
    private List<Compra> comprasFiltradas;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarDatos();
        configurarFiltros();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdCompra().toString()));
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente().getNombreCompleto() + " (" + cellData.getValue().getCliente().getDocumento() + ")"
        ));
        colEvento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().getNombre()));
        colReembolsable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().permiteReembolso() ? "Sí" : "No"));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstadoCompra().toString()));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaCompra().toString()));
        colTotal.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$%.2f", cellData.getValue().getTotalCompra())));

        tblCompras.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnVerDetalles.setDisable(newSelection == null);
            btnReembolsar.setDisable(newSelection == null || !newSelection.getEvento().permiteReembolso() || newSelection.getEstadoCompra() == lospolimorficos.boletopolis.models.EstadoCompra.REEMBOLSADA);
        });
    }

    private void cargarDatos() {
        tblCompras.setItems(compraController.getCompras());
    }

    private void configurarFiltros() {
        txtBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            comprasFiltradas = compraController.filtrarCompras(compraController.getCompras(), newValue);
            tblCompras.setItems(FXCollections.observableArrayList(comprasFiltradas));
        });
    }

    @FXML
    private void limpiarFiltros() {
        txtBusqueda.clear();
    }

    @FXML
    private void verDetalles() {
        Compra seleccionada = tblCompras.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/resumenCompraView.fxml"));
                Parent root = loader.load();

                ResumenCompraController controller = loader.getController();
                controller.setCompra(seleccionada, "Detalles de la Compra");

                Stage stage = new Stage();
                stage.setTitle("Detalles de Compra - " + seleccionada.getIdCompra());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(tblCompras.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                mostrarAlertaError("No se pudo abrir el resumen de la compra");
            }
        }
    }

    @FXML
    private void reembolsar() {
        Compra seleccionada = tblCompras.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Reembolso");
            confirmacion.setHeaderText("¿Está seguro de reembolsar esta compra?");
            confirmacion.setContentText("Esta acción devolverá el dinero al cliente y liberará los asientos.");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (compraController.reembolsarCompra(seleccionada)) {
                    Alert exito = new Alert(Alert.AlertType.INFORMATION);
                    exito.setTitle("Reembolso Exitoso");
                    exito.setHeaderText(null);
                    exito.setContentText("El reembolso se ha procesado correctamente.");
                    exito.showAndWait();
                    tblCompras.refresh();
                } else {
                    mostrarAlertaError("No se pudo procesar el reembolso. Verifique los fondos de la empresa o el estado del pago.");
                }
            }
        }
    }

    @FXML
    private void nuevaCompra() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/nuevaCompraView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nueva Compra");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tblCompras.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
        } catch (IOException e) {
            mostrarAlertaError("No se pudo abrir el formulario de compra");
        }
    }
}
