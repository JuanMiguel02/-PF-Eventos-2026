package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Compra {

    private final UUID idCompra;
    private Cliente cliente;
    private Evento evento;
    private LocalDateTime fechaCompra;
    private EstadoCompra estadoCompra;
    private List<Entrada> entradas;
    private List<ServicioAdicional> servicios;
    private Pago pago;

    public Compra(Cliente cliente, Evento evento) {
        this.idCompra = UUID.randomUUID();
        this.cliente = cliente;
        this.evento = evento;
        this.fechaCompra = LocalDateTime.now();
        this.estadoCompra = EstadoCompra.CREADA;
        this.entradas = new ArrayList<>();
        this.servicios = new ArrayList<>();
    }

    public void agregarServicio(ServicioAdicional servicio){
        this.servicios.add(servicio);
    }

    public double calcularTotalServicios(){
        return servicios.stream()
                .mapToDouble(ServicioAdicional::getPrecio)
                .sum();
    }

    public double calcularTotalEntradas(){
        return entradas.stream()
                .mapToDouble(Entrada::getPrecioFinal)
                .sum();
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public double calcularTotalCompra(){
        return calcularTotalServicios() + calcularTotalEntradas();
    }

    public int getCantidadEntradas(){
        return entradas.size();
    }

    public UUID getIdCompra() {
        return idCompra;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Evento getEvento() {
        return evento;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public double getTotalCompra() {
        return calcularTotalCompra();
    }

    public EstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public List<ServicioAdicional> getServicios() {
        return servicios;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }


    public void setEstadoCompra(EstadoCompra estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }
}
