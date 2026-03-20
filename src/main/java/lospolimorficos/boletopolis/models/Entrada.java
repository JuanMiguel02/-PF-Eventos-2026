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
}
