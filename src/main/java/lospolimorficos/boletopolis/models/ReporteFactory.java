package lospolimorficos.boletopolis.models;

/**
 * Clase Factory que proporciona un método para crear instancias de {@link ConstructorReporte}
 * basándose en el tipo de exportación deseado.
 * Implementa el patrón Factory Method para desacoplar la creación de objetos de su uso.
 */
public class ReporteFactory {

    /**
     * Crea y devuelve una implementación concreta de {@link ConstructorReporte}
     * según el {@link TipoExportacion} especificado.
     *
     * @param tipo El tipo de exportación deseado (PDF o EXCEL).
     * @return Una instancia de {@link ConstructorReporte} adecuada para el tipo de exportación.
     * @throws IllegalArgumentException Si el tipo de exportación no es reconocido.
     */
    public static ConstructorReporte crearReporte(TipoExportacion tipo){
        return switch (tipo) {
            case PDF -> new AdaptadorReportePDF();
            case EXCEL -> new AdaptadorReporteExcel();
        };
    }

}
