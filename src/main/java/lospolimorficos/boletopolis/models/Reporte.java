package lospolimorficos.boletopolis.models;

/**
 * Interfaz que define el contrato para cualquier tipo de reporte en el sistema.
 * Los objetos que implementen esta interfaz serán capaces de construir su contenido
 * utilizando un {@link ConstructorReporte} proporcionado.
 * Esta interfaz es parte del patrón Decorator, permitiendo que los reportes
 * sean extendidos con funcionalidades adicionales.
 */
public interface Reporte {
    /**
     * Construye el contenido del reporte utilizando el {@link ConstructorReporte} dado.
     * Las implementaciones de este método definirán qué secciones y datos se incluyen en el reporte.
     *
     * @param constructorReporte El constructor de reporte que se utilizará para añadir elementos al reporte.
     */
    void construirReporte(ConstructorReporte constructorReporte);
}
