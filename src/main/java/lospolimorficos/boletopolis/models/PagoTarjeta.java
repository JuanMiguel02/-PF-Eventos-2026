package lospolimorficos.boletopolis.models;

import java.util.Random;

public class PagoTarjeta implements MetodoPago{

    private final String numeroTarjeta;
    private final CuentaSimulada cuenta;
    private String tipoTarjeta;

    public PagoTarjeta(CuentaSimulada cuenta, String tipoTarjeta){
        this.numeroTarjeta = generarNumeroTarjeta();
        this.cuenta = cuenta;
        this.tipoTarjeta = tipoTarjeta;
    }

    public PagoTarjeta(CuentaSimulada cuenta, String tipoTarjeta, String numeroTarjeta){
        this.numeroTarjeta = numeroTarjeta;
        this.cuenta = cuenta;
        this.tipoTarjeta = tipoTarjeta;
    }

    private String generarNumeroTarjeta() {
        Random random = new Random();
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 16; i++) {
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
        return "Tarjeta " + tipoTarjeta + " terminada en " + numeroTarjeta.substring(numeroTarjeta.length() -4 );
    }
}
