package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.ClienteController;
import lospolimorficos.boletopolis.controller.CompraController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

public class NuevaCompraController {

    @FXML
    private TextField txtBusquedaEvento;
    @FXML
    private TextField txtBusquedaCliente;
    @FXML
    private ScrollPane scrollCatalogo;
    @FXML
    private FlowPane flowEventos;
    @FXML
    private VBox vboxMapa;
    @FXML
    private Label lblEventoSeleccionado;
    @FXML
    private Label lblLugarEvento;
    @FXML
    private AnchorPane panelMapa;
    @FXML
    private Label lblResumenSeleccion;

    private FilteredList<Evento> filteredEventos;
    private Evento eventoSeleccionado;
    private ServicioDibujoRecinto servicioDibujo;
    private final Map<Asiento, Zona> zonaAsientoMap = new HashMap<>();
    private final CompraController compraController = new CompraController();
    private final ClienteController clienteController = new ClienteController();
    private Cliente clienteCompra;

    private Compra compraTemporal;

    @FXML
    public void initialize() {
        this.servicioDibujo = new ServicioDibujoRecinto(panelMapa);
        this.servicioDibujo.setInteractivo(true);
        this.servicioDibujo.setStrategy(new CompraInteraccionStrategy());
        this.servicioDibujo.setOnAsientoChanged(() -> {
            compraTemporal = null;
            actualizarResumen();
        });
        configurarCatalogo();
    }

    private void configurarCatalogo() {
        filteredEventos = new FilteredList<>(EventoRepositorio.getInstancia().getEventos(), p -> p.getEstado() == EstadoEvento.PUBLICADO);

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
        lblLugarEvento.setText(" | Lugar: " + evento.getCiudad().toString() + ", " + evento.getRecinto().getNombre());
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

        if (compraTemporal != null) {
            total += compraTemporal.calcularTotalServicios();
        }

        lblResumenSeleccion.setText(String.format("Asientos seleccionados: %d | Total: $%.2f", seleccionados.size(), total));
    }

    @FXML
    private void finalizarCompra() {
        if (compraTemporal == null) {
            List<Asiento> seleccionados = servicioDibujo.getAsientosSeleccionados();
            if (seleccionados.isEmpty() || clienteCompra == null) {
                mostrarAlerta("Error", "Debe seleccionar al menos un asiento y un cliente para finalizar la compra", Alert.AlertType.WARNING);
                return;
            }
            compraTemporal = compraController.realizarCompra(clienteCompra, eventoSeleccionado, seleccionados, zonaAsientoMap);
        }

        if (compraController.registrarCompra(compraTemporal)) {
            mostrarResumenFinal(compraTemporal);
            ((Stage) panelMapa.getScene().getWindow()).close();
        } else {
            mostrarAlertaError( "No se pudo registrar la compra");
        }
    }

    @FXML
    private void mostrarResumenPreCompra() {
        List<Asiento> seleccionados = servicioDibujo.getAsientosSeleccionados();
        if (seleccionados.isEmpty() || clienteCompra == null) {
            mostrarAlerta("Error", "Debe seleccionar al menos un asiento y un cliente para ver el resumen", Alert.AlertType.WARNING);
            return;
        }

        if (compraTemporal == null) {
            compraTemporal = compraController.realizarCompra(clienteCompra, eventoSeleccionado, seleccionados, zonaAsientoMap);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/serviciosAdicionalesView.fxml"));
            Parent root = loader.load();
            
            ServiciosAdicionalesController controller = loader.getController();
            controller.setCompra(compraTemporal, this::actualizarResumen);
            
            Stage stage = new Stage();
            stage.setTitle("Servicios Adicionales - Boletopolis");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            mostrarAlertaError("Error al mostrar los servicios adicionales");
        }
    }

    private void mostrarResumenFinal(Compra compra) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/resumenCompraView.fxml"));
            Parent root = loader.load();
            
            ResumenCompraController controller = loader.getController();
            controller.setCompra(compra);
            
            Stage stage = new Stage();
            stage.setTitle("Resumen de Compra - Boletopolis");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            mostrarAlertaError("Error al mostrar el resumen de la compra");
        }
    }

    @FXML
    private void buscarCliente(){
        compraTemporal = null;
        String busqueda = txtBusquedaCliente.getText();
        clienteCompra = clienteController.buscarCliente(busqueda);
        if(clienteCompra != null){
            txtBusquedaCliente.setText(clienteCompra.getNombreCompleto() + " - Documento: " + clienteCompra.getDocumento());
        }
        actualizarResumen();
    }
}
