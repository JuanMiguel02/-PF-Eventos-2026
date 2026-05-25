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
        // Paso 1: Obtener la fecha actual para incluirla en el título del reporte.
        LocalDate fecha = LocalDate.now();

        // Paso 2: Agregar un título al reporte con el nombre del sistema y la fecha actual.
        constructorReporte.agregarTitulo("Reporte Operativo: Boletoplis " + " - Fecha: " + fecha);

        // Paso 3: Obtener el conteo de usuarios, recintos, eventos y compras desde sus respectivos repositorios.
        int totalUsuarios = usuarioRepositorio.contarUsuarios();
        int totalRecintos = recintoRepositorio.contarRecintos();
        int totalEventos = eventoRepositorio.contarEventos();
        int totalCompras = compraRepositorio.contarCompras();

        // Paso 4: Agregar cada estadística como una línea de texto al reporte.
        constructorReporte.agregarTexto("Total de Usuarios: " + totalUsuarios + " ");
        constructorReporte.agregarTexto("Total de Recintos: " + totalRecintos + " ");
        constructorReporte.agregarTexto("Total de Eventos: " + totalEventos + " ");
        constructorReporte.agregarTexto("Total de Compras: " + totalCompras + " ");

    }
}
