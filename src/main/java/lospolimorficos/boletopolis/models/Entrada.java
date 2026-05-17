package lospolimorficos.boletopolis.models;

import java.util.UUID;

public class Entrada {
    private final UUID idEntrada;
    private Zona zona;
    private Asiento asiento;
    private double precioFinal;
    private EstadoEntrada estado;

    public Entrada(Zona zona, Asiento asiento, double precioFinal, EstadoEntrada estado) {
        this.idEntrada = UUID.randomUUID();
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estado = estado;
    }

    public UUID getIdEntrada() {
        return idEntrada;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntrada estado) {
        this.estado = estado;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
}
