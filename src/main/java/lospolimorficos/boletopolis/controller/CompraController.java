package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.services.ServicioCompra;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompraController {

    private final CompraRepositorio compraRepositorio = CompraRepositorio.getInstancia();
    private final EventoRepositorio eventoRepositorio = EventoRepositorio.getInstancia();

    private final ServicioCompra servicioCompra = new ServicioCompra();

    public boolean registrarCompra(Compra compra) {
        return compraRepositorio.registrarCompra(compra);
    }

    public boolean eliminarCompra(Compra compra) {
        return compraRepositorio.eliminarCompra(compra);
    }

    public ObservableList<Compra> getCompras() {
        return compraRepositorio.getCompras();
    }

    public Map<String, Number> obtenerVentasPorEvento() {

        Map<String, Number> datos = new LinkedHashMap<>();

        for (Evento evento : eventoRepositorio.getEventos()) {

            int ventas = compraRepositorio.obtenerVentasEvento(evento);

            datos.put(evento.getNombre(), ventas);
        }

        return datos;
    }

    public Map<String, Number> obtenerTopEventos() {

        Map<String, Number> datos = new LinkedHashMap<>();

        for (MetricaEvento evento : eventoRepositorio.obtenerTopEventos(5)) {

            double ocupacionRedondeada =
                    Math.round(evento.ocupacion() * 100.0) / 100.0;

            datos.put(evento.nombre(), ocupacionRedondeada);
        }

        return datos;
    }

    public Map<String, Number> obtenerVentasPorMes() {
        return compraRepositorio.obtenerVentasPorMes();
    }

    public List<Compra> filtrarCompras(List<Compra> compras, String filtro) {

        if (filtro == null || filtro.isEmpty()) {
            return compras;
        }

        String filtroLimpio = filtro.toLowerCase();

        return compras.stream()
                .filter(compra ->
                        compra.getCliente().getNombre().toLowerCase().contains(filtroLimpio)
                                || compra.getCliente().getDocumento().contains(filtroLimpio)
                                || compra.getEvento().getNombre().toLowerCase().contains(filtroLimpio)
                                || compra.getEstadoCompra().toString().toLowerCase().contains(filtroLimpio)
                )
                .toList();
    }

    public boolean reembolsarCompra(Compra compra) {

        boolean exito = servicioCompra.reembolsarCompra(compra);

        if (exito) {
            compraRepositorio.actualizarCompra(compra);
        }

        return exito;
    }

    public Compra realizarCompra(Cliente cliente, Evento evento, List<Asiento> asientos, Map<Asiento, Zona> zonaAsientoMap, MetodoPago metodoPago) {
        return servicioCompra.realizarCompra(cliente, evento, asientos, zonaAsientoMap, metodoPago);
    }
}