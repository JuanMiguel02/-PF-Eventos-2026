package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>ServicioLayoutRecinto</b></p>
 *
 * <p>Este servicio se encarga de gestionar el layout y posicionamiento de los elementos
 * visuales del recinto dentro de un {@link AnchorPane}. Sus responsabilidades incluyen
 * calcular las dimensiones necesarias del panel, determinar las posiciones de los
 * escenarios y las zonas, y ajustar el tamaño del panel dinámicamente.</p>
 */
public class ServicioLayoutRecinto {

    private double centroX;
    private double centroY;
    private final AnchorPane panelMapa;

    /**
     * Constructor del servicio de layout del recinto.
     *
     * @param panelMapa El {@link AnchorPane} de JavaFX cuyo layout será gestionado por este servicio.
     */
    public ServicioLayoutRecinto(AnchorPane panelMapa) {
        this.panelMapa = panelMapa;
        actualizarCentros();
    }

    /**
     * Actualiza las coordenadas centrales del lienzo basándose en el tamaño actual del panel.
     * Si el panel no tiene dimensiones definidas, utiliza valores por defecto (500, 400).
     * Este método debe ser llamado cada vez que las dimensiones del {@link AnchorPane} puedan haber cambiado.
     */
    public void actualizarCentros() {
        this.centroX = panelMapa.getPrefWidth() > 0 ? panelMapa.getPrefWidth() / 2 : 500;
        this.centroY = panelMapa.getPrefHeight() > 0 ? panelMapa.getPrefHeight() / 2 : 400;
    }

    /**
     * Obtiene la coordenada X central actual del panel.
     * @return La coordenada X central.
     */
    public double getCentroX() {
        return centroX;
    }

    /**
     * Obtiene la coordenada Y central actual del panel.
     * @return La coordenada Y central.
     */
    public double getCentroY() {
        return centroY;
    }

