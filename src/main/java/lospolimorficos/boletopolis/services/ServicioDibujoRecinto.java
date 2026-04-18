package lospolimorficos.boletopolis.services;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de renderizar visualmente un recinto en un AnchorPane.
 */
public class ServicioDibujoRecinto {

    /**
     * Define los modos de interacción permitidos para los asientos.
     */
    public enum ModoInteraccion {
        ADMIN_RECINTO, // Solo permite alternar entre DISPONIBLE y BLOQUEADO
        ADMIN_EVENTO   // Permite todos los estados (DISPONIBLE, RESERVADO, VENDIDO, BLOQUEADO)
    }

    private final AnchorPane panelMapa;
    private double centroX;
    private double centroY;
    private boolean interactivo = false;
    private ModoInteraccion modoInteraccion = ModoInteraccion.ADMIN_EVENTO;
    private Runnable onAsientoChanged;

    /**
     * Constructor del servicio de dibujo.
     * @param panelMapa Panel de tipo AnchorPane donde se realizarán los dibujos.
     */
    public ServicioDibujoRecinto(AnchorPane panelMapa) {
        this.panelMapa = panelMapa;
        actualizarCentros();
    }

    /**
     * Establece la acción a ejecutar cuando cambie el estado de un asiento.
     * @param onAsientoChanged Runnable con la acción de actualización.
     */
    public void setOnAsientoChanged(Runnable onAsientoChanged) {
        this.onAsientoChanged = onAsientoChanged;
    }

    /**
     * Actualiza las coordenadas centrales del lienzo basándose en el tamaño actual del panel.
     * Si el panel no tiene dimensiones definidas, utiliza valores por defecto (500, 400).
     */
    public void actualizarCentros() {
        this.centroX = panelMapa.getPrefWidth() > 0 ? panelMapa.getPrefWidth() / 2 : 500;
        this.centroY = panelMapa.getPrefHeight() > 0 ? panelMapa.getPrefHeight() / 2 : 400;
    }

    /**
     * Establece el modo de interacción para restringir los estados de los asientos.
     * @param modo El modo deseado.
     */
    public void setModoInteraccion(ModoInteraccion modo) {
        this.modoInteraccion = modo;
    }

    /**
     * Define si los asientos dibujados permitirán interacción (cambio de estado al hacer clic).
     * @param interactivo true para habilitar interacción, false para solo visualización.
     */
    public void setInteractivo(boolean interactivo) {
        this.interactivo = interactivo;
    }

    /**
     * Renderiza un recinto completo usando objetos del modelo.
     * @param escenario Escenario del recinto.
     * @param zonas Lista de zonas a dibujar.
     */
    public void renderizar(Escenario escenario, List<Zona> zonas) {
        panelMapa.getChildren().clear();

        double[] datosEscenario = dibujarEscenario(escenario != null ? escenario.posicion() : null);
        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        Map<PosicionZona, Integer> contadorZonas = new HashMap<>();

        for (Zona zona : zonas) {
            int index = contadorZonas.getOrDefault(zona.getPosicionZona(), 0);
            contadorZonas.put(zona.getPosicionZona(), index + 1);

            double[] base = calcularPosicionBaseZona(zona.getPosicionZona(), escX, escY, escW, escH, index);
            int filas = zona.getAsientos().stream().mapToInt(Asiento::getFila).max().orElse(0);
            int columnas = zona.getAsientos().stream().mapToInt(Asiento::getNumero).max().orElse(0);

            dibujarZonaGenerica(zona.getNombre(), zona.getTipoZona(), filas, columnas, base[0], base[1], zona.getAsientos());
        }
    }

