package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;
import lospolimorficos.boletopolis.services.ServicioAlerta;

import java.time.LocalDateTime;

import static lospolimorficos.boletopolis.models.ReporteFactory.crearReporte;

/**
 * Controlador para la vista de reportes de Boletópolis.
 * Permite previsualizar y exportar reportes operativos en diferentes formatos
 * utilizando patrones Builder y Decorator.
 */
public class ReportesController {

    @FXML
    private CheckBox checkVentasPeriodo;

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


    @FXML
    public void initialize() {
        comboFormato.getItems().setAll(TipoExportacion.values());
    }

    /**
     * Genera la previsualización del reporte en el contenedor VBox de la interfaz.
     */
    @FXML
    private void generarVistaPrevia() {

        ConstructorUIBuilder uiBuilder = new ConstructorUIBuilder();
        uiBuilder.iniciarDocumento(null);

        Reporte reporte = configurarReporte();
        reporte.construirReporte(uiBuilder);
        uiBuilder.finalizarDocumento();

        vboxContenedorReporte.getChildren().clear();
        vboxContenedorReporte.getChildren().add(uiBuilder.getVista());
    }

    /**
     * Exporta el reporte al formato seleccionado (PDF o Excel).
     */
    @FXML
    private void exportarReporte() {
        TipoExportacion formato = comboFormato.getValue();

        if (formato == null) {
            ServicioAlerta.mostrarAlerta("Error", "Debe seleccionar un formato de exportación.", Alert.AlertType.WARNING);
            return;
        }

        ConstructorReporte adaptador = crearReporte(formato);;

        LocalDateTime fechaExportacion = LocalDateTime.now();
        String rutaArchivo = "reporte_operativo - " + fechaExportacion + "." + formato.getExtension();
        adaptador.iniciarDocumento(rutaArchivo);

        Reporte reporte = configurarReporte();
        reporte.construirReporte(adaptador);
        adaptador.finalizarDocumento();

        ServicioAlerta.mostrarAlerta("Éxito", "Reporte exportado correctamente como: " + rutaArchivo, Alert.AlertType.INFORMATION);
    }

    /**
     * Configura el reporte base y aplica los decoradores según la selección del usuario.
     * @return El reporte configurado.
     */
    private Reporte configurarReporte() {
        Reporte reporte = new ReporteBase(usuarioRepositorio, eventoRepositorio, recintoRepositorio);

        if (checkVentasPeriodo.isSelected()) {
            reporte = new DecoradorVentasPeriodo(reporte, compraRepositorio);
        }

        // Se pueden añadir más decoradores aquí si se implementan en el futuro.

        return reporte;
    }
}
