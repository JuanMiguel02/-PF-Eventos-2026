package lospolimorficos.boletopolis.services;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>ServicioDibujoRecinto</b></p>
 *
 * <p>Esta clase actúa como una fachada para la lógica de dibujo y gestión de un recinto en la interfaz de usuario.
 * Centraliza las operaciones relacionadas con el layout, renderizado, estado e interacción de los asientos,
 * delegando las responsabilidades específicas a otros servicios.</p>
 *
 * <p>Utiliza los patrones de diseño Strategy (para la interacción) y State (para el estado visual de los asientos)
 * para una mayor flexibilidad y mantenibilidad.</p>
 */
public class ServicioDibujoRecinto {

    private final ServicioLayoutRecinto layout;
    private final ServicioRenderizadorRecinto renderizador;
    private final ServicioEstadoAsientos gestorEstados;
    private final ServicioInteraccionAsientos interaccion;

    /**
     * Constructor del servicio de dibujo del recinto.
     * Inicializa los servicios internos de layout, renderizado, gestión de estados e interacción.
     *
     * @param panelMapa El {@link AnchorPane} de JavaFX donde se dibujará el recinto.
     */
    public ServicioDibujoRecinto(AnchorPane panelMapa) {
        this.layout = new ServicioLayoutRecinto(panelMapa);
        this.renderizador = new ServicioRenderizadorRecinto(panelMapa);
        this.gestorEstados = new ServicioEstadoAsientos();
        this.interaccion = new ServicioInteraccionAsientos(new ArrayList<>());
    }

    /**
     * Establece la acción a ejecutar cuando el estado o la selección de un asiento cambia.
     * Esta acción se delega al {@link ServicioInteraccionAsientos}.
     *
     * @param onAsientoChanged Un {@link Runnable} que contiene la lógica a ejecutar.
     */
    public void setOnAsientoChanged(Runnable onAsientoChanged) {
        this.interaccion.setOnAsientoChanged(onAsientoChanged);
    }

    /**
     * Obtiene la lista de asientos que están actualmente seleccionados.
     * Esta operación se delega al {@link ServicioInteraccionAsientos}.
     *
     * @return Una {@link List} de {@link Asiento}s seleccionados.
     */
    public List<Asiento> getAsientosSeleccionados() {
        return interaccion.getAsientosSeleccionados();
    }

    /**
     * Limpia la selección actual de asientos.
     * Esta operación se delega al {@link ServicioInteraccionAsientos}.
     */
    public void limpiarSeleccion() {
        interaccion.limpiarSeleccion();
    }

    /**
     * Actualiza las coordenadas centrales del panel de dibujo, lo cual es crucial para el posicionamiento
     * de los elementos del recinto.
     * Esta operación se delega al {@link ServicioLayoutRecinto}.
     */
    public void actualizarCentros() {
        layout.actualizarCentros();
    }

    /**
     * Establece la estrategia de interacción actual para los asientos.
     * Esto permite cambiar el comportamiento de los clics en los asientos dinámicamente.
     * Esta operación se delega al {@link ServicioInteraccionAsientos}.
     *
     * @param strategy La {@link InteraccionStrategy} a utilizar.
     */
    public void setStrategy(InteraccionStrategy strategy) {
        interaccion.setStrategy(strategy);
    }

    /**
     * Habilita o deshabilita la interactividad de los asientos.
     * Si es {@code true}, los asientos responderán a los clics según la estrategia configurada.
     * Esta operación se delega al {@link ServicioInteraccionAsientos}.
     *
     * @param interactivo {@code true} para habilitar la interacción, {@code false} para deshabilitarla.
     */
    public void setInteractivo(boolean interactivo) {
        interaccion.setInteractivo(interactivo);
    }

