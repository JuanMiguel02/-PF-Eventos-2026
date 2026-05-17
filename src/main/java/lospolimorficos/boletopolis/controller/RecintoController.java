package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Recinto;
import lospolimorficos.boletopolis.models.TipoZona;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;

import java.util.List;

public class RecintoController {

    private final RecintoRepositorio recintoRepositorio = RecintoRepositorio.getInstancia();

    public long generarNumeroZona(List<PlantillaZona> plantillas, TipoZona tipoSeleccionado){
       return plantillas.stream()
                .filter(z -> z.getTipoZona() == tipoSeleccionado)
                .count();
    }

    public int calcularCapacidadTotal(List<PlantillaZona> plantillas) {
        int numero = 0;
        for(PlantillaZona plantilla : plantillas) {
            numero += plantilla.calcularCapacidad();
        }
        System.out.println("Capacidad de: " + numero);
        return numero;
    }

    public List<Recinto> filtrarRecintos(List<Recinto> recintos, String filtro){
        if(filtro == null || filtro.isEmpty()){
            return recintos;
        }
        String filtroLimpio = filtro.toLowerCase();
        return recintos.stream()
                .filter(recinto -> recinto.getNombre().toLowerCase().contains(filtroLimpio)
                                        || recinto.getIdRecinto().toString().contains(filtroLimpio)
                                        || recinto.getDireccion().toLowerCase().contains(filtroLimpio)
                                        || recinto.getCiudad().toString().toLowerCase().contains(filtroLimpio)
                                        || String.valueOf(recinto.getCapacidad()).contains(filtroLimpio))
                .toList();
    }

    public boolean registrarRecinto(Recinto recinto){
        return recintoRepositorio.registrarRecinto(recinto);
    }

    public boolean eliminarRecinto(Recinto recinto){
        return recintoRepositorio.eliminarRecinto(recinto);
    }

    public boolean actualizarRecinto(Recinto recintoActualizado){
        return recintoRepositorio.actualizarRecinto(recintoActualizado);
    }

    public ObservableList<Recinto> getRecintos(){
        return recintoRepositorio.getRecintos();
    }

}
