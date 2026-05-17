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

//Patrón Facade para simplificar creación de los reportes
public class ReporteAdminFacadeController {

    private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy__HH-mm-ss");

    private final UsuarioRepositorio usuarioRepositorio;
    private final EventoRepositorio eventoRepositorio;
    private final RecintoRepositorio recintoRepositorio;
    private final CompraRepositorio compraRepositorio;

    public ReporteAdminFacadeController(UsuarioRepositorio usuarioRepositorio, EventoRepositorio eventoRepositorio, RecintoRepositorio recintoRepositorio, CompraRepositorio compraRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.eventoRepositorio = eventoRepositorio;
        this.recintoRepositorio = recintoRepositorio;
        this.compraRepositorio = compraRepositorio;
    }

    private Reporte construirReporte(Set<TipoSeccionReporte> secciones, FiltroReporte filtro){

        Reporte reporte = new ReporteAdminBase(usuarioRepositorio, eventoRepositorio, recintoRepositorio, compraRepositorio);

        for(TipoSeccionReporte seccion : secciones){
            switch (seccion){
                case TOP_EVENTOS -> reporte = new DecoradorMetricas(reporte, new MetricaTopEventos(eventoRepositorio));
                case VENTAS_POR_PERIODO -> reporte = new DecoradorMetricas(reporte, new MetricaVentasPorPeriodo(compraRepositorio, filtro));
            }
        }
        return reporte;
    }

    public VBox generarVistaPrevia(Set<TipoSeccionReporte> secciones, FiltroReporte filtro){
        Reporte reporte = construirReporte(secciones, filtro);

        ConstructorReporteUI uiBuilder = new ConstructorReporteUI();
        uiBuilder.iniciarDocumento(null);

        reporte.construirReporte(uiBuilder);
        uiBuilder.finalizarDocumento();

        return uiBuilder.getVista();
    }

    public String exportarReporte(TipoExportacion tipoExportacion, Set<TipoSeccionReporte> secciones, FiltroReporte filtro){

       ConstructorReporte constructorReporte = crearReporte(tipoExportacion);
       String fechaExportacion = LocalDateTime.now().format(formatoFecha);
       String rutaArchivo = "reporte_operativo_" + fechaExportacion + tipoExportacion.getExtension();

       constructorReporte.iniciarDocumento(rutaArchivo);

       Reporte reporte = construirReporte(secciones, filtro);
       reporte.construirReporte(constructorReporte);
       constructorReporte.finalizarDocumento();

       return rutaArchivo;

    }
}