    /**
     * Dibuja una zona en el mapa, incluyendo su etiqueta de nombre y todos sus asientos.
     * Maneja el ajuste de límites para asegurar que la zona no se dibuje fuera del panel.
     * Soporta dibujo desde una lista de asientos persistidos o basándose en dimensiones (plantilla).
     *
     * @param nombre Nombre de la zona.
     * @param tipo Tipo de zona (determina el color/estilo).
     * @param filas Número de filas de asientos.
     * @param columnas Número de columnas de asientos.
     * @param baseX Coordenada X central donde se ubicará la zona.
     * @param baseY Coordenada Y central donde se ubicará la zona.
     * @param asientos Lista de objetos Asiento (opcional, para datos persistidos).
     */
    private void dibujarZonaGenerica(String nombre, TipoZona tipo, int filas, int columnas, double baseX, double baseY, List<Asiento> asientos) {
        double ancho = columnas * 12;
        double alto = filas * 12;
        double inicioX = baseX - ancho / 2;
        double inicioY = baseY - alto / 2;

        // El clamping se ha movido a los controladores mediante ajustarDimensionesPanelMapa,
        // sin embargo, mantenemos una validación mínima para evitar coordenadas negativas.
        if (inicioX < 5) inicioX = 5;
        if (inicioY < 25) inicioY = 25;

        // Dibujar etiqueta de nombre
        dibujarEtiquetaZona(nombre, inicioX, inicioY, ancho);

        if (asientos != null && !asientos.isEmpty()) {
            // Dibujar asientos desde lista (objetos persistidos)
            for (Asiento asiento : asientos) {
                Rectangle r = crearRectanguloAsiento(tipo, inicioX, inicioY, asiento.getFila() - 1, asiento.getNumero() - 1);
                configurarInteractividadAsiento(r, asiento, tipo);
                panelMapa.getChildren().add(r);
            }
        } else {
            // Dibujar asientos desde dimensiones (plantilla)
            for (int f = 0; f < filas; f++) {
                for (int c = 0; c < columnas; c++) {
                    Rectangle r = crearRectanguloAsiento(tipo, inicioX, inicioY, f, c);
                    Tooltip.install(r, new Tooltip("Fila: " + (char)('A' + f) + "\nNúmero: " + (c + 1)));
                    panelMapa.getChildren().add(r);
                }
            }
        }
    }

    /**
     * Crea un objeto Rectangle que representa visualmente un asiento.
     *
     * @param tipo Tipo de zona para aplicar el estilo visual.
     * @param inicioX Punto de origen X de la zona.
     * @param inicioY Punto de origen Y de la zona.
     * @param fila Índice de la fila (base 0).
     * @param columna Índice de la columna (base 0).
     * @return Un Rectangle configurado con la posición y estilo correspondiente.
     */
    private Rectangle crearRectanguloAsiento(TipoZona tipo, double inicioX, double inicioY, int fila, int columna) {
        Rectangle r = new Rectangle(10, 10);
        r.setStyle(tipo.getEstilo());
        r.setLayoutX(inicioX + (columna * 12));
        r.setLayoutY(inicioY + (fila * 12));
        return r;
    }

    /**
     * Dibuja la etiqueta con el nombre de la zona sobre los asientos.
     * Incluye un listener para centrar dinámicamente el texto según su ancho real.
     *
     * @param nombre Texto a mostrar.
     * @param inicioX Coordenada X de inicio de la zona.
     * @param inicioY Coordenada Y de inicio de la zona.
     * @param anchoZona Ancho total de la zona de asientos para propósitos de centrado.
     */
    private void dibujarEtiquetaZona(String nombre, double inicioX, double inicioY, double anchoZona) {
        Label label = new Label(nombre);
        label.setLayoutY(inicioY - 20);
        
        // Listener para centrado dinámico
        label.widthProperty().addListener((obs, oldVal, newVal) -> {
            double labelX = (inicioX + anchoZona / 2) - newVal.doubleValue() / 2;
            label.setLayoutX(Math.max(5, Math.min(labelX, panelMapa.getPrefWidth() - newVal.doubleValue() - 5)));
        });
        
        label.setLayoutX(inicioX); // Posición inicial antes del listener
        panelMapa.getChildren().add(label);
    }

