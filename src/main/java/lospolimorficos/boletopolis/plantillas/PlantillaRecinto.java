package lospolimorficos.boletopolis.plantillas;

import java.util.List;

public class PlantillaRecinto {
    private String nombre;
    private List<PlantillaZona> zonas;

    public PlantillaRecinto(String nombre, List<PlantillaZona> zonas) {
        this.nombre = nombre;
        this.zonas = zonas;
    }

    public String getNombre() {
        return nombre;
    }

    public List<PlantillaZona> getZonas() {
        return zonas;
    }
}
