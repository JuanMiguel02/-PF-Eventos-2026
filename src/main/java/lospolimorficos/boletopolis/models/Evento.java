package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Evento {
    private final UUID idEvento;
    private String nombre;
    private String descripcion;
    private Ciudad ciudad;
    private LocalDateTime fechaYHora;
    private EstadoEvento estado;
    private Recinto recinto;
    private Duration duracion;

    public Evento(String descripcion, String nombre, Ciudad ciudad, LocalDateTime fechaYHora, EstadoEvento estado, Recinto recinto, Duration duracion) {
        this.idEvento = UUID.randomUUID();
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.fechaYHora = fechaYHora;
        this.estado = estado;
        this.recinto = recinto;
        this.duracion = duracion;
    }
}
