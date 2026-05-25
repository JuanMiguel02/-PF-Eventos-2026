package lospolimorficos.boletopolis.services;

import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.Asiento;
import lospolimorficos.boletopolis.models.InteraccionStrategy;
import lospolimorficos.boletopolis.models.TipoZona;
import java.util.List;

/**
 * <p><b>ServicioInteraccionAsientos</b></p>
 *
 * <p>Este servicio gestiona la interactividad de los asientos en la interfaz de usuario.
 * Actúa como el "contexto" para el patrón Strategy, permitiendo que el comportamiento
 * de interacción de los asientos cambie dinámicamente. También maneja la selección
 * de asientos y la notificación de cambios.</p>
 */
public class ServicioInteraccionAsientos {

    private Runnable onAsientoChanged;
    private final List<Asiento> asientosSeleccionados;
    private InteraccionStrategy strategy;
    private boolean interactivo = false;

    /**
     * Constructor del servicio de interacción de asientos.
     *
     * @param asientosSeleccionados Una {@link List} mutable de {@link Asiento}s que se utilizará
     *                               para mantener un registro de los asientos seleccionados.
     */
    public ServicioInteraccionAsientos(List<Asiento> asientosSeleccionados) {
        this.asientosSeleccionados = asientosSeleccionados;
    }

    /**
     * Establece un {@link Runnable} que se ejecutará cada vez que el estado o la selección de un asiento cambie.
     * Esto permite que los controladores de la UI reaccionen a los cambios en los asientos.
     *
     * @param onAsientoChanged El {@link Runnable} a ejecutar.
     */
    public void setOnAsientoChanged(Runnable onAsientoChanged) {
        this.onAsientoChanged = onAsientoChanged;
    }

    /**
     * Establece la {@link InteraccionStrategy} actual que definirá cómo los asientos
     * responderán a los clics del usuario.
     *
     * @param strategy La {@link InteraccionStrategy} a utilizar.
     */
    public void setStrategy(InteraccionStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Habilita o deshabilita la interactividad general de los asientos.
     * Si es {@code true}, los asientos responderán a los clics según la estrategia configurada.
     *
     * @param interactivo {@code true} para habilitar la interacción, {@code false} para deshabilitarla.
     */
    public void setInteractivo(boolean interactivo) {
        this.interactivo = interactivo;
    }

    /**
     * Verifica si la interactividad de los asientos está habilitada.
     * @return {@code true} si los asientos son interactivos, {@code false} en caso contrario.
     */
    public boolean isInteractivo() {
        return interactivo;
    }

    /**
     * Obtiene la {@link InteraccionStrategy} actualmente configurada.
     * @return La {@link InteraccionStrategy} activa.
     */
    public InteraccionStrategy getStrategy() {
        return strategy;
    }

    /**
     * Notifica a los suscriptores (a través del {@link Runnable} {@code onAsientoChanged})
     * que ha ocurrido un cambio en un asiento.
     */
    public void notifyAsientoChanged() {
        if (onAsientoChanged != null) {
            onAsientoChanged.run();
        }
    }

    /**
     * Obtiene la lista de asientos que están actualmente seleccionados.
     * @return Una {@link List} de {@link Asiento}s seleccionados.
     */
    public List<Asiento> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    /**
     * <p><b>Configura la interactividad y la información emergente (Tooltip) para un asiento.</b></p>
     *
     * <p>Este método se encarga de adjuntar los manejadores de eventos y actualizar
     * el cursor del mouse según la estrategia de interacción actual y el estado del asiento.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Actualiza el {@link Tooltip} del {@link Rectangle} del asiento para mostrar su ID y estado actual.</li>
     *     <li><b>Si la interactividad está habilitada ({@code interactivo} es {@code true}):</b>
     *         <ul>
     *             <li>Adjunta un manejador de eventos {@code setOnMouseClicked} al {@link Rectangle}.
     *                 Cuando se hace clic en el asiento:
     *                 <ul>
     *                     <li>Llama al método {@code onClick()} de la {@link InteraccionStrategy} actual,
     *                         pasándole el {@link Asiento} y una referencia a este servicio.</li>
     *                     <li>Después de la interacción, actualiza la apariencia visual del asiento
     *                         llamando a {@code servicioEstado.actualizarVisualAsiento()}.</li>
     *                     <li>Vuelve a actualizar el {@link Tooltip} del asiento.</li>
     *                 </ul>
     *             </li>
     *             <li>Verifica si el asiento es interactuable según la {@link InteraccionStrategy} actual
     *                 utilizando {@code strategy.esInteractuable()}.</li>
     *             <li>Si es interactuable, establece el estilo del cursor a "hand" (mano) para indicar
     *                 que es clicable; de lo contrario, lo establece a "default".</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param r El {@link Rectangle} de JavaFX que representa visualmente el asiento.
     * @param asiento El objeto {@link Asiento} del modelo.
     * @param tipo El {@link TipoZona} a la que pertenece el asiento.
     * @param servicioEstado Una instancia de {@link ServicioEstadoAsientos} para actualizar la visualización del asiento.
     */
    public void configurarAsiento(Rectangle r, Asiento asiento, TipoZona tipo, ServicioEstadoAsientos servicioEstado) {
        actualizarTooltip(r, asiento);

        if (interactivo) {
            r.setOnMouseClicked(event -> {
                strategy.onClick(asiento, this);
                
                // Re-apply state after interaction to update visual
                servicioEstado.actualizarVisualAsiento(r, asiento, tipo, asientosSeleccionados.contains(asiento));
                actualizarTooltip(r, asiento);
            });

            if (strategy.esInteractuable(asiento)) {
                r.setStyle(r.getStyle() + "; -fx-cursor: hand;");
            } else {
                r.setStyle(r.getStyle() + "; -fx-cursor: default;");
            }
        }
    }

    /**
     * Actualiza el {@link Tooltip} de un {@link Rectangle} de asiento para mostrar
     * el ID del asiento y su estado actual.
     *
     * @param r El {@link Rectangle} visual del asiento.
     * @param asiento El objeto {@link Asiento} del modelo.
     */
    public void actualizarTooltip(Rectangle r, Asiento asiento) {
        Tooltip.install(r, new Tooltip("Asiento: " + asiento.getIdAsiento() + "\nEstado: " + asiento.getEstado()));
    }

    /**
     * Alterna la selección de un asiento en la lista de {@code asientosSeleccionados}.
     * Si el asiento ya está en la lista, se elimina; si no, se añade.
     * Este método es utilizado principalmente por la {@link CompraInteraccionStrategy}.
     *
     * @param asiento El {@link Asiento} cuya selección se va a alternar.
     */
    public void toggleSeleccionCompra(Asiento asiento) {
        if (asientosSeleccionados.contains(asiento)) {
            asientosSeleccionados.remove(asiento);
        } else {
            asientosSeleccionados.add(asiento);
        }
    }

    /**
     * Limpia la lista de asientos actualmente seleccionados.
     */
    public void limpiarSeleccion() {
        asientosSeleccionados.clear();
    }
}