    /**
     * Configura la interactividad y la información emergente (Tooltip) para un asiento persistido.
     * Si el modo interactivo está activo, permite cambiar el estado del asiento cíclicamente al hacer clic.
     *
     * @param r El rectángulo que representa al asiento en la UI.
     * @param asiento El objeto del modelo con los datos del asiento.
     * @param tipo El tipo de zona para restaurar el color si vuelve a estar disponible.
     */
    private void configurarInteractividadAsiento(Rectangle r, Asiento asiento, TipoZona tipo) {
        actualizarColorAsiento(r, asiento, tipo);
        actualizarTooltipAsiento(r, asiento);

        if (interactivo) {
            r.setOnMouseClicked(event -> {
                toggleEstadoAsiento(asiento);
                actualizarColorAsiento(r, asiento, tipo);
                actualizarTooltipAsiento(r, asiento);
            });
        }
    }

    /**
     * Actualiza el tooltip del asiento con su ID y estado actual.
     * @param r El rectángulo visual.
     * @param asiento El objeto del modelo.
     */
    private void actualizarTooltipAsiento(Rectangle r, Asiento asiento) {
        Tooltip.install(r, new Tooltip("Asiento: " + asiento.getIdAsiento() + "\nEstado: " + asiento.getEstado()));
    }

    /**
     * Cambia el estado de un asiento al siguiente en el ciclo definido en EstadoAsiento,
     * respetando el modo de interacción configurado.
     * @param asiento Asiento a modificar.
     */
    private void toggleEstadoAsiento(Asiento asiento) {
        EstadoAsiento actual = asiento.getEstado();
        EstadoAsiento nuevo;

        if (modoInteraccion == ModoInteraccion.ADMIN_RECINTO) {
            // En recinto solo alternamos entre DISPONIBLE y BLOQUEADO
            nuevo = (actual == EstadoAsiento.BLOQUEADO) ? EstadoAsiento.DISPONIBLE : EstadoAsiento.BLOQUEADO;
        } else {
            // En evento alternamos entre todos los estados (DISPONIBLE, RESERVADO, VENDIDO, BLOQUEADO)
            EstadoAsiento[] estados = EstadoAsiento.values();
            int siguienteIndex = (actual.ordinal() + 1) % estados.length;
            nuevo = estados[siguienteIndex];
        }

        asiento.setEstado(nuevo);

        // Notificar cambio si existe acción definida
        if (onAsientoChanged != null) {
            onAsientoChanged.run();
        }
    }

    /**
     * Actualiza el color del rectángulo según el estado actual del asiento.
     *
     * @param r El rectángulo visual.
     * @param asiento El objeto del modelo que contiene el estado.
     * @param tipo El tipo de zona para obtener el color base de disponibilidad.
     */
    private void actualizarColorAsiento(Rectangle r, Asiento asiento, TipoZona tipo) {
        if (asiento.getEstado() == null) return;
        
        switch (asiento.getEstado()) {
            case DISPONIBLE -> {
                r.setFill(null); // Limpiar color sólido previo si existe
                r.setStyle(tipo.getEstilo());
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(0.5);
            }
            case VENDIDO -> {
                r.setStyle(""); // Limpiar estilo previo
                r.setFill(Color.RED);
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(0.5);
            }
            case RESERVADO -> {
                r.setStyle(""); // Limpiar estilo previo
                r.setFill(Color.ORANGE);
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(0.5);
            }
            case BLOQUEADO -> {
                r.setStyle(""); // Limpiar estilo previo
                r.setFill(Color.GRAY);
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(0.5);
            }
        }
    }

