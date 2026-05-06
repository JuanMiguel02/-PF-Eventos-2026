package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lospolimorficos.boletopolis.controller.EventoController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class DetalleEventoController {

    @FXML
    private ImageView ivEvento;
    @FXML
    private ComboBox<EstadoEvento> cmbEstado;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtFecha;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private AnchorPane panelMapa;
    @FXML
    private TableView<Zona> tblZonas;
    @FXML
    private TableColumn<Zona, String> colZonaNombre;
    @FXML
    private TableColumn<Zona, Integer> colOcupacion;
    @FXML
    private TableColumn<Zona, Integer> colZonaCapacidad;

    private Evento evento;
    private final EventoController eventoController = new EventoController();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private ServicioDibujoRecinto servicioDibujo;
    private String rutaImagenSeleccionada;


    @FXML
    public void initialize() {
        cmbEstado.getItems().setAll(EstadoEvento.values());
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
        this.servicioDibujo = new ServicioDibujoRecinto(panelMapa);
        this.servicioDibujo.setInteractivo(true);
        this.servicioDibujo.setStrategy(new AdminEventoInteraccionStrategy());
        this.servicioDibujo.setOnAsientoChanged(() -> {
            eventoController.actualizarEvento(evento);
            tblZonas.refresh();
        });
        cargarDatos();
        renderizarMapa();
        inicializarTabla();
        cargarZonas();
    }

    private void inicializarTabla() {
        colZonaNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colOcupacion.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().calcularOcupacion()).asObject());
        colZonaCapacidad.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCapacidad()).asObject());
    }

    private void cargarZonas() {
        if (evento != null && evento.getRecinto() != null) {
            tblZonas.setItems(FXCollections.observableArrayList(evento.getRecinto().getZonas()));
        }
    }

    private void cargarDatos() {
        if (evento == null) return;

        txtNombre.setText(evento.getNombre());
        txtFecha.setText(evento.getFechaYHora().format(formatter));
        txtDescripcion.setText(evento.getDescripcion());

        cmbEstado.setValue(evento.getEstado());

        if (evento.getRutaImagen() != null && !evento.getRutaImagen().isEmpty()) {
            try {

                String ruta = evento.getRutaImagen();
                Image imagen;
                //Imágenes internas
                if(ruta.startsWith("/")){
                    var recurso = getClass().getResource(ruta);
                    if(recurso != null){
                        imagen = new Image(recurso.toExternalForm());
                    }else{
                        throw new Exception("Recurso no encontrado: " + ruta);
                    }
                }else{ //Imágenes externas
                   imagen = new Image(ruta);
                }

                ivEvento.setImage(imagen);
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + e.getMessage());
            }
        }
    }

    private void renderizarMapa() {
        if (evento == null || evento.getRecinto() == null) return;

        // Ajustamos el tamaño del panel para que el ScrollPane funcione correctamente
        Platform.runLater(() -> {
            servicioDibujo.renderizar(evento.getRecinto().getEscenario(), evento.getRecinto().getZonas());
        });
    }

    @FXML
    private void actualizarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Evento");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(txtNombre.getScene().getWindow());
        if (selectedFile != null) {
            rutaImagenSeleccionada = selectedFile.toURI().toString();
            ivEvento.setImage(new Image(rutaImagenSeleccionada));
        }
    }

    @FXML
    private void actualizarInformacion() {
        try {
            evento.setNombre(txtNombre.getText());
            evento.setDescripcion(txtDescripcion.getText());
            evento.setEstado(cmbEstado.getValue());
            evento.setFechaYHora(LocalDateTime.parse(txtFecha.getText(), formatter));
            evento.setRutaImagen(rutaImagenSeleccionada);

            if (eventoController.actualizarEvento(evento)) {
                mostrarAlerta("Éxito", "Evento actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlertaError("No se pudo actualizar el evento en el repositorio");
            }
        } catch (Exception e) {
            mostrarAlertaError("Error al actualizar: " + e.getMessage() + ". Asegúrese de que la fecha tenga el formato dd/MM/yyyy HH:mm");
        }
    }
}
