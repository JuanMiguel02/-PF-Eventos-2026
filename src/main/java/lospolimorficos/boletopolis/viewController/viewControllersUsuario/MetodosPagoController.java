package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.models.Compra;
import lospolimorficos.boletopolis.models.MetodoPago;
import lospolimorficos.boletopolis.services.GestorSesion;
import lospolimorficos.boletopolis.services.ServicioAlerta;

public class MetodosPagoController {

    @FXML
    private ListView<MetodoPago> listMetodosPago;
    @FXML
    private TableView<Compra> tblHistorialCompras;
    @FXML
    private TableColumn<Compra, String> colEvento;
    @FXML
    private TableColumn<Compra, String> colFecha;
    @FXML
    private TableColumn<Compra, String> colTotal;
    @FXML
    private TableColumn<Compra, String> colEstado;

    private Cliente clienteActual;

    @FXML
    public void initialize() {
        if (GestorSesion.getInstancia().getUsuarioActual() instanceof Cliente cliente) {
            clienteActual = cliente;
            configurarTablas();
            cargarDatos();
        }
    }

    private void configurarTablas() {
        if (colEvento == null) return;

        colEvento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().getNombre()));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaCompra().toString()));
        colTotal.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$%.2f", cellData.getValue().getTotalCompra())));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstadoCompra().toString()));

        listMetodosPago.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(MetodoPago item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescripcion() + " - Saldo: $" + String.format("%.2f", item.getSaldoDisponible()));
                }
            }
        });
    }

    private void cargarDatos() {
        if (clienteActual != null) {
            listMetodosPago.setItems(FXCollections.observableArrayList(clienteActual.getMetodosPago()));
            tblHistorialCompras.setItems(FXCollections.observableArrayList(clienteActual.getCompras()));
        }
    }

    @FXML
    private void eliminarMetodoPago() {
        MetodoPago seleccionado = listMetodosPago.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            clienteActual.getMetodosPago().remove(seleccionado);
            cargarDatos();
            ServicioAlerta.mostrarAlerta("Éxito", "Método de pago eliminado.", Alert.AlertType.INFORMATION);
        } else {
            ServicioAlerta.mostrarAlertaError("Seleccione un método de pago para eliminar.");
        }
    }
}
