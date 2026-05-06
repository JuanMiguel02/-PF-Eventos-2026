package lospolimorficos.boletopolis.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DisponibleState implements EstadoAsientoState {
    @Override
    public void aplicar(Rectangle rectangulo, TipoZona tipoZona, boolean seleccionado) {
        if (seleccionado) {
            rectangulo.setStyle("");
            rectangulo.setFill(Color.GREEN);
            rectangulo.setStroke(Color.BLACK);
            rectangulo.setStrokeWidth(0.5);
            rectangulo.getStrokeDashArray().clear();
        } else {
            rectangulo.setFill(null);
            rectangulo.setStyle(tipoZona.getEstilo());
            rectangulo.setStroke(Color.BLACK);
            rectangulo.setStrokeWidth(0.5);
            rectangulo.getStrokeDashArray().setAll(2.0, 2.0);
        }
    }
}
