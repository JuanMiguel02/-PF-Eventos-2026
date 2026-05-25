package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.CompraRepositorio;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementación de la estrategia de métrica para generar un reporte de ventas por un período específico.
 * Esta clase se encarga de obtener las compras realizadas dentro de un rango de fechas
 * y calcular métricas como el total de compras, entradas vendidas y el total de ventas.
 */
public class MetricaVentasPorPeriodo implements EstrategiaMetrica{

    private final CompraRepositorio compraRepositorio;
    private final FiltroReporte filtro;

    /**
     * Constructor de MetricaVentasPorPeriodo.
     *
     * @param compraRepositorio El repositorio de compras utilizado para obtener los datos de las compras.
     * @param filtro El objeto FiltroReporte que contiene las fechas de inicio y fin del período.
     */
    public MetricaVentasPorPeriodo(CompraRepositorio compraRepositorio, FiltroReporte filtro) {
        this.compraRepositorio = compraRepositorio;
        this.filtro = filtro;
    }

    /**
     * Genera la sección del reporte correspondiente a las ventas por período.
     * Este método calcula y añade un resumen textual de las ventas al constructor del reporte.
     *
     * @param constructor El constructor de reporte al que se añadirán los elementos.
     */
    @Override
    public void generarSeccion(ConstructorReporte constructor) {
        // Paso 1: Definir el formato de fecha para mostrar en el reporte.
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Paso 2: Agregar un subtítulo al reporte indicando la sección de ventas por período.
        constructor.agregarSubtitulo("Ventas por período: ");
        // Paso 3: Obtener la lista de compras realizadas dentro del período especificado por el filtro.
        List<Compra> compras = compraRepositorio.obtenerComprasPorPeriodo(filtro.getFechaInicio(), filtro.getFechaFin());
        // Paso 4: Calcular el número total de compras realizadas en el período.
        int totalCompras = compras.size();
        // Paso 5: Calcular el total de ventas sumando el total de compra de cada objeto Compra.
        double totalVentas = compras.stream().mapToDouble(Compra::getTotalCompra).sum();
        // Paso 6: Calcular el número total de entradas vendidas sumando el tamaño de la lista de entradas de cada compra.
        int entradasVendidas = compras.stream().mapToInt(compra -> compra.getEntradas().size()).sum();
        // Paso 7: Construir el contenido textual del reporte con las métricas calculadas.
        String contenido =
                "Período: " + filtro.getFechaInicio().format(formato)+
                        " - " + filtro.getFechaFin().format(formato) + " " +
                        "Compras realizadas: " + totalCompras + " - " +
                        "Entradas vendidas: " + entradasVendidas + " -  " +
                        "Total vendido: $" + totalVentas;
        // Paso 8: Agregar el contenido textual al constructor del reporte.
        constructor.agregarTexto(contenido);
    }
}
