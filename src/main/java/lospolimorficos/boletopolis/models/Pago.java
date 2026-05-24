package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pago {

    private final UUID idPago;
    private Compra compra;
    private MetodoPago metodoPago;
    private double monto;
    private EstadoPago estadoPago;
    private LocalDateTime fechaPago;

    public Pago(Compra compra, MetodoPago metodoPago, double monto){
        this.idPago = UUID.randomUUID();
        this.compra = compra;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.fechaPago = LocalDateTime.now();
        this.estadoPago = EstadoPago.PENDIENTE;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public UUID getIdPago() {
        return idPago;
    }
}