    /**
     * <p><b>Renderiza un recinto completo con sus zonas y asientos persistidos.</b></p>
     *
     * <p>Este método orquesta el proceso de dibujo del recinto en el {@link AnchorPane}.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li><b>Ajuste de Dimensiones del Panel:</b> Llama a {@code layout.ajustarDimensionesPanel()}
     *         para asegurar que el panel tenga el tamaño adecuado para contener el escenario y todas las zonas.</li>
     *     <li><b>Limpieza del Panel:</b> Llama a {@code renderizador.limpiarPanel()} para eliminar
     *         cualquier elemento dibujado previamente en él {@link AnchorPane}.</li>
     *     <li><b>Dibujo del Escenario:</b>
     *         <ul>
     *             <li>Obtiene los datos de posición y tamaño del escenario a través de {@code layout.obtenerDatosEscenarioSilencioso()}.</li>
     *             <li>Llama a {@code renderizador.dibujarEscenario()} para dibujar el escenario en el panel.</li>
     *         </ul>
     *     </li>
     *     <li><b>Iteración y Dibujo de Zonas:</b>
     *         <ul>
     *             <li>Inicializa un contador para manejar múltiples zonas en la misma {@link PosicionZona}.</li>
     *             <li>Para cada {@link Zona} en la lista:
     *                 <ul>
     *                     <li>Calcula la posición base central de la zona utilizando {@code layout.calcularPosicionBaseZona()},
     *                         teniendo en cuenta la posición del escenario y el índice de la zona para apilamiento.</li>
     *                     <li>Determina el número de filas y columnas de la zona a partir de sus asientos.</li>
     *                     <li>Llama a {@code dibujarZonaGenerica()} para dibujar la etiqueta de la zona y todos sus asientos.</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param escenario El {@link Escenario} del recinto, que define su posición.
     * @param zonas Una {@link List} de {@link Zona}s a dibujar, conteniendo los asientos persistidos.
     */
    public void renderizar(Escenario escenario, List<Zona> zonas) {
        layout.ajustarDimensionesPanel(escenario, zonas);
        renderizador.limpiarPanel();

        PosicionEscenario posEsc = escenario != null ? escenario.posicion() : null;
        double[] datosEscenario = layout.obtenerDatosEscenarioSilencioso(posEsc);
        renderizador.dibujarEscenario(datosEscenario, posEsc);

        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        Map<PosicionZona, Integer> contadorZonas = new HashMap<>();

        for (Zona zona : zonas) {
            int index = contadorZonas.getOrDefault(zona.getPosicionZona(), 0);
            contadorZonas.put(zona.getPosicionZona(), index + 1);

            double[] base = layout.calcularPosicionBaseZona(zona.getPosicionZona(), escX, escY, escW, escH, index);
            int filas = zona.getAsientos().stream().mapToInt(Asiento::getFila).max().orElse(0);
            int columnas = zona.getAsientos().stream().mapToInt(Asiento::getNumero).max().orElse(0);

            dibujarZonaGenerica(zona.getNombre(), zona.getTipoZona(), filas, columnas, base[0], base[1], zona.getAsientos());
        }
    }

