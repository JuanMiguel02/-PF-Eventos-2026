package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.Asiento;
import lospolimorficos.boletopolis.models.Ciudad;
import lospolimorficos.boletopolis.models.Recinto;
import lospolimorficos.boletopolis.models.Zona;
import lospolimorficos.boletopolis.plantillas.PlantillaRecinto;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;

public class GeneradorRecinto {

    public static Recinto generarRecinto(PlantillaRecinto plantilla, String direccion, Ciudad ciudad){

        Recinto recinto = new Recinto(plantilla.getNombre(), direccion, ciudad);

        for(PlantillaZona pZona : plantilla.getZonas()){

            int capacidad = pZona.getFilas() * pZona.getColumnas();

            Zona zona = new Zona(pZona.getNombre(), capacidad, pZona.getTipoZona(),pZona.getPosicionZona(), pZona.getPrecioBase());

            for(int fila = 1; fila <= pZona.getFilas(); fila++){
                for(int columna = 1; columna <= pZona.getColumnas(); columna++){
                    Asiento asiento = new Asiento(fila, columna);
                    zona.agregarAsiento(asiento);
                }
            }
            recinto.agregarZona(zona);
        }

       return recinto;
    }
}
