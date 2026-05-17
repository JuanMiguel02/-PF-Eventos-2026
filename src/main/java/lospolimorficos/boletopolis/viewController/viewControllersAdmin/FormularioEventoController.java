package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.EventoController;
import lospolimorficos.boletopolis.controller.RecintoController;
import lospolimorficos.boletopolis.models.*;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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

    // Paneles específicos
    @FXML
    private GridPane gridConcierto;
    @FXML
    private GridPane gridTeatro;
    @FXML
    private GridPane gridConferencia;

    // Campos Concierto
    @FXML
    private TextField txtArtista;
    @FXML
    private TextField txtGeneroMusical;

    // Campos Teatro
    @FXML
    private TextField txtCompania;
    @FXML
    private TextField txtDirector;
    @FXML
    private TextField txtNumActos;

    // Campos Conferencia
    @FXML
    private TextField txtPonente;
    @FXML
    private TextField txtTema;
    @FXML
    private TextField txtInstitucion;

    private final EventoController eventoController = new EventoController();
    private final RecintoController recintoController = new RecintoController();
    private String rutaImagenSeleccionada;

    @FXML
    public void initialize() {
        cmbCiudad.getItems().setAll(Ciudad.values());
        cmbRecinto.setItems(recintoController.getRecintos());
        cmbTipoEvento.getItems().setAll("Concierto", "Teatro", "Conferencia");

        cmbTipoEvento.valueProperty().addListener((obs, oldVal, newVal) -> actualizaCamposEspecificos(newVal));
    }

    private void actualizaCamposEspecificos(String tipo) {
        gridConcierto.setVisible("Concierto".equals(tipo));
        gridConcierto.setManaged("Concierto".equals(tipo));

        gridTeatro.setVisible("Teatro".equals(tipo));
        gridTeatro.setManaged("Teatro".equals(tipo));

        gridConferencia.setVisible("Conferencia".equals(tipo));
        gridConferencia.setManaged("Conferencia".equals(tipo));
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

            Duration duracionDefault = Duration.ofHours(2);
            if (eventoController.existeConflicto(recinto, fechaHora, duracionDefault)) {
                mostrarAlertaError("Ya existe un evento en este recinto durante el horario seleccionado");
                return;
            }

            Map<String, String> especificos = obtenerDatosSegunTipo(tipo);
            Recinto copiaRecinto = recinto.copiar();

            Evento nuevoEvento = eventoController.crearEvento(tipo, especificos, nombre, descripcion, ciudad, fechaHora, copiaRecinto, duracionDefault);
            nuevoEvento.setRutaImagen(rutaImagenSeleccionada);

            if (eventoController.registrarEvento(nuevoEvento)) {
                mostrarAlerta("Éxito", "Evento creado correctamente", Alert.AlertType.INFORMATION);
                cancelar();
            } else {
                mostrarAlertaError("No se pudo registrar el evento");
            }

        } catch (Exception e) {
            mostrarAlertaError("Error en los datos: " + e.getMessage());
            System.err.println("Error en los datos: " + e.getMessage());
        }
    }

    private Map<String, String> obtenerDatosSegunTipo(String tipo) {
        Map<String, String> especificos = new HashMap<>();

        if("Concierto".equals(tipo)){
            especificos.put("artista", txtArtista.getText());
            especificos.put("generoMusical", txtGeneroMusical.getText());
        }else if("Teatro".equals(tipo)){
            especificos.put("compania", txtCompania.getText());
            especificos.put("director", txtDirector.getText());
            especificos.put("numActos", txtNumActos.getText());
        }else if("Conferencia".equals(tipo)){
            especificos.put("ponente", txtPonente.getText());
            especificos.put("tema", txtTema.getText());
            especificos.put("institucion", txtInstitucion.getText());
        }
        return especificos;
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

        String tipo = cmbTipoEvento.getValue();
        if ("Concierto".equals(tipo)) {
            if (txtArtista.getText().isEmpty() || txtGeneroMusical.getText().isEmpty()) {
                mostrarAlertaError("Por favor complete los campos del concierto");
                return false;
            }
        } else if ("Teatro".equals(tipo)) {
            if (txtCompania.getText().isEmpty() || txtDirector.getText().isEmpty() || txtNumActos.getText().isEmpty()) {
                mostrarAlertaError("Por favor complete los campos de la obra de teatro");
                return false;
            }
            try {
                Integer.parseInt(txtNumActos.getText());
            } catch (NumberFormatException e) {
                mostrarAlertaError("El número de actos debe ser un número entero");
                return false;
            }
        } else if ("Conferencia".equals(tipo)) {
            if (txtPonente.getText().isEmpty() || txtTema.getText().isEmpty() || txtInstitucion.getText().isEmpty()) {
                mostrarAlertaError("Por favor complete los campos de la conferencia");
                return false;
            }
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
