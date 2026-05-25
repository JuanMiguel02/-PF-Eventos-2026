package lospolimorficos.boletopolis.models;

import javafx.scene.shape.Rectangle;

/**
 * <p><b>EstadoAsientoState</b></p>
 *
 * <p>Esta interfaz define el contrato para las diferentes representaciones visuales
 * de los estados de un asiento. Permite que la apariencia de un asiento cambie
 * dinámicamente según su estado, sin modificar la clase {@link Asiento}
 * o el servicio de renderizado directamente.</p>
 *
 * <p>Implementa el patrón de diseño State.</p>
 */
public interface EstadoAsientoState {

    /**
     * Aplica la configuración visual específica de este estado al {@link Rectangle}
     * que representa el asiento en la interfaz de usuario.
     * Esto incluye establecer el color de relleno, el estilo del borde, etc.
     *
     * @param rectangulo El {@link Rectangle} de JavaFX que representa visualmente el asiento.
     * @param tipoZona El {@link TipoZona} a la que pertenece el asiento, que puede influir
     *                 en el estilo base (por ejemplo, el color cuando está DISPONIBLE).
     * @param seleccionado Un valor booleano que indica si el asiento está actualmente
     *                     seleccionado (por ejemplo, para compra), lo que podría modificar
     *                     su apariencia incluso dentro de un estado.
     */
    void aplicar(Rectangle rectangulo, TipoZona tipoZona, boolean seleccionado);

}
