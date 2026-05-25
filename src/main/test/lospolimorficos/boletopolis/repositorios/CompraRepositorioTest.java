package lospolimorficos.boletopolis.repositorios;

import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompraRepositorioTest {
    private CompraRepositorio compraRepositorio;

    @BeforeEach
    public void setUp() {
        compraRepositorio = CompraRepositorio.getInstancia();
        compraRepositorio.getCompras().clear();
    }

    @Test
    public void registrarCompraCorrectamente() {

        int inicial = compraRepositorio.contarCompras();

        Compra compra = crearCompraEjemplo();

        boolean resultado = compraRepositorio.registrarCompra(compra);

        assertTrue(resultado);

        assertEquals(inicial + 1, compraRepositorio.contarCompras());
    }

    @Test
    public void eliminarCompraCorrectamente() {

        Compra compra = crearCompraEjemplo();

        compraRepositorio.registrarCompra(compra);

        boolean resultado = compraRepositorio.eliminarCompra(compra);

        assertTrue(resultado);

        assertFalse(compraRepositorio.getCompras().contains(compra));
    }

    @Test
    public void actualizarCompraCorrectamente() {

        Compra compra = crearCompraEjemplo();

        compraRepositorio.registrarCompra(compra);

        compra.setEstadoCompra(EstadoCompra.REEMBOLSADA);

        compraRepositorio.actualizarCompra(compra);

        Compra actualizada = compraRepositorio.getCompras()
                .stream()
                .filter(c -> c.getIdCompra().equals(compra.getIdCompra()))
                .findFirst()
                .orElse(null);

        assertNotNull(actualizada);

        assertEquals(
                EstadoCompra.REEMBOLSADA,
                actualizada.getEstadoCompra()
        );
    }

    @Test
    public void contarComprasCorrectamente() {

        compraRepositorio.registrarCompra(crearCompraEjemplo());
        compraRepositorio.registrarCompra(crearCompraEjemplo());

        assertEquals(2, compraRepositorio.contarCompras());
    }

    @Test
    public void obtenerVentasEventoCorrectamente() {

        Evento evento = crearEventoEjemplo();

        Compra compra = crearCompraEjemplo(evento, 3);

        compraRepositorio.registrarCompra(compra);

        int ventas = compraRepositorio.obtenerVentasEvento(evento);

        assertEquals(3, ventas);
    }

    @Test
    public void calcularGananciaEventoCorrectamente() {

        Evento evento = crearEventoEjemplo();

        Compra compra = crearCompraEjemplo(evento, 2);


        compraRepositorio.registrarCompra(compra);

        double ganancia = compraRepositorio.calcularGananciaPorEvento(evento);

        assertEquals(200000, ganancia);
    }

    @Test
    public void obtenerComprasPorPeriodoCorrectamente() {

        Compra compra = crearCompraEjemplo();

        compra.setFechaCompra(
                LocalDateTime.now().minusDays(5)
        );

        compraRepositorio.registrarCompra(compra);

        List<Compra> compras = compraRepositorio.obtenerComprasPorPeriodo(
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now()
        );

        assertTrue(compras.contains(compra));
    }

    @Test
    public void obtenerVentasPorMesCorrectamente() {

        Compra compra = crearCompraEjemplo();


        compraRepositorio.registrarCompra(compra);

        var ventas = compraRepositorio.obtenerVentasPorMes();

        assertFalse(ventas.isEmpty());
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private Compra crearCompraEjemplo() {

        Evento evento = crearEventoEjemplo();

        return crearCompraEjemplo(evento, 1);
    }

    private Compra crearCompraEjemplo(Evento evento, int cantidadEntradas) {

        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        Compra compra = new Compra(cliente, evento);

        List<Entrada> entradas = new ArrayList<>();

        for (int i = 0; i < cantidadEntradas; i++) {

            Asiento asiento = new Asiento(i + 1, i + 1);

            Zona zona = new Zona(
                    "VIP",
                    20,
                    TipoZona.VIP,
                    PosicionZona.NORTE,
                    100000
            );

            Entrada entrada = new Entrada(
                    zona,
                    asiento,
                    100000,
                    EstadoEntrada.ACTIVA
            );

            entradas.add(entrada);
        }

        compra.setEntradas(entradas);

        return compra;
    }

    private Evento crearEventoEjemplo() {

        return new Concierto(
                "Rock Fest",
                "Concierto de Rock",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );
    }
}