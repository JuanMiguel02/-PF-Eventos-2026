package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para {@link ServicioPago}.
 * Verifica el correcto funcionamiento del procesamiento de pagos y reembolsos.
 */
class ServicioPagoTest {

    /**
     * Verifica que un pago se procese exitosamente cuando el método de pago tiene fondos suficientes.
     * Se espera que el pago sea aprobado, la cuenta de la empresa reciba el dinero y el estado de la compra se actualice.
     */
    @Test
    public void procesarPagoExitosamente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con saldo suficiente para el pago.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        // Paso 3: Crear un método de pago (Tarjeta) asociado a la cuenta.
        MetodoPago metodo = new PagoTarjeta(cuenta, "Crédito");

        // Paso 4: Crear un evento de prueba (el recinto es nulo para simplificar, no es relevante para esta prueba).
        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto de rock",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        // Paso 5: Crear una compra asociada al cliente y al evento.
        Compra compra = new Compra(cliente, evento);

        // Paso 6: Crear un pago con la compra, el método de pago y el monto.
        Pago pago = new Pago(compra, metodo, 50000);

        // Paso 7: Crear una instancia del servicio de pago.
        ServicioPago servicio = new ServicioPago();

        // Paso 8: Procesar el pago.
        boolean resultado = servicio.procesarPago(pago);

        // Paso 9: Afirmar que el procesamiento del pago fue exitoso.
        assertTrue(resultado);

        // Paso 10: Afirmar que el saldo de la cuenta del cliente se redujo correctamente.
        assertEquals(50000, cuenta.getSaldo());

        // Paso 11: Afirmar que el estado del pago es APROBADO.
        assertEquals(EstadoPago.APROBADO, pago.getEstadoPago());

        // Paso 12: Afirmar que el estado de la compra es PAGADA.
        assertEquals(EstadoCompra.PAGADA, compra.getEstadoCompra());
    }

    /**
     * Verifica que el procesamiento de un pago falle cuando el método de pago no tiene fondos suficientes.
     * Se espera que el pago sea rechazado y el estado de la compra no se actualice a PAGADA.
     */
    @Test
    public void procesarPagoSinFondos() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con saldo insuficiente para el pago.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 1000);

        // Paso 3: Crear un método de pago (Tarjeta) asociado a la cuenta.
        MetodoPago metodo = new PagoTarjeta(cuenta, "Crédito");

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

        // Paso 5: Crear una compra asociada al cliente y al evento.
        Compra compra = new Compra(cliente, evento);

        // Paso 6: Crear un pago con la compra, el método de pago y el monto.
        Pago pago = new Pago(compra, metodo, 50000);

        // Paso 7: Crear una instancia del servicio de pago.
        ServicioPago servicio = new ServicioPago();

        // Paso 8: Procesar el pago.
        boolean resultado = servicio.procesarPago(pago);

        // Paso 9: Afirmar que el procesamiento del pago no fue exitoso.
        assertFalse(resultado);

        // Paso 10: Afirmar que el estado del pago es RECHAZADO.
        assertEquals(EstadoPago.RECHAZADO, pago.getEstadoPago());

        // Paso 11: Afirmar que el estado de la compra NO es PAGADA.
        assertNotEquals(EstadoCompra.PAGADA, compra.getEstadoCompra());
    }

    /**
     * Verifica que un reembolso se procese correctamente cuando el evento lo permite y el pago fue aprobado.
     * Se espera que el reembolso sea exitoso, el saldo del cliente se recupere y el estado de la compra se actualice.
     */
    @Test
    public void reembolsarPagoCorrectamente() {
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
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        // Paso 3: Crear un método de pago (Tarjeta) asociado a la cuenta.
        MetodoPago metodo = new PagoTarjeta(cuenta, "Crédito");

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

        // Paso 5: Crear una compra asociada al cliente y al evento.
        Compra compra = new Compra(cliente, evento);

        // Paso 6: Crear un pago con la compra, el método de pago y el monto.
        Pago pago = new Pago(compra, metodo, 40000);

        // Paso 7: Crear una instancia del servicio de pago.
        ServicioPago servicio = new ServicioPago();

        // Paso 8: Procesar el pago inicialmente para que esté en estado APROBADO.
        servicio.procesarPago(pago);

        // Paso 9: Intentar reembolsar el pago.
        boolean reembolso = servicio.reembolsarPago(pago);

        // Paso 10: Afirmar que el reembolso fue exitoso.
        assertTrue(reembolso);

        // Paso 11: Afirmar que el saldo de la cuenta del cliente volvió a su valor inicial (100000).
        assertEquals(100000, cuenta.getSaldo());

        // Paso 12: Afirmar que el estado del pago es REEMBOLSADO.
        assertEquals(EstadoPago.REEMBOLSADO, pago.getEstadoPago());

        // Paso 13: Afirmar que el estado de la compra es REEMBOLSADA.
        assertEquals(
                EstadoCompra.REEMBOLSADA,
                compra.getEstadoCompra()
        );
    }

    /**
     * Verifica que un reembolso no se procese si el evento no permite reembolsos.
     * Se espera que el reembolso falle y el estado del pago permanezca como APROBADO.
     */
    @Test
    public void noDebeReembolsarEventoSinReembolso() {
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
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        // Paso 3: Crear un método de pago (Tarjeta) asociado a la cuenta.
        MetodoPago metodo = new PagoTarjeta(cuenta, "Crédito");

        // Paso 4: Crear un evento de prueba y configurarlo para NO permitir reembolsos.
        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );
        evento.setPermiteReembolso(false); // Deshabilitar reembolsos para el evento.

        // Paso 5: Crear una compra asociada al cliente y al evento.
        Compra compra = new Compra(cliente, evento);

        // Paso 6: Crear un pago con la compra, el método de pago y el monto.
        Pago pago = new Pago(compra, metodo, 30000);

        // Paso 7: Crear una instancia del servicio de pago.
        ServicioPago servicio = new ServicioPago();

        // Paso 8: Procesar el pago inicialmente para que esté en estado APROBADO.
        servicio.procesarPago(pago);

        // Paso 9: Intentar reembolsar el pago.
        boolean resultado = servicio.reembolsarPago(pago);

        // Paso 10: Afirmar que el reembolso no fue exitoso.
        assertFalse(resultado);

        // Paso 11: Afirmar que el estado del pago permanece como APROBADO.
        assertEquals(EstadoPago.APROBADO, pago.getEstadoPago());
    }
}
