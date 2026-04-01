package lospolimorficos.boletopolis.viewController;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lospolimorficos.boletopolis.controller.EventoController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

    @FXML
    public void initialize() {
        cmbEstado.getItems().setAll(EstadoEvento.values());
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
        this.servicioDibujo = new ServicioDibujoRecinto(panelMapa);
        this.servicioDibujo.setInteractivo(true);
        this.servicioDibujo.setModoInteraccion(ServicioDibujoRecinto.ModoInteraccion.ADMIN_EVENTO);
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
                ivEvento.setImage(new Image(evento.getRutaImagen()));
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + e.getMessage());
            }
        }
    }

    private void renderizarMapa() {
        if (evento == null || evento.getRecinto() == null) return;

        // Ajustamos el tamaño del panel para que el ScrollPane funcione correctamente
        Platform.runLater(() -> {
            ajustarTamañoPanelMapa();
            servicioDibujo.renderizar(evento.getRecinto().getEscenario(), evento.getRecinto().getZonas());
        });
    }

    /**
     * Calcula y establece las dimensiones mínimas y preferidas del panelMapa para asegurar
     * que todo el contenido (escenario y zonas) sea visible dentro del ScrollPane.
     */
    private void ajustarTamañoPanelMapa() {
        if (evento == null || evento.getRecinto() == null) return;

        Recinto recinto = evento.getRecinto();
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        double[] datosEsc = servicioDibujo.obtenerDatosEscenarioSilencioso(recinto.getEscenario() != null ? recinto.getEscenario().posicion() : null);
        minX = Math.min(minX, datosEsc[0] - 50);
        minY = Math.min(minY, datosEsc[1] - 50);
        maxX = Math.max(maxX, datosEsc[0] + datosEsc[2] + 100);
        maxY = Math.max(maxY, datosEsc[1] + datosEsc[3] + 100);

        Map<PosicionZona, Integer> contadores = new HashMap<>();
        for (Zona zona : recinto.getZonas()) {
            int index = contadores.getOrDefault(zona.getPosicionZona(), 0);
            contadores.put(zona.getPosicionZona(), index + 1);

            double[] base = servicioDibujo.calcularPosicionBaseZona(zona.getPosicionZona(), datosEsc[0], datosEsc[1], datosEsc[2], datosEsc[3], index);
            int filas = zona.getAsientos().stream().mapToInt(Asiento::getFila).max().orElse(0);
            int columnas = zona.getAsientos().stream().mapToInt(Asiento::getNumero).max().orElse(0);

            double ancho = columnas * 12;
            double alto = filas * 12;

            minX = Math.min(minX, base[0] - (ancho / 2) - 50);
            minY = Math.min(minY, base[1] - (alto / 2) - 50);
            maxX = Math.max(maxX, base[0] + (ancho / 2) + 100);
            maxY = Math.max(maxY, base[1] + (alto / 2) + 100);
        }

        // Aseguramos que siempre haya un espacio mínimo visible
        minX = Math.min(minX, 0);
        minY = Math.min(minY, 0);
        maxX = Math.max(maxX, 600);
        maxY = Math.max(maxY, 400);

        double finalWidth = maxX - minX;
        double finalHeight = maxY - minY;

        panelMapa.setPrefWidth(finalWidth);
        panelMapa.setPrefHeight(finalHeight);
        panelMapa.setMinWidth(finalWidth);
        panelMapa.setMinHeight(finalHeight);
        servicioDibujo.actualizarCentros();
    }

    @FXML
    private void actualizarInformacion() {
        try {
            evento.setNombre(txtNombre.getText());
            evento.setDescripcion(txtDescripcion.getText());
            evento.setEstado(cmbEstado.getValue());
            evento.setFechaYHora(LocalDateTime.parse(txtFecha.getText(), formatter));

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