    /**
     * Renderiza un recinto completo usando plantillas (para previsualización).
     * @param posicionEscenario Posición del escenario.
     * @param plantillas Lista de plantillas de zona.
     */
    public void renderizarPlantillas(PosicionEscenario posicionEscenario, List<PlantillaZona> plantillas) {
        panelMapa.getChildren().clear();

        double[] datosEscenario = dibujarEscenario(posicionEscenario);
        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        Map<PosicionZona, Integer> contadorZonas = new HashMap<>();

        for (PlantillaZona pZona : plantillas) {
            int index = contadorZonas.getOrDefault(pZona.getPosicionZona(), 0);
            contadorZonas.put(pZona.getPosicionZona(), index + 1);

            double[] base = calcularPosicionBaseZona(pZona.getPosicionZona(), escX, escY, escW, escH, index);
            dibujarZonaGenerica(pZona.getNombre(), pZona.getTipoZona(), pZona.getFilas(), pZona.getColumnas(), base[0], base[1], null);
        }
    }

    /**
     * Obtiene las coordenadas y dimensiones del escenario sin dibujarlo.
     */
    public double[] obtenerDatosEscenarioSilencioso(PosicionEscenario posicion) {
        double escW = 150;
        double escH = 40;
        double escX = centroX - escW / 2;
        double escY = centroY - escH / 2;

        if (posicion != null) {
            switch (posicion) {
                case ARRIBA -> escY -= 200;
                case ABAJO -> escY += 200;
                case IZQUIERDA -> escX -= 250;
                case DERECHA -> escX += 250;
            }
        }
        return new double[]{escX, escY, escW, escH};
    }

    /**
     * Dibuja el escenario en el panel basándose en su posición cardinal.
     *
     * @param posicion Posición relativa (ARRIBA, ABAJO, etc.).
     * @return Un arreglo double con {X, Y, Ancho, Alto} del escenario dibujado.
     */
    private double[] dibujarEscenario(PosicionEscenario posicion) {
        double[] datos = obtenerDatosEscenarioSilencioso(posicion);
        double escX = datos[0];
        double escY = datos[1];
        double escW = datos[2];
        double escH = datos[3];

        if (posicion != null) {
            Rectangle rect = new Rectangle(escW, escH);
            rect.setStyle("-fx-fill: #575252;");
            rect.setLayoutX(escX);
            rect.setLayoutY(escY);

            Label labelEscenario = new Label("Escenario");
            labelEscenario.setLayoutY(escY - 20);

            // Listener para centrar el título del escenario horizontalmente
            labelEscenario.widthProperty().addListener((obs, oldVal, newVal) -> {
                double labelX = (escX + escW / 2) - newVal.doubleValue() / 2;
                labelEscenario.setLayoutX(labelX);
            });

            labelEscenario.setLayoutX(escX); // Posición inicial
            
            panelMapa.getChildren().add(rect);
            panelMapa.getChildren().add(labelEscenario);
        }
        return datos;
    }

    /**
     * Calcula el punto central base para una zona en función de la posición del escenario.
     * Implementa la lógica de apilamiento cuando hay múltiples zonas en la misma dirección.
     *
     * @param posicion Dirección cardinal (NORTE, SUR, etc.).
     * @param escX X del escenario.
     * @param escY Y del escenario.
     * @param escW Ancho del escenario.
     * @param escH Alto del escenario.
     * @param index Índice de la zona en esa dirección (para aplicar separación).
     * @return Arreglo con {X, Y} central de la zona.
     */
    public double[] calcularPosicionBaseZona(PosicionZona posicion, double escX, double escY, double escW, double escH, int index) {
        double baseX = escX + escW / 2;
        double baseY = escY + escH / 2;
        double offset = 60;
        double separacion = 100;

        double dx = 0;
        double dy = 0;

        switch (posicion) {
            case NORTE -> dy -= (escH / 2) + offset + (index * separacion);
            case SUR -> dy += (escH / 2) + offset + (index * separacion);
            case ESTE -> dx += (escW / 2) + offset + (index * separacion);
            case OESTE -> dx -= (escW / 2) + offset + (index * separacion);
        }

        return new double[]{baseX + dx, baseY + dy};
    }

}
