package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private List<EventoObserver> observadores = new ArrayList<>();

    public Evento(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto, Duration duracion) {
        this.idEvento = UUID.randomUUID();
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.fechaYHora = fechaYHora;
        this.estado = EstadoEvento.BORRADOR;
        this.recinto = recinto;
        this.duracion = duracion;
    }

    public void agregarObservador(EventoObserver observador){
        observadores.add(observador);
    }

    public void eliminarObservador(EventoObserver observer){
        observadores.remove(observer);
    }

    private void notificarObservadores(){
        for(EventoObserver observador : observadores){
            observador.actualizarEvento(this, estado);
        }
    }

    public void cambiarEstado(EstadoEvento nuevoEstado){
        if(this.estado != nuevoEstado){
            this.estado = nuevoEstado;
            notificarObservadores();
        }
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
