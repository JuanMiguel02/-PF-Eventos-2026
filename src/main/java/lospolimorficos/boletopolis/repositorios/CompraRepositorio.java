package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.services.ServicioCompra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Repositorio para la gestión de objetos {@link Compra}.
 * Implementa el patrón Singleton para asegurar una única instancia global.
 * Proporciona métodos para almacenar, recuperar, actualizar y eliminar compras,
 * así como para obtener métricas relacionadas con las ventas.
 */
public final class CompraRepositorio {

    private final ObservableList<Compra> compras = FXCollections.observableArrayList();
    private static CompraRepositorio instancia;
    private boolean datosEjemploCargados;

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private CompraRepositorio() {
    }

    /**
     * Obtiene la única instancia de {@code CompraRepositorio}.
     * Si la instancia no ha sido creada, la inicializa.
     *
     * @return La instancia de {@code CompraRepositorio}.
     */
    public static CompraRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new CompraRepositorio();
        }
        return instancia;
    }

    /**
     * Obtiene la lista observable de todas las compras registradas.
     *
     * @return Una {@link ObservableList} de objetos {@link Compra}.
     */
    public ObservableList<Compra> getCompras() {
        return compras;
    }

    /**
     * Registra una nueva compra en el repositorio.
     *
     * @param compra El objeto {@link Compra} a registrar.
     * @return {@code true} si la compra fue añadida exitosamente, {@code false} en caso contrario.
     */
    public boolean registrarCompra(Compra compra) {
        return compras.add(compra);
    }

    /**
     * Elimina una compra del repositorio.
     *
     * @param compra El objeto {@link Compra} a eliminar.
     * @return {@code true} si la compra fue eliminada exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarCompra(Compra compra) {
        return compras.remove(compra);
    }

    /**
     * Actualiza una compra existente en el repositorio.
     * Busca la compra por su ID y la reemplaza con la versión actualizada.
     *
     * @param compraActualizada El objeto {@link Compra} con la información actualizada.
     */
    public void actualizarCompra(Compra compraActualizada) {
        // Paso 1: Iterar sobre la lista de compras para encontrar la compra a actualizar.
        for (int i = 0; i < compras.size(); i++) {
            // Paso 1.1: Comparar el ID de la compra actual con el ID de la compra actualizada.
            if (compras.get(i).getIdCompra().equals(compraActualizada.getIdCompra())) {
                // Paso 1.2: Si los IDs coinciden, reemplazar la compra existente con la versión actualizada.
                compras.set(i, compraActualizada);
                // Paso 1.3: Salir del bucle una vez que la compra ha sido actualizada.
                break;
            }
        }
    }

    /**
     * Cuenta el número total de compras registradas en el repositorio.
     *
     * @return El número total de compras.
     */
    public int contarCompras(){
        return compras.size();
    }

    /**
     * Obtiene el número total de entradas vendidas para un evento específico.
     *
     * @param evento El {@link Evento} para el cual se quieren contar las ventas.
     * @return El número total de entradas vendidas para el evento.
     */
    public int obtenerVentasEvento(Evento evento){
        return compras.stream()
                .filter(c -> c.getEvento().getIdEvento().equals(evento.getIdEvento())) // Filtrar compras por el ID del evento.
                .mapToInt(Compra::getCantidadEntradas) // Mapear cada compra a la cantidad de entradas que contiene.
                .sum(); // Sumar todas las cantidades de entradas.
    }

    /**
     * Calcula la ganancia total generada por un evento específico.
     *
     * @param evento El {@link Evento} para el cual se quiere calcular la ganancia.
     * @return La ganancia total del evento.
     */
    public double calcularGananciaPorEvento(Evento evento){
        return compras.stream()
                .filter(c -> c.getEvento().getIdEvento().equals(evento.getIdEvento())) // Filtrar compras por el ID del evento.
                .mapToDouble(Compra::getTotalCompra) // Mapear cada compra a su monto total.
                .sum(); // Sumar todos los montos totales.
    }

    /**
     * Obtiene una lista de compras realizadas dentro de un período de tiempo específico.
     *
     * @param fechaInicio La fecha y hora de inicio del período (inclusive).
     * @param fechaFin La fecha y hora de fin del período (inclusive).
     * @return Una lista de {@link Compra}s que caen dentro del rango de fechas.
     */
    public List<Compra> obtenerComprasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin){
        return compras.stream()
                .filter(compra ->
                        !compra.getFechaCompra().isBefore(fechaInicio) // La fecha de compra no es anterior a la fecha de inicio.
                        &&
                        !compra.getFechaCompra().isAfter(fechaFin) // La fecha de compra no es posterior a la fecha de fin.
                ).toList();
    }

    /**
     * Obtiene un mapa que resume las ventas totales por mes.
     *
     * @return Un {@link Map} donde la clave es el nombre del mes (String) y el valor
     *         es el total de ventas (Number) para ese mes.
     */
    public Map<String, Number> obtenerVentasPorMes(){
        Map<String, Double> ventasPorMes = new LinkedHashMap<>();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("MMMM"); // Formato para obtener el nombre completo del mes.
        for(Compra compra : compras){
            String mes = compra.getFechaCompra().format(formato); // Obtener el nombre del mes de la fecha de compra.
            ventasPorMes.put(mes, ventasPorMes.getOrDefault(mes, 0.0) + compra.getTotalCompra()); // Sumar el total de la compra al mes correspondiente.
        }
        return new LinkedHashMap<>(ventasPorMes); // Devolver un LinkedHashMap para mantener el orden de inserción.
    }

    /**
     * Carga datos de ejemplo en el repositorio de compras.
     * Genera compras aleatorias para clientes y eventos existentes, simulando el proceso de compra.
     */
    public void cargarDatosEjemplo() {
        // Paso 1: Verificar si los datos de ejemplo ya han sido cargados para evitar duplicados.
        if (datosEjemploCargados) {
            return;
        }

        // Paso 2: Obtener instancias de otros repositorios necesarios.
        EventoRepositorio eventoRepositorio = EventoRepositorio.getInstancia();
        UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstancia();

        // Paso 3: Crear una instancia del servicio de compra para simular las transacciones.
        ServicioCompra servicioCompra = new ServicioCompra();

        // Paso 4: Obtener listas de eventos y clientes existentes.
        List<Evento> eventos = eventoRepositorio.getEventos();
        List<Cliente> clientes = usuarioRepositorio.getClientes();

        // Paso 5: Si no hay eventos o clientes, no se pueden generar compras de ejemplo.
        if (eventos.isEmpty() || clientes.isEmpty()) {
            return;
        }

        // Paso 6: Marcar que los datos de ejemplo están siendo cargados.
        datosEjemploCargados = true;

        // Paso 7: Inicializar un generador de números aleatorios.
        Random random = new Random();

        // Paso 8: Generar 25 compras de ejemplo.
        for (int i = 0; i < 25; i++) {
            // Paso 8.1: Seleccionar un cliente aleatorio de la lista de clientes.
            Cliente cliente = clientes.get(random.nextInt(clientes.size()));

            // Paso 8.2: Seleccionar un evento aleatorio de la lista de eventos.
            Evento evento = eventos.get(random.nextInt(eventos.size()));

            // Paso 8.3: Seleccionar un método de pago aleatorio del cliente.
            // Si el cliente no tiene métodos de pago, se salta esta iteración.
            if (cliente.getMetodosPago().isEmpty()) {
                continue;
            }
            MetodoPago metodoPago = cliente.getMetodosPago()
                            .get(random.nextInt(cliente.getMetodosPago().size()));

            // Paso 8.4: Identificar los asientos disponibles para el evento.
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

            // Paso 8.5: Si no hay asientos disponibles para el evento, se salta esta iteración.
            if (disponibles.isEmpty()) {
                continue;
            }

            // Paso 8.6: Mezclar la lista de asientos disponibles para seleccionar aleatoriamente.
            Collections.shuffle(disponibles);

            // Paso 8.7: Determinar una cantidad aleatoria de asientos a comprar (entre 1 y 5).
            int cantidad = random.nextInt(5) + 1;

            // Paso 8.8: Asegurarse de no exceder la cantidad de asientos realmente disponibles.
            cantidad = Math.min(cantidad, disponibles.size());

            // Paso 8.9: Seleccionar un subconjunto de asientos disponibles para la compra.
            List<Asiento> asientosSeleccionados =
                    disponibles.subList(0, cantidad);

            // Paso 8.10: Realizar la compra utilizando el servicio de compra.
            Compra compra = servicioCompra.realizarCompra(cliente, evento, asientosSeleccionados, zonaAsientoMap, metodoPago);

            // Paso 8.11: Si la compra falló (ej. pago rechazado), se salta esta iteración.
            if (compra == null) {
                continue;
            }

            // Paso 8.12: Asignar una fecha de compra aleatoria dentro de los últimos 6 meses.
            compra.setFechaCompra(LocalDateTime.now().minusDays(random.nextInt(180)));

            // Paso 8.13: Guardar la compra exitosa en el repositorio.
            compras.add(compra);
        }
    }

}
