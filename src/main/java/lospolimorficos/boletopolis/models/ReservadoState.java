package lospolimorficos.boletopolis.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ReservadoState implements EstadoAsientoState {
    @Override
    public void aplicar(Rectangle rectangulo, TipoZona tipoZona, boolean seleccionado) {
        rectangulo.setStyle("");
        rectangulo.setFill(Color.ORANGE);
        rectangulo.setStroke(Color.BLACK);
        rectangulo.setStrokeWidth(0.5);
        rectangulo.getStrokeDashArray().clear();
    }
}
