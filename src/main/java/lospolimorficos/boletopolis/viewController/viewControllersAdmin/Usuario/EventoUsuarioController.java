package lospolimorficos.boletopolis.viewController.viewControllersAdmin.Usuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
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

        System.out.println(
                "Comprar evento: "
                        + evento.getNombre()
        );
    }
}