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
    private String rutaImagen;

    public Evento(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, EstadoEvento estado, Recinto recinto, Duration duracion) {
        this.idEvento = UUID.randomUUID();
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.fechaYHora = fechaYHora;
        this.estado = estado;
        this.recinto = recinto;
        this.duracion = duracion;
    }

    public int getCapacidad(){
        return recinto.getCapacidad();
    }

    public UUID getIdEvento() {
        return idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public Recinto getRecinto() {
        return recinto;
    }

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) {
        this.duracion = duracion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}
