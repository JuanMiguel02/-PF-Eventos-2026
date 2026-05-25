package lospolimorficos.boletopolis.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <p><b>ReservadoState</b></p>
 *
 * <p>Esta implementación de {@link EstadoAsientoState} define la apariencia visual
 * de un asiento cuando se encuentra en el estado {@link EstadoAsiento#RESERVADO}.</p>
 */
public class ReservadoState implements EstadoAsientoState {

    /**
     * <p><b>Aplica la configuración visual para un asiento RESERVADO.</b></p>
     *
     * <p>Un asiento reservado se representa con un color de relleno naranja sólido y un borde negro.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Limpia cualquier estilo CSS previo del {@code rectangulo}.</li>
     *     <li>Establece el color de relleno a {@link Color#ORANGE}.</li>
     *     <li>Establece el color del borde a {@link Color#BLACK} con un ancho de 0.5.</li>
     *     <li>Limpia cualquier patrón de guiones del borde para que sea sólido.</li>
     * </ol>
     *
     * @param rectangulo El {@link Rectangle} de JavaFX que representa visualmente el asiento.
     * @param tipoZona El {@link TipoZona} a la que pertenece el asiento (no se utiliza en este estado).
     * @param seleccionado Un valor booleano que indica si el asiento está seleccionado (no se utiliza en este estado).
     */
    @Override
    public void aplicar(Rectangle rectangulo, TipoZona tipoZona, boolean seleccionado) {
        rectangulo.setStyle(""); // Limpiar estilo previo
        rectangulo.setFill(Color.ORANGE);
        rectangulo.setStroke(Color.BLACK);
        rectangulo.setStrokeWidth(0.5);
        rectangulo.getStrokeDashArray().clear(); // Borde sólido
    }
}
