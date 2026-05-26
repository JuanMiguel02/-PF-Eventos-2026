package lospolimorficos.boletopolis.controller;

import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static lospolimorficos.boletopolis.models.ReporteFactory.crearReporte;

/**
 * Facade para gestionar reportes del usuario.
 *
 * Permite:
 * - Generar vista previa
 * - Exportar PDF
 * - Exportar CSV/Excel
 */
public class ReporteUsuarioFacadeController {

    private static final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern(
                    "dd-MM-yyyy__HH-mm-ss"
            );


    /**
     * Constructor.
     *
     */
    public ReporteUsuarioFacadeController() {

    }

    /**
     * Construye el reporte del usuario
     * aplicando filtros.
     */
    private Reporte construirReporte(
            Cliente cliente,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Evento evento,
            EstadoCompra estado
    ) {

        ReporteUsuario reporte =
                new ReporteUsuario(
                        cliente
                );

        // =========================
        // FILTRO FECHA
        // =========================

        if (fechaInicio != null &&
                fechaFin != null) {

            reporte.setFiltroFecha(
                    fechaInicio,
                    fechaFin
            );
        }

        // =========================
        // FILTRO EVENTO
        // =========================

        if (evento != null) {

            reporte.setFiltroEvento(evento);
        }

        // =========================
        // FILTRO ESTADO
        // =========================

        if (estado != null) {

            reporte.setFiltroEstado(estado);
        }

        return reporte;
    }

    /**
     * Genera vista previa del reporte.
     */
    public VBox generarVistaPrevia(
            Cliente cliente,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Evento evento,
            EstadoCompra estado
    ) {

        Reporte reporte =
                construirReporte(
                        cliente,
                        fechaInicio,
                        fechaFin,
                        evento,
                        estado
                );

        ConstructorReporteUI uiBuilder =
                new ConstructorReporteUI();

        uiBuilder.iniciarDocumento(null);

        reporte.construirReporte(uiBuilder);

        uiBuilder.finalizarDocumento();

        return uiBuilder.getVista();
    }

    /**
     * Exporta reporte PDF.
     */
    public String exportarPDF(
            Cliente cliente,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Evento evento,
            EstadoCompra estado
    ) {

        return exportarReporte(
                TipoExportacion.PDF,
                cliente,
                fechaInicio,
                fechaFin,
                evento,
                estado
        );
    }

    /**
     * Exporta reporte CSV/Excel.
     */
    public String exportarCSV(
            Cliente cliente,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Evento evento,
            EstadoCompra estado
    ) {

        return exportarReporte(
                TipoExportacion.EXCEL,
                cliente,
                fechaInicio,
                fechaFin,
                evento,
                estado
        );
    }

    /**
     * Exporta el reporte en el formato indicado.
     */
    private String exportarReporte(
            TipoExportacion tipoExportacion,
            Cliente cliente,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Evento evento,
            EstadoCompra estado
    ) {

        ConstructorReporte constructorReporte =
                crearReporte(tipoExportacion);

        String fechaExportacion =
                LocalDateTime.now()
                        .format(formatoFecha);

        String rutaArchivo =
                "reporte_usuario_"
                        + fechaExportacion
                        + tipoExportacion.getExtension();

        constructorReporte.iniciarDocumento(
                rutaArchivo
        );

        Reporte reporte =
                construirReporte(
                        cliente,
                        fechaInicio,
                        fechaFin,
                        evento,
                        estado
                );

        reporte.construirReporte(
                constructorReporte
        );

        constructorReporte.finalizarDocumento();

        return rutaArchivo;
    }
}