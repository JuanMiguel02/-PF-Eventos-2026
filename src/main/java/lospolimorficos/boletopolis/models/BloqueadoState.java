package lospolimorficos.boletopolis.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BloqueadoState implements EstadoAsientoState {
    @Override
    public void aplicar(Rectangle rectangulo, TipoZona tipoZona, boolean seleccionado) {
        rectangulo.setStyle("");
        rectangulo.setFill(Color.GRAY);
        rectangulo.setStroke(Color.BLACK);
        rectangulo.setStrokeWidth(0.5);
        rectangulo.getStrokeDashArray().clear();
    }
}