    /**
     * <p><b>Obtiene las coordenadas y dimensiones de un escenario sin dibujarlo.</b></p>
     *
     * <p>Calcula la posición y el tamaño del escenario basándose en la posición cardinal
     * especificada y el centro actual del panel. Esto permite determinar el espacio
     * que ocupará el escenario antes de su renderizado.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Define un ancho y alto por defecto para el escenario (150x40).</li>
     *     <li>Calcula la posición inicial (X, Y) del escenario para que esté centrado en el panel.</li>
     *     <li>Si se especifica una {@link PosicionEscenario} (ARRIBA, ABAJO, IZQUIERDA, DERECHA),
     *         ajusta las coordenadas X o Y del escenario para moverlo a la posición deseada
     *         con un desplazamiento predefinido.</li>
     *     <li>Retorna un arreglo con las coordenadas X, Y, ancho y alto calculadas.</li>
     * </ol>
     *
     * @param posicion La {@link PosicionEscenario} deseada para el escenario (puede ser {@code null} para un escenario central).
     * @return Un arreglo {@code double[]} que contiene {X, Y, Ancho, Alto} del escenario.
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
     * <p><b>Calcula el punto central base para una zona en función de la posición del escenario.</b></p>
     *
     * <p>Implementa la lógica de apilamiento cuando hay múltiples zonas en la misma dirección
     * cardinal (Norte, Sur, Este, Oeste) respecto al escenario. Esto asegura que las zonas
     * no se superpongan y se distribuyan ordenadamente.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Calcula el punto central del escenario ({@code baseX}, {@code baseY}).</li>
     *     <li>Define un desplazamiento inicial ({@code offset}) y una separación entre zonas ({@code separacion}).</li>
     *     <li>Inicializa los deltas {@code dx} y {@code dy} a cero.</li>
     *     <li>Según la {@link PosicionZona} especificada:
     *         <ul>
     *             <li>Ajusta {@code dy} para zonas NORTE o SUR, moviéndolas verticalmente desde el escenario.</li>
     *             <li>Ajusta {@code dx} para zonas ESTE u OESTE, moviéndolas horizontalmente desde el escenario.</li>
     *             <li>El ajuste incluye la mitad del alto/ancho del escenario, el offset inicial y
     *                 un factor de {@code index * separacion} para apilar zonas en la misma dirección.</li>
     *         </ul>
     *     </li>
     *     <li>Retorna un arreglo con las coordenadas {X, Y} del punto central base de la zona.</li>
     * </ol>
     *
     * @param posicion La {@link PosicionZona} cardinal de la zona (NORTE, SUR, ESTE, OESTE).
     * @param escX La coordenada X del escenario.
     * @param escY La coordenada Y del escenario.
     * @param escW El ancho del escenario.
     * @param escH El alto del escenario.
     * @param index El índice de la zona en esa dirección (0 para la primera, 1 para la segunda, etc.),
     *              utilizado para aplicar una separación progresiva.
     * @return Un arreglo {@code double[]} con las coordenadas {X, Y} centrales de la zona.
     */
    public double[] calcularPosicionBaseZona(PosicionZona posicion, double escX, double escY, double escW, double escH, int index) {
        double baseX = escX + escW / 2;
        double baseY = escY + escH / 2;
        double offset = 60; // Desplazamiento inicial desde el escenario
        double separacion = 100; // Separación entre zonas apiladas

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

    /**
     * Ajusta las dimensiones preferidas, mínimas y máximas del {@link AnchorPane}
     * para que contenga adecuadamente el escenario y todas las zonas con asientos persistidos.
     *
     * @param escenario El {@link Escenario} del recinto.
     * @param zonas La {@link List} de {@link Zona}s con asientos persistidos.
     */
    public void ajustarDimensionesPanel(Escenario escenario, List<Zona> zonas) {
        double[] bounds = calcularBounds(escenario != null ? escenario.posicion() : null, zonas, null);
        aplicarDimensiones(bounds);
    }

    /**
     * Ajusta las dimensiones preferidas, mínimas y máximas del {@link AnchorPane}
     * para que contenga adecuadamente el escenario y todas las {@link PlantillaZona}s.
     * Este método es útil para la previsualización de layouts.
     *
     * @param posEsc La {@link PosicionEscenario} del escenario.
     * @param plantillas La {@link List} de {@link PlantillaZona}s a considerar.
     */
    public void ajustarDimensionesPanelPlantillas(PosicionEscenario posEsc, List<PlantillaZona> plantillas) {
        double[] bounds = calcularBounds(posEsc, null, plantillas);
        aplicarDimensiones(bounds);
    }

    /**
     * <p><b>Calcula los límites mínimos y máximos (bounding box) que abarcan el escenario y todas las zonas.</b></p>
     *
     * <p>Este método es fundamental para determinar el tamaño óptimo del {@link AnchorPane}
     * para que todos los elementos del recinto sean visibles sin recortes.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li><b>Inicialización de Límites:</b>
     *         <ul>
     *             <li>Inicializa {@code minX, minY} con valores máximos y {@code maxX, maxY} con valores mínimos
     *                 para que cualquier coordenada real los actualice.</li>
     *         </ul>
     *     </li>
     *     <li><b>Cálculo de Límites del Escenario:</b>
     *         <ul>
     *             <li>Obtiene los datos del escenario (X, Y, Ancho, Alto) utilizando {@code obtenerDatosEscenarioSilencioso()}.</li>
     *             <li>Actualiza {@code minX, minY, maxX, maxY} para incluir el área del escenario,
     *                 añadiendo un margen de seguridad (50px antes, 100px después).</li>
     *         </ul>
     *     </li>
     *     <li><b>Cálculo de Límites de Zonas (si se proporcionan):</b>
     *         <ul>
     *             <li>Itera sobre la lista de {@link Zona}s.</li>
     *             <li>Para cada zona, calcula su posición base central usando {@code calcularPosicionBaseZona()}.</li>
     *             <li>Determina el ancho y alto de la zona basándose en el número de filas y columnas de sus asientos.</li>
     *             <li>Actualiza {@code minX, minY, maxX, maxY} para incluir el área de la zona,
     *                 también con un margen de seguridad.</li>
     *         </ul>
     *     </li>
     *     <li><b>Cálculo de Límites de Plantillas de Zona (si se proporcionan):</b>
     *         <ul>
     *             <li>Similar al paso anterior, pero opera con {@link PlantillaZona}s para previsualización.</li>
     *         </ul>
     *     </li>
     *     <li><b>Ajuste Final de Límites:</b>
     *         <ul>
     *             <li>Asegura que {@code minX} y {@code minY} no sean menores que 0.</li>
     *             <li>Asegura que {@code maxX} y {@code maxY} sean al menos 800x600 para un tamaño mínimo del panel.</li>
     *         </ul>
     *     </li>
     *     <li>Retorna un arreglo {@code double[]} con los límites calculados: {minX, minY, maxX, maxY}.</li>
     * </ol>
     *
     * @param posEsc La {@link PosicionEscenario} del escenario.
     * @param zonas Una {@link List} de {@link Zona}s (puede ser {@code null}).
     * @param plantillas Una {@link List} de {@link PlantillaZona}s (puede ser {@code null}).
     * @return Un arreglo {@code double[]} con los límites {minX, minY, maxX, maxY} que abarcan todos los elementos.
     */
    private double[] calcularBounds(PosicionEscenario posEsc, List<Zona> zonas, List<PlantillaZona> plantillas) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        // Incluir el escenario en los límites
        double[] datosEsc = obtenerDatosEscenarioSilencioso(posEsc);
        minX = Math.min(minX, datosEsc[0] - 50); // Margen
        minY = Math.min(minY, datosEsc[1] - 50); // Margen
        maxX = Math.max(maxX, datosEsc[0] + datosEsc[2] + 100); // Margen
        maxY = Math.max(maxY, datosEsc[1] + datosEsc[3] + 100); // Margen

        Map<PosicionZona, Integer> contadores = new HashMap<>();

        // Incluir zonas persistidas
        if (zonas != null) {
            for (Zona zona : zonas) {
                int index = contadores.getOrDefault(zona.getPosicionZona(), 0);
                contadores.put(zona.getPosicionZona(), index + 1);

                double[] base = calcularPosicionBaseZona(zona.getPosicionZona(), datosEsc[0], datosEsc[1], datosEsc[2], datosEsc[3], index);
                int filas = zona.getAsientos().stream().mapToInt(Asiento::getFila).max().orElse(0);
                int columnas = zona.getAsientos().stream().mapToInt(Asiento::getNumero).max().orElse(0);

                double ancho = columnas * 12;
                double alto = filas * 12;

                minX = Math.min(minX, base[0] - (ancho / 2) - 50);
                minY = Math.min(minY, base[1] - (alto / 2) - 50);
                maxX = Math.max(maxX, base[0] + (ancho / 2) + 100);
                maxY = Math.max(maxY, base[1] + (alto / 2) + 100);
            }
        }

        // Incluir plantillas de zona
        if (plantillas != null) {
            for (PlantillaZona p : plantillas) {
                int index = contadores.getOrDefault(p.posicionZona(), 0);
                contadores.put(p.posicionZona(), index + 1);

                double[] base = calcularPosicionBaseZona(p.posicionZona(), datosEsc[0], datosEsc[1], datosEsc[2], datosEsc[3], index);
                double ancho = p.columnas() * 12;
                double alto = p.filas() * 12;

                minX = Math.min(minX, base[0] - (ancho / 2) - 50);
                minY = Math.min(minY, base[1] - (alto / 2) - 50);
                maxX = Math.max(maxX, base[0] + (ancho / 2) + 100);
                maxY = Math.max(maxY, base[1] + (alto / 2) + 100);
            }
        }

        // Asegurar que los límites no sean negativos y un tamaño mínimo
        minX = Math.max(minX, 0);
        minY = Math.max(minY, 0);
        maxX = Math.max(maxX, 800); // Ancho mínimo
        maxY = Math.max(maxY, 600); // Alto mínimo

        return new double[]{minX, minY, maxX, maxY};
    }