    /**
     * <p><b>Dibuja una zona genérica en el panel, incluyendo su etiqueta y sus asientos.</b></p>
     *
     * <p>Este método es utilizado tanto para dibujar zonas con asientos persistidos como para previsualizar
     * zonas basadas en plantillas (sin asientos concretos).</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li><b>Cálculo de Dimensiones y Posición:</b>
     *         <ul>
     *             <li>Calcula el ancho y alto de la zona con base al número de filas y columnas y el tamaño de cada asiento.</li>
     *             <li>Determina las coordenadas de inicio (esquina superior izquierda) de la zona,
     *                 aplicando un ajuste mínimo para evitar coordenadas negativas.</li>
     *         </ul>
     *     </li>
     *     <li><b>Dibujo de la Etiqueta de la Zona:</b> Llama a {@code renderizador.dibujarEtiquetaZona()}
     *         para colocar el nombre de la zona sobre los asientos.</li>
     *     <li><b>Dibujo de Asientos:</b>
     *         <ul>
     *             <li><b>Si hay asientos persistidos ({@code asientos != null && !asientos.isEmpty()}):</b>
     *                 <ul>
     *                     <li>Itera sobre cada {@link Asiento} en la lista.</li>
     *                     <li>Crea un {@link Rectangle} que representa visualmente el asiento utilizando {@code renderizador.crearRectanguloAsiento()}.</li>
     *                     <li>Actualiza la apariencia visual del asiento según su estado actual y si está seleccionado,
     *                         delegando a {@code gestorEstados.actualizarVisualAsiento()}.</li>
     *                     <li>Configura la interactividad del asiento (manejo de clics, tooltips, cursor)
     *                         delegando a {@code interaccion.configurarAsiento()}.</li>
     *                     <li>Añade el {@link Rectangle} al panel de dibujo a través de {@code renderizador.agregarAlPanel()}.</li>
     *                 </ul>
     *             </li>
     *             <li><b>Si no hay asientos persistidos (modo plantilla/previsualización):</b>
     *                 <ul>
     *                     <li>Itera sobre las filas y columnas para crear asientos genéricos.</li>
     *                     <li>Crea un {@link Rectangle} para cada asiento utilizando {@code renderizador.crearRectanguloAsiento()}.</li>
     *                     <li>Instala un  Tooltip simple con la fila y columna del asiento usando {@code renderizador.instalarTooltipSimple()}.</li>
     *                     <li>Añade el {@link Rectangle} al panel de dibujo a través de {@code renderizador.agregarAlPanel()}.</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param nombre El nombre de la zona.
     * @param tipo El {@link TipoZona} de la zona, que influye en el estilo visual de los asientos.
     * @param filas El número de filas de asientos en la zona.
     * @param columnas El número de columnas de asientos en la zona.
     * @param baseX La coordenada X central donde se ubicará la zona.
     * @param baseY La coordenada Y central donde se ubicará la zona.
     * @param asientos Una {@link List} opcional de {@link Asiento}s persistidos para dibujar. Si es {@code null} o vacía,
     *                 se dibujan asientos genéricos basados en {@code filas} y {@code columnas}.
     */
    private void dibujarZonaGenerica(String nombre, TipoZona tipo, int filas, int columnas, double baseX, double baseY, List<Asiento> asientos) {
        double ancho = columnas * 12;
        double alto = filas * 12;
        double inicioX = Math.max(5, baseX - ancho / 2);
        double inicioY = Math.max(25, baseY - alto / 2);

        renderizador.dibujarEtiquetaZona(nombre, inicioX, inicioY, ancho);

        if (asientos != null && !asientos.isEmpty()) {
            for (Asiento asiento : asientos) {
                Rectangle r = renderizador.crearRectanguloAsiento(tipo, inicioX, inicioY, asiento.getFila() - 1, asiento.getNumero() - 1);
                gestorEstados.actualizarVisualAsiento(r, asiento, tipo, interaccion.getAsientosSeleccionados().contains(asiento));
                interaccion.configurarAsiento(r, asiento, tipo, gestorEstados);
                renderizador.agregarAlPanel(r);
            }
        } else {
            for (int f = 0; f < filas; f++) {
                for (int c = 0; c < columnas; c++) {
                    Rectangle r = renderizador.crearRectanguloAsiento(tipo, inicioX, inicioY, f, c);
                    renderizador.instalarTooltipSimple(r, f, c);
                    renderizador.agregarAlPanel(r);
                }
            }
        }
    }

