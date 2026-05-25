package lospolimorficos.boletopolis.models;

import java.util.Random;

public class CuentaSimulada {

    private final String numeroCuenta;
    private final Cliente cliente;
    private double saldo;

    public CuentaSimulada(Cliente cliente, double saldoInicial){
        this.numeroCuenta = generarNumeroCuenta();
        this.cliente = cliente;
        this.saldo = saldoInicial;
    }

    private String generarNumeroCuenta(){
        Random random = new Random();
        StringBuilder numero = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }

    public boolean retirar(double monto){
        if(monto < 0){
            return false;
        }
        if(saldo >= monto){
            saldo -= monto;
            return true;
        }else{
            return false;
        }
    }

    public void depositar(double monto){
        saldo += monto;
    }

    public double getSaldo(){
        return saldo;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
