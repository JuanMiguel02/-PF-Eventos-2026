package lospolimorficos.boletopolis.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para {@link PagoNequi}.
 * Verifica el correcto funcionamiento de los pagos y reembolsos con Nequi.
 */
class PagoNequiTest {

    /**
     * Verifica que un pago con Nequi se realice correctamente cuando hay saldo suficiente.
     * Se espera que el pago sea exitoso y el saldo de la cuenta se actualice.
     */
    @Test
    public void pagarConNequiCorrectamente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Pedro",
                "López",
                "321",
                "pedro@gmail.com",
                "3001234567",
                "222"
        );

        // Paso 2: Crear una cuenta simulada con saldo inicial para el cliente.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 80000);

        // Paso 3: Crear un método de pago Nequi asociado a la cuenta.
        PagoNequi nequi = new PagoNequi(cuenta, "3001234567");

        // Paso 4: Intentar realizar un pago de 30000.
        boolean resultado = nequi.pagar(30000);

        // Paso 5: Afirmar que el pago fue exitoso.
        assertTrue(resultado);
        // Paso 6: Afirmar que el saldo restante en la cuenta es el esperado (80000 - 30000 = 50000).
        assertEquals(50000, cuenta.getSaldo());
    }

    /**
     * Verifica que un reembolso con Nequi se realice correctamente.
     * Se espera que el saldo de la cuenta se incremente con el monto reembolsado.
     */
    @Test
    public void reembolsarConNequi() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Pedro",
                "López",
                "321",
                "pedro@gmail.com",
                "3001234567",
                "222"
        );

        // Paso 2: Crear una cuenta simulada con saldo inicial para el cliente.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 10000);

        // Paso 3: Crear un método de pago Nequi asociado a la cuenta.
        PagoNequi nequi = new PagoNequi(cuenta, "3001234567");

        // Paso 4: Realizar un reembolso de 15000.
        nequi.reembolsar(15000);

        // Paso 5: Afirmar que el saldo final en la cuenta es el esperado (10000 + 15000 = 25000).
        assertEquals(25000, cuenta.getSaldo());
    }

    /**
     * Verifica que un pago con Nequi falle cuando no hay saldo suficiente en la cuenta.
     * Se espera que el pago no sea exitoso y el saldo de la cuenta no cambie.
     */
    @Test
    public void pagoNequiDebeFallarSinSaldo() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Pedro",
                "López",
                "321",
                "pedro@gmail.com",
                "3001234567",
                "222"
        );

        // Paso 2: Crear una cuenta simulada con un saldo inicial insuficiente para el pago.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 5000);

        // Paso 3: Crear un método de pago Nequi asociado a la cuenta.
        PagoNequi nequi = new PagoNequi(cuenta, "3001234567");

        // Paso 4: Intentar realizar un pago de 20000.
        boolean resultado = nequi.pagar(20000);

        // Paso 5: Afirmar que el pago no fue exitoso.
        assertFalse(resultado);
        // Paso 6: Afirmar que el saldo de la cuenta permanece sin cambios.
        assertEquals(5000, cuenta.getSaldo());
    }
}
