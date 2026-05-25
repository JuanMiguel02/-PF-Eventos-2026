package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.services.ServicioCompra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class CompraRepositorio {

    private final ObservableList<Compra> compras = FXCollections.observableArrayList();
    private static CompraRepositorio instancia;
    private boolean datosEjemploCargados;

    private CompraRepositorio() {
    }

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

    public void actualizarCompra(Compra compraActualizada) {

        for (int i = 0; i < compras.size(); i++) {

            if (compras.get(i).getIdCompra().equals(compraActualizada.getIdCompra())) {
                compras.set(i, compraActualizada);
            }
        }

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

    public double calcularGananciaPorEvento(Evento evento){
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

    public Map<String, Number> obtenerVentasPorMes(){
        Map<String, Double> ventasPorMes = new LinkedHashMap<>();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("MMMM");
        for(Compra compra : compras){
            String mes = compra.getFechaCompra().format(formato);
            ventasPorMes.put(mes, ventasPorMes.getOrDefault(mes, 0.0) + compra.getTotalCompra());
        }
        return new LinkedHashMap<>(ventasPorMes);
    }

    public void cargarDatosEjemplo() {

        if (datosEjemploCargados) {
            return;
        }

        EventoRepositorio eventoRepositorio = EventoRepositorio.getInstancia();
        UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstancia();

        ServicioCompra servicioCompra = new ServicioCompra();

        List<Evento> eventos = eventoRepositorio.getEventos();
        List<Cliente> clientes = usuarioRepositorio.getClientes();

        if (eventos.isEmpty() || clientes.isEmpty()) {
            return;
        }

        datosEjemploCargados = true;

        Random random = new Random();

        for (int i = 0; i < 25; i++) {

            // Cliente aleatorio
            Cliente cliente = clientes.get(random.nextInt(clientes.size()));

            // Evento aleatorio
            Evento evento = eventos.get(random.nextInt(eventos.size()));

            // Método de pago aleatorio
            if (cliente.getMetodosPago().isEmpty()) {
                continue;
            }

            MetodoPago metodoPago = cliente.getMetodosPago()
                            .get(random.nextInt(cliente.getMetodosPago().size()));

            // Obtener asientos disponibles
            List<Asiento> disponibles = new ArrayList<>();
            Map<Asiento, Zona> zonaAsientoMap = new HashMap<>();

            for (Zona zona : evento.getRecinto().getZonas()) {

                for (Asiento asiento : zona.getAsientos()) {

                    if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {

                        disponibles.add(asiento);
                        zonaAsientoMap.put(asiento, zona);
                    }
                }
            }

            // Si no hay asientos disponibles
            if (disponibles.isEmpty()) {
                continue;
            }

            // Mezclar asientos
            Collections.shuffle(disponibles);

            // Cantidad entre 1 y 5
            int cantidad = random.nextInt(5) + 1;

            // Evitar exceder disponibles
            cantidad = Math.min(cantidad, disponibles.size());

            // Seleccionar asientos
            List<Asiento> asientosSeleccionados =
                    disponibles.subList(0, cantidad);

            // Realizar compra usando el servicio
            Compra compra = servicioCompra.realizarCompra(cliente, evento, asientosSeleccionados, zonaAsientoMap, metodoPago);

            // Si el pago falló
            if (compra == null) {
                continue;
            }

            // Fecha aleatoria últimos 6 meses
            compra.setFechaCompra(
                    LocalDateTime.now()
                            .minusDays(random.nextInt(180))
            );

            // Guardar compra
            compras.add(compra);
        }
    }

}
