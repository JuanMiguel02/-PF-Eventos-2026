package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import lospolimorficos.boletopolis.controller.ClienteController;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.services.ServicioAlerta;

import java.io.IOException;

public class RegistroClienteController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtDocumento;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefono;

    @FXML
    private PasswordField txtContrasena;

    private final ClienteController clienteController =
            new ClienteController();

    @FXML
    private void handleRegistrar(ActionEvent event) {

        try {

            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String documento = txtDocumento.getText().trim();
            String correo = txtEmail.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String contrasena = txtContrasena.getText().trim();

            // Validar campos vacíos
            if (nombre.isEmpty() ||
                    apellido.isEmpty() ||
                    documento.isEmpty() ||
                    correo.isEmpty() ||
                    telefono.isEmpty() ||
                    contrasena.isEmpty()) {

                ServicioAlerta.mostrarAlertaError(
                        "Todos los campos son obligatorios."
                );
                return;
            }

            // Validar correo
            if (!correo.contains("@")) {

                ServicioAlerta.mostrarAlertaError(
                        "Ingrese un correo válido."
                );
                return;
            }

            // Validar si ya existe el usuario
            if (clienteController.buscarPorCorreo(correo) != null) {

                ServicioAlerta.mostrarAlertaError(
                        "Ya existe un usuario con ese correo."
                );
                return;
            }

            // Crear cliente
            Cliente cliente = new Cliente(
                    nombre,
                    apellido,
                    documento,
                    correo,
                    telefono,
                    contrasena
            );

            // Registrar cliente
            clienteController.registrarCliente(cliente);

            ServicioAlerta.mostrarAlerta(
                    "Registro exitoso",
                    "La cuenta fue creada correctamente.",
                    Alert.AlertType.INFORMATION
            );

            abrirLogin();

        } catch (Exception e) {

            e.printStackTrace();

            ServicioAlerta.mostrarAlertaError(
                    "Ocurrió un error al registrar el usuario."
            );
        }
    }

    @FXML
    private void handleVolverLogin(ActionEvent event) {

        abrirLogin();
    }

    private void abrirLogin() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/lospolimorficos/boletopolis/view.userViews/loginView.fxml")
                    );


            Parent root = loader.load();

            Stage stage = (Stage) txtNombre.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.centerOnScreen();

        } catch (IOException e) {

            e.printStackTrace();

            ServicioAlerta.mostrarAlertaError(
                    "No se pudo abrir el login."
            );
        }
    }
}