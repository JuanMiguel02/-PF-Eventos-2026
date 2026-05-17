package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Compra;
import lospolimorficos.boletopolis.models.Evento;

import java.time.LocalDateTime;
import java.util.List;

public final class CompraRepositorio {

    private final ObservableList<Compra> compras = FXCollections.observableArrayList();
    private static CompraRepositorio instancia;

    private CompraRepositorio() {}

    public static CompraRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new CompraRepositorio();
        }
        return instancia;
    }

    public ObservableList<Compra> getCompras() {
        return compras;
    }

    public boolean registrarCompra(Compra compra) {
        return compras.add(compra);
    }

    public boolean eliminarCompra(Compra compra) {
        return compras.remove(compra);
    }

    public int contarCompras(){
        return compras.size();
    }

    public int obtenerVentasEvento(Evento evento){
        return compras.stream()
                .filter(c -> c.getEvento().getIdEvento().equals(evento.getIdEvento()))
                .mapToInt(Compra::getCantidadEntradas)
                .sum();
    }

    public double calcularGananciaPorEveneto(Evento evento){
        return compras.stream()
                .filter(c -> c.getEvento().getIdEvento().equals(evento.getIdEvento()))
                .mapToDouble(Compra::getTotalCompra)
                .sum();
    }

    public List<Compra> obtenerComprasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin){
        return compras.stream()
                .filter(compra ->
                        !compra.getFechaCompra().isBefore(fechaInicio)
                        &&
                        !compra.getFechaCompra().isAfter(fechaFin)
                ).toList();
    }

}
