package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.services.ServicioCompra;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de compras en el sistema Boletopolis.
 * Proporciona métodos para registrar, eliminar, filtrar compras, obtener métricas
 * relacionadas con ventas y gestionar el proceso de compra y reembolso.
 * Interactúa con {@link CompraRepositorio}, {@link EventoRepositorio} y {@link ServicioCompra}.
 */
public class CompraController {

    private final CompraRepositorio compraRepositorio = CompraRepositorio.getInstancia();
    private final EventoRepositorio eventoRepositorio = EventoRepositorio.getInstancia();

    private final ServicioCompra servicioCompra = new ServicioCompra();

    /**
     * Registra una nueva compra en el sistema.
     *
     * @param compra El objeto {@link Compra} a registrar.
     * @return {@code true} si la compra fue registrada exitosamente, {@code false} en caso contrario.
     */
    public boolean registrarCompra(Compra compra) {
        return compraRepositorio.registrarCompra(compra);
    }

    /**
     * Elimina una compra del sistema.
     *
     * @param compra El objeto {@link Compra} a eliminar.
     * @return {@code true} si la compra fue eliminada exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarCompra(Compra compra) {
        return compraRepositorio.eliminarCompra(compra);
    }

    /**
     * Obtiene una lista observable de todas las compras registradas en el sistema.
     *
     * @return Una {@link ObservableList} de objetos {@link Compra}.
     */
    public ObservableList<Compra> getCompras() {
        return compraRepositorio.getCompras();
    }

    /**
     * Obtiene un mapa con el número de ventas por cada evento.
     *
     * @return Un {@link Map} donde la clave es el nombre del evento (String) y el valor
     *         es el número de ventas (Number) para ese evento.
     */
    public Map<String, Number> obtenerVentasPorEvento() {
        // Paso 1: Inicializar un LinkedHashMap para mantener el orden de inserción de los eventos.
        Map<String, Number> datos = new LinkedHashMap<>();

        // Paso 2: Iterar sobre todos los eventos disponibles en el repositorio de eventos.
        for (Evento evento : eventoRepositorio.getEventos()) {
            // Paso 2.1: Obtener el número de ventas para el evento actual desde el repositorio de compras.
            int ventas = compraRepositorio.obtenerVentasEvento(evento);
            // Paso 2.2: Añadir el nombre del evento y su número de ventas al mapa.
            datos.put(evento.getNombre(), ventas);
        }
        // Paso 3: Devolver el mapa con las ventas por evento.
        return datos;
    }

    /**
     * Obtiene un mapa con la ocupación de los 5 eventos principales.
     * La ocupación se redondea a dos decimales.
     *
     * @return Un {@link Map} donde la clave es el nombre del evento (String) y el valor
     *         es el porcentaje de ocupación (Number) para ese evento.
     */
    public Map<String, Number> obtenerTopEventos() {
        // Paso 1: Inicializar un LinkedHashMap para mantener el orden de inserción de los eventos.
        Map<String, Number> datos = new LinkedHashMap<>();

        // Paso 2: Obtener la lista de los 5 eventos principales (top eventos) desde el repositorio de eventos.
        for (MetricaEvento evento : eventoRepositorio.obtenerTopEventos(5)) {
            // Paso 2.1: Redondear el porcentaje de ocupación del evento a dos decimales.
            double ocupacionRedondeada =
                    Math.round(evento.ocupacion() * 100.0) / 100.0;
            // Paso 2.2: Añadir el nombre del evento y su ocupación redondeada al mapa.
            datos.put(evento.nombre(), ocupacionRedondeada);
        }
        // Paso 3: Devolver el mapa con la ocupación de los top eventos.
        return datos;
    }

    /**
     * Obtiene un mapa con el total de ventas agrupadas por mes.
     *
     * @return Un {@link Map} donde la clave es el nombre del mes (String) y el valor
     *         es el total de ventas (Number) para ese mes.
     */
    public Map<String, Number> obtenerVentasPorMes() {
        return compraRepositorio.obtenerVentasPorMes();
    }

    /**
     * Filtra una lista de compras basándose en un criterio de búsqueda.
     * El filtro se aplica al nombre del cliente, documento del cliente, nombre del evento
     * y estado de la compra.
     *
     * @param compras La lista original de compras a filtrar.
     * @param filtro El texto de búsqueda para filtrar las compras.
     * @return Una nueva lista de compras que coinciden con el filtro. Si el filtro es nulo o vacío,
     *         se devuelve la lista original.
     */
    public List<Compra> filtrarCompras(List<Compra> compras, String filtro) {
        // Paso 1: Verificar si el filtro es nulo o vacío. Si lo es, no se aplica ningún filtro y se devuelve la lista original.
        if (filtro == null || filtro.isEmpty()) {
            return compras;
        }

        // Paso 2: Convertir el filtro a minúsculas para realizar una búsqueda insensible a mayúsculas y minúsculas.
        String filtroLimpio = filtro.toLowerCase();

        // Paso 3: Filtrar la lista de compras utilizando un stream.
        return compras.stream()
                // Paso 3.1: Para cada compra, verificar si alguna de sus propiedades (nombre del cliente,
                // documento del cliente, nombre del evento, estado de la compra) contiene el texto del filtro.
                .filter(compra ->
                        compra.getCliente().getNombre().toLowerCase().contains(filtroLimpio)
                                || compra.getCliente().getDocumento().contains(filtroLimpio)
                                || compra.getEvento().getNombre().toLowerCase().contains(filtroLimpio)
                                || compra.getEstadoCompra().toString().toLowerCase().contains(filtroLimpio)
                )
                // Paso 3.2: Recolectar las compras filtradas en una nueva lista.
                .toList();
    }

    /**
     * Realiza el reembolso de una compra.
     * Si el reembolso es exitoso, actualiza el estado de la compra en el repositorio.
     *
     * @param compra El objeto {@link Compra} a reembolsar.
     * @return {@code true} si el reembolso fue exitoso, {@code false} en caso contrario.
     */
    public boolean reembolsarCompra(Compra compra) {
        // Paso 1: Intentar realizar el reembolso de la compra a través del servicio de compra.
        boolean exito = servicioCompra.reembolsarCompra(compra);

        // Paso 2: Si el reembolso fue exitoso, actualizar el estado de la compra en el repositorio.
        if (exito) {
            compraRepositorio.actualizarCompra(compra);
        }
        // Paso 3: Devolver el resultado del reembolso.
        return exito;
    }

    /**
     * Realiza el proceso de compra de entradas para un evento.
     *
     * @param cliente El {@link Cliente} que realiza la compra.
     * @param evento El {@link Evento} para el cual se compran las entradas.
     * @param asientos La lista de {@link Asiento}s seleccionados.
     * @param zonaAsientoMap Un mapa que relaciona cada {@link Asiento} con su {@link Zona} correspondiente.
     * @param metodoPago El {@link MetodoPago} utilizado para la compra.
     * @return El objeto {@link Compra} resultante si la operación fue exitosa, o {@code null} en caso de fallo.
     */
    public Compra realizarCompra(Cliente cliente, Evento evento, List<Asiento> asientos, Map<Asiento, Zona> zonaAsientoMap, MetodoPago metodoPago) {
        // Delega la lógica de negocio de realizar la compra al servicio de compra.
        return servicioCompra.realizarCompra(cliente, evento, asientos, zonaAsientoMap, metodoPago);
    }
}