    /**
     * <p><b>Renderiza un recinto utilizando plantillas de zona para previsualización.</b></p>
     *
     * <p>Este método es similar a {@code renderizar(Escenario, List<Zona>)}, pero opera con {@link PlantillaZona}s
     * que no contienen asientos persistidos, sino solo sus dimensiones y posición.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li><b>Ajuste de Dimensiones del Panel:</b> Llama a {@code layout.ajustarDimensionesPanelPlantillas()}
     *         para asegurar que el panel tenga el tamaño adecuado para contener el escenario y las plantillas de zona.</li>
     *     <li><b>Limpieza del Panel:</b> Llama a {@code renderizador.limpiarPanel()} para eliminar
     *         cualquier elemento dibujado previamente.</li>
     *     <li><b>Dibujo del Escenario:</b>
     *         <ul>
     *             <li>Obtiene los datos de posición y tamaño del escenario a través de {@code layout.obtenerDatosEscenarioSilencioso()}.</li>
     *             <li>Llama a {@code renderizador.dibujarEscenario()} para dibujar el escenario.</li>
     *         </ul>
     *     </li>
     *     <li><b>Iteración y Dibujo de Plantillas de Zona:</b>
     *         <ul>
     *             <li>Inicializa un contador para manejar múltiples plantillas en la misma {@link PosicionZona}.</li>
     *             <li>Para cada {@link PlantillaZona} en la lista:
     *                 <ul>
     *                     <li>Calcula la posición base central de la plantilla de zona utilizando {@code layout.calcularPosicionBaseZona()}.</li>
     *                     <li>Llama a {@code dibujarZonaGenerica()} pasando {@code null} para los asientos,
     *                         lo que indica que se deben dibujar asientos genéricos basados en las dimensiones de la plantilla.</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param posicionEscenario La {@link PosicionEscenario} del escenario para la previsualización.
     * @param plantillas Una {@link List} de {@link PlantillaZona}s a dibujar.
     */
    public void renderizarPlantillas(PosicionEscenario posicionEscenario, List<PlantillaZona> plantillas) {
        layout.ajustarDimensionesPanelPlantillas(posicionEscenario, plantillas);
        renderizador.limpiarPanel();

        double[] datosEscenario = layout.obtenerDatosEscenarioSilencioso(posicionEscenario);
        renderizador.dibujarEscenario(datosEscenario, posicionEscenario);

        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        Map<PosicionZona, Integer> contadorZonas = new HashMap<>();

        for (PlantillaZona pZona : plantillas) {
            int index = contadorZonas.getOrDefault(pZona.posicionZona(), 0);
            contadorZonas.put(pZona.posicionZona(), index + 1);

            double[] base = layout.calcularPosicionBaseZona(pZona.posicionZona(), escX, escY, escW, escH, index);
            dibujarZonaGenerica(pZona.nombre(), pZona.tipoZona(), pZona.filas(), pZona.columnas(), base[0], base[1], null);
        }
    }

    /**
     * Calcula la posición central base para una zona en función de la posición del escenario y su índice.
     * Esta operación se delega al {@link ServicioLayoutRecinto}.
     *
     * @param posicion La {@link PosicionZona} cardinal de la zona.
     * @param escX La coordenada X del escenario.
     * @param escY La coordenada Y del escenario.
     * @param escW El ancho del escenario.
     * @param escH El alto del escenario.
     * @param index El índice de la zona en esa dirección (para aplicar separación y apilamiento).
     * @return Un arreglo {@code double[]} con las coordenadas {X, Y} centrales de la zona.
     */
    public double[] calcularPosicionBaseZona(PosicionZona posicion, double escX, double escY, double escW, double escH, int index) {
        return layout.calcularPosicionBaseZona(posicion, escX, escY, escW, escH, index);
    }

    /**
     * Obtiene las coordenadas y dimensiones del escenario sin dibujarlo.
     * Esta operación se delega al {@link ServicioLayoutRecinto}.
     *
     * @param posicion La {@link PosicionEscenario} del escenario.
     * @return Un arreglo {@code double[]} con {X, Y, Ancho, Alto} del escenario.
     */
    public double[] obtenerDatosEscenarioSilencioso(PosicionEscenario posicion) {
        return layout.obtenerDatosEscenarioSilencioso(posicion);
    }
}
