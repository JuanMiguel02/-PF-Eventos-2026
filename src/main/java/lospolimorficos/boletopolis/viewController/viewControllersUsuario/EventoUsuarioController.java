package lospolimorficos.boletopolis.viewController.viewControllersUsuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import lospolimorficos.boletopolis.models.Evento;

import lospolimorficos.boletopolis.repositorios.EventoRepositorio;

import lospolimorficos.boletopolis.viewController.viewControllersAdmin.EventoCardController;

import java.io.IOException;

public class EventoUsuarioController {

    @FXML
    private FlowPane flowEventos;

    private final EventoRepositorio eventoRepositorio =
            EventoRepositorio.getInstancia();

    /**
     * Inicializa la vista.
     */
    @FXML
    public void initialize() {

        cargarEventos();
    }

    /**
     * Carga los eventos
     * disponibles en tarjetas.
     */
    private void cargarEventos() {
        flowEventos.getChildren().clear();

        for (Evento evento : eventoRepositorio.getEventos()) {

            try {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/lospolimorficos/boletopolis/views/adminViews/eventoCard.fxml"
                        )
                );

                VBox card = loader.load();

                EventoCardController controller =
                        loader.getController();

                controller.setEvento(
                        evento,
                        this::comprarEvento
                );

                flowEventos.getChildren().add(card);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /**
     * Maneja la compra
     * de un evento.
     *
     * @param evento Evento seleccionado.
     */
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
                } else {
                    System.err.println("[DEBUG_LOG] No se encontró contentPane en la escena");
                }
            } else {
                System.err.println("[DEBUG_LOG] La escena es nula en EventoUsuarioController");
            }
        } catch (IOException e) {
            e.printStackTrace();
            lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError("No se pudo cargar la vista de compra.");
        }
    }
}