package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

import java.time.LocalDate;

public class ReporteBase implements Reporte{

    private final UsuarioRepositorio usuarioRepositorio;
    private final EventoRepositorio eventoRepositorio;
    private final RecintoRepositorio recintoRepositorio;

    public ReporteBase(UsuarioRepositorio usuarioRepositorio, EventoRepositorio eventoRepositorio, RecintoRepositorio recintoRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.eventoRepositorio = eventoRepositorio;
        this.recintoRepositorio = recintoRepositorio;
    }

    @Override
    public void construirReporte(ConstructorReporte constructorReporte) {

        LocalDate fecha = LocalDate.now();

        constructorReporte.agregarTitulo("Reporte Operativo: Boletoplis " + " - Fecha: " + fecha);

        int totalUsuarios = usuarioRepositorio.contarUsuarios();
        int totalRecintos = recintoRepositorio.contarRecintos();
        int totalEventos = eventoRepositorio.contarEventos();

        constructorReporte.agregarTexto("Total de Usuarios: " + totalUsuarios + " ");
        constructorReporte.agregarTexto("Total de Recintos: " + totalRecintos + " ");
        constructorReporte.agregarTexto("Total de Eventos: " + totalEventos + " ");

    }
}
