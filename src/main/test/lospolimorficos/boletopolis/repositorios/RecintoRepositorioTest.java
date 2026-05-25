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

class RecintoRepositorioTest {
    private RecintoRepositorio recintoRepositorio;

    @BeforeEach
    void setUp() {

        recintoRepositorio = RecintoRepositorio.getInstancia();

        // Limpiar datos de ejemplo
        recintoRepositorio.getRecintos().clear();
    }

    @Test
    void registrarRecinto_DeberiaRegistrarCorrectamente() {

        Recinto recinto = crearRecintoEjemplo();

        boolean resultado = recintoRepositorio.registrarRecinto(recinto);

        assertTrue(resultado);
        assertEquals(1, recintoRepositorio.contarRecintos());
        assertTrue(recintoRepositorio.getRecintos().contains(recinto));
    }

    @Test
    void eliminarRecinto_DeberiaEliminarCorrectamente() {

        Recinto recinto = crearRecintoEjemplo();

        recintoRepositorio.registrarRecinto(recinto);

        boolean resultado = recintoRepositorio.eliminarRecinto(recinto);

        assertTrue(resultado);
        assertEquals(0, recintoRepositorio.contarRecintos());
    }

    @Test
    void actualizarRecinto_DeberiaActualizarCorrectamente() {

        Recinto recinto = crearRecintoEjemplo();

        recintoRepositorio.registrarRecinto(recinto);

        recinto.setNombre("Nuevo Nombre");

        boolean resultado = recintoRepositorio.actualizarRecinto(recinto);

        assertTrue(resultado);

        assertEquals(
                "Nuevo Nombre",
                recintoRepositorio
                        .getRecintos()
                        .getFirst()
                        .getNombre()
        );
    }

    @Test
    void actualizarRecinto_DeberiaLanzarExcepcionSiNoExiste() {

        Recinto recinto = crearRecintoEjemplo();

        assertThrows(
                IllegalArgumentException.class,
                () -> recintoRepositorio.actualizarRecinto(recinto)
        );
    }

    @Test
    void getRecintos_DeberiaRetornarObservableList() {

        ObservableList<Recinto> recintos = recintoRepositorio.getRecintos();

        assertNotNull(recintos);
    }

    @Test
    void contarRecintos_DeberiaContarCorrectamente() {

        recintoRepositorio.registrarRecinto(crearRecintoEjemplo());

        recintoRepositorio.registrarRecinto(crearRecintoEjemplo());

        assertEquals(2, recintoRepositorio.contarRecintos());
    }

    @Test
    void getPrimerRecinto_DeberiaRetornarPrimerRecinto() {

        Recinto recinto1 = crearRecintoEjemplo();
        Recinto recinto2 = crearRecintoEjemplo();

        recintoRepositorio.registrarRecinto(recinto1);
        recintoRepositorio.registrarRecinto(recinto2);

        Recinto resultado = recintoRepositorio.getPrimerRecinto();

        assertEquals(recinto1, resultado);
    }

    private Recinto crearRecintoEjemplo() {

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

        List<PlantillaZona> zonas = new ArrayList<>();

        zonas.add(zona1);
        zonas.add(zona2);

        PlantillaRecinto plantilla = new PlantillaRecinto(
                "Recinto Test",
                zonas
        );

        Recinto recinto =
                GeneradorRecinto.generarRecinto(
                        plantilla,
                        "Calle Test",
                        Ciudad.ARMENIA
                );

        recinto.setEscenario(new Escenario(PosicionEscenario.CENTRO));

        return recinto;
    }
}