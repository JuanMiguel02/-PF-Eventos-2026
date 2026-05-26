package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clase abstracta base para representar un evento en el sistema.
 * Contiene información general del evento, su estado, recinto asociado y gestiona observadores
 * para notificar cambios de estado.
 */
public abstract class Evento {
    private final UUID idEvento;
    private String nombre;
    private String descripcion;
    private Ciudad ciudad;
    private LocalDateTime fechaYHora;
    private EstadoEvento estado;
    private Recinto recinto;
    private String rutaImagen;
    private final List<EventoObserver> observadores = new ArrayList<>();
    private boolean permiteReembolso;

    /**
     * Constructor para crear un nuevo Evento.
     *
     * @param nombre El nombre del evento.
     * @param descripcion La descripción del evento.
     * @param ciudad La ciudad donde se realizará el evento.
     * @param fechaYHora La fecha y hora del evento.
     * @param recinto El {@link Recinto} donde se llevará a cabo el evento.
     */
    public Evento(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto) {
        this.idEvento = UUID.randomUUID();
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.fechaYHora = fechaYHora;
        this.estado = EstadoEvento.BORRADOR; // Estado inicial por defecto.
        this.recinto = recinto;
        this.permiteReembolso = false; // Por defecto, no permite reembolso.
    }

    /**
     * Verifica si el evento permite reembolsos.
     *
     * @return {@code true} si el evento permite reembolsos, {@code false} en caso contrario.
     */
    public boolean permiteReembolso() {
        return permiteReembolso;
    }

    /**
     * Establece si el evento permite reembolsos.
     *
     * @param permiteReembolso {@code true} para permitir reembolsos, {@code false} para no permitirlos.
     */
    public void setPermiteReembolso(boolean permiteReembolso) {
        this.permiteReembolso = permiteReembolso;
    }

    /**
     * Agrega un observador a la lista de observadores del evento.
     *
     * @param observador El {@link EventoObserver} a agregar.
     */
    public void agregarObservador(EventoObserver observador){
        observadores.add(observador);
    }

    /**
     * Elimina un observador de la lista de observadores del evento.
     *
     * @param observer El {@link EventoObserver} a eliminar.
     */
    public void eliminarObservador(EventoObserver observer){
        observadores.remove(observer);
    }

    /**
     * Notifica a todos los observadores registrados sobre un cambio en el estado del evento.
     */
    private void notificarObservadores(){
        for(EventoObserver observador : observadores){
            observador.actualizarEvento(this, estado);
        }
    }

    /**
     * Cambia el estado del evento y notifica a todos los observadores si el estado ha cambiado.
     * Si el estado es CANCELADO, se reembolsa el dinero a todos los compradores.
     *
     * @param nuevoEstado El nuevo {@link EstadoEvento} para el evento.
     */
    public void cambiarEstado(EstadoEvento nuevoEstado){
        if(this.estado != nuevoEstado){
            this.estado = nuevoEstado;
            notificarObservadores();

            if (nuevoEstado == EstadoEvento.CANCELADO) {
                reembolsarATodos();
            }
        }
    }

    /**
     * Reembolsa el dinero a todos los clientes que compraron entradas para este evento.
     */
    private void reembolsarATodos() {
        // Obtenemos el repositorio de compras para encontrar todas las compras de este evento.
        // Nota: CompraRepositorio es un singleton.
        lospolimorficos.boletopolis.repositorios.CompraRepositorio repo =
                lospolimorficos.boletopolis.repositorios.CompraRepositorio.getInstancia();

        lospolimorficos.boletopolis.services.ServicioCompra servicioCompra =
                new lospolimorficos.boletopolis.services.ServicioCompra();

        for (Compra compra : repo.getCompras()) {
            if (compra.getEvento().getIdEvento().equals(this.idEvento) &&
                    compra.getEstadoCompra() == EstadoCompra.PAGADA) {
                servicioCompra.reembolsarCompra(compra);
                repo.actualizarCompra(compra);
            }
        }
    }

    /**
     * Obtiene la capacidad total del evento, delegando al recinto asociado.
     *
     * @return La capacidad total del evento.
     */
    public int getCapacidad(){
        return recinto.getCapacidad();
    }

    /**
     * Obtiene el ID único del evento.
     *
     * @return El UUID del evento.
     */
    public UUID getIdEvento() {
        return idEvento;
    }

    /**
     * Obtiene el nombre del evento.
     *
     * @return El nombre del evento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del evento.
     *
     * @param nombre El nuevo nombre del evento.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del evento.
     *
     * @return La descripción del evento.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del evento.
     *
     * @param descripcion La nueva descripción del evento.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la ciudad donde se realizará el evento.
     *
     * @return La {@link Ciudad} del evento.
     */
    public Ciudad getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad donde se realizará el evento.
     *
     * @param ciudad La nueva {@link Ciudad} del evento.
     */
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Obtiene la fecha y hora del evento.
     *
     * @return La {@link LocalDateTime} del evento.
     */
    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    /**
     * Establece la fecha y hora del evento.
     *
     * @param fechaYHora La nueva {@link LocalDateTime} del evento.
     */
    public void setFechaYHora(LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    /**
     * Obtiene el estado actual del evento.
     *
     * @return El {@link EstadoEvento} del evento.
     */
    public EstadoEvento getEstado() {
        return estado;
    }

    /**
     * Establece el estado del evento.
     *
     * @param estado El nuevo {@link EstadoEvento} del evento.
     */
    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el recinto asociado al evento.
     *
     * @return El {@link Recinto} del evento.
     */
    public Recinto getRecinto() {
        return recinto;
    }

    /**
     * Establece el recinto asociado al evento.
     *
     * @param recinto El nuevo {@link Recinto} del evento.
     */
    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    /**
     * Obtiene la ruta de la imagen asociada al evento.
     *
     * @return La ruta de la imagen como String.
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    /**
     * Establece la ruta de la imagen asociada al evento.
     *
     * @param rutaImagen La nueva ruta de la imagen como String.
     */
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}
