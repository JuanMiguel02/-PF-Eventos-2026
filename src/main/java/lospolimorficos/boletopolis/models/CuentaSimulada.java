package lospolimorficos.boletopolis.models;

import java.util.Random;

/**
 * Representa una cuenta bancaria simulada para un cliente.
 * Permite realizar operaciones básicas como retirar y depositar dinero.
 */
public class CuentaSimulada {

    private final String numeroCuenta;
    private final Cliente cliente;
    private double saldo;

    /**
     * Constructor para crear una nueva CuentaSimulada.
     *
     * @param cliente El {@link Cliente} al que pertenece esta cuenta.
     * @param saldoInicial El saldo inicial de la cuenta.
     */
    public CuentaSimulada(Cliente cliente, double saldoInicial){
        this.numeroCuenta = generarNumeroCuenta();
        this.cliente = cliente;
        this.saldo = saldoInicial;
    }

    /**
     * Genera un número de cuenta aleatorio de 10 dígitos.
     *
     * @return Una cadena de 10 dígitos que simula un número de cuenta.
     */
    private String generarNumeroCuenta(){
        Random random = new Random();
        StringBuilder numero = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }

    /**
     * Intenta retirar un monto de la cuenta.
     *
     * @param monto El monto a retirar. Debe ser un valor positivo.
     * @return {@code true} si el retiro fue exitoso (saldo suficiente), {@code false} en caso contrario.
     */
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

    /**
     * Deposita un monto en la cuenta.
     *
     * @param monto El monto a depositar.
     */
    public void depositar(double monto){
        saldo += monto;
    }

    /**
     * Obtiene el saldo actual de la cuenta.
     *
     * @return El saldo de la cuenta.
     */
    public double getSaldo(){
        return saldo;
    }

    /**
     * Obtiene el número de cuenta.
     *
     * @return El número de cuenta.
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * Obtiene el cliente asociado a esta cuenta.
     *
     * @return El {@link Cliente} de la cuenta.
     */
    public Cliente getCliente() {
        return cliente;
    }
}
