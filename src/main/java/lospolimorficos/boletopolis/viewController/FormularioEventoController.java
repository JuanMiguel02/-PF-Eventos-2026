package lospolimorficos.boletopolis.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.EventoController;
import lospolimorficos.boletopolis.controller.RecintoController;
import lospolimorficos.boletopolis.models.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class FormularioEventoController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox<Ciudad> cmbCiudad;
    @FXML
    private ComboBox<Recinto> cmbRecinto;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField txtHora;
    @FXML
    private ComboBox<String> cmbTipoEvento;
    @FXML
    private ImageView ivImagen;

    private final EventoController eventoController = new EventoController();
    private final RecintoController recintoController = new RecintoController();
    private String rutaImagenSeleccionada;

    @FXML
    public void initialize() {
        cmbCiudad.getItems().setAll(Ciudad.values());
        cmbRecinto.setItems(recintoController.getRecintos());
        cmbTipoEvento.getItems().setAll("Concierto", "Teatro", "Conferencia");
    }

    @FXML
    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Evento");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(txtNombre.getScene().getWindow());
        if (selectedFile != null) {
            rutaImagenSeleccionada = selectedFile.toURI().toString();
            ivImagen.setImage(new Image(rutaImagenSeleccionada));
        }
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;

        try {
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();
            Ciudad ciudad = cmbCiudad.getValue();
            Recinto recinto = cmbRecinto.getValue();
            LocalDateTime fechaHora = LocalDateTime.of(dpFecha.getValue(), LocalTime.parse(txtHora.getText()));
            String tipo = cmbTipoEvento.getValue();

            Evento nuevoEvento;
            if ("Concierto".equals(tipo)) {
                nuevoEvento = new Concierto(nombre, descripcion, ciudad, fechaHora, EstadoEvento.BORRADOR, recinto, Duration.ofHours(2), "Artista Desconocido", "Varios");
            } else {
                // Por ahora usamos Concierto como fallback si no hay otras clases concretas listas para instanciar con sus campos específicos
                nuevoEvento = new Concierto(nombre, descripcion, ciudad, fechaHora, EstadoEvento.BORRADOR, recinto, Duration.ofHours(1), "N/A", "N/A");
            }

            nuevoEvento.setRutaImagen(rutaImagenSeleccionada);

            if (eventoController.registrarEvento(nuevoEvento)) {
                mostrarAlerta("Éxito", "Evento creado correctamente", Alert.AlertType.INFORMATION);
                cancelar();
            } else {
                mostrarAlertaError("No se pudo registrar el evento");
            }

        } catch (Exception e) {
            mostrarAlertaError("Error en los datos: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtDescripcion.getText().isEmpty() ||
            cmbCiudad.getValue() == null || cmbRecinto.getValue() == null ||
            dpFecha.getValue() == null || txtHora.getText().isEmpty() ||
            cmbTipoEvento.getValue() == null) {
            mostrarAlertaError("Por favor complete todos los campos");
            return false;
        }
        try {
            LocalTime.parse(txtHora.getText());
        } catch (Exception e) {
            mostrarAlertaError("Formato de hora inválido (HH:mm)");
            return false;
        }
        return true;
    }
}
