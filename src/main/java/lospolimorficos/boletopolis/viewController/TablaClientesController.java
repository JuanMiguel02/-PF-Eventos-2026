package lospolimorficos.boletopolis.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import lospolimorficos.boletopolis.controller.ClienteController;
import lospolimorficos.boletopolis.models.Cliente;


import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class TablaClientesController {

    @FXML
    private TableColumn<Cliente, String> colApellidoCliente;

    @FXML
    private TableColumn<Cliente, String> colClienteId;

    @FXML
    private TableColumn<Cliente, String> colCorreo;

    @FXML
    private TableColumn<Cliente, String> colDocumentoCliente;

    @FXML
    private TableColumn<Cliente, String> colNombreCliente;

    @FXML
    private TableColumn<Cliente, String> colTelefonoCliente;


    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TextField txtApellidoCliente;

    @FXML
    private TextField txtBuscarCliente;

    @FXML
    private TextField txtDocumentoCliente;

    @FXML
    private TextField txtIdCliente;

    @FXML
    private TextField txtNombreCliente;

    @FXML
    private TextField txtTelefonoCliente;

    @FXML
    private TextField txtCorreoCliente;

    private final ClienteController clienteController = new ClienteController();

    @FXML
    public void initialize(){
        tblClientes.getSelectionModel().selectedItemProperty().addListener((
                observable, oldValue, newValue) -> mostrarDatosCliente(newValue));
        inicializarTabla();
        cargarClientes();
    }

    private void inicializarTabla(){
        colNombreCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colApellidoCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getApellido()));
        colDocumentoCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumDocumento()));
        colTelefonoCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumTelefono()));
        colCorreo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCorreo()));
        colClienteId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdUsuario().toString()));
    }

    private void cargarClientes(){
        tblClientes.setItems(clienteController.getClientes());
    }

    @FXML
    public void guardarCliente() {
        try{
            String nombre = txtNombreCliente.getText();
            String apellido = txtApellidoCliente.getText();
            String documento = txtDocumentoCliente.getText();
            String telefono = txtTelefonoCliente.getText();
            String correo = txtCorreoCliente.getText();

            Cliente cliente = new Cliente(nombre, apellido, correo, telefono, "123456", documento);

            if(clienteController.registrarCliente(cliente)){

                mostrarAlerta("Éxito", "Cliente: " + cliente.getNombreCompleto()
                        + " " + cliente.getNumDocumento() + " Registrado Éxitosamente", Alert.AlertType.INFORMATION);
                limpiarCampos();
                cargarClientes();
            }else{
                mostrarAlertaError("Este cliente ya se encuentra registrado");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void actualizarCliente() {
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if(clienteSeleccionado == null){
            mostrarAlertaError("Por favor seleccione un cliente para actualizar");
            return;
        }

        clienteSeleccionado.setNombre(txtNombreCliente.getText());
        clienteSeleccionado.setApellido(txtApellidoCliente.getText());
        clienteSeleccionado.setNumDocumento(txtDocumentoCliente.getText());
        clienteSeleccionado.setNumTelefono(txtTelefonoCliente.getText());
        clienteSeleccionado.setCorreo(txtCorreoCliente.getText());

        if(clienteController.actualizarCliente(clienteSeleccionado)){
            limpiarCampos();
            cargarClientes();
            tblClientes.refresh();
            mostrarAlerta("Éxito", "Cliente Actualizado Éxitosamente", Alert.AlertType.INFORMATION  );
        }

    }

    @FXML
    public void eliminarCliente() {
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();

        if(clienteSeleccionado == null){
            mostrarAlertaError("Por favor seleccione un cliente para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro que desea eliminar este cliente?");
        confirmacion.setContentText("Cliente: " + clienteSeleccionado.getNombre() + " - " + clienteSeleccionado
                .getNumDocumento());

        confirmacion.showAndWait().ifPresent(respuesta ->{
            if(respuesta == ButtonType.OK){
                if(clienteController.eliminarCliente(clienteSeleccionado)){

                    cargarClientes();
                    mostrarAlerta("Éxito", "Cliente Eliminado Éxitosamente", Alert.AlertType.INFORMATION  );
                }

            }
        });
    }

    @FXML
    public void onLimpiarCampos() {
        limpiarCampos();
    }

    private void limpiarCampos(){
        txtNombreCliente.clear();
        txtApellidoCliente.clear();
        txtDocumentoCliente.clear();
        txtTelefonoCliente.clear();
        txtCorreoCliente.clear();
        txtIdCliente.clear();
    }

    private void mostrarDatosCliente(Cliente cliente){
        if(cliente == null){
            limpiarCampos();
            return;
        }
        txtNombreCliente.setText(cliente.getNombre());
        txtApellidoCliente.setText(cliente.getApellido());
        txtDocumentoCliente.setText(cliente.getNumDocumento());
        txtTelefonoCliente.setText(cliente.getNumTelefono());
        txtCorreoCliente.setText(cliente.getCorreo());
        txtIdCliente.setText(cliente.getIdUsuario().toString());
    }

}
