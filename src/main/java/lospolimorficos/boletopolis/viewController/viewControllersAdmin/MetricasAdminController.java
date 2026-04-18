package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.VBox;
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

    @FXML
    public void initialize() {
        cargarMetricasEjemplo();
    }

    /**
     * Carga gráficos de ejemplo utilizando el servicio generador de gráficos.
     */
    private void cargarMetricasEjemplo() {
        // Datos de ejemplo para ventas por mes
        Map<String, Number> datosVentas = new HashMap<>();
        datosVentas.put("Enero", 150);
        datosVentas.put("Febrero", 200);
        datosVentas.put("Marzo", 180);
        datosVentas.put("Abril", 250);

        BarChart<String, Number> graficoVentas = ServicioGeneradorGraficos.crearBarChart("Ventas por Mes", datosVentas);
        graficoVentas.setPrefHeight(400);

        // Datos de ejemplo para asistencia por evento
        Map<String, Number> datosAsistencia = new HashMap<>();
        datosAsistencia.put("Concierto Rock", 500);
        datosAsistencia.put("Obra Teatro", 300);
        datosAsistencia.put("Festival Jazz", 450);

        BarChart<String, Number> graficoAsistencia = ServicioGeneradorGraficos.crearBarChart("Asistencia por Evento", datosAsistencia);
        graficoAsistencia.setPrefHeight(400);

        contenedorGraficos.getChildren().addAll(graficoVentas, graficoAsistencia);
    }
}
