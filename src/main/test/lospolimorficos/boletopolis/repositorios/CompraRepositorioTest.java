package lospolimorficos.boletopolis.repositorios;

import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para {@link CompraRepositorio}.
 * Verifica el correcto funcionamiento de las operaciones CRUD y de consulta de compras.
 */
class CompraRepositorioTest {
    private CompraRepositorio compraRepositorio;

    /**
     * Configuración inicial para cada prueba.
     * Se obtiene una nueva instancia del repositorio y se limpia la lista de compras.
     */
    @BeforeEach
    public void setUp() {
        compraRepositorio = CompraRepositorio.getInstancia();
        compraRepositorio.getCompras().clear(); // Limpiar el repositorio antes de cada prueba.
    }

    /**
     * Verifica que una compra se registre correctamente en el repositorio.
     * Se espera que el tamaño del repositorio aumente en uno y el método devuelva true.
     */
    @Test
    public void registrarCompraCorrectamente() {
        // Paso 1: Obtener el número inicial de compras en el repositorio.
        int inicial = compraRepositorio.contarCompras();

        // Paso 2: Crear una compra de ejemplo.
        Compra compra = crearCompraEjemplo();

        // Paso 3: Intentar registrar la compra.
        boolean resultado = compraRepositorio.registrarCompra(compra);

        // Paso 4: Afirmar que el registro fue exitoso.
        assertTrue(resultado);

        // Paso 5: Afirmar que el número de compras en el repositorio ha aumentado en uno.
        assertEquals(inicial + 1, compraRepositorio.contarCompras());
    }

    /**
     * Verifica que una compra se elimine correctamente del repositorio.
     * Se espera que el método devuelva true y la compra ya no esté presente en el repositorio.
     */
    @Test
    public void eliminarCompraCorrectamente() {
        // Paso 1: Crear una compra de ejemplo y registrarla.
        Compra compra = crearCompraEjemplo();
        compraRepositorio.registrarCompra(compra);

        // Paso 2: Intentar eliminar la compra.
        boolean resultado = compraRepositorio.eliminarCompra(compra);

        // Paso 3: Afirmar que la eliminación fue exitosa.
        assertTrue(resultado);

        // Paso 4: Afirmar que la compra ya no está contenida en el repositorio.
        assertFalse(compraRepositorio.getCompras().contains(compra));
    }

    /**
     * Verifica que una compra se actualice correctamente en el repositorio.
     * Se espera que la compra recuperada del repositorio tenga los datos actualizados.
     */
    @Test
    public void actualizarCompraCorrectamente() {
        // Paso 1: Crear una compra de ejemplo y registrarla.
        Compra compra = crearCompraEjemplo();
        compraRepositorio.registrarCompra(compra);

        // Paso 2: Modificar el estado de la compra.
        compra.setEstadoCompra(EstadoCompra.REEMBOLSADA);

        // Paso 3: Actualizar la compra en el repositorio.
        compraRepositorio.actualizarCompra(compra);

        // Paso 4: Recuperar la compra actualizada del repositorio.
        Compra actualizada = compraRepositorio.getCompras()
                .stream()
                .filter(c -> c.getIdCompra().equals(compra.getIdCompra()))
                .findFirst()
                .orElse(null);

        // Paso 5: Afirmar que la compra actualizada no es nula.
        assertNotNull(actualizada);

        // Paso 6: Afirmar que el estado de la compra recuperada es el estado actualizado.
        assertEquals(
                EstadoCompra.REEMBOLSADA,
                actualizada.getEstadoCompra()
        );
    }

    /**
     * Verifica que el método {@code contarCompras()} devuelva el número correcto de compras.
     */
    @Test
    public void contarComprasCorrectamente() {
        // Paso 1: Registrar dos compras de ejemplo.
        compraRepositorio.registrarCompra(crearCompraEjemplo());
        compraRepositorio.registrarCompra(crearCompraEjemplo());

        // Paso 2: Afirmar que el conteo de compras es 2.
        assertEquals(2, compraRepositorio.contarCompras());
    }

    /**
     * Verifica que el método {@code obtenerVentasEvento()} devuelva el número correcto de entradas vendidas para un evento.
     */
    @Test
    public void obtenerVentasEventoCorrectamente() {
        // Paso 1: Crear un evento de ejemplo.
        Evento evento = crearEventoEjemplo();

        // Paso 2: Crear una compra con 3 entradas para el evento y registrarla.
        Compra compra = crearCompraEjemplo(evento, 3);
        compraRepositorio.registrarCompra(compra);

        // Paso 3: Obtener el número de ventas para el evento.
        int ventas = compraRepositorio.obtenerVentasEvento(evento);

        // Paso 4: Afirmar que el número de ventas es 3.
        assertEquals(3, ventas);
    }

