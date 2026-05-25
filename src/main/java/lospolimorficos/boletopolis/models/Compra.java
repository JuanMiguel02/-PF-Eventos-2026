package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa una compra realizada por un cliente para un evento específico.
 * Contiene detalles como el cliente, el evento, la fecha de compra, el estado,
 * las entradas adquiridas, los servicios adicionales y el pago asociado.
 */
public class Compra {

    private final UUID idCompra;
    private Cliente cliente;
    private Evento evento;
    private LocalDateTime fechaCompra;
    private EstadoCompra estadoCompra;
    private List<Entrada> entradas;
    private final List<ServicioAdicional> servicios;
    private Pago pago;

    /**
     * Constructor para crear una nueva Compra.
     *
     * @param cliente El {@link Cliente} que realiza la compra.
     * @param evento El {@link Evento} para el cual se realiza la compra.
     */
    public Compra(Cliente cliente, Evento evento) {
        this.idCompra = UUID.randomUUID();
        this.cliente = cliente;
        this.evento = evento;
        this.fechaCompra = LocalDateTime.now();
        this.estadoCompra = EstadoCompra.CREADA; // Estado inicial por defecto.
        this.entradas = new ArrayList<>();
        this.servicios = new ArrayList<>();
    }

    /**
     * Agrega un servicio adicional a la compra.
     *
     * @param servicio El {@link ServicioAdicional} a agregar.
     */
    public void agregarServicio(ServicioAdicional servicio){
        this.servicios.add(servicio);
    }

    /**
     * Calcula el costo total de todos los servicios adicionales agregados a la compra.
     *
     * @return El monto total de los servicios adicionales.
     */
    public double calcularTotalServicios(){
        return servicios.stream()
                .mapToDouble(ServicioAdicional::getPrecio)
                .sum();
    }

    /**
     * Calcula el costo total de todas las entradas incluidas en la compra.
     *
     * @return El monto total de las entradas.
     */
    public double calcularTotalEntradas(){
        return entradas.stream()
                .mapToDouble(Entrada::getPrecioFinal)
                .sum();
    }

    /**
     * Obtiene el pago asociado a esta compra.
     *
     * @return El {@link Pago} de la compra.
     */
    public Pago getPago() {
        return pago;
    }

    /**
     * Establece el pago asociado a esta compra.
     *
     * @param pago El {@link Pago} a asociar con la compra.
     */
    public void setPago(Pago pago) {
        this.pago = pago;
    }

    /**
     * Calcula el monto total de la compra, sumando el costo de las entradas y los servicios adicionales.
     *
     * @return El monto total de la compra.
     */
    public double calcularTotalCompra(){
        return calcularTotalServicios() + calcularTotalEntradas();
    }

    /**
     * Obtiene la cantidad de entradas incluidas en la compra.
     *
     * @return El número de entradas.
     */
    public int getCantidadEntradas(){
        return entradas.size();
    }

    /**
     * Obtiene el ID único de la compra.
     *
     * @return El UUID de la compra.
     */
    public UUID getIdCompra() {
        return idCompra;
    }

    /**
     * Obtiene el cliente que realizó la compra.
     *
     * @return El {@link Cliente} de la compra.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Obtiene el evento asociado a la compra.
     *
     * @return El {@link Evento} de la compra.
     */
    public Evento getEvento() {
        return evento;
    }

    /**
     * Obtiene la fecha y hora en que se realizó la compra.
     *
     * @return La {@link LocalDateTime} de la compra.
     */
    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    /**
     * Obtiene el monto total de la compra.
     *
     * @return El monto total de la compra.
     */
    public double getTotalCompra() {
        return calcularTotalCompra();
    }

    /**
     * Obtiene el estado actual de la compra.
     *
     * @return El {@link EstadoCompra} de la compra.
     */
    public EstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

    /**
     * Obtiene la lista de entradas incluidas en la compra.
     *
     * @return Una lista de {@link Entrada}s.
     */
    public List<Entrada> getEntradas() {
        return entradas;
    }

    /**
     * Obtiene la lista de servicios adicionales incluidos en la compra.
     *
     * @return Una lista de {@link ServicioAdicional}es.
     */
    public List<ServicioAdicional> getServicios() {
        return servicios;
    }

    /**
     * Establece el cliente que realizó la compra.
     *
     * @param cliente El nuevo {@link Cliente} de la compra.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Establece el evento asociado a la compra.
     *
     * @param evento El nuevo {@link Evento} de la compra.
     */
    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    /**
     * Establece la fecha y hora en que se realizó la compra.
     *
     * @param fechaCompra La nueva {@link LocalDateTime} de la compra.
     */
    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    /**
     * Establece el estado actual de la compra.
     *
     * @param estadoCompra El nuevo {@link EstadoCompra} de la compra.
     */
    public void setEstadoCompra(EstadoCompra estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    /**
     * Establece la lista de entradas incluidas en la compra.
     *
     * @param entradas La nueva lista de {@link Entrada}s.
     */
    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }
}
