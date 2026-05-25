package lospolimorficos.boletopolis.models;

/**
 * Interfaz que define la estrategia para generar una sección específica de un reporte.
 * Las implementaciones de esta interfaz se encargarán de construir una parte del reporte
 * utilizando un {@link ConstructorReporte}.
 */
public interface EstrategiaMetrica {

    /**
     * Genera una sección del reporte utilizando el constructor proporcionado.
     *
     * @param constructor El {@link ConstructorReporte} que se utilizará para añadir elementos al reporte.
     */
    void generarSeccion(ConstructorReporte constructor);

}
