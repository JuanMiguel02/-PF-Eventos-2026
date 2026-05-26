package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lospolimorficos.boletopolis.controller.ClienteController;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.services.GestorSesion;
import lospolimorficos.boletopolis.services.ServicioAlerta;

public class PerfilUsuarioController {
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtDocumento;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;

    // Inyección de los nuevos campos de contraseña
    @FXML
    private PasswordField txtPasswordActual;
    @FXML
    private PasswordField txtPasswordNueva;
    @FXML
    private PasswordField txtPasswordConfirmar;

    @FXML
    private ListView<String> listNotificaciones;

    private Cliente clienteActual;
    // Instancia del controlador lógico para operaciones de persistencia
    private final ClienteController clienteController = new ClienteController();

    @FXML
    public void initialize() {
        if (GestorSesion.getInstancia().getUsuarioActual() instanceof Cliente cliente) {
            clienteActual = cliente;
            cargarDatos();
            cargarNotificaciones();
        }
    }

    private void cargarDatos() {
        if (clienteActual != null) {
            txtNombre.setText(clienteActual.getNombre());
            txtApellido.setText(clienteActual.getApellido());
            txtDocumento.setText(clienteActual.getDocumento());
            txtCorreo.setText(clienteActual.getCorreo());
            txtTelefono.setText(clienteActual.getNumTelefono());
        }
    }

    private void cargarNotificaciones() {
        if (clienteActual != null) {
            listNotificaciones.setItems(FXCollections.observableArrayList(clienteActual.getNotificaciones()));
        }
    }

    @FXML
    private void limpiarNotificaciones() {
        if (clienteActual != null) {
            clienteActual.getNotificaciones().clear();
            cargarNotificaciones();
        }
    }

    @FXML
    private void guardarCambios() {
        if (clienteActual == null) return;

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String documento = txtDocumento.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();

        String passActual = txtPasswordActual.getText();
        String passNueva = txtPasswordNueva.getText();
        String passConfirmar = txtPasswordConfirmar.getText();

        // 1. Validar campos obligatorios de texto estándar
        if (nombre.isEmpty() || apellido.isEmpty() || documento.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            ServicioAlerta.mostrarAlertaError("Todos los campos de información personal son obligatorios.");
            return;
        }

        // 2. Validar credenciales obligatorias de confirmación
        if (passActual.isEmpty()) {
            ServicioAlerta.mostrarAlertaError("Debe ingresar su contraseña actual para confirmar los cambios.");
            return;
        }

        // Suponiendo que la clase Usuario o Cliente hereda o implementa getContrasenia()
        if (!clienteActual.getContrasena().equals(passActual)) {
            ServicioAlerta.mostrarAlertaError("La contraseña actual introducida es incorrecta.");
            return;
        }

        // 3. Validar lógica de cambio de contraseña si el usuario escribió una nueva
        if (!passNueva.isEmpty()) {
            if (passConfirmar.isEmpty()) {
                ServicioAlerta.mostrarAlertaError("Por favor, confirme su nueva contraseña.");
                return;
            }
            if (!passNueva.equals(passConfirmar)) {
                ServicioAlerta.mostrarAlertaError("La nueva contraseña y su confirmación no coinciden.");
                return;
            }
            // Si supera los filtros, actualizamos la propiedad en el modelo
            clienteActual.setContrasena(passNueva);
        }

        // 4. Actualizar propiedades del modelo
        clienteActual.setNombre(nombre);
        clienteActual.setApellido(apellido);
        clienteActual.setDocumento(documento);
        clienteActual.setCorreo(correo);
        clienteActual.setNumTelefono(telefono);

        // 5. Persistir los cambios a través del ClienteController
        if (clienteController.actualizarCliente(clienteActual)) {
            ServicioAlerta.mostrarAlerta("Éxito", "Perfil actualizado correctamente.", javafx.scene.control.Alert.AlertType.INFORMATION);
            limpiarCamposContrasenia();
        } else {
            ServicioAlerta.mostrarAlertaError("Ocurrió un error al guardar los cambios en el sistema.");
        }
    }

    private void limpiarCamposContrasenia() {
        txtPasswordActual.clear();
        txtPasswordNueva.clear();
        txtPasswordConfirmar.clear();
    }
}