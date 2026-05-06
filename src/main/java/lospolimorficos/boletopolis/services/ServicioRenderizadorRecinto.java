package lospolimorficos.boletopolis.services;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.Asiento;
import lospolimorficos.boletopolis.models.PosicionEscenario;
import lospolimorficos.boletopolis.models.TipoZona;
import java.util.List;

/**
 * Servicio encargado de crear los componentes visuales del recinto.
 */
public class ServicioRenderizadorRecinto {

    private final AnchorPane panelMapa;

    public ServicioRenderizadorRecinto(AnchorPane panelMapa) {
        this.panelMapa = panelMapa;
    }

    public void limpiarPanel() {
        panelMapa.getChildren().clear();
    }

    public void dibujarEscenario(double[] datos, PosicionEscenario posicion) {
        if (posicion == null) return;

        double escX = datos[0];
        double escY = datos[1];
        double escW = datos[2];
        double escH = datos[3];

        Rectangle rect = new Rectangle(escW, escH);
        rect.setStyle("-fx-fill: #575252;");
        rect.setLayoutX(escX);
        rect.setLayoutY(escY);

        Label labelEscenario = new Label("Escenario");
        labelEscenario.setLayoutY(escY - 20);
        labelEscenario.widthProperty().addListener((obs, oldVal, newVal) -> {
            labelEscenario.setLayoutX((escX + escW / 2) - newVal.doubleValue() / 2);
        });
        labelEscenario.setLayoutX(escX);

        panelMapa.getChildren().addAll(rect, labelEscenario);
    }

    public void dibujarEtiquetaZona(String nombre, double inicioX, double inicioY, double anchoZona) {
        Label label = new Label(nombre);
        label.setLayoutY(inicioY - 20);
        label.widthProperty().addListener((obs, oldVal, newVal) -> {
            double labelX = (inicioX + anchoZona / 2) - newVal.doubleValue() / 2;
            label.setLayoutX(Math.max(5, Math.min(labelX, panelMapa.getPrefWidth() - newVal.doubleValue() - 5)));
        });
        label.setLayoutX(inicioX);
        panelMapa.getChildren().add(label);
    }

    public Rectangle crearRectanguloAsiento(TipoZona tipo, double inicioX, double inicioY, int fila, int columna) {
        Rectangle r = new Rectangle(10, 10);
        r.setStyle(tipo.getEstilo());
        r.setLayoutX(inicioX + (columna * 12));
        r.setLayoutY(inicioY + (fila * 12));
        return r;
    }

    public void agregarAlPanel(Rectangle r) {
        panelMapa.getChildren().add(r);
    }
    
    public void instalarTooltipSimple(Rectangle r, int fila, int columna) {
        Tooltip.install(r, new Tooltip("Fila: " + (char)('A' + fila) + "\nNúmero: " + (columna + 1)));
    }
}
