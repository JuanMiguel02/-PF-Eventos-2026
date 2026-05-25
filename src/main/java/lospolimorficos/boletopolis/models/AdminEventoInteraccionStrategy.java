package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.services.ServicioInteraccionAsientos;

/**
 * <p><b>AdminEventoInteraccionStrategy</b></p>
 *
 * <p>Esta implementación de {@link InteraccionStrategy} define el comportamiento
 * de interacción para los asientos cuando el usuario está en el modo de
 * administración de evento. En este modo, al hacer clic en un asiento,
 * su estado cicla a través de todos los posibles valores de {@link EstadoAsiento}
 * (DISPONIBLE, RESERVADO, VENDIDO, BLOQUEADO).</p>
 *
 * <p>Todos los asientos son considerados interactuables en este modo.</p>
 */
public class AdminEventoInteraccionStrategy implements InteraccionStrategy {

    /**
     * <p><b>Maneja el evento de clic en un asiento en el modo de administración de evento.</b></p>
     *
     * <p>Al hacer clic, el estado del asiento cicla al siguiente estado disponible
     * en la enumeración {@link EstadoAsiento}. Después de cambiar el estado,
     * se notifica al servicio de interacción que el asiento ha cambiado.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Obtiene el {@link EstadoAsiento} actual del {@code asiento}.</li>
     *     <li>Obtiene un arreglo de todos los valores de la enumeración {@link EstadoAsiento}.</li>
     *     <li>Calcula el índice del siguiente estado en el ciclo, usando el operador módulo
     *         para volver al principio de la enumeración si se alcanza el final.</li>
     *     <li>Obtiene el nuevo {@link EstadoAsiento} utilizando el índice calculado.</li>
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
        EstadoAsiento[] estados = EstadoAsiento.values();
        int siguienteIndex = (actual.ordinal() + 1) % estados.length;
        EstadoAsiento nuevo = estados[siguienteIndex];
        asiento.setEstado(nuevo);
        servicio.notifyAsientoChanged();
    }

    /**
     * Determina si un asiento es interactuable en el modo de administración de evento.
     * En este modo, todos los asientos son siempre interactuables.
     *
     * @param asiento El {@link Asiento} a verificar (no se utiliza en esta implementación).
     * @return Siempre {@code true}, indicando que todos los asientos son interactuables.
     */
    @Override
    public boolean esInteractuable(Asiento asiento) {
        return true; // Todos los asientos son interactuables en modo admin evento
    }
}
