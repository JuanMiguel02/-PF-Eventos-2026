package lospolimorficos.boletopolis.services;

import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.Asiento;
import lospolimorficos.boletopolis.models.EstadoFactory;
import lospolimorficos.boletopolis.models.TipoZona;

/**
 * <p><b>ServicioEstadoAsientos</b></p>
 *
 * <p>Este servicio se encarga de gestionar la representación visual de los estados de los asientos.
 * Utiliza el patrón Factory para obtener la implementación correcta de {@link lospolimorficos.boletopolis.models.EstadoAsientoState}
 * y aplicarla al {@link Rectangle} que representa el asiento en la interfaz de usuario.</p>
 */
public class ServicioEstadoAsientos {

    /**
     * <p><b>Actualiza la representación visual de un asiento en la interfaz de usuario.</b></p>
     *
     * <p>Este método delega la lógica específica de cómo se debe dibujar un asiento
     * (color, estilo de borde, etc.) a la implementación de {@link lospolimorficos.boletopolis.models.EstadoAsientoState}
     * correspondiente al estado actual del {@link Asiento}.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Verifica si el estado del {@link Asiento} es nulo; si lo es, no realiza ninguna acción.</li>
     *     <li>Obtiene la implementación de {@link lospolimorficos.boletopolis.models.EstadoAsientoState}
     *         correspondiente al estado actual del {@link Asiento} utilizando {@link EstadoFactory#crearEstadoAsiento(lospolimorficos.boletopolis.models.EstadoAsiento)}.</li>
     *     <li>Llama al método {@code aplicar()} de la instancia de {@link lospolimorficos.boletopolis.models.EstadoAsientoState}
     *         obtenida, pasándole el {@link Rectangle} del asiento, su {@link TipoZona} y un indicador
     *         de si está seleccionado. Esto permite que el objeto de estado se encargue de configurar
     *         el aspecto visual del rectángulo.</li>
     * </ol>
     *
     * @param r El {@link Rectangle} de JavaFX que representa visualmente el asiento.
     * @param asiento El objeto {@link Asiento} del modelo, que contiene el estado actual.
     * @param tipo El {@link TipoZona} a la que pertenece el asiento, utilizado para aplicar estilos base.
     * @param esSeleccionado Un valor booleano que indica si el asiento está actualmente seleccionado (por ejemplo, para compra).
     */
    public void actualizarVisualAsiento(Rectangle r, Asiento asiento, TipoZona tipo, boolean esSeleccionado) {
        if (asiento.getEstado() == null) return;

        EstadoFactory.crearEstadoAsiento(asiento.getEstado())
                .aplicar(r, tipo, esSeleccionado);
    }
}
