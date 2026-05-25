package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los diferentes tipos de secciones que pueden incluirse en un reporte.
 * Estas secciones representan métricas o información específica que se puede generar.
 */
public enum TipoSeccionReporte {

    /**
     * Sección que muestra las ventas totales o detalladas por un período de tiempo.
     */
    VENTAS_POR_PERIODO,
    /**
     * Sección que presenta datos relacionados con la ocupación de eventos o recintos.
     */
    OCUPACION,
    /**
     * Sección que detalla las cancelaciones de eventos o compras.
     */
    CANCELACIONES,
    /**
     * Sección que lista los eventos con mejor rendimiento según alguna métrica (ej. ocupación, ventas).
     */
    TOP_EVENTOS;
}
