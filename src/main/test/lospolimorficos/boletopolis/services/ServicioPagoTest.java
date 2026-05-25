package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ServicioPagoTest {

    @Test
    public void procesarPagoExitosamente() {

        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        MetodoPago metodo = new PagoTarjeta(cuenta, "Crédito");

        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto de rock",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        Compra compra = new Compra(cliente, evento);

        Pago pago = new Pago(compra, metodo, 50000);

        ServicioPago servicio = new ServicioPago();

        boolean resultado = servicio.procesarPago(pago);

        assertTrue(resultado);

        assertEquals(50000, cuenta.getSaldo());

        assertEquals(EstadoPago.APROBADO, pago.getEstadoPago());

        assertEquals(EstadoCompra.PAGADA, compra.getEstadoCompra());
    }

    @Test
    public void procesarPagoSinFondos() {

        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 1000);

        MetodoPago metodo = new PagoTarjeta(cuenta, "Crédito");

        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        Compra compra = new Compra(cliente, evento);

        Pago pago = new Pago(compra, metodo, 50000);

        ServicioPago servicio = new ServicioPago();

        boolean resultado = servicio.procesarPago(pago);

        assertFalse(resultado);

        assertEquals(EstadoPago.RECHAZADO, pago.getEstadoPago());

        assertNotEquals(EstadoCompra.PAGADA, compra.getEstadoCompra());
    }

    @Test
    public void reembolsarPagoCorrectamente() {

        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        MetodoPago metodo = new PagoTarjeta(cuenta, "Crédito");

        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        evento.setPermiteReembolso(true);

        Compra compra = new Compra(cliente, evento);

        Pago pago = new Pago(compra, metodo, 40000);

        ServicioPago servicio = new ServicioPago();

        servicio.procesarPago(pago);

        boolean reembolso = servicio.reembolsarPago(pago);

        assertTrue(reembolso);

        assertEquals(100000, cuenta.getSaldo());

        assertEquals(EstadoPago.REEMBOLSADO, pago.getEstadoPago());

        assertEquals(
                EstadoCompra.REEMBOLSADA,
                compra.getEstadoCompra()
        );
    }

    @Test
    public void noDebeReembolsarEventoSinReembolso() {

        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        MetodoPago metodo = new PagoTarjeta(cuenta, "Crédito");

        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        evento.setPermiteReembolso(false);

        Compra compra = new Compra(cliente, evento);

        Pago pago = new Pago(compra, metodo, 30000);

        ServicioPago servicio = new ServicioPago();

        servicio.procesarPago(pago);

        boolean resultado = servicio.reembolsarPago(pago);

        assertFalse(resultado);

        assertEquals(EstadoPago.APROBADO, pago.getEstadoPago());
    }
}