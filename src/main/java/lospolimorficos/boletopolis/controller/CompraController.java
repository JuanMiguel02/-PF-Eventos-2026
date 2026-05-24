package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompraController {

    private final CompraRepositorio compraRepositorio = CompraRepositorio.getInstancia();
    private final EventoRepositorio eventoRepositorio = EventoRepositorio.getInstancia();

    public boolean registrarCompra(Compra compra){
        return compraRepositorio.registrarCompra(compra);
    }

    public boolean eliminarCompra(Compra compra){
        return compraRepositorio.eliminarCompra(compra);
    }

    public ObservableList<Compra> getCompras(){
        return compraRepositorio.getCompras();
    }

    public Map<String, Number> obtenerVentasPorEvento(){
        Map<String, Number> datos = new LinkedHashMap<>();
        for(Evento evento : eventoRepositorio.getEventos()){
            int ventas = compraRepositorio.obtenerVentasEvento(evento);
            datos.put(evento.getNombre(), ventas);
        }
        return datos;
    }

    public Map<String, Number> obtenerTopEventos(){
        Map<String, Number> datos = new LinkedHashMap<>();
        for(MetricaEvento evento : eventoRepositorio.obtenerTopEventos(5)){
            double ocupacionRedondeada = Math.round(evento.ocupacion() * 100.0) / 100.0;
            datos.put(evento.nombre(), ocupacionRedondeada);
        }
        return datos;
    }

    public Map<String, Number> obtenerVentasPorMes(){
        return compraRepositorio.obtenerVentasPorMes();
    }

    public List<Compra> filtrarCompras(List<Compra> compras, String filtro){
        if(filtro == null || filtro.isEmpty()){
            return compras;
        }
        String filtroLimpio = filtro.toLowerCase();
        return compras.stream()
                .filter(compra -> compra.getCliente().getNombre().toLowerCase().contains(filtroLimpio)
                        || compra.getCliente().getDocumento().contains(filtroLimpio)
                        || compra.getEvento().getNombre().toLowerCase().contains(filtroLimpio))
                .toList();
    }

    public Compra realizarCompra(Cliente cliente, Evento evento, List<Asiento> asientos, Map<Asiento, Zona> zonaAsientoMap){
        double total = 0;
        List<Entrada> entradas = new ArrayList<>();

        for(Asiento asiento: asientos){
            Zona zona = zonaAsientoMap.get(asiento);

            Entrada entrada = new Entrada(zona, asiento, zona.getPrecioZona(), EstadoEntrada.ACTIVA);
            entradas.add(entrada);

            asiento.setEstado(EstadoAsiento.VENDIDO);

        }
        Compra compra = new Compra(cliente, evento);
        compra.setEntradas(entradas);
        compra.setEstadoCompra(EstadoCompra.PAGADA);
        cliente.agregarCompra(compra);
        return compra;
    }
}
