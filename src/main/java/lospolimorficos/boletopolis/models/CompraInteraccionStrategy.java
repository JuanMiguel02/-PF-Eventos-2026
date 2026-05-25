package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.services.ServicioInteraccionAsientos;

/**
 * <p><b>CompraInteraccionStrategy</b></p>
 *
 * <p>Esta implementación de {@link InteraccionStrategy} define el comportamiento
 * de interacción para los asientos cuando el usuario está en el modo de compra.
 * En este modo, solo los asientos {@link EstadoAsiento#DISPONIBLE} son interactuables
 * y, al hacer clic, se alternan entre seleccionados y deseleccionados para la compra.</p>
 */
public class CompraInteraccionStrategy implements InteraccionStrategy {

    /**
     * <p><b>Maneja el evento de clic en un asiento en el modo de compra.</b></p>
     *
     * <p>Solo permite la interacción si el asiento es interactuable (es decir, está DISPONIBLE).
     * Si el asiento es DISPONIBLE, se alterna su selección para la compra y se notifica el cambio.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Verifica si el {@code asiento} es interactuable utilizando {@code esInteractuable(asiento)}.</li>
     *     <li>Si el asiento es interactuable (está DISPONIBLE):
     *         <ul>
     *             <li>Llama a {@code servicio.toggleSeleccionCompra(asiento)} para añadir o quitar el asiento de la lista de seleccionados.</li>
     *             <li>Llama a {@code servicio.notifyAsientoChanged()} para informar que la selección de asientos ha sido modificada.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param asiento El {@link Asiento} en el que se hizo clic.
     * @param servicio La instancia de {@link ServicioInteraccionAsientos} para gestionar la selección y notificar cambios.
     */
    @Override
    public void onClick(Asiento asiento, ServicioInteraccionAsientos servicio) {
        if (esInteractuable(asiento)) {
            servicio.toggleSeleccionCompra(asiento);
            servicio.notifyAsientoChanged();
        }
    }

    /**
     * Determina si un asiento es interactuable en el modo de compra.
     * Un asiento es interactuable solo si su estado es {@link EstadoAsiento#DISPONIBLE}.
     *
     * @param asiento El {@link Asiento} a verificar.
     * @return {@code true} si el asiento está DISPONIBLE, {@code false} en caso contrario.
     */
    @Override
    public boolean esInteractuable(Asiento asiento) {
        return asiento.getEstado() == EstadoAsiento.DISPONIBLE;
    }
}
