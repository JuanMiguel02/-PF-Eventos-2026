package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.controller.CompraController;
import lospolimorficos.boletopolis.services.ServicioGeneradorGraficos;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la vista de métricas administrativas.
 * Utiliza ServicioGeneradorGraficos para mostrar datos visuales.
 */
public class MetricasAdminController {

    @FXML
    private VBox contenedorGraficos;

    private final CompraController compraController = new CompraController();

    @FXML
    public void initialize() {
        cargarMetricasEjemplo();
    }

    /**
     * Carga gráficos de ejemplo utilizando el servicio generador de gráficos.
     */
    private void cargarMetricasEjemplo() {

        Map<String, Number> datosVentas = compraController.obtenerVentasPorMes();
        BarChart<String, Number> graficoVentas = ServicioGeneradorGraficos.crearBarChart("Ventas por Mes", datosVentas);
        graficoVentas.setPrefHeight(400);

        Map<String, Number> datosAsistencia = compraController.obtenerVentasPorEvento();
        BarChart<String, Number> graficoAsistencia = ServicioGeneradorGraficos.crearBarChart("Asistencia por Evento", datosAsistencia);
        graficoAsistencia.setPrefHeight(400);

        contenedorGraficos.getChildren().addAll(graficoVentas, graficoAsistencia);
    }
}
