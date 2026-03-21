package lospolimorficos.boletopolis.viewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class DashboardAdminController {
    @FXML
    private StackPane contenedorCentro;
    @FXML
    private AnchorPane vistaInicio;

    public void cargarVista(String fxml){
        try{
            Parent vista = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            contenedorCentro.getChildren().setAll(vista);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void cargarVistaClientes(){
        cargarVista("/lospolimorficos/boletopolis/views/adminViews/tablaClientes.fxml");
    }

    @FXML
    private void cargarVistaEventos(){

    }

    @FXML
    private void cargarVistaRecintos(){

    }

    @FXML
    private void cargarVistaInicio(){
        contenedorCentro.getChildren().clear();
        vistaInicio.setVisible(true);
        vistaInicio.setManaged(true);
        contenedorCentro.getChildren().add(vistaInicio);
    }

    public void cerrar(){
        System.exit(0);
    }

    public void minimizar() {
        Stage stage = (Stage) contenedorCentro.getScene().getWindow();
        stage.setIconified(true);

    }
}
