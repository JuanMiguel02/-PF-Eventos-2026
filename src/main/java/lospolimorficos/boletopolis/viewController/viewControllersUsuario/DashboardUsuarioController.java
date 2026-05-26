package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import lospolimorficos.boletopolis.models.Usuario;

import lospolimorficos.boletopolis.services.GestorNavegacion;
import lospolimorficos.boletopolis.services.GestorSesion;

import java.io.IOException;

public class DashboardUsuarioController {

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private StackPane contentPane;

    /**
     * Inicializa el dashboard.
     */
    @FXML
    public void initialize() {

        cargarDatosUsuario();

        mostrarInicio();
    }

    /**
     * Carga la información
     * del usuario actual.
     */
    private void cargarDatosUsuario() {

        Usuario usuario =
                GestorSesion.getInstancia()
                        .getUsuarioActual();

        if(usuario != null) {

            lblNombreUsuario.setText(
                    "Hola, " +
                            usuario.getNombre() +
                            " 👋"
            );
        }
    }

    /**
     * Muestra la vista inicial.
     */
    @FXML
    private void mostrarInicio() {

        cargarVista(
                "/lospolimorficos/boletopolis/view.userViews/inicioUsuario.fxml"
        );
    }

    /**
     * Muestra la vista de eventos.
     */
    @FXML
    private void mostrarEventos() {
        cargarVista("/lospolimorficos/boletopolis/view.userViews/eventosUsuario.fxml");
    }

    /**
     * Muestra compras.
     */
    @FXML
    private void mostrarCompras() {
        cargarVista("/lospolimorficos/boletopolis/view.userViews/metodosPago.fxml");
    }

    /**
     * Muestra métodos de pago.
     */
    @FXML
    private void mostrarMetodosPago() {
        cargarVista("/lospolimorficos/boletopolis/view.userViews/metodosPago.fxml");
    }

    /**
     * Muestra perfil.
     */
    @FXML
    private void mostrarPerfil() {
        cargarVista("/lospolimorficos/boletopolis/view.userViews/perfilUsuario.fxml");
    }

    /**
     * Cierra la sesión actual.
     */
    @FXML
    private void cerrarSesion() {
        GestorSesion.getInstancia().cerrarSesion();
        Stage stage = (Stage) contentPane.getScene().getWindow();

        GestorNavegacion.cambiarVista(stage, "/lospolimorficos/boletopolis/view.userViews/loginView.fxml");
    }

    /**
     * Minimiza la ventana.
     */
    @FXML
    private void minimizar() {
        Stage stage = (Stage) contentPane.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Cierra la aplicación.
     */
    @FXML
    private void cerrar() {
        System.exit(0);
    }

    /**
     * Carga una vista en el panel central.
     *
     * @param rutaFXML Ruta del FXML.
     */
    private void cargarVista(String rutaFXML) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent vista = loader.load();
            Object controller = loader.getController();
            if(controller instanceof InicioUsuarioController inicioController){
                inicioController.setContentPane(contentPane);
            }
            contentPane.getChildren().clear();
            contentPane.getChildren().add(vista);
        } catch (IOException e) {System.err.println(e.getMessage());
        }
    }
}