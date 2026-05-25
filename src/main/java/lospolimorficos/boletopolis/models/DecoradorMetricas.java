package lospolimorficos.boletopolis.models;

/**
 * Decorador concreto que añade una sección de métricas a un reporte existente.
 * Utiliza una {@link EstrategiaMetrica} para generar el contenido específico de las métricas.
 */
public class DecoradorMetricas extends DecoradorReporte {

    /**
     * La estrategia de métrica que se utilizará para generar la sección de métricas.
     */
    private final EstrategiaMetrica estrategiaMetrica;

    /**
     * Constructor para {@code DecoradorMetricas}.
     *
     * @param reporte El objeto {@link Reporte} base al que se le añadirán las métricas.
     * @param estrategiaMetrica La {@link EstrategiaMetrica} que define cómo se generarán las métricas.
     */
    public DecoradorMetricas(Reporte reporte, EstrategiaMetrica estrategiaMetrica) {
        super(reporte);
        this.estrategiaMetrica = estrategiaMetrica;
    }

    /**
     * Construye el reporte añadiendo primero el contenido del reporte base
     * y luego una sección de métricas generada por la estrategia de métrica.
     *
     * @param constructorReporte El {@link ConstructorReporte} utilizado para construir el reporte.
     */
    @Override
    public void construirReporte(ConstructorReporte constructorReporte) {
        // Paso 1: Llamar al método construirReporte del objeto Reporte base.
        // Esto asegura que el contenido original del reporte se mantenga.
        reporte.construirReporte(constructorReporte);

        // Paso 2: Agregar un subtítulo para la sección de métricas.
        constructorReporte.agregarSubtitulo("Metricas");
        // Paso 3: Utilizar la estrategia de métrica para generar el contenido específico de las métricas
        // y añadirlo al constructor del reporte.
        estrategiaMetrica.generarSeccion(constructorReporte);

    }
}
