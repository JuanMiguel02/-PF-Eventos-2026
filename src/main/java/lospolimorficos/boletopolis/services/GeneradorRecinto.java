package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.Asiento;
import lospolimorficos.boletopolis.models.Ciudad;
import lospolimorficos.boletopolis.models.Recinto;
import lospolimorficos.boletopolis.models.Zona;
import lospolimorficos.boletopolis.plantillas.PlantillaRecinto;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;

/**
 * Clase de servicio encargada de generar objetos {@link Recinto} a partir de plantillas predefinidas.
 * Facilita la creación de recintos con sus zonas y asientos de manera estructurada.
 */
public class GeneradorRecinto {

    /**
     * Genera un objeto {@link Recinto} completamente configurado a partir de una {@link PlantillaRecinto}.
     * Este método crea las zonas y los asientos dentro de cada zona según las especificaciones de la plantilla.
     *
     * @param plantilla La {@link PlantillaRecinto} que define la estructura del recinto.
     * @param direccion La dirección física del nuevo recinto.
     * @param ciudad La {@link Ciudad} donde se ubicará el recinto.
     * @return Un nuevo objeto {@link Recinto} con todas sus zonas y asientos inicializados.
     */
    public static Recinto generarRecinto(PlantillaRecinto plantilla, String direccion, Ciudad ciudad){
        // Paso 1: Crear una nueva instancia de Recinto utilizando el nombre de la plantilla, la dirección y la ciudad.
        Recinto recinto = new Recinto(plantilla.nombre(), direccion, ciudad);

        // Paso 2: Iterar sobre cada PlantillaZona definida en la PlantillaRecinto.
        for(PlantillaZona pZona : plantilla.zonas()){
            // Paso 2.1: Calcular la capacidad de la zona multiplicando el número de filas por el número de columnas.
            int capacidad = pZona.filas() * pZona.columnas();

            // Paso 2.2: Crear una nueva instancia de Zona con los atributos definidos en la PlantillaZona.
            Zona zona = new Zona(pZona.nombre(), capacidad, pZona.tipoZona(),pZona.posicionZona(), pZona.precioBase());

            // Paso 2.3: Generar los asientos para la zona actual.
            // Iterar desde la fila 1 hasta el número de filas especificado en la plantilla.
            for(int fila = 1; fila <= pZona.filas(); fila++){
                // Iterar desde la columna 1 hasta el número de columnas especificado en la plantilla.
                for(int columna = 1; columna <= pZona.columnas(); columna++){
                    // Crear un nuevo Asiento con la fila y columna actuales.
                    Asiento asiento = new Asiento(fila, columna);
                    // Agregar el asiento a la zona.
                    zona.agregarAsiento(asiento);
                }
            }
            // Paso 2.4: Agregar la zona completamente configurada al recinto.
            recinto.agregarZona(zona);
        }
        // Paso 3: Devolver el recinto generado.
        return recinto;
    }
}
