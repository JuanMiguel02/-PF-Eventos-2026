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
    private TableColumn<Compra, String> colFecha;
    @FXML
    private TableColumn<Compra, String> colTotal;
    @FXML
    private Button btnVerDetalles;

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
                cellData.getValue().getCliente().getNombre() + " (" + cellData.getValue().getCliente().getNumDocumento() + ")"
        ));
        colEvento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().getNombre()));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaCompra().toString()));
        colTotal.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$%.2f", cellData.getValue().getTotalCompra())));

        tblCompras.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> btnVerDetalles.setDisable(newSelection == null));
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
            // Implementación futura de detalles
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Detalles de Compra");
            alert.setHeaderText("ID: " + seleccionada.getIdCompra());
            alert.setContentText("Cliente: " + seleccionada.getCliente().getNombre() + "\nEvento: " + seleccionada.getEvento().getNombre() + "\nTotal: " + seleccionada.getTotalCompra());
            alert.showAndWait();
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
