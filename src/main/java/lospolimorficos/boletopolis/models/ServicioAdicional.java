package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los servicios adicionales que pueden ser ofrecidos en un evento,
 * junto con su precio asociado.
 */
public enum ServicioAdicional {
    /**
     * Servicio de parqueadero con un precio de 50000.
     */
    PARQUEADERO(50000),
    /**
     * Servicio de comida con un precio de 30000.
     */
    COMIDA(30000),
    /**
     * Servicio de merchandising con un precio de 80000.
     */
    MERCHANDISING(80000);

    private final double precio;

    /**
     * Constructor para {@code ServicioAdicional}.
     *
     * @param precio El costo del servicio adicional.
     */
    ServicioAdicional(double precio){
        this.precio = precio;
    }

    /**
     * Obtiene el precio del servicio adicional.
     *
     * @return El precio del servicio.
     */
    public double getPrecio(){
        return this.precio;
    }
}
