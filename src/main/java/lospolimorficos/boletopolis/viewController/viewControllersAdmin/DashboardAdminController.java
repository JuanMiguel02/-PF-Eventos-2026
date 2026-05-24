package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.controller.ClienteController;
import lospolimorficos.boletopolis.controller.CompraController;
import lospolimorficos.boletopolis.controller.EventoController;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.models.Compra;
import lospolimorficos.boletopolis.models.Evento;
import lospolimorficos.boletopolis.services.ServicioGeneradorGraficos;

import java.util.Map;
import java.util.Objects;

public class DashboardAdminController {
    @FXML
    private StackPane contenedorCentro;
    @FXML
    private AnchorPane vistaInicio;
    @FXML
    private VBox contenedorGrafico;
    @FXML
    private Label lblTotalClientes;
    @FXML
    private Label lblTotalEventos;
    @FXML
    private Label lblTotalCompras;

    private final ClienteController clienteController = new ClienteController();
    private final EventoController eventoController = new EventoController();
    private final CompraController compraController = new CompraController();

    @FXML
    public void initialize(){
        actualizarTotales();
        cargarMetricas();
        clienteController.getClientes().addListener((ListChangeListener<Cliente>) c -> actualizarDashboard());
        eventoController.getEventos().addListener((ListChangeListener<Evento>) e -> actualizarDashboard());
        compraController.getCompras().addListener((ListChangeListener<Compra>) c -> actualizarDashboard());

    }

    private void actualizarDashboard(){

        actualizarTotales();
        cargarMetricas();
    }

    private void actualizarTotales(){
        lblTotalClientes.setText(String.valueOf(clienteController.getClientes().size()));
        lblTotalEventos.setText(String.valueOf(eventoController.getEventos().size()));
        lblTotalCompras.setText(String.valueOf(compraController.getCompras().size()));
    }

    private void cargarMetricas() {
        contenedorGrafico.getChildren().clear();
        Map<String, Number> datosVentas = compraController.obtenerVentasPorMes();
        BarChart<String, Number> graficoVentas = ServicioGeneradorGraficos.crearBarChart("Ventas por Mes", datosVentas);
        graficoVentas.setPrefHeight(400);
        contenedorGrafico.getChildren().add(graficoVentas);
    }


    public void cargarVista(String fxml){
        try{
            Parent vista = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            contenedorCentro.getChildren().setAll(vista);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void cargarVistaClientes(){
        cargarVista("/lospolimorficos/boletopolis/views/adminViews/tablaClientes.fxml");
    }

    @FXML
    private void cargarVistaEventos(){
        cargarVista("/lospolimorficos/boletopolis/views/adminViews/tablaEventos.fxml");
    }

    @FXML
    private void cargarVistaRecintos(){
        cargarVista("/lospolimorficos/boletopolis/views/adminViews/creacionRecinto.fxml");
    }

    @FXML
    private void cargarVistaTablaRecintos(){
        cargarVista("/lospolimorficos/boletopolis/views/adminViews/tablaRecintos.fxml");
    }

    @FXML
    private void cargarVistaReportes(){
        cargarVista("/lospolimorficos/boletopolis/views/adminViews/reportesAdmin.fxml");
    }

    @FXML
    private void cargarVistaMetricas(){
        cargarVista("/lospolimorficos/boletopolis/views/adminViews/metricasAdmin.fxml");
    }

    @FXML
    private void cargarVistaCompras(){
        cargarVista("/lospolimorficos/boletopolis/views/adminViews/comprasView.fxml");
    }

    @FXML
    private void cargarVistaInicio(){
        contenedorCentro.getChildren().clear();
        vistaInicio.setVisible(true);
        vistaInicio.setManaged(true);
        contenedorCentro.getChildren().add(vistaInicio);
    }

    public void cerrar(){
        System.exit(0);
    }

    public void minimizar() {
        Stage stage = (Stage) contenedorCentro.getScene().getWindow();
        stage.setIconified(true);

    }
}
