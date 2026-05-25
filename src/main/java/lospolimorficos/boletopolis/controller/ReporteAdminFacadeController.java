package lospolimorficos.boletopolis.controller;

import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;
import lospolimorficos.boletopolis.models.FiltroReporte;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static lospolimorficos.boletopolis.models.ReporteFactory.crearReporte;

/**
 * Controlador Facade para simplificar la creación y exportación de reportes administrativos.
 * Este controlador abstrae la complejidad de construir reportes con diferentes secciones
 * y formatos de exportación, utilizando patrones como Decorator y Factory.
 */
public class ReporteAdminFacadeController {

    private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy__HH-mm-ss");

    private final UsuarioRepositorio usuarioRepositorio;
    private final EventoRepositorio eventoRepositorio;
    private final RecintoRepositorio recintoRepositorio;
    private final CompraRepositorio compraRepositorio;

    /**
     * Constructor para {@code ReporteAdminFacadeController}.
     *
     * @param usuarioRepositorio El repositorio de usuarios para obtener datos de clientes.
     * @param eventoRepositorio El repositorio de eventos para obtener datos de eventos.
     * @param recintoRepositorio El repositorio de recintos para obtener datos de recintos.
     * @param compraRepositorio El repositorio de compras para obtener datos de ventas.
     */
    public ReporteAdminFacadeController(UsuarioRepositorio usuarioRepositorio, EventoRepositorio eventoRepositorio, RecintoRepositorio recintoRepositorio, CompraRepositorio compraRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.eventoRepositorio = eventoRepositorio;
        this.recintoRepositorio = recintoRepositorio;
        this.compraRepositorio = compraRepositorio;
    }

    /**
     * Construye un objeto {@link Reporte} base y lo decora con las secciones de métricas especificadas.
     *
     * @param secciones Un conjunto de {@link TipoSeccionReporte} que indican qué métricas deben incluirse.
     * @param filtro Un {@link FiltroReporte} que puede contener parámetros para las métricas (ej. rango de fechas).
     * @return Un objeto {@link Reporte} decorado con las métricas solicitadas.
     */
    private Reporte construirReporte(Set<TipoSeccionReporte> secciones, FiltroReporte filtro){
        // Paso 1: Crear un reporte base que incluye la información general administrativa.
        Reporte reporte = new ReporteAdminBase(usuarioRepositorio, eventoRepositorio, recintoRepositorio, compraRepositorio);

        // Paso 2: Iterar sobre las secciones de reporte solicitadas para aplicar los decoradores correspondientes.
        for(TipoSeccionReporte seccion : secciones){
            switch (seccion){
                // Si la sección es TOP_EVENTOS, decorar el reporte con la métrica de Top Eventos.
                case TOP_EVENTOS -> reporte = new DecoradorMetricas(reporte, new MetricaTopEventos(eventoRepositorio));
                // Si la sección es VENTAS_POR_PERIODO, decorar el reporte con la métrica de Ventas por Período.
                case VENTAS_POR_PERIODO -> reporte = new DecoradorMetricas(reporte, new MetricaVentasPorPeriodo(compraRepositorio, filtro));
            }
        }
        // Paso 3: Devolver el reporte final con todos los decoradores aplicados.
        return reporte;
    }

    /**
     * Genera una vista previa de un reporte en formato de interfaz de usuario (JavaFX VBox).
     *
     * @param secciones Un conjunto de {@link TipoSeccionReporte} que indican qué métricas deben incluirse.
     * @param filtro Un {@link FiltroReporte} que puede contener parámetros para las métricas.
     * @return Un {@link VBox} de JavaFX que contiene la representación visual del reporte.
     */
    public VBox generarVistaPrevia(Set<TipoSeccionReporte> secciones, FiltroReporte filtro){
        // Paso 1: Construir el objeto Reporte con las secciones y filtros especificados.
        Reporte reporte = construirReporte(secciones, filtro);

        // Paso 2: Crear un ConstructorReporteUI para construir el reporte en una interfaz de usuario.
        ConstructorReporteUI uiBuilder = new ConstructorReporteUI();
        // Paso 3: Iniciar el documento del constructor UI (la ruta del archivo no es relevante para la vista previa).
        uiBuilder.iniciarDocumento(null);

        // Paso 4: Construir el reporte utilizando el constructor UI.
        reporte.construirReporte(uiBuilder);
        // Paso 5: Finalizar el documento del constructor UI.
        uiBuilder.finalizarDocumento();

        // Paso 6: Devolver la vista (VBox) generada por el constructor UI.
        return uiBuilder.getVista();
    }

    /**
     * Exporta un reporte a un archivo en el formato especificado (PDF o Excel).
     *
     * @param tipoExportacion El {@link TipoExportacion} deseado (PDF o EXCEL).
     * @param secciones Un conjunto de {@link TipoSeccionReporte} que indican qué métricas deben incluirse.
     * @param filtro Un {@link FiltroReporte} que puede contener parámetros para las métricas.
     * @return La ruta del archivo donde se guardó el reporte exportado.
     */
    public String exportarReporte(TipoExportacion tipoExportacion, Set<TipoSeccionReporte> secciones, FiltroReporte filtro){
        // Paso 1: Crear un constructor de reporte específico para el tipo de exportación (PDF o Excel)
        // utilizando el ReporteFactory.
        ConstructorReporte constructorReporte = crearReporte(tipoExportacion);
        // Paso 2: Generar una marca de tiempo para el nombre del archivo, asegurando que sea único.
        String fechaExportacion = LocalDateTime.now().format(formatoFecha);
        // Paso 3: Construir la ruta completa del archivo de salida.
        String rutaArchivo = "reporte_operativo_" + fechaExportacion + tipoExportacion.getExtension();

        // Paso 4: Iniciar el documento del constructor de reporte con la ruta del archivo.
        constructorReporte.iniciarDocumento(rutaArchivo);

        // Paso 5: Construir el objeto Reporte con las secciones y filtros especificados.
        Reporte reporte = construirReporte(secciones, filtro);
        // Paso 6: Construir el reporte utilizando el constructor de reporte específico para la exportación.
        reporte.construirReporte(constructorReporte);
        // Paso 7: Finalizar el documento del constructor de reporte, lo que guarda el archivo.
        constructorReporte.finalizarDocumento();

        // Paso 8: Devolver la ruta del archivo generado.
        return rutaArchivo;
    }
}
