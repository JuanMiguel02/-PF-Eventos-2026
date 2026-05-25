package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servicio que encapsula la lógica de negocio relacionada con las operaciones de compra y reembolso.
 * Interactúa con {@link ServicioPago} para gestionar las transacciones monetarias.
 */
public class ServicioCompra {

    private final ServicioPago servicioPago;

    /**
     * Constructor para {@code ServicioCompra}.
     * Inicializa el {@link ServicioPago} que se utilizará para procesar pagos y reembolsos.
     */
    public ServicioCompra() {
        this.servicioPago = new ServicioPago();
    }

    /**
     * Procesa el reembolso de una compra.
     * Si el reembolso del pago es exitoso, actualiza el estado de la compra y libera los asientos.
     *
     * @param compra La {@link Compra} a reembolsar.
     * @return {@code true} si el reembolso fue exitoso, {@code false} en caso contrario.
     */
    public boolean reembolsarCompra(Compra compra) {
        // Paso 1: Verificar si la compra o su pago asociado son nulos. Si lo son, el reembolso no es posible.
        if (compra == null || compra.getPago() == null) {
            return false;
        }

        // Paso 2: Intentar reembolsar el pago a través del ServicioPago.
        boolean exito = servicioPago.reembolsarPago(compra.getPago());

        // Paso 3: Si el reembolso del pago fue exitoso, actualizar el estado de la compra y liberar los asientos.
        if (exito) {
            // Paso 3.1: Cambiar el estado de la compra a REEMBOLSADA.
            compra.setEstadoCompra(EstadoCompra.REEMBOLSADA);

            // Paso 3.2: Iterar sobre todas las entradas de la compra para cancelarlas y liberar los asientos.
            for (Entrada entrada : compra.getEntradas()) {
                // Paso 3.2.1: Cambiar el estado de la entrada a CANCELADA.
                entrada.setEstado(EstadoEntrada.CANCELADA);

                // Paso 3.2.2: Si la entrada tiene un asiento asociado, cambiar el estado del asiento a DISPONIBLE.
                if (entrada.getAsiento() != null) {
                    entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
                }
            }
        }
        // Paso 4: Devolver el resultado del reembolso.
        return exito;
    }

    /**
     * Realiza el proceso completo de una compra, incluyendo la creación de entradas, el procesamiento del pago
     * y la actualización de los estados de los asientos y la compra.
     *
     * @param cliente El {@link Cliente} que realiza la compra.
     * @param evento El {@link Evento} para el cual se compran las entradas.
     * @param asientos La lista de {@link Asiento}s seleccionados por el cliente.
     * @param zonaAsientoMap Un mapa que relaciona cada {@link Asiento} con su {@link Zona} correspondiente.
     * @param metodoPago El {@link MetodoPago} seleccionado por el cliente.
     * @return El objeto {@link Compra} si la operación fue exitosa, o {@code null} si el pago falló.
     */
    public Compra realizarCompra(Cliente cliente, Evento evento, List<Asiento> asientos, Map<Asiento, Zona> zonaAsientoMap, MetodoPago metodoPago) {
        // Paso 1: Inicializar una lista para las entradas y una variable para el total de la compra.
        List<Entrada> entradas = new ArrayList<>();
        double total = 0;

        // Paso 2: Crear las entradas individuales y calcular el costo total de las mismas.
        for (Asiento asiento : asientos) {
            // Paso 2.1: Obtener la zona a la que pertenece el asiento.
            Zona zona = zonaAsientoMap.get(asiento);

            // Paso 2.2: Sumar el precio de la zona al total de la compra.
            total += zona.getPrecioZona();

            // Paso 2.3: Crear una nueva instancia de Entrada.
            Entrada entrada = new Entrada(
                    zona,
                    asiento,
                    zona.getPrecioZona(),
                    EstadoEntrada.ACTIVA
            );

            // Paso 2.4: Añadir la entrada a la lista.
            entradas.add(entrada);
        }

        // Paso 3: Crear el objeto Compra con el cliente y el evento.
        Compra compra = new Compra(cliente, evento);
        // Paso 3.1: Asociar las entradas creadas a la compra.
        compra.setEntradas(entradas);
        // Paso 3.2: Establecer el estado inicial de la compra como CREADA.
        compra.setEstadoCompra(EstadoCompra.CREADA);

        // Paso 4: Crear el objeto Pago con la compra, el método de pago y el total calculado.
        Pago pago = new Pago(compra, metodoPago, total);

        // Paso 5: Procesar el pago utilizando el ServicioPago.
        boolean pagoExitoso = servicioPago.procesarPago(pago);

        // Paso 6: Si el pago no fue exitoso, cancelar la compra y devolver null.
        if (!pagoExitoso) {
            compra.setEstadoCompra(EstadoCompra.CANCELADA);
            return null;
        }

        // Paso 7: Si el pago fue exitoso, asociar el pago a la compra.
        compra.setPago(pago);

        // Paso 8: Cambiar el estado de la compra a PAGADA.
        compra.setEstadoCompra(EstadoCompra.PAGADA);

        // Paso 9: Marcar todos los asientos seleccionados como VENDIDOS.
        for (Asiento asiento : asientos) {
            asiento.setEstado(EstadoAsiento.VENDIDO);
        }

        // Paso 10: Asociar la compra al cliente.
        cliente.agregarCompra(compra);

        // Paso 11: Devolver el objeto Compra exitosamente realizada.
        return compra;
    }
}
