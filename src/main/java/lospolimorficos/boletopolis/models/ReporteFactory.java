package lospolimorficos.boletopolis.models;

//Factory Method para crear reportes según el tipo
public class ReporteFactory {

    public static ConstructorReporte crearReporte(TipoExportacion tipo){
        return switch (tipo) {
            case PDF -> new AdaptadorReportePDF();
            case EXCEL -> new AdaptadorReporteExcel();
        };
    }

}
