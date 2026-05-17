package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.controller.ReporteAdminFacadeController;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlerta;
import static lospolimorficos.boletopolis.services.ServicioAlerta.mostrarAlertaError;

/**
 * Controlador para la vista de reportes de Boletópolis.
 * Permite previsualizar y exportar reportes operativos en diferentes formatos
 * utilizando patrones Builder, Facade y Decorator.
 */
public class ReportesAdminViewController {

    @FXML
    private CheckBox checkVentasPeriodo;

    @FXML
    private CheckBox checkTopEventos;

    @FXML
    private DatePicker datePickerInicio;

    @FXML
    private DatePicker datePickerFin;

    @FXML
    private HBox hboxFechas;

    @FXML
    private ComboBox<TipoExportacion> comboFormato;

    @FXML
    private ScrollPane scrollReporte;

    private final UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstancia();

    private final EventoRepositorio eventoRepositorio = EventoRepositorio.getInstance();

    private final RecintoRepositorio recintoRepositorio = RecintoRepositorio.getInstancia();

    private final CompraRepositorio compraRepositorio = CompraRepositorio.getInstancia();

    private ReporteAdminFacadeController reporteAdminFacadeController;

    @FXML
    public void initialize() {

        comboFormato.getItems().setAll(TipoExportacion.values());

        reporteAdminFacadeController = new ReporteAdminFacadeController(usuarioRepositorio, eventoRepositorio, recintoRepositorio, compraRepositorio);

        hboxFechas.setVisible(false);
        hboxFechas.setManaged(false);
    }

    @FXML
    private void exportarReporte() {

        if (!validarFiltros()) {
            return;
        }

        TipoExportacion formato = comboFormato.getValue();

        if (formato == null) {
            mostrarAlerta("Error", "Seleccione un formato para exportar", Alert.AlertType.WARNING);
            return;
        }

        Set<TipoSeccionReporte> secciones = obtenerSeccionesReporte();

        FiltroReporte filtro = obtenerFiltroReporte();

        String ruta = reporteAdminFacadeController.exportarReporte(formato, secciones, filtro);

        mostrarAlerta("Reporte exportado", "Reporte exportado exitosamente a la ruta:\n" + ruta, Alert.AlertType.INFORMATION);
    }

    @FXML
    private void generarVistaPrevia() {

        if (!validarFiltros()) {
            return;
        }
        Set<TipoSeccionReporte> secciones = obtenerSeccionesReporte();
        FiltroReporte filtro = obtenerFiltroReporte();

        VBox vistaPrevia = reporteAdminFacadeController.generarVistaPrevia(secciones, filtro);

        scrollReporte.setContent(vistaPrevia);
    }

    @FXML
    private void handleCheckVentasPeriodo() {

        boolean visible = checkVentasPeriodo.isSelected();

        hboxFechas.setVisible(visible);
        hboxFechas.setManaged(visible);
    }

    private boolean validarFiltros() {

        if (checkVentasPeriodo.isSelected()) {

            if (datePickerInicio.getValue() == null || datePickerFin.getValue() == null) {
                mostrarAlertaError("Seleccione un rango de fechas");
                return false;
            }

            if (datePickerInicio.getValue().isAfter(datePickerFin.getValue())) {
                mostrarAlertaError("La fecha inicial no puede ser mayor a la fecha final");
                return false;
            }
        }

        return true;
    }

    private FiltroReporte obtenerFiltroReporte() {

        LocalDateTime fechaInicio = null;
        LocalDateTime fechaFin = null;

        LocalDate inicio = datePickerInicio.getValue();

        LocalDate fin = datePickerFin.getValue();

        if (inicio != null) {
            fechaInicio = LocalDateTime.of(inicio, LocalTime.MIN);
        }

        if (fin != null) {
            fechaFin = LocalDateTime.of(fin, LocalTime.MAX);
        }

        return new FiltroReporte(
                fechaInicio,
                fechaFin
        );
    }

    private Set<TipoSeccionReporte> obtenerSeccionesReporte() {

        Set<TipoSeccionReporte> secciones = new HashSet<>();

        if (checkVentasPeriodo.isSelected()) {
            secciones.add(TipoSeccionReporte.VENTAS_POR_PERIODO);
        }
        if (checkTopEventos.isSelected()) {
            secciones.add(TipoSeccionReporte.TOP_EVENTOS);
        }
        return secciones;
    }
}