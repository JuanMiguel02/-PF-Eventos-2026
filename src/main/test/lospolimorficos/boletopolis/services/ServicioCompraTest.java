package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para {@link ServicioCompra}.
 * Verifica el correcto funcionamiento de las operaciones de realizar compra y reembolsar compra.
 */
class ServicioCompraTest {

    /**
     * Verifica que una compra se realice exitosamente cuando hay saldo suficiente y los asientos están disponibles.
     * Se espera que la compra se marque como pagada, el pago como aprobado, los asientos como vendidos,
     * y la compra se añada al cliente.
     */
    @Test
    public void realizarCompraExitosamente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con saldo suficiente para la compra.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 500000);

        // Paso 3: Crear un método de pago (Tarjeta) asociado a la cuenta.
        MetodoPago metodoPago = new PagoTarjeta(cuenta, "Crédito");

        // Paso 4: Crear un evento de prueba (el recinto es nulo para simplificar, no es relevante para esta prueba).
        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        // Paso 5: Crear un asiento de prueba.
        Asiento asiento = new Asiento(1, 1);

        // Paso 6: Crear una zona de prueba y añadir el asiento a ella.
        Zona zona = new Zona(
                "VIP",
                20,
                TipoZona.VIP,
                PosicionZona.NORTE,
                100000
        );
        zona.getAsientos().add(asiento);

        // Paso 7: Preparar las listas y mapas necesarios para el método realizarCompra.
        List<Asiento> asientos = List.of(asiento);
        Map<Asiento, Zona> mapa = Map.of(asiento, zona);

        // Paso 8: Crear una instancia del servicio de compra.
        ServicioCompra servicio = new ServicioCompra();

        // Paso 9: Realizar la compra.
        Compra compra = servicio.realizarCompra(
                cliente,
                evento,
                asientos,
                mapa,
                metodoPago
        );

        // Paso 10: Afirmar que la compra no es nula (es decir, se realizó exitosamente).
        assertNotNull(compra);

        // Paso 11: Afirmar que el estado de la compra es PAGADA.
        assertEquals(
                EstadoCompra.PAGADA,
                compra.getEstadoCompra()
        );

        // Paso 12: Afirmar que el estado del pago asociado a la compra es APROBADO.
        assertEquals(
                EstadoPago.APROBADO,
                compra.getPago().getEstadoPago()
        );

        // Paso 13: Afirmar que el estado del asiento es VENDIDO.
        assertEquals(
                EstadoAsiento.VENDIDO,
                asiento.getEstado()
        );

        // Paso 14: Afirmar que la compra contiene una entrada.
        assertEquals(1, compra.getEntradas().size());

        // Paso 15: Afirmar que la compra se añadió a la lista de compras del cliente.
        assertTrue(cliente.getCompras().contains(compra));
    }

    /**
     * Verifica que una compra falle cuando el método de pago no tiene saldo suficiente.
     * Se espera que la compra sea nula, y el estado del asiento permanezca DISPONIBLE.
     */
    @Test
    public void compraDebeFallarSinSaldo() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con saldo insuficiente para la compra.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 1000);

        // Paso 3: Crear un método de pago (Tarjeta) asociado a la cuenta.
        MetodoPago metodoPago = new PagoTarjeta(cuenta, "Crétido");

        // Paso 4: Crear un evento de prueba.
        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        // Paso 5: Crear un asiento de prueba.
        Asiento asiento = new Asiento(1, 1);

        // Paso 6: Crear una zona de prueba y añadir el asiento a ella.
        Zona zona = new Zona(
                "VIP",
                20,
                TipoZona.VIP,
                PosicionZona.NORTE,
                100000
        );
        zona.getAsientos().add(asiento);

        // Paso 7: Preparar las listas y mapas necesarios para el método realizarCompra.
        List<Asiento> asientos = List.of(asiento);
        Map<Asiento, Zona> mapa = Map.of(asiento, zona);

        // Paso 8: Crear una instancia del servicio de compra.
        ServicioCompra servicio = new ServicioCompra();

        // Paso 9: Intentar realizar la compra.
        Compra compra = servicio.realizarCompra(
                cliente,
                evento,
                asientos,
                mapa,
                metodoPago
        );

        // Paso 10: Afirmar que la compra es nula (indicando que falló).
        assertNull(compra);

        // Paso 11: Afirmar que el estado del asiento permanece DISPONIBLE.
        assertEquals(
                EstadoAsiento.DISPONIBLE,
                asiento.getEstado()
        );
    }

    /**
     * Verifica que una compra se reembolse correctamente.
     * Se espera que el estado de la compra, el pago y las entradas se actualicen a REEMBOLSADA/CANCELADA,
     * y los asientos se liberen (DISPONIBLE).
     */
    @Test
    public void reembolsarCompraCorrectamente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con saldo inicial.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 500000);

        // Paso 3: Crear un método de pago (Tarjeta) asociado a la cuenta.
        MetodoPago metodoPago = new PagoTarjeta(cuenta, "Crédito");

        // Paso 4: Crear un evento de prueba y configurarlo para permitir reembolsos.
        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );
        evento.setPermiteReembolso(true); // Habilitar reembolsos para el evento.

        // Paso 5: Crear un asiento de prueba.
        Asiento asiento = new Asiento(1, 1);

        // Paso 6: Crear una zona de prueba y añadir el asiento a ella.
        Zona zona = new Zona(
                "VIP",
                20,
                TipoZona.VIP,
                PosicionZona.NORTE,
                100000
        );
        zona.getAsientos().add(asiento);

        // Paso 7: Preparar las listas y mapas necesarios para el método realizarCompra.
        List<Asiento> asientos = List.of(asiento);
        Map<Asiento, Zona> mapa = Map.of(asiento, zona);

        // Paso 8: Crear una instancia del servicio de compra.
        ServicioCompra servicio = new ServicioCompra();

        // Paso 9: Realizar la compra inicialmente para que haya algo que reembolsar.
        Compra compra = servicio.realizarCompra(
                cliente,
                evento,
                asientos,
                mapa,
                metodoPago
        );

        // Paso 10: Intentar reembolsar la compra.
        boolean resultado = servicio.reembolsarCompra(compra);

        // Paso 11: Afirmar que el reembolso fue exitoso.
        assertTrue(resultado);

        // Paso 12: Afirmar que el estado de la compra es REEMBOLSADA.
        assertEquals(
                EstadoCompra.REEMBOLSADA,
                compra.getEstadoCompra()
        );

        // Paso 13: Afirmar que el estado del pago asociado a la compra es REEMBOLSADO.
        assertEquals(
                EstadoPago.REEMBOLSADO,
                compra.getPago().getEstadoPago()
        );

        // Paso 14: Afirmar que el estado del asiento es DISPONIBLE (ha sido liberado).
        assertEquals(
                EstadoAsiento.DISPONIBLE,
                asiento.getEstado()
        );

        // Paso 15: Afirmar que el estado de la entrada es CANCELADA.
        assertEquals(
                EstadoEntrada.CANCELADA,
                compra.getEntradas().getFirst().getEstado()
        );
    }
}
