package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.models.Admin;
import lospolimorficos.boletopolis.models.Usuario;
import lospolimorficos.boletopolis.services.GestorNavegacion;

import lospolimorficos.boletopolis.services.GestorSesion;
import lospolimorficos.boletopolis.services.ServicioAlerta;
import lospolimorficos.boletopolis.services.ServicioAutenticacion;

public class LoginController {
    private final ServicioAutenticacion servicioAutenticacion =
            new ServicioAutenticacion();

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPasswordVisible;

    private boolean mostrandoPassword = false;

    @FXML
    public void iniciarSesion(){
        // Paso 1: Obtener el correo ingresado por el usuario.
        String correo = txtCorreo.getText().trim();

        // Paso 2: Obtener la contraseña dependiendo
        // de si está visible o no.
        String password;

        if (mostrandoPassword) {
            password = txtPasswordVisible.getText().trim();
        } else {
            password = txtPassword.getText().trim();
        }

        // Paso 3: Validar que los campos no estén vacíos.
        if (correo.isEmpty() || password.isEmpty()) {

            ServicioAlerta.mostrarAlertaError(
                    "Debes completar todos los campos."
            );

            return;
        }

        // Paso 4: Intentar autenticar el usuario.
        Usuario usuarioAutenticado =
                servicioAutenticacion.iniciarSesion(correo, password);

        // Paso 5: Validar si las credenciales son incorrectas.
        if (usuarioAutenticado == null) {

            ServicioAlerta.mostrarAlertaError(
                    "Correo o contraseña incorrectos."
            );

            return;
        }

        // Paso 6: Guardar la sesión activa del usuario.
        GestorSesion.getInstancia()
                .setUsuarioActual(usuarioAutenticado);

        // Paso 7: Obtener la ventana actual.
        Stage stage =
                (Stage) txtCorreo.getScene().getWindow();

        // Paso 8: Redireccionar según el tipo de usuario.
        if (usuarioAutenticado instanceof Admin) {

            GestorNavegacion.cambiarVista(
                    stage,
                    "/lospolimorficos/boletopolis/views/adminViews/dashboardAdmin.fxml"
            );

        } else {

            GestorNavegacion.cambiarVista(
                    stage,
                    "/lospolimorficos/boletopolis/view.userViews/dashboardUsuario.fxml"
            );
        }


    }

    @FXML
    public void mostrarPassword(){

        if(mostrandoPassword){

            txtPassword.setText(txtPasswordVisible.getText());

            txtPassword.setVisible(true);
            txtPassword.setManaged(true);

            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);

        }else{

            txtPasswordVisible.setText(txtPassword.getText());

            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);

            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
        }

        mostrandoPassword = !mostrandoPassword;
    }
    @FXML
    public void cerrar() {
        System.exit(0);
    }

    @FXML
    public void minimizar() {
        Stage stage = (Stage) txtCorreo.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleRegistrar() {
        Stage stage = (Stage) txtCorreo.getScene().getWindow();
        GestorNavegacion.cambiarVista(
                stage,
                "/lospolimorficos/boletopolis/view.userViews/registroCliente.fxml"
        );
    }
}
