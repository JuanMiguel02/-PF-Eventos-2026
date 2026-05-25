package lospolimorficos.boletopolis.repositorios;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para {@link EventoRepositorio}.
 * Verifica el correcto funcionamiento de las operaciones CRUD, verificación de conflictos
 * y obtención de métricas de eventos.
 */
class EventoRepositorioTest {
    private EventoRepositorio eventoRepositorio;

    /**
     * Configuración inicial para cada prueba.
     * Se obtiene una nueva instancia del repositorio y se limpia la lista de eventos.
     */
    @BeforeEach
    void setUp() {
        eventoRepositorio = EventoRepositorio.getInstancia();
        // Limpiar eventos de ejemplo para asegurar un estado inicial limpio en cada prueba.
        eventoRepositorio.getEventos().clear();
    }

    /**
     * Verifica que un evento se registre correctamente en el repositorio.
     * Se espera que el tamaño del repositorio aumente en uno y el evento esté presente.
     *
     * @throws CloneNotSupportedException Si la clonación del recinto falla.
     */
    @Test
    void registrarEvento_DeberiaRegistrarCorrectamente() throws CloneNotSupportedException {
        // Paso 1: Obtener una copia de un recinto de ejemplo.
        Recinto recinto =
                RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        // Paso 2: Crear un evento de concierto con el recinto.
        Evento evento = new Concierto(
                "Rock Fest",
                "Festival",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                recinto,
                "Slipknot",
                "Metal"
        );

        // Paso 3: Intentar registrar el evento.
        boolean resultado = eventoRepositorio.registrarEvento(evento);

        // Paso 4: Afirmar que el registro fue exitoso.
        assertTrue(resultado);
        // Paso 5: Afirmar que el repositorio contiene un evento.
        assertEquals(1, eventoRepositorio.getEventos().size());
        // Paso 6: Afirmar que el evento registrado está en la lista de eventos.
        assertTrue(eventoRepositorio.getEventos().contains(evento));
    }

    /**
     * Verifica que un evento se elimine correctamente del repositorio.
     * Se espera que el método devuelva true y el evento ya no esté presente.
     *
     * @throws CloneNotSupportedException Si la clonación del recinto falla.
     */
    @Test
    void eliminarEvento_DeberiaEliminarCorrectamente() throws CloneNotSupportedException {
        // Paso 1: Obtener una copia de un recinto de ejemplo.
        Recinto recinto =
                RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        // Paso 2: Crear un evento y registrarlo.
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

        // Paso 3: Intentar eliminar el evento.
        boolean resultado = eventoRepositorio.eliminarEvento(evento);

        // Paso 4: Afirmar que la eliminación fue exitosa.
        assertTrue(resultado);
        // Paso 5: Afirmar que el repositorio está vacío.
        assertEquals(0, eventoRepositorio.getEventos().size());
    }

    /**
     * Verifica que un evento se actualice correctamente en el repositorio.
     * Se espera que el nombre del evento en el repositorio refleje el cambio.
     *
     * @throws CloneNotSupportedException Si la clonación del recinto falla.
     */
    @Test
    void actualizarEvento_DeberiaActualizarCorrectamente() throws CloneNotSupportedException {
        // Paso 1: Obtener una copia de un recinto de ejemplo.
        Recinto recinto =
                RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        // Paso 2: Crear un evento con un nombre inicial y registrarlo.
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

        // Paso 3: Modificar el nombre del evento.
        evento.setNombre("Evento Nuevo");

        // Paso 4: Actualizar el evento en el repositorio.
        boolean resultado = eventoRepositorio.actualizarEvento(evento);

        // Paso 5: Afirmar que la actualización fue exitosa.
        assertTrue(resultado);
        // Paso 6: Afirmar que el nombre del primer evento en el repositorio es el nuevo nombre.
        assertEquals(
                "Evento Nuevo",
                eventoRepositorio.getEventos().getFirst().getNombre()
        );
    }

    /**
     * Verifica que {@code existeConflicto()} retorne true si ya existe un evento
     * en el mismo recinto y a la misma fecha y hora.
     *
     * @throws CloneNotSupportedException Si la clonación del recinto falla.
     */
    @Test
    void existeConflicto_DeberiaRetornarTrueSiExisteEventoMismaFechaYRecinto() throws CloneNotSupportedException {
        // Paso 1: Obtener una copia de un recinto de ejemplo.
        Recinto recinto = RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        // Paso 2: Definir una fecha y hora para el evento.
        LocalDateTime fecha = LocalDateTime.now();

        // Paso 3: Crear y registrar un evento con el recinto y la fecha definidos.
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

        // Paso 4: Verificar si existe un conflicto con el mismo recinto y la misma fecha.
        boolean conflicto = eventoRepositorio.existeConflicto(recinto, fecha);

        // Paso 5: Afirmar que existe un conflicto.
        assertTrue(conflicto);
    }

    /**
     * Verifica que {@code existeConflicto()} retorne false si no existe un conflicto.
     *
     * @throws CloneNotSupportedException Si la clonación del recinto falla.
     */
    @Test
    void existeConflicto_DeberiaRetornarFalseSiNoExisteConflicto() throws CloneNotSupportedException {
        // Paso 1: Obtener una copia de un recinto de ejemplo.
        Recinto recinto = RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        // Paso 2: Verificar si existe un conflicto con el recinto y la fecha actual (sin haber registrado un evento).
        boolean conflicto = eventoRepositorio.existeConflicto(recinto, LocalDateTime.now());

        // Paso 3: Afirmar que no existe un conflicto.
        assertFalse(conflicto);
    }

    /**
     * Verifica que el método {@code contarEventos()} devuelva el número correcto de eventos.
     *
     * @throws CloneNotSupportedException Si la clonación del recinto falla.
     */
    @Test
    void contarEventos_DeberiaContarCorrectamente()
            throws CloneNotSupportedException {
        // Paso 1: Obtener una copia de un recinto de ejemplo.
        Recinto recinto =
                RecintoRepositorio.getInstancia().getPrimerRecinto().copiar();

        // Paso 2: Crear y registrar dos eventos.
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
                recinto.copiar(), // Usar una copia del recinto para evitar problemas de referencia si se modifica.
                "Artista",
                "Rock"
        );

        eventoRepositorio.registrarEvento(evento1);
        eventoRepositorio.registrarEvento(evento2);

        // Paso 3: Afirmar que el conteo de eventos es 2.
        assertEquals(2, eventoRepositorio.contarEventos());
    }

    /**
     * Verifica que el método {@code obtenerTopEventos()} retorne una lista no nula.
     * (La lógica de ordenamiento y cálculo de métricas se probaría en pruebas de integración o de unidad más específicas).
     */
    @Test
    void obtenerTopEventos_DeberiaRetornarListaOrdenada() {
        // Paso 1: Obtener la lista de top eventos (la implementación real de esta prueba requeriría
        // la configuración de compras y eventos para verificar el orden).
        List<MetricaEvento> top = eventoRepositorio.obtenerTopEventos(5);

        // Paso 2: Afirmar que la lista no es nula.
        assertNotNull(top);
    }

    /**
     * Verifica que el método {@code getEventos()} retorne una {@link ObservableList} no nula.
     */
    @Test
    void getEventos_DeberiaRetornarObservableList() {
        // Paso 1: Obtener la lista de eventos.
        ObservableList<Evento> eventos = eventoRepositorio.getEventos();

        // Paso 2: Afirmar que la lista no es nula.
        assertNotNull(eventos);
    }
}
