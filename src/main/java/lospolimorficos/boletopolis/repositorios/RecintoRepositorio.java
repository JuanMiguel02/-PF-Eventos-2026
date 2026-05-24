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
        throw new IllegalArgumentException("Recinto no encontrado");
    }

    public ObservableList<Recinto> getRecintos() {
        return recintos;
    }

    public int contarRecintos(){
        return recintos.size();
    }

    private void cargarDatosEjemplo(){
        PlantillaZona recintoZona1 = new PlantillaZona("VIP-1", PosicionZona.SUR, TipoZona.VIP, 2, 7, 60000);
        PlantillaZona recintoZona2 = new PlantillaZona("VIP-2", PosicionZona.NORTE, TipoZona.VIP, 2, 7, 60000);
        PlantillaZona recintoZona3 = new PlantillaZona("GENERAL-1", PosicionZona.ESTE, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona recintoZona4 = new PlantillaZona("GENERAL-2", PosicionZona.OESTE, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona recintoZona5 = new PlantillaZona("GENERAL-3", PosicionZona.SUR, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona recintoZona6= new PlantillaZona("GENERAL-4", PosicionZona.NORTE, TipoZona.GENERAL, 8, 8, 30000);

        List<PlantillaZona> plantillaZonas = new ArrayList<>();
        plantillaZonas.add(recintoZona1);
        plantillaZonas.add(recintoZona2);
        plantillaZonas.add(recintoZona3);
        plantillaZonas.add(recintoZona4);
        plantillaZonas.add(recintoZona5);
        plantillaZonas.add(recintoZona6);

        PlantillaRecinto plantillaRecinto = new PlantillaRecinto("Estadio de Pacho", plantillaZonas);
        Recinto recinto = GeneradorRecinto.generarRecinto(plantillaRecinto, "Calle 123, Col. Centro", Ciudad.ARMENIA);
        recinto.setEscenario(new Escenario(PosicionEscenario.CENTRO));
        registrarRecinto(recinto);

        PlantillaZona recinto2Zona1 = new PlantillaZona("PREFERENCIAL-1", PosicionZona.SUR, TipoZona.PREFERENCIAL, 2, 5, 70000);
        PlantillaZona recinto2Zona2 = new PlantillaZona("GENERAL-1", PosicionZona.SUR, TipoZona.GENERAL, 4, 6, 50000);
        PlantillaZona recinto2Zona3 = new PlantillaZona("GENERAL-2", PosicionZona.SUR, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona recinto2Zona4 = new PlantillaZona("VIP-2", PosicionZona.ESTE, TipoZona.VIP, 6, 2, 80000);
        PlantillaZona recinto2Zona5 = new PlantillaZona("VIP-3", PosicionZona.OESTE, TipoZona.VIP, 6, 2, 80000);
        PlantillaZona recinto2Zona6 = new PlantillaZona("GENERAL-3", PosicionZona.ESTE, TipoZona.GENERAL, 8, 8, 50000);
        PlantillaZona recinto2Zona7 = new PlantillaZona("GENERAL-4", PosicionZona.OESTE, TipoZona.GENERAL, 8, 8, 50000);

        List<PlantillaZona> plantillaZonas2 = new ArrayList<>();
        plantillaZonas2.add(recinto2Zona1);
        plantillaZonas2.add(recinto2Zona2);
        plantillaZonas2.add(recinto2Zona3);
        plantillaZonas2.add(recinto2Zona4);
        plantillaZonas2.add(recinto2Zona5);
        plantillaZonas2.add(recinto2Zona6);
        plantillaZonas2.add(recinto2Zona7);

        PlantillaRecinto plantillaRecinto2 = new PlantillaRecinto("Estadio Casablanca", plantillaZonas2);
        Recinto recinto2 = GeneradorRecinto.generarRecinto(plantillaRecinto2, "Calle 18, Carrera #14", Ciudad.PEREIRA);
        recinto2.setEscenario(new Escenario(PosicionEscenario.ARRIBA));
        registrarRecinto(recinto2);


    }

    public Recinto getPrimerRecinto(){
        return recintos.getFirst();
    }

}
