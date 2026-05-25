package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.services.ServicioInteraccionAsientos;

/**
 * <p><b>AdminRecintoInteraccionStrategy</b></p>
 *
 * <p>Esta implementación de {@link InteraccionStrategy} define el comportamiento
 * de interacción para los asientos cuando el usuario está en el modo de
 * administración de recinto. En este modo, al hacer clic en un asiento,
 * su estado alterna entre {@link EstadoAsiento#DISPONIBLE} y {@link EstadoAsiento#BLOQUEADO}.</p>
 *
 * <p>Todos los asientos son considerados interactuables en este modo.</p>
 */
public class AdminRecintoInteraccionStrategy implements InteraccionStrategy {

    /**
     * <p><b>Maneja el evento de clic en un asiento en el modo de administración de recinto.</b></p>
     *
     * <p>Al hacer clic, el estado del asiento se alterna entre DISPONIBLE y BLOQUEADO.
     * Después de cambiar el estado, se notifica al servicio de interacción que el asiento ha cambiado.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Obtiene el {@link EstadoAsiento} actual del {@code asiento}.</li>
     *     <li>Determina el nuevo estado: si el estado actual es BLOQUEADO, el nuevo estado será DISPONIBLE;
     *         de lo contrario, el nuevo estado será BLOQUEADO.</li>
     *     <li>Establece el nuevo estado en el {@code asiento}.</li>
     *     <li>Llama a {@code servicio.notifyAsientoChanged()} para informar que el estado del asiento ha sido modificado.</li>
     * </ol>
     *
     * @param asiento El {@link Asiento} en el que se hizo clic.
     * @param servicio La instancia de {@link ServicioInteraccionAsientos} para notificar cambios.
     */
    @Override
    public void onClick(Asiento asiento, ServicioInteraccionAsientos servicio) {
        EstadoAsiento actual = asiento.getEstado();
        EstadoAsiento nuevo = (actual == EstadoAsiento.BLOQUEADO) ? EstadoAsiento.DISPONIBLE : EstadoAsiento.BLOQUEADO;
        asiento.setEstado(nuevo);
        servicio.notifyAsientoChanged();
    }

    /**
     * Determina si un asiento es interactuable en el modo de administración de recinto.
     * En este modo, todos los asientos son siempre interactuables.
     *
     * @param asiento El {@link Asiento} a verificar (no se utiliza en esta implementación).
     * @return Siempre {@code true}, indicando que todos los asientos son interactuables.
     */
    @Override
    public boolean esInteractuable(Asiento asiento) {
        return true; // Todos los asientos son interactuables en modo admin recinto
    }
}
