package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa un pago realizado en el sistema, asociado a una compra, un método de pago,
 * un monto, un estado y una fecha.
 */
public class Pago {

    private final UUID idPago;
    private Compra compra;
    private MetodoPago metodoPago;
    private double monto;
    private EstadoPago estadoPago;
    private final LocalDateTime fechaPago;

    /**
     * Constructor para crear un nuevo Pago.
     *
     * @param compra La {@link Compra} a la que está asociado este pago.
     * @param metodoPago El {@link MetodoPago} utilizado para realizar este pago.
     * @param monto El monto total del pago.
     */
    public Pago(Compra compra, MetodoPago metodoPago, double monto){
        this.idPago = UUID.randomUUID();
        this.compra = compra;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.fechaPago = LocalDateTime.now();
        this.estadoPago = EstadoPago.PENDIENTE; // Por defecto, un pago se crea en estado PENDIENTE.
    }

    /**
     * Obtiene la compra asociada a este pago.
     *
     * @return La {@link Compra} del pago.
     */
    public Compra getCompra() {
        return compra;
    }

    /**
     * Establece la compra asociada a este pago.
     *
     * @param compra La nueva {@link Compra} del pago.
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    /**
     * Obtiene el método de pago utilizado.
     *
     * @return El {@link MetodoPago} del pago.
     */
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    /**
     * Establece el método de pago utilizado.
     *
     * @param metodoPago El nuevo {@link MetodoPago} del pago.
     */
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Obtiene el monto del pago.
     *
     * @return El monto del pago.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Establece el monto del pago.
     *
     * @param monto El nuevo monto del pago.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Obtiene el estado actual del pago.
     *
     * @return El {@link EstadoPago} del pago.
     */
    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    /**
     * Establece el estado del pago.
     *
     * @param estadoPago El nuevo {@link EstadoPago} del pago.
     */
    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    /**
     * Obtiene la fecha y hora en que se realizó el pago.
     *
     * @return La {@link LocalDateTime} del pago.
     */
    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    /**
     * Obtiene el ID único del pago.
     *
     * @return El UUID del pago.
     */
    public UUID getIdPago() {
        return idPago;
    }
}
