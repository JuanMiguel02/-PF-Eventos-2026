package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Compra;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;

import java.util.List;

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
}
