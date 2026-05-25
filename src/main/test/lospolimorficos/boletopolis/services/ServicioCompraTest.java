package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServicioCompraTest {
    @Test
    public void realizarCompraExitosamente() {

        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 500000);

        MetodoPago metodoPago = new PagoTarjeta(cuenta, "Crédito");

        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        Asiento asiento = new Asiento(1, 1);

        Zona zona = new Zona(
                "VIP",
                20,
                TipoZona.VIP,
                PosicionZona.NORTE,
                100000
        );

        zona.getAsientos().add(asiento);

        List<Asiento> asientos = List.of(asiento);

        Map<Asiento, Zona> mapa = Map.of(asiento, zona);

        ServicioCompra servicio = new ServicioCompra();

        Compra compra = servicio.realizarCompra(
                cliente,
                evento,
                asientos,
                mapa,
                metodoPago
        );

        assertNotNull(compra);

        assertEquals(
                EstadoCompra.PAGADA,
                compra.getEstadoCompra()
        );

        assertEquals(
                EstadoPago.APROBADO,
                compra.getPago().getEstadoPago()
        );

        assertEquals(
                EstadoAsiento.VENDIDO,
                asiento.getEstado()
        );

        assertEquals(1, compra.getEntradas().size());

        assertTrue(cliente.getCompras().contains(compra));
    }

    @Test
    public void compraDebeFallarSinSaldo() {

        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 1000);

        MetodoPago metodoPago = new PagoTarjeta(cuenta, "Crétido");

        Evento evento = new Concierto(
                "Rock Fest",
                "Concierto",
                Ciudad.ARMENIA,
                LocalDateTime.now(),
                null,
                "Metallica",
                "Rock"
        );

        Asiento asiento = new Asiento(1, 1);

        Zona zona = new Zona(
                "VIP",
                20,
                TipoZona.VIP,
                PosicionZona.NORTE,
                100000
        );

        zona.getAsientos().add(asiento);

        List<Asiento> asientos = List.of(asiento);

        Map<Asiento, Zona> mapa = Map.of(asiento, zona);

        ServicioCompra servicio = new ServicioCompra();

        Compra compra = servicio.realizarCompra(
                cliente,
                evento,
                asientos,
                mapa,
                metodoPago
        );

        assertNull(compra);

        assertEquals(
                EstadoAsiento.DISPONIBLE,
                asiento.getEstado()
        );
    }

    @Test
    public void reembolsarCompraCorrectamente() {

        Cliente cliente = new Cliente(
                "Juan",
                "Perez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 500000);

        MetodoPago metodoPago = new PagoTarjeta(cuenta, "Crédito");

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

        Asiento asiento = new Asiento(1, 1);

        Zona zona = new Zona(
                "VIP",
                20,
                TipoZona.VIP,
                PosicionZona.NORTE,
                100000
        );

        zona.getAsientos().add(asiento);

        List<Asiento> asientos = List.of(asiento);

        Map<Asiento, Zona> mapa = Map.of(asiento, zona);

        ServicioCompra servicio = new ServicioCompra();

        Compra compra = servicio.realizarCompra(
                cliente,
                evento,
                asientos,
                mapa,
                metodoPago
        );

        boolean resultado = servicio.reembolsarCompra(compra);

        assertTrue(resultado);

        assertEquals(
                EstadoCompra.REEMBOLSADA,
                compra.getEstadoCompra()
        );

        assertEquals(
                EstadoPago.REEMBOLSADO,
                compra.getPago().getEstadoPago()
        );

        assertEquals(
                EstadoAsiento.DISPONIBLE,
                asiento.getEstado()
        );

        assertEquals(
                EstadoEntrada.CANCELADA,
                compra.getEntradas().getFirst().getEstado()
        );
    }
}