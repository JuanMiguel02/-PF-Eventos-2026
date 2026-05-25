package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.services.ServicioInteraccionAsientos;

/**
 * <p><b>InteraccionStrategy</b></p>
 *
 * <p>Esta interfaz define el contrato para las diferentes estrategias de interacción
 * con los asientos en el recinto. Permite cambiar el comportamiento de un asiento
 * cuando se hace clic en él, así como determinar si es interactuable,
 * sin modificar la clase {@link Asiento} o el servicio de dibujo.</p>
 *
 * <p>Implementa el patrón de diseño Strategy.</p>
 */
public interface InteraccionStrategy {

    /**
     * Define la acción a realizar cuando se hace clic en un {@link Asiento}.
     * La lógica específica de la interacción (cambio de estado, selección, etc.)
     * dependerá de la implementación concreta de esta interfaz.
     *
     * @param asiento El {@link Asiento} en el que se hizo clic.
     * @param servicio Una instancia de {@link ServicioInteraccionAsientos} para
     *                 realizar operaciones auxiliares como notificar cambios o
     *                 gestionar la selección de asientos.
     */
    void onClick(Asiento asiento, ServicioInteraccionAsientos servicio);

    /**
     * Determina si un {@link Asiento} es interactuable (por ejemplo, si el cursor
     * del mouse debe cambiar a "mano"). Esto puede depender del estado actual del asiento
     * y del modo de interacción.
     *
     * @param asiento El {@link Asiento} a verificar.
     * @return {@code true} si el asiento es interactuable en la estrategia actual,
     *         {@code false} en caso contrario.
     */
    boolean esInteractuable(Asiento asiento);

}

