package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Implementación base de la interfaz {@link Reporte} para generar un reporte administrativo.
 * Este reporte incluye información general y estadísticas básicas del sistema.
 */
public class ReporteAdminBase implements Reporte {

    private final UsuarioRepositorio usuarioRepositorio;
    private final EventoRepositorio eventoRepositorio;
    private final RecintoRepositorio recintoRepositorio;
    private final CompraRepositorio compraRepositorio;

    /**
     * Constructor para {@code ReporteAdminBase}.
     */
    public ReporteAdminBase(UsuarioRepositorio usuarioRepositorio, EventoRepositorio eventoRepositorio, RecintoRepositorio recintoRepositorio, CompraRepositorio compraRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.eventoRepositorio = eventoRepositorio;
        this.recintoRepositorio = recintoRepositorio;
        this.compraRepositorio = compraRepositorio;
    }

    /**
     * Construye el contenido del reporte administrativo base.
     * Incluye un título con la fecha actual y estadísticas generales del sistema.
     *
     * @param constructorReporte El {@link ConstructorReporte} que se utilizará para añadir elementos al reporte.
     */
    @Override
    public void construirReporte(ConstructorReporte constructorReporte) {
        LocalDate fecha = LocalDate.now();

        // ======= ENCABEZADO ESTILIZADO =======
        constructorReporte.agregarTitulo("REPORTE OPERATIVO E INDUSTRIAL: BOLETÓPOLIS");
        constructorReporte.agregarTexto("Fecha de Emisión: " + fecha);
        constructorReporte.agregarTexto("Generado por: Módulo de Auditoría Automatizado - Grupo 'Los Polimórficos'");
        constructorReporte.agregarTexto("---------------------------------------------------------------------------------");

        // ======= 1. VOLUMETRÍA GENERAL =======
        constructorReporte.agregarTitulo("1. Inventario de Datos Generales");
        int totalUsuarios = usuarioRepositorio.contarUsuarios();
        int totalRecintos = recintoRepositorio.contarRecintos();
        int totalEventos = eventoRepositorio.contarEventos();
        int totalCompras = compraRepositorio.contarCompras();

        constructorReporte.agregarTexto("Total de Usuarios Registrados: " + totalUsuarios);
        constructorReporte.agregarTexto("Total de Recintos Configurados: " + totalRecintos);
        constructorReporte.agregarTexto("Total de Eventos Históricos: " + totalEventos);
        constructorReporte.agregarTexto("Transacciones de Compra Procesadas: " + totalCompras);

        // ======= 2. RENDIMIENTO FINANCIERO (STREAMS) =======
        constructorReporte.agregarTitulo("2. Balance Financiero Simulado");

        // Calcular el dinero total recaudado sumando el total de cada objeto Compra
        double ingresosTotales = compraRepositorio.getCompras().stream()
                .mapToDouble(Compra::getTotalCompra)
                .sum();

        double ticketPromedio = totalCompras > 0 ? (ingresosTotales / totalCompras) : 0.0;

        // CORRECCIÓN PARA PDFBOX: Convertimos los decimales a String plano formateado usando DecimalFormat
        String ingresosFormateados = formatearMoneda(ingresosTotales);
        String ticketFormateado = formatearMoneda(ticketPromedio);

        // Pasamos strings limpios y concatenados tradicionalmente para evitar fallos de PDFBox
        constructorReporte.agregarTexto("Ingresos Brutos Totales: " + ingresosFormateados);
        constructorReporte.agregarTexto("Valor del Ticket Promedio de Compra: " + ticketFormateado);
    }

    /**
     * Formatea un valor numérico a formato moneda estándar sin usar String.format.
     * Garantiza compatibilidad absoluta con motores de PDF (PDFBox, iText, etc.).
     * * @param valor Número decimal a formatear.
     * @return String con formato "$#,##0.00" (ej: $9,050,000.00)
     */
    private String formatearMoneda(double valor) {
        // Forzamos símbolos estándar (punto para decimales, coma para miles) independientemente del SO
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.US);
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');

        // Definimos el patrón de dos decimales fijos y agrupación de miles
        DecimalFormat df = new DecimalFormat("$#,##0.00", simbolos);
        return df.format(valor);
    }
}