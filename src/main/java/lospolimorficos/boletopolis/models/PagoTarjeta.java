package lospolimorficos.boletopolis.models;

import java.util.Random;

public class PagoTarjeta implements MetodoPago{

    private final String numeroTarjeta;
    private final CuentaSimulada cuenta;

    public PagoTarjeta(CuentaSimulada cuenta){
        this.numeroTarjeta = generarNumeroTarjeta();
        this.cuenta = cuenta;
    }

    private String generarNumeroTarjeta() {
        Random random = new Random();
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            numero.append(random.nextInt(16));
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
        return "Tarjeta terminada en " + numeroTarjeta.substring(numeroTarjeta.length() -4 );
    }
}
