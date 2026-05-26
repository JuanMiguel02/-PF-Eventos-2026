package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import lospolimorficos.boletopolis.controller.EventoController; // 🌟 Importamos tu controlador de negocio
import lospolimorficos.boletopolis.models.Evento;
import lospolimorficos.boletopolis.viewController.viewControllersAdmin.EventoCardController;

import java.io.IOException;
import java.util.List;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class EventoUsuarioController {

    @FXML
    private FlowPane flowEventos;
    @FXML
    private TextField txtBuscarNombre;
    @FXML
    private ComboBox<String> cmbBuscarTipo;

    private final EventoController eventoController = new EventoController();

    @FXML
    public void initialize() {
        // Inicializar las opciones del ComboBox
        cmbBuscarTipo.setItems(FXCollections.observableArrayList(
                "Todos", "Concierto", "Teatro", "Conferencia"
        ));
        cmbBuscarTipo.setValue("Todos");

        // Carga inicial: le pasamos la lista completa provista por el controlador de negocio
        cargarEventos(eventoController.getEventos());
    }

    /**
     * Método que se ejecuta cada vez que el usuario escribe o cambia el combo box.
     */
    @FXML
    private void aplicarFiltros() {
        String textoBusqueda = txtBuscarNombre.getText();
        String tipoSeleccionado = cmbBuscarTipo.getValue();

        //  DELEGACIÓN: Invocamos la lógica de filtrado del EventoController
        List<Evento> eventosFiltrados = eventoController.filtrarPorNombreYTipo(
                eventoController.getEventos(),
                textoBusqueda,
                tipoSeleccionado
        );

        // Volvemos a renderizar las tarjetas con el resultado devuelto
        cargarEventos(eventosFiltrados);
    }

    /**
     * Se encarga únicamente de limpiar y dibujar los componentes visuales
     */
    private void cargarEventos(List<Evento> listaEventos) {
        flowEventos.getChildren().clear();

        for (Evento evento : listaEventos) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/eventoCard.fxml")
                );

                VBox card = loader.load();
                EventoCardController controller = loader.getController();

                controller.setEvento(evento, this::comprarEvento);
                flowEventos.getChildren().add(card);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void comprarEvento(Evento evento) {
        if (evento == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/view.userViews/comprasUsuario.fxml"));
            Parent vistaCompra = loader.load();

            CompraUsuarioController controller = loader.getController();
            controller.setEvento(evento);

            if (flowEventos.getScene() != null) {
                StackPane contentPane = (StackPane) flowEventos.getScene().lookup("#contentPane");
                if (contentPane != null) {
                    contentPane.getChildren().clear();
                    contentPane.getChildren().add(vistaCompra);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            mostrarAlertaError("No se pudo cargar la vista de compra.");
        }
    }
}