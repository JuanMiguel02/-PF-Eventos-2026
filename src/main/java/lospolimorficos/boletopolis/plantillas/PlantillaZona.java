package lospolimorficos.boletopolis.plantillas;

import lospolimorficos.boletopolis.models.PosicionZona;
import lospolimorficos.boletopolis.models.TipoZona;

/**
 * Clase que representa una plantilla para la creación de una zona dentro de un recinto.
 * Define las características estructurales y de precio de una zona.
 */
public record PlantillaZona(String nombre, PosicionZona posicionZona, TipoZona tipoZona, int filas, int columnas, double precioBase) {
    /**
     * Constructor para crear una nueva PlantillaZona.
     *
     * @param nombre       El nombre de la zona.
     * @param posicionZona La {@link PosicionZona} de la zona dentro del recinto.
     * @param tipoZona     El {@link TipoZona} de la zona (e.g., VIP, General).
     * @param filas        El número de filas de asientos en esta zona.
     * @param columnas     El número de columnas de asientos en esta zona.
     * @param precioBase   El precio base de un asiento en esta zona.
     */


    /**
     * Calcula la capacidad total de la zona (número total de asientos).
     *
     * @return La capacidad total de la zona.
     */
    public int calcularCapacidad() {
        return filas * columnas;
    }
}