    /**
     * Verifica que el método {@code calcularGananciaEvento()} devuelva la ganancia correcta para un evento.
     */
    @Test
    public void calcularGananciaEventoCorrectamente() {
        // Paso 1: Crear un evento de ejemplo.
        Evento evento = crearEventoEjemplo();

        // Paso 2: Crear una compra con 2 entradas para el evento. Cada entrada cuesta 100000, total 200000.
        Compra compra = crearCompraEjemplo(evento, 2);
        compraRepositorio.registrarCompra(compra);

        // Paso 3: Calcular la ganancia para el evento.
        double ganancia = compraRepositorio.calcularGananciaPorEvento(evento);

        // Paso 4: Afirmar que la ganancia es 200000.
        assertEquals(200000, ganancia);
    }

    /**
     * Verifica que el método {@code obtenerComprasPorPeriodo()} devuelva las compras dentro del rango de fechas especificado.
     */
    @Test
    public void obtenerComprasPorPeriodoCorrectamente() {
        // Paso 1: Crear una compra de ejemplo.
        Compra compra = crearCompraEjemplo();

        // Paso 2: Establecer la fecha de compra 5 días antes de la fecha actual.
        compra.setFechaCompra(
                LocalDateTime.now().minusDays(5)
        );

        // Paso 3: Registrar la compra.
        compraRepositorio.registrarCompra(compra);

        // Paso 4: Obtener las compras dentro de un período de 10 días antes de la fecha actual hasta la fecha actual.
        List<Compra> compras = compraRepositorio.obtenerComprasPorPeriodo(
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now()
        );

        // Paso 5: Afirmar que la lista de compras contiene la compra creada.
        assertTrue(compras.contains(compra));
    }

    /**
     * Verifica que el método {@code obtenerVentasPorMes()} devuelva un mapa no vacío con las ventas por mes.
     */
    @Test
    public void obtenerVentasPorMesCorrectamente() {
        // Paso 1: Crear una compra de ejemplo y registrarla.
        Compra compra = crearCompraEjemplo();
        compraRepositorio.registrarCompra(compra);

        // Paso 2: Obtener el mapa de ventas por mes.
        var ventas = compraRepositorio.obtenerVentasPorMes();

        // Paso 3: Afirmar que el mapa de ventas no está vacío.
        assertFalse(ventas.isEmpty());
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    /**
     * Método auxiliar para crear una compra de ejemplo con una entrada y un evento genérico.
     *
     * @return Una nueva instancia de {@link Compra}.
     */
    private Compra crearCompraEjemplo() {
        Evento evento = crearEventoEjemplo();
        return crearCompraEjemplo(evento, 1);
    }

    /**
     * Método auxiliar para crear una compra de ejemplo con un evento y una cantidad específica de entradas.
     *
     * @param evento El {@link Evento} para el cual se crea la compra.
     * @param cantidadEntradas La cantidad de entradas a incluir en la compra.
     * @return Una nueva instancia de {@link Compra}.
     */
    private Compra crearCompraEjemplo(Evento evento, int cantidadEntradas) {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una nueva compra con el cliente y el evento.
        Compra compra = new Compra(cliente, evento);

        // Paso 3: Crear una lista de entradas.
        List<Entrada> entradas = new ArrayList<>();

        // Paso 4: Generar la cantidad de entradas solicitadas.
        for (int i = 0; i < cantidadEntradas; i++) {
            // Paso 4.1: Crear un asiento de ejemplo.
            Asiento asiento = new Asiento(i + 1, i + 1);

            // Paso 4.2: Crear una zona de ejemplo.
            Zona zona = new Zona(
                    "VIP",
                    20,
                    TipoZona.VIP,
                    PosicionZona.NORTE,
                    100000
            );

            // Paso 4.3: Crear una entrada con la zona, el asiento, el precio y el estado.
            Entrada entrada = new Entrada(
                    zona,
                    asiento,
                    100000,
                    EstadoEntrada.ACTIVA
            );

            // Paso 4.4: Añadir la entrada a la lista.
            entradas.add(entrada);
        }

        // Paso 5: Establecer las entradas en la compra.
        compra.setEntradas(entradas);

        // Paso 6: Devolver la compra creada.
        return compra;
    }

    /**
     * Método auxiliar para crear un evento de ejemplo.
     *
     * @return Una nueva instancia de {@link Evento} (Concierto).
     */
    private Evento crearEventoEjemplo() {
        return new Concierto(
                "Rock Fest",
                "Concierto de Rock",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null, // Recinto es nulo para simplificar, no es relevante para esta prueba.
                "Metallica",
                "Rock"
        );
    }
}
