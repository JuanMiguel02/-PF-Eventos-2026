package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaRecinto;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import lospolimorficos.boletopolis.services.GeneradorRecinto;

import java.util.ArrayList;
import java.util.List;


public final class RecintoRepositorio {

    private final ObservableList<Recinto> recintos = FXCollections.observableArrayList();
    private static RecintoRepositorio instancia;

    private RecintoRepositorio() {
        cargarDatosEjemplo();
    }

    public static RecintoRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new RecintoRepositorio();
        }
        return instancia;
    }

    public boolean registrarRecinto(Recinto recinto) {
        return recintos.add(recinto);
    }

    public boolean eliminarRecinto(Recinto recinto) {
        return recintos.remove(recinto);
    }

    public boolean actualizarRecinto(Recinto recintoActualizado) {
        for(int i = 0; i < recintos.size(); i++) {
            if(recintos.get(i).getIdRecinto().equals(recintoActualizado.getIdRecinto())) {

                recintos.set(i, recintoActualizado);
                return true;
            }
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    public ObservableList<Recinto> getRecintos() {
        return recintos;
    }

    public int contarRecintos(){
        return recintos.size();
    }

    private void cargarDatosEjemplo(){
        PlantillaZona zona1 = new PlantillaZona("VIP-1", PosicionZona.SUR, TipoZona.VIP, 2, 7, 60000);
        PlantillaZona zona2 = new PlantillaZona("VIP-2", PosicionZona.NORTE, TipoZona.VIP, 2, 7, 60000);
        PlantillaZona zona3 = new PlantillaZona("GENERAL-1", PosicionZona.ESTE, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona zona4 = new PlantillaZona("GENERAL-2", PosicionZona.OESTE, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona zona5 = new PlantillaZona("GENERAL-3", PosicionZona.SUR, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona zona6= new PlantillaZona("GENERAL-4", PosicionZona.NORTE, TipoZona.GENERAL, 8, 8, 30000);

        List<PlantillaZona> plantillaZonas = new ArrayList<>();
        plantillaZonas.add(zona1);
        plantillaZonas.add(zona2);
        plantillaZonas.add(zona3);
        plantillaZonas.add(zona4);
        plantillaZonas.add(zona5);
        plantillaZonas.add(zona6);

        PlantillaRecinto plantillaRecinto = new PlantillaRecinto("Estadio de Pacho", plantillaZonas);
        Recinto recinto = GeneradorRecinto.generarRecinto(plantillaRecinto, "Calle 123, Col. Centro", Ciudad.ARMENIA);
        recinto.setEscenario(new Escenario(PosicionEscenario.CENTRO));
        registrarRecinto(recinto);
    }

    public Recinto getPrimerRecinto(){
        return recintos.getFirst();
    }

}
