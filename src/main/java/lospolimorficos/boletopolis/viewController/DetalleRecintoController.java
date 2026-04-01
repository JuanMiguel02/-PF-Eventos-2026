package lospolimorficos.boletopolis.viewController;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lospolimorficos.boletopolis.controller.RecintoController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.services.ServicioAlerta;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DetalleRecintoController {

    @FXML private Label lblTitulo, lblNombre, lblCiudad, lblDireccion, lblCapacidad, lblOcupacion;
    @FXML private TableView<Zona> tblZonas;
    @FXML private TableColumn<Zona, String> colZonaNombre;
    @FXML private TableColumn<Zona, Integer> colZonaCapacidad;
    @FXML private TableColumn<Zona, Double> colZonaPrecio;
    @FXML private TableColumn<Zona, Integer> colOcupacion;
    @FXML private AnchorPane panelMapa;

    private Recinto recinto;
    private ServicioDibujoRecinto servicioDibujo;
    private final RecintoController recintoController = new RecintoController();

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
        this.servicioDibujo = new ServicioDibujoRecinto(panelMapa);
        this.servicioDibujo.setInteractivo(true);
        this.servicioDibujo.setModoInteraccion(ServicioDibujoRecinto.ModoInteraccion.ADMIN_RECINTO);
        this.servicioDibujo.setOnAsientoChanged(() -> {
            recintoController.actualizarRecinto(recinto);
            cargarDatosRecinto(); // Por si cambia la ocupación total
            tblZonas.refresh();   // Actualizar la columna de ocupación por zonas
        });
        mostrarDetalles();
    }

    private void mostrarDetalles() {
        if (recinto == null) return;

        cargarDatosRecinto();
        inicializarTabla();

        cargarZonas();

        // Dibujar el mapa (esperamos a que el panel esté listo)
        Platform.runLater(() -> {
            ajustarTamañoPanelMapa();
            servicioDibujo.renderizar(recinto.getEscenario(), recinto.getZonas());
        });
    }

    /**
     * Calcula y establece las dimensiones mínimas y preferidas del panelMapa para asegurar
     * que todo el contenido (escenario y zonas) sea visible dentro del ScrollPane.
     * Itera sobre los elementos del recinto para encontrar las coordenadas máximas.
     */
    private void ajustarTamañoPanelMapa() {
        if (recinto == null) return;

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        // Considerar escenario
        double[] datosEsc = servicioDibujo.obtenerDatosEscenarioSilencioso(recinto.getEscenario() != null ? recinto.getEscenario().posicion() : null);
        minX = Math.min(minX, datosEsc[0] - 50);
        minY = Math.min(minY, datosEsc[1] - 50);
        maxX = Math.max(maxX, datosEsc[0] + datosEsc[2] + 100);
        maxY = Math.max(maxY, datosEsc[1] + datosEsc[3] + 100);

        // Considerar zonas
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

    private void cargarDatosRecinto(){
        lblNombre.setText(recinto.getNombre());
        lblCiudad.setText(recinto.getCiudad().toString());
        lblDireccion.setText(recinto.getDireccion());
        lblCapacidad.setText(String.valueOf(recinto.getCapacidad()));
        lblOcupacion.setText(String.valueOf(recinto.getOcupacion()));
    }

    private void inicializarTabla(){
        colZonaNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colZonaCapacidad.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCapacidad()).asObject());
        colZonaPrecio.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getPrecioZona()).asObject());
        colOcupacion.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().calcularOcupacion()).asObject());
    }

    private void cargarZonas(){
        tblZonas.setItems(FXCollections.observableArrayList(recinto.getZonas()));
    }

    @FXML
    private void eliminarZona() {
        Zona seleccionada = tblZonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            ServicioAlerta.mostrarAlertaError("Seleccione una zona para eliminar");
            return;
        }

        recinto.getZonas().remove(seleccionada);
        recinto.setCapacidad(recinto.getZonas().stream().mapToInt(Zona::getCapacidad).sum());
        if(recintoController.actualizarRecinto(recinto)){
            mostrarDetalles();
            ServicioAlerta.mostrarAlerta("Éxito", "Zona eliminada", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void actualizarZona() {
        Zona seleccionada = tblZonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            ServicioAlerta.mostrarAlertaError("Seleccione una zona para actualizar");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(seleccionada.getPrecioZona()));
        dialog.setTitle("Actualizar Zona");
        dialog.setHeaderText("Editar precio para: " + seleccionada.getNombre());
        dialog.setContentText("Nuevo Precio:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nuevoPrecio -> {
            try {
                double precio = Double.parseDouble(nuevoPrecio);
                seleccionada.setPrecioZona(precio);
                if (recintoController.actualizarRecinto(recinto)) {
                    tblZonas.refresh();
                    ServicioAlerta.mostrarAlerta("Éxito", "Precio actualizado", Alert.AlertType.INFORMATION);
                }
            } catch (NumberFormatException e) {
                ServicioAlerta.mostrarAlertaError("Precio inválido");
            }
        });
    }

    @FXML
    private void agregarNuevaZona() {

    }

    @FXML
    private void regresar() {
        panelMapa.getScene().getWindow().hide();
    }
}
