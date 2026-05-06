package lospolimorficos.boletopolis.services;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.Asiento;
import lospolimorficos.boletopolis.models.EstadoFactory;
import lospolimorficos.boletopolis.models.TipoZona;
import java.util.List;

/**
 * Servicio encargado de gestionar el estado visual de los asientos.
 */
public class ServicioEstadoAsientos {

    /**
     * Actualiza el color del rectángulo según el estado actual del asiento.
     *
     * @param r El rectángulo visual.
     * @param asiento El objeto del modelo que contiene el estado.
     * @param tipo El tipo de zona para obtener el color base de disponibilidad.
     * @param esSeleccionado Indica si el asiento está seleccionado para compra.
     */
    public void actualizarVisualAsiento(Rectangle r, Asiento asiento, TipoZona tipo, boolean esSeleccionado) {
        if (asiento.getEstado() == null) return;

        EstadoFactory.crearEstadoAsiento(asiento.getEstado())
                .aplicar(r, tipo, esSeleccionado);
    }
}
