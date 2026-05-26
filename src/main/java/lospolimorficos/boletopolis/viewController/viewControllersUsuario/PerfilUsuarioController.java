package lospolimorficos.boletopolis.viewController.viewControllersUsuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    @FXML
    private ListView<String> listNotificaciones;

    private Cliente clienteActual;

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

        if (nombre.isEmpty() || apellido.isEmpty() || documento.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            ServicioAlerta.mostrarAlertaError("Todos los campos son obligatorios.");
            return;
        }

        clienteActual.setNombre(nombre);
        clienteActual.setApellido(apellido);
        clienteActual.setDocumento(documento);
        clienteActual.setCorreo(correo);
        clienteActual.setNumTelefono(telefono);

        ServicioAlerta.mostrarAlerta("Éxito", "Perfil actualizado correctamente.", javafx.scene.control.Alert.AlertType.INFORMATION);
    }
}
