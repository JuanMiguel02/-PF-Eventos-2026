package lospolimorficos.boletopolis.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <p><b>DisponibleState</b></p>
 *
 * <p>Esta implementación de {@link EstadoAsientoState} define la apariencia visual
 * de un asiento cuando se encuentra en el estado {@link EstadoAsiento#DISPONIBLE}.</p>
 *
 * <p>La apariencia puede variar si el asiento está seleccionado para compra.</p>
 */
public class DisponibleState implements EstadoAsientoState {

    /**
     * <p><b>Aplica la configuración visual para un asiento DISPONIBLE.</b></p>
     *
     * <p>Si el asiento está seleccionado para compra, se muestra de color verde sólido.
     * Si no está seleccionado, su color se basa en el estilo de la {@link TipoZona}
     * y tiene un borde punteado para indicar disponibilidad.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li><b>Si {@code seleccionado} es {@code true} (asiento seleccionado para compra):</b>
     *         <ul>
     *             <li>Limpia cualquier estilo CSS previo del {@code rectangulo}.</li>
     *             <li>Establece el color de relleno a {@link Color#GREEN}.</li>
     *             <li>Establece el color del borde a {@link Color#BLACK} con un ancho de 0.5.</li>
     *             <li>Limpia cualquier patrón de guiones del borde para que sea sólido.</li>
     *         </ul>
     *     </li>
     *     <li><b>Si {@code seleccionado} es {@code false} (asiento DISPONIBLE pero no seleccionado):</b>
     *         <ul>
     *             <li>Establece el relleno del {@code rectangulo} a {@code null} para que el color
     *                 base provenga del estilo CSS de la {@link TipoZona}.</li>
     *             <li>Aplica el estilo CSS de la {@code tipoZona} al {@code rectangulo}.</li>
     *             <li>Establece el color del borde a {@link Color#BLACK} con un ancho de 0.5.</li>
     *             <li>Establece un patrón de guiones (2.0, 2.0) para el borde, indicando que es punteado.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param rectangulo El {@link Rectangle} de JavaFX que representa visualmente el asiento.
     * @param tipoZona El {@link TipoZona} a la que pertenece el asiento, para obtener su estilo base.
     * @param seleccionado Un valor booleano que indica si el asiento está seleccionado para compra.
     */
    @Override
    public void aplicar(Rectangle rectangulo, TipoZona tipoZona, boolean seleccionado) {
        if (seleccionado) {
            rectangulo.setStyle(""); // Limpiar estilo previo
            rectangulo.setFill(Color.GREEN);
            rectangulo.setStroke(Color.BLACK);
            rectangulo.setStrokeWidth(0.5);
            rectangulo.getStrokeDashArray().clear(); // Borde sólido
        } else {
            rectangulo.setFill(null); // Limpiar color sólido previo si existe
            rectangulo.setStyle(tipoZona.getEstilo()); // Color base de la zona
            rectangulo.setStroke(Color.BLACK);
            rectangulo.setStrokeWidth(0.5);
            rectangulo.getStrokeDashArray().setAll(2.0, 2.0); // Borde punteado
        }
    }
}
