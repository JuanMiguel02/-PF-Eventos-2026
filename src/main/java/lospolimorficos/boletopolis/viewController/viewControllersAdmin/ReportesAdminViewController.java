package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.controller.ReporteFacadeController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

import java.util.HashSet;
import java.util.Set;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;

/**
 * Controlador para la vista de reportes de Boletópolis.
 * Permite previsualizar y exportar reportes operativos en diferentes formatos
 * utilizando patrones Builder y Decorator.
 */
public class ReportesAdminViewController {

    @FXML
    private CheckBox checkVentasPeriodo;

    @FXML
    private CheckBox checkTopEventos;

    @FXML
    private ComboBox<TipoExportacion> comboFormato;

    @FXML
    private Button btnVistaPrevia;

    @FXML
    private Button btnExportar;

    @FXML
    private ScrollPane scrollReporte;

    @FXML
    private VBox vboxContenedorReporte;

    private final UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstancia();
    private final EventoRepositorio eventoRepositorio = EventoRepositorio.getInstance();
    private final RecintoRepositorio recintoRepositorio = RecintoRepositorio.getInstancia();
    private final CompraRepositorio compraRepositorio = CompraRepositorio.getInstancia();

    private ReporteFacadeController reporteFacadeController;


    @FXML
    public void initialize() {
        comboFormato.getItems().setAll(TipoExportacion.values());

        reporteFacadeController = new ReporteFacadeController(usuarioRepositorio, eventoRepositorio, recintoRepositorio, compraRepositorio);
    }

    @FXML
    private void exportarReporte(){
        TipoExportacion formato = comboFormato.getValue();

        if(formato == null){
            mostrarAlerta("Error", "Seleccione un formato para exportar", Alert.AlertType.WARNING);
            return;
        }

        Set<TipoSeccionReporte> secciones = obtenerSeccionesReporte();
        String ruta = reporteFacadeController.exportarReporte(formato, secciones);
        mostrarAlerta("Reporte exportado", "Reporte exportado exitosamente a la ruta: " + ruta, Alert.AlertType.INFORMATION);
    }

    @FXML
    private void generarVistaPrevia(){

        Set<TipoSeccionReporte> secciones = obtenerSeccionesReporte();

        VBox vistaPrevia = reporteFacadeController.generarVistaPrevia(secciones);
        scrollReporte.setContent(vistaPrevia);

    }

    private Set<TipoSeccionReporte> obtenerSeccionesReporte(){

        Set<TipoSeccionReporte> secciones = new HashSet<>();

        if(checkVentasPeriodo.isSelected()){
            secciones.add(TipoSeccionReporte.VENTAS_POR_PERIODO);
        }
        if(checkTopEventos.isSelected()){
            secciones.add(TipoSeccionReporte.TOP_EVENTOS);
        }

        return secciones;

    }
}
