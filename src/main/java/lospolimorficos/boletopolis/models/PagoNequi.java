package lospolimorficos.boletopolis.models;

import java.util.Random;

/**
 * Implementación de {@link MetodoPago} para pagos a través de Nequi.
 * Simula la funcionalidad de una cuenta Nequi, interactuando con una {@link CuentaSimulada}.
 */
public class PagoNequi implements MetodoPago {

    private final String numeroCelular;
    private final CuentaSimulada cuenta;

    /**
     * Constructor para crear un nuevo PagoNequi con un número de celular generado aleatoriamente.
     *
     * @param cuenta La {@link CuentaSimulada} asociada a la cuenta Nequi.
     */
    public PagoNequi(CuentaSimulada cuenta) {
        this.numeroCelular = generarNumeroCelular();
        this.cuenta = cuenta;
    }

    /**
     * Constructor para crear un nuevo PagoNequi con un número de celular específico.
     *
     * @param cuenta La {@link CuentaSimulada} asociada a la cuenta Nequi.
     * @param numeroCelular El número de celular asociado a la cuenta Nequi.
     */
    public PagoNequi(CuentaSimulada cuenta, String numeroCelular) {
        this.numeroCelular = numeroCelular;
        this.cuenta = cuenta;
    }

    /**
     * Genera un número de celular simulado que comienza con '3' y tiene 10 dígitos.
     *
     * @return Una cadena de 10 dígitos que simula un número de celular.
     */
    private String generarNumeroCelular() {
        Random random = new Random();
        StringBuilder numero = new StringBuilder("3"); // Los números de Nequi suelen empezar por 3.

        for(int i = 0; i < 9; i++){ // Generar los 9 dígitos restantes.
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }

    /**
     * Intenta realizar un pago retirando el monto de la cuenta simulada.
     *
     * @param monto El monto a pagar.
     * @return {@code true} si el pago fue exitoso, {@code false} en caso contrario (saldo insuficiente).
     */
    @Override
    public boolean pagar(double monto) {
        return cuenta.retirar(monto);
    }

    /**
     * Realiza un reembolso depositando el monto en la cuenta simulada.
     *
     * @param monto El monto a reembolsar.
     */
    @Override
    public void reembolsar(double monto) {
        cuenta.depositar(monto);
    }

    /**
     * Obtiene el saldo disponible en la cuenta Nequi asociada.
     *
     * @return El saldo disponible.
     */
    @Override
    public double getSaldoDisponible() {
        return cuenta.getSaldo();
    }

    /**
     * Obtiene una descripción del método de pago, mostrando el número de celular asociado.
     *
     * @return Una cadena descriptiva del método de pago.
     */
    @Override
    public String getDescripcion() {
        return "Nequi asociado al número " + numeroCelular;
    }

    @Override
    public String toString(){
        return  getDescripcion();
    }
}
