package lospolimorficos.boletopolis.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias para {@link PagoTarjeta}.
 * Verifica el correcto funcionamiento de los pagos, reembolsos y consulta de saldo
 * utilizando una tarjeta simulada.
 */
public class PagoTarjetaTest {

    /**
     * Verifica que un pago con tarjeta se realice correctamente cuando hay saldo suficiente.
     * Se espera que el pago sea exitoso y el saldo de la cuenta se actualice.
     */
    @Test
    public void pagarConSaldoSuficiente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con saldo inicial suficiente para el pago.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        // Paso 3: Crear un método de pago PagoTarjeta asociado a la cuenta.
        PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Crédito");

        // Paso 4: Intentar realizar un pago de 50000.
        boolean resultado = tarjeta.pagar(50000);

        // Paso 5: Afirmar que el pago fue exitoso.
        assertTrue(resultado);
        // Paso 6: Afirmar que el saldo restante en la cuenta es el esperado (100000 - 50000 = 50000).
        assertEquals(50000, cuenta.getSaldo());
    }

    /**
     * Verifica que un pago con tarjeta falle cuando no hay saldo suficiente en la cuenta.
     * Se espera que el pago no sea exitoso y el saldo de la cuenta no cambie.
     */
    @Test
    public void pagarSinSaldoSuficiente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con un saldo inicial insuficiente para el pago.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 10000);

        // Paso 3: Crear un método de pago PagoTarjeta asociado a la cuenta.
        PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Crédito");

        // Paso 4: Intentar realizar un pago de 50000.
        boolean resultado = tarjeta.pagar(50000);

        // Paso 5: Afirmar que el pago no fue exitoso.
        assertFalse(resultado);
        // Paso 6: Afirmar que el saldo de la cuenta permanece sin cambios.
        assertEquals(10000, cuenta.getSaldo());
    }

    /**
     * Verifica que un reembolso con tarjeta se realice correctamente.
     * Se espera que el saldo de la cuenta se incremente con el monto reembolsado.
     */
    @Test
    public void reembolsarDineroCorrectamente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con saldo inicial para el cliente.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 20000);

        // Paso 3: Crear un método de pago PagoTarjeta asociado a la cuenta.
        PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Débito");

        // Paso 4: Realizar un reembolso de 30000.
        tarjeta.reembolsar(30000);

        // Paso 5: Afirmar que el saldo final en la cuenta es el esperado (20000 + 30000 = 50000).
        assertEquals(50000, cuenta.getSaldo());
    }

    /**
     * Verifica que el método {@code getSaldoDisponible()} devuelva el saldo correcto de la cuenta.
     */
    @Test
    public void obtenerSaldoDisponible() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con un saldo específico.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 70000);

        // Paso 3: Crear un método de pago PagoTarjeta asociado a la cuenta.
        PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Crédito");

        // Paso 4: Afirmar que el saldo disponible reportado por la tarjeta coincide con el saldo de la cuenta.
        assertEquals(70000, tarjeta.getSaldoDisponible());
    }

    /**
     * Verifica que la descripción del método de pago no sea nula.
     * Esto asegura que el método {@code getDescripcion()} siempre devuelva una cadena.
     */
    @Test
    public void descripcionNoDebeSerNula() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 10000);

        // Paso 3: Crear un método de pago PagoTarjeta asociado a la cuenta.
        PagoTarjeta tarjeta = new PagoTarjeta(cuenta, "Débito");

        // Paso 4: Afirmar que la descripción obtenida no es nula.
        assertNotNull(tarjeta.getDescripcion());
    }

}
