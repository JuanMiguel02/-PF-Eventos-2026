package lospolimorficos.boletopolis.models;

import javafx.scene.shape.Rectangle;

public interface EstadoAsientoState {

    void aplicar(Rectangle rectangulo, TipoZona tipoZona, boolean seleccionado);

}
