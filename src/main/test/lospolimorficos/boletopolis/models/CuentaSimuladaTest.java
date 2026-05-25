package lospolimorficos.boletopolis.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CuentaSimuladaTest {

    @Test
    public void retirarDineroConSaldoSuficiente() {

        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        boolean resultado = cuenta.retirar(50000);

        assertTrue(resultado);
        assertEquals(50000, cuenta.getSaldo());
    }

    @Test
    public void retirarDineroSinSaldoSuficiente() {

        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 10000);

        boolean resultado = cuenta.retirar(50000);

        assertFalse(resultado);
        assertEquals(10000, cuenta.getSaldo());
    }

    @Test
    public void depositarDineroCorrectamente() {

        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 20000);

        cuenta.depositar(30000);

        assertEquals(50000, cuenta.getSaldo());
    }

    @Test
    public void generarNumeroCuentaCorrectamente() {

        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 1000);

        String numeroCuenta = cuenta.getNumeroCuenta();

        assertNotNull(numeroCuenta);
        assertEquals(10, numeroCuenta.length());
    }

    @Test
    public void numerosCuentaDebenSerDiferentes() {

        Cliente cliente = new Cliente(
                "Juan",
                "Pérez",
                "123",
                "juan@gmail.com",
                "123456",
                "111"
        );

        CuentaSimulada cuenta1 = new CuentaSimulada(cliente, 1000);
        CuentaSimulada cuenta2 = new CuentaSimulada(cliente, 1000);

        assertNotEquals(
                cuenta1.getNumeroCuenta(),
                cuenta2.getNumeroCuenta()
        );
    }
}