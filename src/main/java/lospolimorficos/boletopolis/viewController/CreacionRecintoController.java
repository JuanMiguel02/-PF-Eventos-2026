package lospolimorficos.boletopolis.viewController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lospolimorficos.boletopolis.controller.RecintoController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaRecinto;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import lospolimorficos.boletopolis.services.GeneradorRecinto;
import lospolimorficos.boletopolis.services.ServicioAlerta;
import lospolimorficos.boletopolis.services.ServicioDibujoRecinto;

import java.util.ArrayList;
import java.util.List;


/**
 * Controlador para la vista de creación de recintos.
 * Permite definir la estructura física de un recinto, incluyendo el escenario,
 * las zonas de asientos y su disposición visual antes de persistirlo.
 */
public class CreacionRecintoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private ComboBox<Ciudad> cmbCiudad;

    @FXML private ComboBox<PosicionEscenario> cmbEscenario;
    @FXML private ComboBox<PosicionZona> cmbPosicionZona;
    @FXML private ComboBox<TipoZona> cmbTipoZona;

    @FXML private TextField txtFilas;
    @FXML private TextField txtColumnas;
    @FXML private TextField txtPrecio;

    @FXML private AnchorPane panelMapa;
    @FXML private ScrollPane scrollPane;
    @FXML private Label lblConteoZonas;
    @FXML private Label lblCapacidadTotal;
    private double escala = 1.0;
    private double ultimoX, ultimoY;
    private double centroX;
    private double centroY;

    private List<PlantillaZona> zonasActuales = new ArrayList<>();
    private final RecintoController recintoController = new RecintoController();
    private ServicioDibujoRecinto servicioDibujo;

    /**
     * Inicializa los componentes de la vista, configura los eventos de scroll y arrastre
     * para el mapa, y carga las opciones en los ComboBox.
     */
    @FXML
    public void initialize() {
        // Inicializar servicio de dibujo
        servicioDibujo = new ServicioDibujoRecinto(panelMapa);

        centroX = panelMapa.getPrefWidth() > 0 ? panelMapa.getPrefWidth() / 2 : 500;
        centroY = panelMapa.getPrefHeight() > 0 ? panelMapa.getPrefHeight() / 2 : 400;

        panelMapa.setOnScroll(event -> {

            double zoomFactor = 1.1;

            if (event.getDeltaY() < 0) {
                zoomFactor = 1 / zoomFactor;
            }

            escala *= zoomFactor;

            escala = Math.max(0.5, Math.min(escala, 3));

            ajustarEscalaInicial();

            event.consume();
        });

        panelMapa.setOnMousePressed(event -> {
            ultimoX = event.getX();
            ultimoY = event.getY();
        });

        panelMapa.setOnMouseDragged(event -> {
            double dx = event.getX() - ultimoX;
            double dy = event.getY() - ultimoY;

            panelMapa.setTranslateX(panelMapa.getTranslateX() + dx);
            panelMapa.setTranslateY(panelMapa.getTranslateY() + dy);

            ultimoX = event.getX();
            ultimoY = event.getY();
        });

        cmbEscenario.getItems().addAll(PosicionEscenario.values());
        cmbPosicionZona.getItems().addAll(PosicionZona.values());
        cmbTipoZona.getItems().addAll(TipoZona.values());
        cmbCiudad.getItems().addAll(Ciudad.values());

        cmbEscenario.setOnAction(e -> renderizarMapa());

        actualizarResumen();
    }

    /**
     * Elimina la última zona agregada a la lista actual.
     * Útil para corregir errores durante el proceso de diseño del recinto.
     */
    @FXML
    private void eliminarUltimaZona() {
        if (!zonasActuales.isEmpty()) {
            zonasActuales.removeLast();
            renderizarMapa();
            actualizarResumen();
        }
    }

    /**
     * Actualiza las etiquetas de información en la interfaz de usuario con el
     * conteo actual de zonas y la capacidad total sumada.
     */
    private void actualizarResumen() {
        lblConteoZonas.setText(String.valueOf(zonasActuales.size()));
        lblCapacidadTotal.setText(String.valueOf(calcularCapacidadTotal(zonasActuales)));
    }

    @FXML
    private void cargarPlantilla() {
        // Funcionalidad deshabilitada temporalmente por cambio de UI
    }

    /**
     * Toma los datos de los campos de texto y selectores para crear una nueva zona.
     * Realiza validaciones de selección y de espacio antes de añadirla a la previsualización.
     * El nombre de la zona se genera automáticamente siguiendo el patrón TIPO-N.
     */
    @FXML
    private void agregarZona() {

        TipoZona tipoSeleccionado = cmbTipoZona.getValue();
        PosicionZona posicionSeleccionada = cmbPosicionZona.getValue();

        if (tipoSeleccionado == null || posicionSeleccionada == null) {
            ServicioAlerta.mostrarAlertaError("Debe seleccionar un tipo de zona y una posición.");
            return;
        }

        // 1. Generar nombre dinámico (Tipo-N)
        long count = zonasActuales.stream()
                .filter(z -> z.getTipoZona() == tipoSeleccionado)
                .count();
        String nombreGenerado = tipoSeleccionado.name() + "-" + (count + 1);

        int filas = Integer.parseInt(txtFilas.getText());
        int columnas = Integer.parseInt(txtColumnas.getText());
        double precio = Double.parseDouble(txtPrecio.getText());

        PlantillaZona nuevaZona = new PlantillaZona(
                nombreGenerado,
                posicionSeleccionada,
                tipoSeleccionado,
                filas,
                columnas,
                precio
        );

        // 2. Validación de límites: ¿Cabe la nueva zona?
        if (!validarEspacioZona(nuevaZona)) {
            ServicioAlerta.mostrarAlertaError("No se pueden crear más zonas en esta dirección: se ha alcanzado el límite del panel.");
            return;
        }

        zonasActuales.add(nuevaZona);
        renderizarMapa();
        actualizarResumen();
    }

    /**
     * Valida si una nueva zona cabe dentro de los límites del panelMapa antes de agregarla.
     * @param nuevaZona La zona que se intenta agregar.
     * @return true si cabe, false si excede los límites.
     */
    private boolean validarEspacioZona(PlantillaZona nuevaZona) {

        double[] datosEscenario = servicioDibujo.obtenerDatosEscenarioSilencioso(cmbEscenario.getValue());
        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        // Contamos cuántas zonas hay ya en esa posición para calcular el index de la nueva
        int index = (int) zonasActuales.stream()
                .filter(z -> z.getPosicionZona() == nuevaZona.getPosicionZona())
                .count();

        double[] base = servicioDibujo.calcularPosicionBaseZona(nuevaZona.getPosicionZona(), escX, escY, escW, escH, index);

        double ancho = nuevaZona.getColumnas() * 12;
        double alto = nuevaZona.getFilas() * 12;

        double inicioX = base[0] - ancho / 2;
        double inicioY = base[1] - alto / 2;

        // Si la posición calculada se sale del panel, rechazamos
        return !(inicioX < 0 || inicioY < 20 ||
                 inicioX + ancho > panelMapa.getPrefWidth() ||
                 inicioY + alto > panelMapa.getPrefHeight());
    }

    /**
     * Renderiza el mapa completo del recinto, incluyendo el escenario y las zonas de asientos.
     * Limpia el panel y recalcula las posiciones para asegurar el centrado.
     */
    private void renderizarMapa() {
        servicioDibujo.renderizarPlantillas(cmbEscenario.getValue(), zonasActuales);
        ajustarEscalaInicial();
    }


    /**
     * Ajusta la escala y traslación del panelMapa para que todo el contenido (escenario y zonas)
     * sea visible y quede centrado dentro del ScrollPane.
     * Implementa una escala base automática y permite zoom adicional del usuario.
     */
    private void ajustarEscalaInicial() {

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        // calcular bounds reales del contenido
        for (Node node : panelMapa.getChildren()) {

            double x = node.getLayoutX();
            double y = node.getLayoutY();

            double width = node.getBoundsInParent().getWidth();
            double height = node.getBoundsInParent().getHeight();

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x + width);
            maxY = Math.max(maxY, y + height);
        }

        double contentWidth = maxX - minX;
        double contentHeight = maxY - minY;

        //  escala base automática
        double scaleX = panelMapa.getPrefWidth() / (contentWidth + 100);
        double scaleY = panelMapa.getPrefHeight() / (contentHeight + 100);

        double escalaBase = Math.min(scaleX, scaleY);

        // limitar escala
        escalaBase = Math.max(0.5, Math.min(escalaBase, 1.2));

        //  aplicar escala total
        double escalaFinal = escalaBase * escala;

        panelMapa.setScaleX(escalaFinal);
        panelMapa.setScaleY(escalaFinal);

        //  dimensiones escaladas
        double scaledWidth = contentWidth * escalaFinal;
        double scaledHeight = contentHeight * escalaFinal;

        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        //  centrar
        double offsetX = (viewportWidth - scaledWidth) / 2;
        double offsetY = (viewportHeight - scaledHeight) / 2;

        panelMapa.setTranslateX(offsetX - (minX * escalaFinal));
        panelMapa.setTranslateY(offsetY - (minY * escalaFinal));
    }

    /**
     * Procesa la información recolectada en la interfaz para generar y registrar
     * un nuevo objeto Recinto en el sistema.
     */
    @FXML
    private void crearRecinto() {

        PlantillaRecinto plantillaFinal = construirPlantillaDesdeUI();

        Recinto recinto = GeneradorRecinto.generarRecinto(
                plantillaFinal,
                txtDireccion.getText(),
                cmbCiudad.getValue()
        );

        recinto.setEscenario(new Escenario(cmbEscenario.getValue()));
        recinto.setCapacidad(calcularCapacidadTotal(plantillaFinal.getZonas()));
        if(recintoController.registrarRecinto(recinto)){
            ServicioAlerta.mostrarAlerta("Éxito", "El recinto: " + recinto.getNombre() + " ha sido registrado éxitosamente", Alert.AlertType.INFORMATION);
        }
        renderizarMapa();
    }

    /**
     * Calcula la suma total de asientos disponibles en todas las plantillas de zona proporcionadas.
     * @param plantillas Lista de plantillas de zona a sumar.
     * @return Capacidad total entera.
     */
    private int calcularCapacidadTotal(List<PlantillaZona> plantillas) {
        int numero = 0;
        for(PlantillaZona plantilla : plantillas) {
            numero += plantilla.calcularCapacidad();
        }
        System.out.println("Capacidad de: " + numero);
        return numero;
    }

    /**
     * Crea un objeto PlantillaRecinto consolidando el nombre y las zonas definidas en la UI.
     * @return Objeto PlantillaRecinto listo para ser procesado por el generador.
     */
    private PlantillaRecinto construirPlantillaDesdeUI() {

        String nombre = txtNombre.getText();

        List<PlantillaZona> zonas = new ArrayList<>(zonasActuales);

        return new PlantillaRecinto(nombre, zonas);
    }


}
