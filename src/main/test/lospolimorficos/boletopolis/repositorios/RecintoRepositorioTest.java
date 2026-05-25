package lospolimorficos.boletopolis.repositorios;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaRecinto;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import lospolimorficos.boletopolis.services.GeneradorRecinto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para {@link RecintoRepositorio}.
 * Verifica el correcto funcionamiento de las operaciones CRUD y de consulta de recintos.
 */
class RecintoRepositorioTest {
    private RecintoRepositorio recintoRepositorio;

    /**
     * Configuración inicial para cada prueba.
     * Se obtiene una nueva instancia del repositorio y se limpia la lista de recintos.
     */
    @BeforeEach
    void setUp() {
        recintoRepositorio = RecintoRepositorio.getInstancia();
        // Limpiar los recintos de ejemplo para asegurar un estado inicial limpio en cada prueba.
        recintoRepositorio.getRecintos().clear();
    }

    /**
     * Verifica que un recinto se registre correctamente en el repositorio.
     * Se espera que el tamaño del repositorio aumente en uno y el recinto esté presente.
     */
    @Test
    void registrarRecinto_DeberiaRegistrarCorrectamente() {
        // Paso 1: Crear un recinto de ejemplo.
        Recinto recinto = crearRecintoEjemplo();

        // Paso 2: Intentar registrar el recinto.
        boolean resultado = recintoRepositorio.registrarRecinto(recinto);

        // Paso 3: Afirmar que el registro fue exitoso.
        assertTrue(resultado);
        // Paso 4: Afirmar que el repositorio contiene un recinto.
        assertEquals(1, recintoRepositorio.contarRecintos());
        // Paso 5: Afirmar que el recinto registrado está en la lista de recintos.
        assertTrue(recintoRepositorio.getRecintos().contains(recinto));
    }

    /**
     * Verifica que un recinto se elimine correctamente del repositorio.
     * Se espera que el método devuelva true y el recinto ya no esté presente.
     */
    @Test
    void eliminarRecinto_DeberiaEliminarCorrectamente() {
        // Paso 1: Crear un recinto de ejemplo y registrarlo.
        Recinto recinto = crearRecintoEjemplo();
        recintoRepositorio.registrarRecinto(recinto);

        // Paso 2: Intentar eliminar el recinto.
        boolean resultado = recintoRepositorio.eliminarRecinto(recinto);

        // Paso 3: Afirmar que la eliminación fue exitosa.
        assertTrue(resultado);
        // Paso 4: Afirmar que el repositorio está vacío.
        assertEquals(0, recintoRepositorio.contarRecintos());
    }

    /**
     * Verifica que un recinto se actualice correctamente en el repositorio.
     * Se espera que el nombre del recinto en el repositorio refleje el cambio.
     */
    @Test
    void actualizarRecinto_DeberiaActualizarCorrectamente() {
        // Paso 1: Crear un recinto de ejemplo y registrarlo.
        Recinto recinto = crearRecintoEjemplo();
        recintoRepositorio.registrarRecinto(recinto);

        // Paso 2: Modificar el nombre del recinto.
        recinto.setNombre("Nuevo Nombre");

        // Paso 3: Actualizar el recinto en el repositorio.
        boolean resultado = recintoRepositorio.actualizarRecinto(recinto);

        // Paso 4: Afirmar que la actualización fue exitosa.
        assertTrue(resultado);

        // Paso 5: Afirmar que el nombre del primer recinto en el repositorio es el nuevo nombre.
        assertEquals(
                "Nuevo Nombre",
                recintoRepositorio
                        .getRecintos()
                        .getFirst()
                        .getNombre()
        );
    }

    /**
     * Verifica que el método {@code actualizarRecinto()} lance una {@link IllegalArgumentException}
     * si se intenta actualizar un recinto que no existe en el repositorio.
     */
    @Test
    void actualizarRecinto_DeberiaLanzarExcepcionSiNoExiste() {
        // Paso 1: Crear un recinto de ejemplo que no será registrado.
        Recinto recinto = crearRecintoEjemplo();

        // Paso 2: Afirmar que se lanza una IllegalArgumentException al intentar actualizar un recinto no existente.
        assertThrows(
                IllegalArgumentException.class,
                () -> recintoRepositorio.actualizarRecinto(recinto)
        );
    }

    /**
     * Verifica que el método {@code getRecintos()} retorne una {@link ObservableList} no nula.
     */
    @Test
    void getRecintos_DeberiaRetornarObservableList() {
        // Paso 1: Obtener la lista de recintos.
        ObservableList<Recinto> recintos = recintoRepositorio.getRecintos();

        // Paso 2: Afirmar que la lista no es nula.
        assertNotNull(recintos);
    }

    /**
     * Verifica que el método {@code contarRecintos()} devuelva el número correcto de recintos.
     */
    @Test
    void contarRecintos_DeberiaContarCorrectamente() {
        // Paso 1: Registrar dos recintos de ejemplo.
        recintoRepositorio.registrarRecinto(crearRecintoEjemplo());
        recintoRepositorio.registrarRecinto(crearRecintoEjemplo());

        // Paso 2: Afirmar que el conteo de recintos es 2.
        assertEquals(2, recintoRepositorio.contarRecintos());
    }

    /**
     * Verifica que el método {@code getPrimerRecinto()} devuelva el primer recinto registrado.
     */
    @Test
    void getPrimerRecinto_DeberiaRetornarPrimerRecinto() {
        // Paso 1: Crear y registrar dos recintos.
        Recinto recinto1 = crearRecintoEjemplo();
        Recinto recinto2 = crearRecintoEjemplo();

        recintoRepositorio.registrarRecinto(recinto1);
        recintoRepositorio.registrarRecinto(recinto2);

        // Paso 2: Obtener el primer recinto.
        Recinto resultado = recintoRepositorio.getPrimerRecinto();

        // Paso 3: Afirmar que el resultado es el primer recinto registrado.
        assertEquals(recinto1, resultado);
    }

    /**
     * Método auxiliar para crear un objeto {@link Recinto} de ejemplo con zonas y un escenario.
     *
     * @return Una nueva instancia de {@link Recinto}.
     */
    private Recinto crearRecintoEjemplo() {
        // Paso 1: Crear plantillas de zonas para el recinto.
        PlantillaZona zona1 = new PlantillaZona(
                "VIP",
                PosicionZona.SUR,
                TipoZona.VIP,
                2,
                5,
                50000
        );

        PlantillaZona zona2 = new PlantillaZona(
                "GENERAL",
                PosicionZona.NORTE,
                TipoZona.GENERAL,
                4,
                6,
                30000
        );

        // Paso 2: Añadir las plantillas de zonas a una lista.
        List<PlantillaZona> zonas = new ArrayList<>();
        zonas.add(zona1);
        zonas.add(zona2);

        // Paso 3: Crear una plantilla de recinto con las zonas.
        PlantillaRecinto plantilla = new PlantillaRecinto(
                "Recinto Test",
                zonas
        );

        // Paso 4: Generar el recinto utilizando el GeneradorRecinto.
        Recinto recinto =
                GeneradorRecinto.generarRecinto(
                        plantilla,
                        "Calle Test",
                        Ciudad.ARMENIA
                );

        // Paso 5: Establecer un escenario para el recinto.
        recinto.setEscenario(new Escenario(PosicionEscenario.CENTRO));

        // Paso 6: Devolver el recinto creado.
        return recinto;
    }
}
