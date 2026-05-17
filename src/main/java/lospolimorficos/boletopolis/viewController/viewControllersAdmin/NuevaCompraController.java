package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.CompraController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class NuevaCompraController {

    @FXML
    private TextField txtBusquedaEvento;
    @FXML
    private StackPane contenedorFlujo;
    @FXML
    private ScrollPane scrollCatalogo;
    @FXML
    private FlowPane flowEventos;
    @FXML
    private VBox vboxMapa;
    @FXML
    private Label lblEventoSeleccionado;
    @FXML
    private AnchorPane panelMapa;
    @FXML
    private Label lblResumenSeleccion;

    private FilteredList<Evento> filteredEventos;
    private Evento eventoSeleccionado;
    private ServicioDibujoRecinto servicioDibujo;
    private final Map<Asiento, Zona> zonaAsientoMap = new HashMap<>();
    private final CompraController compraController = new CompraController();

    @FXML
    public void initialize() {
        this.servicioDibujo = new ServicioDibujoRecinto(panelMapa);
        this.servicioDibujo.setInteractivo(true);
        this.servicioDibujo.setStrategy(new CompraInteraccionStrategy());
        this.servicioDibujo.setOnAsientoChanged(this::actualizarResumen);
        configurarCatalogo();
    }

    private void configurarCatalogo() {
        filteredEventos = new FilteredList<>(EventoRepositorio.getInstance().getEventos(), p -> p.getEstado() == EstadoEvento.PUBLICADO);
        
        txtBusquedaEvento.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredEventos.setPredicate(evento -> {
                if (newVal == null || newVal.isEmpty()) return evento.getEstado() == EstadoEvento.PUBLICADO;
                return evento.getEstado() == EstadoEvento.PUBLICADO && evento.getNombre().toLowerCase().contains(newVal.toLowerCase());
            });
            renderizarCatalogo();
        });

        renderizarCatalogo();
    }

    private void renderizarCatalogo() {
        flowEventos.getChildren().clear();
        for (Evento evento : filteredEventos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/eventoCard.fxml"));
                Parent card = loader.load();
                EventoCardController controller = loader.getController();
                controller.setEvento(evento, this::seleccionarEventoParaCompra);
                flowEventos.getChildren().add(card);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void seleccionarEventoParaCompra(Evento evento) {
        this.eventoSeleccionado = evento;
        lblEventoSeleccionado.setText("Evento: " + evento.getNombre());
        
        scrollCatalogo.setVisible(false);
        scrollCatalogo.setManaged(false);
        vboxMapa.setVisible(true);
        vboxMapa.setManaged(true);
        
        renderizarMapa();
    }

    @FXML
    private void volverCatalogo() {
        vboxMapa.setVisible(false);
        vboxMapa.setManaged(false);
        scrollCatalogo.setVisible(true);
        scrollCatalogo.setManaged(true);
        servicioDibujo.limpiarSeleccion();
        actualizarResumen();
    }

    private void renderizarMapa() {
        if (eventoSeleccionado == null || eventoSeleccionado.getRecinto() == null) return;

        zonaAsientoMap.clear();
        for (Zona zona : eventoSeleccionado.getRecinto().getZonas()) {
            for (Asiento asiento : zona.getAsientos()) {
                zonaAsientoMap.put(asiento, zona);
            }
        }

        Platform.runLater(() -> servicioDibujo.renderizar(eventoSeleccionado.getRecinto().getEscenario(), eventoSeleccionado.getRecinto().getZonas()));
    }

    private void actualizarResumen() {
        double total = 0;
        List<Asiento> seleccionados = servicioDibujo.getAsientosSeleccionados();
        for (Asiento a : seleccionados) {
            total += zonaAsientoMap.get(a).getPrecioZona();
        }
        lblResumenSeleccion.setText(String.format("Asientos seleccionados: %d | Total: $%.2f", seleccionados.size(), total));
    }

    @FXML
    private void finalizarCompra() {
        List<Asiento> seleccionados = servicioDibujo.getAsientosSeleccionados();
        if (seleccionados.isEmpty()) {
            mostrarAlerta("Error", "Debe seleccionar al menos un asiento", Alert.AlertType.WARNING);
            return;
        }

        // Simulación: Tomamos el primer cliente disponible para simplificar
        Cliente cliente = UsuarioRepositorio.getInstancia().getClientes().get(0);
        Compra nuevaCompra = compraController.realizarCompra(cliente, eventoSeleccionado, seleccionados, zonaAsientoMap);
        
        if (compraController.registrarCompra(nuevaCompra)) {
            mostrarAlerta("Éxito", "Compra finalizada correctamente", Alert.AlertType.INFORMATION);
            ((Stage) panelMapa.getScene().getWindow()).close();
        } else {
            mostrarAlertaError( "No se pudo registrar la compra");
        }
    }

}
