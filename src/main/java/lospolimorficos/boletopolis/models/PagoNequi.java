package lospolimorficos.boletopolis.models;

import java.util.Random;

public class PagoNequi implements MetodoPago {

    private final String numeroCelular;
    private final CuentaSimulada cuenta;

    public PagoNequi(CuentaSimulada cuenta) {
        this.numeroCelular = generarNumeroCelular();
        this.cuenta = cuenta;
    }

    public PagoNequi(CuentaSimulada cuenta, String numeroCelular) {
        this.numeroCelular = numeroCelular;
        this.cuenta = cuenta;
    }

    private String generarNumeroCelular() {

        Random random = new Random();

        StringBuilder numero = new StringBuilder("3");

        for(int i = 0; i < 9; i++){
            numero.append(random.nextInt(10));
        }

        return numero.toString();
    }

    @Override
    public boolean pagar(double monto) {
        return cuenta.retirar(monto);
    }

    @Override
    public void reembolsar(double monto) {
        cuenta.depositar(monto);
    }

    @Override
    public double getSaldoDisponible() {
        return cuenta.getSaldo();
    }

    @Override
    public String getDescripcion() {
        return "Nequi asociado al número " + numeroCelular;
    }
}

