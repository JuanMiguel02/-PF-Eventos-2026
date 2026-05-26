package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

import java.time.LocalDate;

/**
 * Implementación base de la interfaz {@link Reporte} para generar un reporte administrativo.
 * Este reporte incluye información general y estadísticas básicas del sistema.
 */
public class ReporteAdminBase implements Reporte{

    private final UsuarioRepositorio usuarioRepositorio;
    private final EventoRepositorio eventoRepositorio;
    private final RecintoRepositorio recintoRepositorio;
    private final CompraRepositorio compraRepositorio;

    /**
     * Constructor para {@code ReporteAdminBase}.
     *
     * @param usuarioRepositorio El repositorio de usuarios para obtener datos de clientes.
     * @param eventoRepositorio El repositorio de eventos para obtener datos de eventos.
     * @param recintoRepositorio El repositorio de recintos para obtener datos de recintos.
     * @param compraRepositorio El repositorio de compras para obtener datos de ventas.
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
        constructorReporte.agregarTitulo("📊REPORTE OPERATIVO E INDUSTRIAL: BOLETÓPOLIS");
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

        constructorReporte.agregarTexto(String.format(" Ingresos Brutos Totales: $%,.2f ", ingresosTotales));
        constructorReporte.agregarTexto(String.format(" Valor del Ticket Promedio de Compra: $%,.2f ", ticketPromedio));

    }
}
