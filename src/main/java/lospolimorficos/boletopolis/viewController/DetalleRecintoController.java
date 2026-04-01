package lospolimorficos.boletopolis.viewController;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.models.Recinto;
import lospolimorficos.boletopolis.models.Zona;
import lospolimorficos.boletopolis.services.ServicioAlerta;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;

import java.io.IOException;

public class DetalleRecintoController {

    @FXML private Label lblTitulo, lblNombre, lblCiudad, lblDireccion, lblCapacidad;
    @FXML private TableView<Zona> tblZonas;
    @FXML private TableColumn<Zona, String> colZonaNombre;
    @FXML private TableColumn<Zona, Integer> colZonaCapacidad;
    @FXML private TableColumn<Zona, Double> colZonaPrecio;
    @FXML private AnchorPane panelMapa;

    private Recinto recinto;
    private ServicioDibujoRecinto servicioDibujo;

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
        this.servicioDibujo = new ServicioDibujoRecinto(panelMapa);
        mostrarDetalles();
    }

    private void mostrarDetalles() {
        if (recinto == null) return;

        lblNombre.setText(recinto.getNombre());
        lblCiudad.setText(recinto.getCiudad().toString());
        lblDireccion.setText(recinto.getDireccion());
        lblCapacidad.setText(String.valueOf(recinto.getCapacidad()));

        colZonaNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colZonaCapacidad.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCapacidad()).asObject());
        colZonaPrecio.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getPrecioZona()).asObject());

        tblZonas.setItems(FXCollections.observableArrayList(recinto.getZonas()));

        // Dibujar el mapa (esperamos a que el panel esté listo)
        javafx.application.Platform.runLater(() -> {
            servicioDibujo.renderizar(recinto.getEscenario(), recinto.getZonas());
        });
    }

    @FXML
    private void eliminarZona() {
        Zona seleccionada = tblZonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            ServicioAlerta.mostrarAlertaError("Seleccione una zona para eliminar");
            return;
        }

        recinto.getZonas().remove(seleccionada);
        mostrarDetalles();
        ServicioAlerta.mostrarAlerta("Éxito", "Zona eliminada", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void actualizarZona() {
        Zona seleccionada = tblZonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            ServicioAlerta.mostrarAlertaError("Seleccione una zona para actualizar");
            return;
        }
        // Aquí podrías abrir un diálogo para editar precio/nombre
        ServicioAlerta.mostrarAlerta("Info", "Funcionalidad de edición de zona en desarrollo", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void regresar() {
        panelMapa.getScene().getWindow().hide();
    }
}
