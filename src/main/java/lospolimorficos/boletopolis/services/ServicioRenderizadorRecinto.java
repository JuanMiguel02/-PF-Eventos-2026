package lospolimorficos.boletopolis.services;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.PosicionEscenario;
import lospolimorficos.boletopolis.models.TipoZona;

/**
 * <p><b>ServicioRenderizadorRecinto</b></p>
 *
 * <p>Este servicio es responsable de la creación y renderizado de los componentes visuales
 * del recinto en un {@link AnchorPane} de JavaFX. Se encarga de dibujar el escenario,
 * las etiquetas de las zonas y los rectángulos que representan los asientos,
 * así como de gestionar los tooltips y añadir los elementos al panel.</p>
 */
public class ServicioRenderizadorRecinto {

    private final AnchorPane panelMapa;

    /**
     * Constructor del servicio de renderizado del recinto.
     *
     * @param panelMapa El {@link AnchorPane} de JavaFX donde se dibujarán los elementos.
     */
    public ServicioRenderizadorRecinto(AnchorPane panelMapa) {
        this.panelMapa = panelMapa;
    }

    /**
     * Limpia todos los nodos hijos del {@link AnchorPane} del mapa, eliminando
     * cualquier elemento visual dibujado previamente.
     */
    public void limpiarPanel() {
        panelMapa.getChildren().clear();
    }

    /**
     * <p><b>Dibuja el escenario en el panel.</b></p>
     *
     * <p>Crea un {@link Rectangle} para representar el escenario y una {@link Label}
     * para su título. La posición del título se ajusta dinámicamente para centrarse
     * horizontalmente sobre el escenario.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Verifica si la {@link PosicionEscenario} es nula; si lo es, no dibuja nada y retorna.</li>
     *     <li>Extrae las coordenadas (X, Y), ancho (W) y alto (H) del escenario del arreglo {@code datos}.</li>
     *     <li>Crea un nuevo {@link Rectangle} con el ancho y alto del escenario.</li>
     *     <li>Establece el estilo de relleno del rectángulo a un color gris oscuro.</li>
     *     <li>Posiciona el rectángulo en las coordenadas {@code escX} y {@code escY}.</li>
     *     <li>Crea una {@link Label} con el texto "Escenario".</li>
     *     <li>Posiciona la etiqueta 20 píxeles por encima del escenario.</li>
     *     <li>Añade un listener a la propiedad {@code widthProperty} de la etiqueta para
     *         recalcular y centrar su posición X cada vez que su ancho cambie.</li>
     *     <li>Añade el rectángulo y la etiqueta al {@link AnchorPane} del mapa.</li>
     * </ol>
     *
     * @param datos Un arreglo {@code double[]} que contiene {X, Y, Ancho, Alto} del escenario.
     * @param posicion La {@link PosicionEscenario} del escenario, utilizada para determinar si debe dibujarse.
     */
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
        labelEscenario.setLayoutX(escX); // Posición inicial antes del listener

        panelMapa.getChildren().addAll(rect, labelEscenario);
    }

    /**
     * <p><b>Dibuja una etiqueta con el nombre de la zona en el panel.</b></p>
     *
     * <p>La etiqueta se posiciona por encima de la zona de asientos y su posición
     * horizontal se ajusta dinámicamente para centrarse sobre el ancho de la zona.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Crea una nueva {@link Label} con el {@code nombre} proporcionado.</li>
     *     <li>Posiciona la etiqueta 20 píxeles por encima del {@code inicioY} de la zona.</li>
     *     <li>Añade un listener a la propiedad {@code widthProperty} de la etiqueta para
     *         recalcular y centrar su posición X cada vez que su ancho cambie.
     *         El cálculo considera el {@code inicioX} y {@code anchoZona} para el centrado,
     *         y asegura que la etiqueta no se salga de los límites del {@link AnchorPane}.</li>
     *     <li>Establece una posición X inicial para la etiqueta.</li>
     *     <li>Añade la etiqueta al {@link AnchorPane} del mapa.</li>
     * </ol>
     *
     * @param nombre El texto a mostrar en la etiqueta.
     * @param inicioX La coordenada X de inicio de la zona de asientos.
     * @param inicioY La coordenada Y de inicio de la zona de asientos.
     * @param anchoZona El ancho total de la zona de asientos, utilizado para el centrado de la etiqueta.
     */
    public void dibujarEtiquetaZona(String nombre, double inicioX, double inicioY, double anchoZona) {
        Label label = new Label(nombre);
        label.setLayoutY(inicioY - 20);
        label.widthProperty().addListener((obs, oldVal, newVal) -> {
            double labelX = (inicioX + anchoZona / 2) - newVal.doubleValue() / 2;
            label.setLayoutX(Math.max(5, Math.min(labelX, panelMapa.getPrefWidth() - newVal.doubleValue() - 5)));
        });
        label.setLayoutX(inicioX); // Posición inicial antes del listener
        panelMapa.getChildren().add(label);
    }

    /**
     * Crea un objeto {@link Rectangle} que representa visualmente un asiento.
     * El rectángulo se configura con un tamaño fijo y se posiciona
     * dentro de la zona de asientos.
     *
     * @param tipo El {@link TipoZona} de la zona a la que pertenece el asiento, para aplicar su estilo.
     * @param inicioX El punto de origen X de la zona de asientos.
     * @param inicioY El punto de origen Y de la zona de asientos.
     * @param fila El índice de la fila del asiento (base 0).
     * @param columna El índice de la columna del asiento (base 0).
     * @return Un {@link Rectangle} configurado con la posición y estilo correspondiente al asiento.
     */
    public Rectangle crearRectanguloAsiento(TipoZona tipo, double inicioX, double inicioY, int fila, int columna) {
        Rectangle r = new Rectangle(10, 10); // Tamaño fijo para cada asiento
        r.setStyle(tipo.getEstilo()); // Aplica el estilo de la zona
        r.setLayoutX(inicioX + (columna * 12)); // Posiciona el asiento en X
        r.setLayoutY(inicioY + (fila * 12));   // Posiciona el asiento en Y
        return r;
    }

    /**
     * Añade un {@link Rectangle} (que representa un asiento o el escenario) al {@link AnchorPane} del mapa.
     * @param r El {@link Rectangle} a añadir.
     */
    public void agregarAlPanel(Rectangle r) {
        panelMapa.getChildren().add(r);
    }

    /**
     * Instala un {@link Tooltip} simple en un {@link Rectangle} de asiento,
     * mostrando la fila y el número del asiento.
     *
     * @param r El {@link Rectangle} del asiento al que se le instalará el tooltip.
     * @param fila El índice de la fila del asiento (base 0).
     * @param columna El índice de la columna del asiento (base 0).
     */
    public void instalarTooltipSimple(Rectangle r, int fila, int columna) {
        Tooltip.install(r, new Tooltip("Fila: " + (char)('A' + fila) + "\nNúmero: " + (columna + 1)));
    }
}
