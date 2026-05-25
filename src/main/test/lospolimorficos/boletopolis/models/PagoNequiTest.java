package lospolimorficos.boletopolis.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagoNequiTest {

    @Test
    public void pagarConNequiCorrectamente() {

        Cliente cliente = new Cliente(
                "Pedro",
                "López",
                "321",
                "pedro@gmail.com",
                "3001234567",
                "222"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 80000);

        PagoNequi nequi = new PagoNequi(cuenta, "3001234567");

        boolean resultado = nequi.pagar(30000);

        assertTrue(resultado);
        assertEquals(50000, cuenta.getSaldo());
    }

    @Test
    public void reembolsarConNequi() {

        Cliente cliente = new Cliente(
                "Pedro",
                "López",
                "321",
                "pedro@gmail.com",
                "3001234567",
                "222"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 10000);

        PagoNequi nequi = new PagoNequi(cuenta, "3001234567");

        nequi.reembolsar(15000);

        assertEquals(25000, cuenta.getSaldo());
    }

    @Test
    public void pagoNequiDebeFallarSinSaldo() {

        Cliente cliente = new Cliente(
                "Pedro",
                "López",
                "321",
                "pedro@gmail.com",
                "3001234567",
                "222"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 5000);

        PagoNequi nequi = new PagoNequi(cuenta, "3001234567");

        boolean resultado = nequi.pagar(20000);

        assertFalse(resultado);
    }
}
