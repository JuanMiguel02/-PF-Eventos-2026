package lospolimorficos.boletopolis.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Compra {

    private final UUID idCompra;
    private Cliente cliente;
    private Evento evento;
    private LocalDate fechaCompra;
    private double totalCompra;
    private EstadoCompra estadoCompra;
    private List<Entrada> entradas;

    public Compra(Cliente cliente, Evento evento, LocalDate fechaCompra, double totalCompra) {
        this.idCompra = UUID.randomUUID();
        this.cliente = cliente;
        this.evento = evento;
        this.fechaCompra = fechaCompra;
        this.totalCompra = totalCompra;
        this.estadoCompra = EstadoCompra.CREADA;
        this.entradas = new ArrayList<>();
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

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public EstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public void setEstadoCompra(EstadoCompra estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }
}
