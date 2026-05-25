package lospolimorficos.boletopolis.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

    public class PagoTarjetaTest {

        @Test
        public void pagarConSaldoSuficiente() {

            Cliente cliente = new Cliente(
                    "Juan",
                    "Pérez",
                    "123",
                    "juan@gmail.com",
                    "123456",
                    "111"
            );

            CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

            PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Crédito");

            boolean resultado = tarjeta.pagar(50000);

            assertTrue(resultado);
            assertEquals(50000, cuenta.getSaldo());
        }

        @Test
        public void pagarSinSaldoSuficiente() {

            Cliente cliente = new Cliente(
                    "Juan",
                    "Pérez",
                    "123",
                    "juan@gmail.com",
                    "123456",
                    "111"
            );

            CuentaSimulada cuenta = new CuentaSimulada(cliente, 10000);

            PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Crédito");

            boolean resultado = tarjeta.pagar(50000);

            assertFalse(resultado);
            assertEquals(10000, cuenta.getSaldo());
        }

        @Test
        public void reembolsarDineroCorrectamente() {

            Cliente cliente = new Cliente(
                    "Juan",
                    "Pérez",
                    "123",
                    "juan@gmail.com",
                    "123456",
                    "111"
            );

            CuentaSimulada cuenta = new CuentaSimulada(cliente, 20000);

            PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Débito");

            tarjeta.reembolsar(30000);

            assertEquals(50000, cuenta.getSaldo());
        }

        @Test
        public void obtenerSaldoDisponible() {

            Cliente cliente = new Cliente(
                    "Juan",
                    "Pérez",
                    "123",
                    "juan@gmail.com",
                    "123456",
                    "111"
            );

            CuentaSimulada cuenta = new CuentaSimulada(cliente, 70000);

            PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Crédito");

            assertEquals(70000, tarjeta.getSaldoDisponible());
        }

        @Test
        public void descripcionNoDebeSerNula() {

            Cliente cliente = new Cliente(
                    "Juan",
                    "Pérez",
                    "123",
                    "juan@gmail.com",
                    "123456",
                    "111"
            );

            CuentaSimulada cuenta = new CuentaSimulada(cliente, 10000);

            PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Débito");

            assertNotNull(tarjeta.getDescripcion());
        }

}