package lospolimorficos.boletopolis.plantillas;

import java.util.List;

/**
 * Clase que representa una plantilla para la creación de un recinto.
 * Contiene el nombre del recinto y una lista de plantillas de zona que lo componen.
 */
public record PlantillaRecinto(String nombre, List<PlantillaZona> zonas) {
    /**
     * Constructor para crear una nueva PlantillaRecinto.
     *
     * @param nombre El nombre de la plantilla de recinto.
     * @param zonas  Una lista de {@link PlantillaZona} que definen las zonas de este recinto.
     */

}
