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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.CompraController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
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
        this.servicioDibujo.setModoInteraccion(ServicioDibujoRecinto.ModoInteraccion.COMPRA);
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
                e.printStackTrace();
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

        Platform.runLater(() -> {
            ajustarDimensionPanelMapa();
            servicioDibujo.renderizar(eventoSeleccionado.getRecinto().getEscenario(), eventoSeleccionado.getRecinto().getZonas());
        });
    }

    private void ajustarDimensionPanelMapa() {
        if (eventoSeleccionado == null || eventoSeleccionado.getRecinto() == null) return;

        Recinto recinto = eventoSeleccionado.getRecinto();
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

        minX = Math.min(minX, 0);
        minY = Math.min(minY, 0);
        maxX = Math.max(maxX, 800);
        maxY = Math.max(maxY, 600);

        double finalWidth = maxX - Math.min(0, minX);
        double finalHeight = maxY - Math.min(0, minY);

        panelMapa.setPrefWidth(finalWidth);
        panelMapa.setPrefHeight(finalHeight);
        panelMapa.setMinWidth(finalWidth);
        panelMapa.setMinHeight(finalHeight);
        servicioDibujo.actualizarCentros();
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
        
        double total = 0;
        List<Entrada> entradas = new ArrayList<>();
        for (Asiento a : seleccionados) {
            Zona z = zonaAsientoMap.get(a);
            total += z.getPrecioZona();
            Entrada entrada = new Entrada(z, a, z.getPrecioZona(), EstadoEntrada.ACTIVA);
            entradas.add(entrada);
            a.setEstado(EstadoAsiento.VENDIDO); // Marcar como vendido
        }
        
        Compra nuevaCompra = new Compra(cliente, eventoSeleccionado, LocalDate.now(), total);
        nuevaCompra.setEntradas(entradas);
        nuevaCompra.setEstadoCompra(EstadoCompra.PAGADA);
        
        if (compraController.registrarCompra(nuevaCompra)) {
            mostrarAlerta("Éxito", "Compra finalizada correctamente", Alert.AlertType.INFORMATION);
            ((Stage) panelMapa.getScene().getWindow()).close();
        } else {
            mostrarAlertaError( "No se pudo registrar la compra");
        }
    }

}
