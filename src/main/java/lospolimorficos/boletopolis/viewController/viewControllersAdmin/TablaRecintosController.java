package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.RecintoController;
import lospolimorficos.boletopolis.models.Ciudad;
import lospolimorficos.boletopolis.models.Recinto;

import java.io.IOException;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class TablaRecintosController {


    @FXML
    private TableColumn<Recinto, Integer> colCapacidad;
    @FXML
    private TableColumn<Recinto, String> colCiudad;
    @FXML
    private TableColumn<Recinto, String> colDireccion;
    @FXML
    private TableColumn<Recinto, String> colNombreRecinto;
    @FXML
    private TableColumn<Recinto, String> colRecintoId;
    @FXML
    private TableView<Recinto> tblRecinto;
    @FXML
    private TextField txtBuscarRecinto;
    @FXML
    private TextField txtCapacidad;
    @FXML
    private ComboBox<Ciudad> cmbCiudad;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;

    private final RecintoController recintoController = new RecintoController();

    @FXML
    public void initialize(){
        tblRecinto.getSelectionModel().selectedItemProperty().addListener((
                observable, oldValue, newValue) -> mostrarDatosRecinto(newValue));
        inicializarTabla();
        cargarRecintos();
    }

    private void inicializarTabla(){
        colNombreRecinto.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colCapacidad.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCapacidad()).asObject());
        colDireccion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDireccion()));
        colCiudad.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCiudad().toString()));
        colRecintoId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdRecinto().toString().substring(0, 8)));
    }

    private void cargarRecintos(){
        tblRecinto.setItems(recintoController.getRecintos());
    }

    @FXML
    private void LimpiarCampos() {
        limpiarCampos();
    }

    @FXML
    private void actualizarRecinto() {
        Recinto recintoSeleccionado = tblRecinto.getSelectionModel().getSelectedItem();
        if(recintoSeleccionado == null){
            mostrarAlertaError("Por favor seleccione un recinto para actualizar");
            return;
        }
        recintoSeleccionado.setNombre(txtNombre.getText());
        recintoSeleccionado.setDireccion(txtDireccion.getText());
        recintoSeleccionado.setCiudad(cmbCiudad.getValue());

        if(recintoController.actualizarRecinto(recintoSeleccionado)){
            limpiarCampos();
            cargarRecintos();
            tblRecinto.refresh();
            mostrarAlerta("Éxito", "Recinto Actualizado Éxitosamente", Alert.AlertType.INFORMATION  );
        }
    }

    @FXML
    private void eliminarRecinto() {
        Recinto recintoSeleccionado = tblRecinto.getSelectionModel().getSelectedItem();

        if(recintoSeleccionado == null){
            mostrarAlertaError("Por favor seleccione un recinto para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro que desea eliminar este recinto?");
        confirmacion.setContentText("Recinto: " + recintoSeleccionado.getNombre() + " - " + recintoSeleccionado.getIdRecinto());

        confirmacion.showAndWait().ifPresent(respuesta ->{
            if(respuesta == ButtonType.OK){
                if(recintoController.eliminarRecinto(recintoSeleccionado)){
                    cargarRecintos();
                    mostrarAlerta("Éxito", "Recinto Eliminado Éxitosamente", Alert.AlertType.INFORMATION  );
                }

            }
        });
    }

    @FXML
    private void abrirDetalleRecinto() {
        Recinto recintoSeleccionado = tblRecinto.getSelectionModel().getSelectedItem();

        if (recintoSeleccionado == null) {
            mostrarAlertaError("Por favor seleccione un recinto para ver detalles");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/detalleRecinto.fxml"));
            Parent root = loader.load();

            DetalleRecintoController controller = loader.getController();
            controller.setRecinto(recintoSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Detalle del Recinto: " + recintoSeleccionado.getNombre());
            stage.setScene(new Scene(root));
            stage.initOwner(tblRecinto.getScene().getWindow());
            stage.showAndWait();

            // Recargar tabla al cerrar por si hubo cambios (zonas eliminadas)
            tblRecinto.refresh();
            mostrarDatosRecinto(recintoSeleccionado);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("No se pudo abrir la vista de detalles");
        }
    }


    private void limpiarCampos(){
        txtId.clear();
        txtNombre.clear();
        txtDireccion.clear();
        cmbCiudad.getSelectionModel().clearSelection();
        txtCapacidad.clear();
    }

    private void mostrarDatosRecinto(Recinto recinto){
        if(recinto == null){
            limpiarCampos();
            return;
        }
        txtId.setText(recinto.getIdRecinto().toString());
        txtNombre.setText(recinto.getNombre());
        txtCapacidad.setText(String.valueOf(recinto.getCapacidad()));
        txtDireccion.setText(recinto.getDireccion());
        cmbCiudad.getSelectionModel().select(recinto.getCiudad());
    }
}
