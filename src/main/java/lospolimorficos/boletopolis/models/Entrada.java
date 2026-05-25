package lospolimorficos.boletopolis.models;

import java.util.UUID;

/**
 * Representa una entrada individual para un evento, asociada a una zona y un asiento específicos.
 * Contiene información sobre su precio final y su estado actual.
 */
public class Entrada {
    private final UUID idEntrada;
    private Zona zona;
    private Asiento asiento;
    private double precioFinal;
    private EstadoEntrada estado;

    /**
     * Constructor para crear una nueva Entrada.
     *
     * @param zona La {@link Zona} a la que pertenece esta entrada.
     * @param asiento El {@link Asiento} específico al que corresponde esta entrada.
     * @param precioFinal El precio final de la entrada, incluyendo posibles recargos o descuentos.
     * @param estado El {@link EstadoEntrada} inicial de la entrada.
     */
    public Entrada(Zona zona, Asiento asiento, double precioFinal, EstadoEntrada estado) {
        this.idEntrada = UUID.randomUUID();
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estado = estado;
    }

    /**
     * Obtiene el ID único de la entrada.
     *
     * @return El UUID de la entrada.
     */
    public UUID getIdEntrada() {
        return idEntrada;
    }

    /**
     * Obtiene la zona a la que pertenece esta entrada.
     *
     * @return La {@link Zona} de la entrada.
     */
    public Zona getZona() {
        return zona;
    }

    /**
     * Establece la zona a la que pertenece esta entrada.
     *
     * @param zona La nueva {@link Zona} de la entrada.
     */
    public void setZona(Zona zona) {
        this.zona = zona;
    }

    /**
     * Obtiene el asiento específico al que corresponde esta entrada.
     *
     * @return El {@link Asiento} de la entrada.
     */
    public Asiento getAsiento() {
        return asiento;
    }

    /**
     * Establece el asiento específico al que corresponde esta entrada.
     *
     * @param asiento El nuevo {@link Asiento} de la entrada.
     */
    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    /**
     * Obtiene el estado actual de la entrada.
     *
     * @return El {@link EstadoEntrada} de la entrada.
     */
    public EstadoEntrada getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la entrada.
     *
     * @param estado El nuevo {@link EstadoEntrada} de la entrada.
     */
    public void setEstado(EstadoEntrada estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el precio final de la entrada.
     *
     * @return El precio final de la entrada.
     */
    public double getPrecioFinal() {
        return precioFinal;
    }

    /**
     * Establece el precio final de la entrada.
     *
     * @param precioFinal El nuevo precio final de la entrada.
     */
    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
}
