package lospolimorficos.boletopolis.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.EventoController;
import lospolimorficos.boletopolis.models.Evento;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class TablaEventosController {

    @FXML
    private TableView<Evento> tblEventos;
    @FXML
    private TableColumn<Evento, String> colId;
    @FXML
    private TableColumn<Evento, String> colNombre;
    @FXML
    private TableColumn<Evento, String> colFecha;
    @FXML
    private TableColumn<Evento, String> colCiudad;
    @FXML
    private TableColumn<Evento, String> colRecinto;
    @FXML
    private TableColumn<Evento, String> colEstado;
    @FXML
    private TextField txtBuscar;

    private final EventoController eventoController = new EventoController();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        inicializarTabla();
        cargarEventos();
        configurarFiltro();
    }

    private void inicializarTabla() {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdEvento().toString().substring(0, 8)));
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colFecha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFechaYHora().format(formatter)));
        colCiudad.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCiudad().toString()));
        colRecinto.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRecinto().getNombre()));
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado().toString()));
    }

    private void cargarEventos() {
        tblEventos.setItems(eventoController.getEventos());
    }

    private void configurarFiltro() {
        FilteredList<Evento> filteredData = new FilteredList<>(eventoController.getEventos(), p -> true);
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(evento -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (evento.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (evento.getCiudad().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (evento.getRecinto().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        tblEventos.setItems(filteredData);
    }

    @FXML
    private void abrirFormularioNuevo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/formularioEvento.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Crear Nuevo Evento");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tblEventos.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            tblEventos.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("No se pudo abrir el formulario de creación");
        }
    }

    @FXML
    private void verDetallesEvento() {
        Evento seleccionado = tblEventos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlertaError("Seleccione un evento para ver sus detalles");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/detalleEvento.fxml"));
            Parent root = loader.load();

            DetalleEventoController controller = loader.getController();
            controller.setEvento(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Detalles de: " + seleccionado.getNombre());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tblEventos.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            tblEventos.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("No se pudo abrir la vista de detalles");
        }
    }

    @FXML
    private void eliminarEvento() {
        Evento seleccionado = tblEventos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlertaError("Seleccione un evento para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Evento");
        confirmacion.setHeaderText("¿Seguro que desea eliminar el evento?");
        confirmacion.setContentText("Evento: " + seleccionado.getNombre());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (eventoController.eliminarEvento(seleccionado)) {
                    mostrarAlerta("Éxito", "Evento eliminado correctamente", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlertaError("No se pudo eliminar el evento");
                }
            }
        });
    }
}