    /**
     * <p><b>Aplica las dimensiones calculadas al {@link AnchorPane} del mapa.</b></p>
     *
     * <p>Este método ajusta el ancho y alto preferido y mínimo del panel
     * para que se adapte a los límites calculados por {@code calcularBounds()}.
     * Después de aplicar las nuevas dimensiones, se llama a {@code actualizarCentros()}
     * para recalcular el centro del panel.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Extrae {@code minX, minY, maxX, maxY} del arreglo {@code bounds}.</li>
     *     <li>Calcula el ancho final ({@code finalWidth}) como la diferencia entre {@code maxX} y el mínimo entre 0 y {@code minX}.
     *         Esto maneja casos donde {@code minX} podría ser negativo, asegurando que el ancho sea positivo.</li>
     *     <li>Calcula el alto final ({@code finalHeight}) de manera similar.</li>
     *     <li>Establece {@code prefWidth, prefHeight, minWidth, minHeight} del {@link AnchorPane}
     *         con los valores calculados.</li>
     *     <li>Llama a {@code actualizarCentros()} para recalcular el centro del panel con las nuevas dimensiones.</li>
     * </ol>
     *
     * @param bounds Un arreglo {@code double[]} que contiene los límites {minX, minY, maxX, maxY}.
     */
    private void aplicarDimensiones(double[] bounds) {
        double minX = bounds[0];
        double minY = bounds[1];
        double maxX = bounds[2];
        double maxY = bounds[3];

        double finalWidth = maxX - Math.min(0, minX);
        double finalHeight = maxY - Math.min(0, minY);

        panelMapa.setPrefWidth(finalWidth);
        panelMapa.setPrefHeight(finalHeight);
        panelMapa.setMinWidth(finalWidth);
        panelMapa.setMinHeight(finalHeight);
        actualizarCentros();
    }
}
