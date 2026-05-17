package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompraController {

    private final CompraRepositorio compraRepositorio = CompraRepositorio.getInstancia();

    public boolean registrarCompra(Compra compra){
        return compraRepositorio.registrarCompra(compra);
    }

    public boolean eliminarCompra(Compra compra){
        return compraRepositorio.eliminarCompra(compra);
    }

    public ObservableList<Compra> getCompras(){
        return compraRepositorio.getCompras();
    }

    public List<Compra> filtrarCompras(List<Compra> compras, String filtro){
        if(filtro == null || filtro.isEmpty()){
            return compras;
        }
        String filtroLimpio = filtro.toLowerCase();
        return compras.stream()
                .filter(compra -> compra.getCliente().getNombre().toLowerCase().contains(filtroLimpio)
                        || compra.getCliente().getNumDocumento().contains(filtroLimpio)
                        || compra.getEvento().getNombre().toLowerCase().contains(filtroLimpio))
                .toList();
    }

    public Compra realizarCompra(Cliente cliente, Evento evento, List<Asiento> asientos, Map<Asiento, Zona> zonaAsientoMap){
        double total = 0;
        List<Entrada> entradas = new ArrayList<>();

        for(Asiento asiento: asientos){
            Zona zona = zonaAsientoMap.get(asiento);
            total += zona.getPrecioZona();

            Entrada entrada = new Entrada(zona, asiento, zona.getPrecioZona(), EstadoEntrada.ACTIVA);
            entradas.add(entrada);

            asiento.setEstado(EstadoAsiento.VENDIDO);

        }
        Compra compra = new Compra(cliente, evento, total);
        compra.setEntradas(entradas);
        compra.setEstadoCompra(EstadoCompra.PAGADA);
        return compra;
    }
}
