package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.CompraRepositorio;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MetricaVentasPorPeriodo implements EstrategiaMetrica{

    private final CompraRepositorio compraRepositorio;
    private final FiltroReporte filtro;

    public MetricaVentasPorPeriodo(CompraRepositorio compraRepositorio, FiltroReporte filtro) {
        this.compraRepositorio = compraRepositorio;
        this.filtro = filtro;
    }

    @Override
    public void generarSeccion(ConstructorReporte constructor) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        constructor.agregarSubtitulo("Ventas por período: ");
        List<Compra> compras = compraRepositorio.obtenerComprasPorPeriodo(filtro.getFechaInicio(), filtro.getFechaFin());
        int totalCompras = compras.size();
        double totalVentas = compras.stream().mapToDouble(Compra::getTotalCompra).sum();
        int entradasVendidas = compras.stream().mapToInt(compra -> compra.getEntradas().size()).sum();
        String contenido =
                "Período: " + filtro.getFechaInicio().format(formato)+
                        " - " + filtro.getFechaFin().format(formato) + "\n" +
                        "Compras realizadas: " + totalCompras + "\n" +
                        "Entradas vendidas: " + entradasVendidas + "\n" +
                        "Total vendido: $" + totalVentas;
        constructor.agregarTexto(contenido);
    }
}
