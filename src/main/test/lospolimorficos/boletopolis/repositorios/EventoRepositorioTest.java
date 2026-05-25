package lospolimorficos.boletopolis.repositorios;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventoRepositorioTest {
    private EventoRepositorio eventoRepositorio;

    @BeforeEach
    void setUp() {

        eventoRepositorio = EventoRepositorio.getInstancia();

        // Limpiar eventos de ejemplo
        eventoRepositorio.getEventos().clear();
    }

    @Test
    void registrarEvento_DeberiaRegistrarCorrectamente() throws CloneNotSupportedException {

        Recinto recinto =
                RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        Evento evento = new Concierto(
                "Rock Fest",
                "Festival",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                recinto,
                "Slipknot",
                "Metal"
        );

        boolean resultado = eventoRepositorio.registrarEvento(evento);

        assertTrue(resultado);
        assertEquals(1, eventoRepositorio.getEventos().size());
        assertTrue(eventoRepositorio.getEventos().contains(evento));
    }

    @Test
    void eliminarEvento_DeberiaEliminarCorrectamente() throws CloneNotSupportedException {

        Recinto recinto =
                RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        Evento evento = new Concierto(
                "Rock Fest",
                "Festival",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                recinto,
                "Slipknot",
                "Metal"
        );

        eventoRepositorio.registrarEvento(evento);

        boolean resultado = eventoRepositorio.eliminarEvento(evento);

        assertTrue(resultado);
        assertEquals(0, eventoRepositorio.getEventos().size());
    }

    @Test
    void actualizarEvento_DeberiaActualizarCorrectamente() throws CloneNotSupportedException {

        Recinto recinto =
                RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        Evento evento = new Concierto(
                "Evento Viejo",
                "Descripcion",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                recinto,
                "Artista",
                "Rock"
        );

        eventoRepositorio.registrarEvento(evento);

        evento.setNombre("Evento Nuevo");

        boolean resultado = eventoRepositorio.actualizarEvento(evento);

        assertTrue(resultado);
        assertEquals(
                "Evento Nuevo",
                eventoRepositorio.getEventos().getFirst().getNombre()
        );
    }

    @Test
    void existeConflicto_DeberiaRetornarTrueSiExisteEventoMismaFechaYRecinto() throws CloneNotSupportedException {

        Recinto recinto = RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        LocalDateTime fecha = LocalDateTime.now();

        Evento evento = new Concierto(
                "Evento",
                "Descripcion",
                Ciudad.ARMENIA,
                fecha,
                recinto,
                "Artista",
                "Rock"
        );

        eventoRepositorio.registrarEvento(evento);

        boolean conflicto = eventoRepositorio.existeConflicto(recinto, fecha);

        assertTrue(conflicto);
    }

    @Test
    void existeConflicto_DeberiaRetornarFalseSiNoExisteConflicto() throws CloneNotSupportedException {

        Recinto recinto = RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        boolean conflicto = eventoRepositorio.existeConflicto(recinto, LocalDateTime.now());

        assertFalse(conflicto);
    }

    @Test
    void contarEventos_DeberiaContarCorrectamente()
            throws CloneNotSupportedException {

        Recinto recinto =
                RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        Evento evento1 = new Concierto(
                "Evento 1",
                "Descripcion",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                recinto,
                "Artista",
                "Rock"
        );

        Evento evento2 = new Concierto(
                "Evento 2",
                "Descripcion",
                Ciudad.ARMENIA,
                LocalDateTime.now().plusDays(1),
                recinto.copiar(),
                "Artista",
                "Rock"
        );

        eventoRepositorio.registrarEvento(evento1);
        eventoRepositorio.registrarEvento(evento2);

        assertEquals(2, eventoRepositorio.contarEventos());
    }

    @Test
    void obtenerTopEventos_DeberiaRetornarListaOrdenada() {

        List<MetricaEvento> top = eventoRepositorio.obtenerTopEventos(5);

        assertNotNull(top);
    }

    @Test
    void getEventos_DeberiaRetornarObservableList() {

        ObservableList<Evento> eventos = eventoRepositorio.getEventos();

        assertNotNull(eventos);
    }
}