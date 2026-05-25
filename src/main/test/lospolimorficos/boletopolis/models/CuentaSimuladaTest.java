package lospolimorficos.boletopolis.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias para {@link CuentaSimulada}.
 * Verifica el correcto funcionamiento de las operaciones de retiro, depósito y generación de números de cuenta.
 */
public class CuentaSimuladaTest {

    /**
     * Verifica que el retiro de dinero se realice correctamente cuando hay saldo suficiente.
     * Se espera que el retiro sea exitoso y el saldo de la cuenta se actualice.
     */
    @Test
    public void retirarDineroConSaldoSuficiente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con saldo inicial suficiente para el retiro.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        // Paso 3: Intentar retirar 50000 de la cuenta.
        boolean resultado = cuenta.retirar(50000);

        // Paso 4: Afirmar que el retiro fue exitoso.
        assertTrue(resultado);
        // Paso 5: Afirmar que el saldo restante en la cuenta es el esperado (100000 - 50000 = 50000).
        assertEquals(50000, cuenta.getSaldo());
    }

    /**
     * Verifica que el retiro de dinero falle cuando no hay saldo suficiente en la cuenta.
     * Se espera que el retiro no sea exitoso y el saldo de la cuenta permanezca sin cambios.
     */
    @Test
    public void retirarDineroSinSaldoSuficiente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con un saldo inicial insuficiente para el retiro.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 10000);

        // Paso 3: Intentar retirar 50000 de la cuenta.
        boolean resultado = cuenta.retirar(50000);

        // Paso 4: Afirmar que el retiro no fue exitoso.
        assertFalse(resultado);
        // Paso 5: Afirmar que el saldo de la cuenta permanece sin cambios.
        assertEquals(10000, cuenta.getSaldo());
    }

    /**
     * Verifica que el depósito de dinero se realice correctamente.
     * Se espera que el saldo de la cuenta se incremente con el monto depositado.
     */
    @Test
    public void depositarDineroCorrectamente() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear una cuenta simulada con un saldo inicial.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 20000);

        // Paso 3: Depositar 30000 en la cuenta.
        cuenta.depositar(30000);

        // Paso 4: Afirmar que el saldo final en la cuenta es el esperado (20000 + 30000 = 50000).
        assertEquals(50000, cuenta.getSaldo());
    }

    /**
     * Verifica que el número de cuenta generado no sea nulo y tenga la longitud esperada.
     */
    @Test
    public void generarNumeroCuentaCorrectamente() {
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
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 1000);

        // Paso 3: Obtener el número de cuenta generado.
        String numeroCuenta = cuenta.getNumeroCuenta();

        // Paso 4: Afirmar que el número de cuenta no es nulo.
        assertNotNull(numeroCuenta);
        // Paso 5: Afirmar que la longitud del número de cuenta es 10 dígitos.
        assertEquals(10, numeroCuenta.length());
    }

    /**
     * Verifica que dos instancias diferentes de {@link CuentaSimulada} generen números de cuenta distintos.
     */
    @Test
    public void numerosCuentaDebenSerDiferentes() {
        // Paso 1: Crear un cliente de prueba.
        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        // Paso 2: Crear dos instancias de CuentaSimulada para el mismo cliente.
        CuentaSimulada cuenta1 = new CuentaSimulada(cliente, 1000);
        CuentaSimulada cuenta2 = new CuentaSimulada(cliente, 1000);

        // Paso 3: Afirmar que los números de cuenta generados son diferentes.
        assertNotEquals(
                cuenta1.getNumeroCuenta(),
                cuenta2.getNumeroCuenta()
        );
    }
}
