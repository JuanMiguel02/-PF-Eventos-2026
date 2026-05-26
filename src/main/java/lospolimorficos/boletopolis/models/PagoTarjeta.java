package lospolimorficos.boletopolis.models;

import java.util.Random;

/**
 * Implementación de {@link MetodoPago} para pagos con tarjeta.
 * Simula la funcionalidad de una tarjeta de crédito/débito, interactuando con una {@link CuentaSimulada}.
 */
public class PagoTarjeta implements MetodoPago{

    private final String numeroTarjeta;
    private final CuentaSimulada cuenta;
    private String tipoTarjeta;

    /**
     * Constructor para crear un nuevo PagoTarjeta con un número de tarjeta generado aleatoriamente.
     *
     * @param cuenta La {@link CuentaSimulada} asociada a la tarjeta.
     * @param tipoTarjeta El tipo de tarjeta (e.g., "VISA", "MasterCard").
     */
    public PagoTarjeta(CuentaSimulada cuenta, String tipoTarjeta){
        this.numeroTarjeta = generarNumeroTarjeta();
        this.cuenta = cuenta;
        this.tipoTarjeta = tipoTarjeta;
    }

    /**
     * Constructor para crear un nuevo PagoTarjeta con un número de tarjeta específico.
     *
     * @param cuenta La {@link CuentaSimulada} asociada a la tarjeta.
     * @param tipoTarjeta El tipo de tarjeta (e.g., "VISA", "MasterCard").
     * @param numeroTarjeta El número de tarjeta.
     */
    public PagoTarjeta(CuentaSimulada cuenta, String tipoTarjeta, String numeroTarjeta){
        this.numeroTarjeta = numeroTarjeta;
        this.cuenta = cuenta;
        this.tipoTarjeta = tipoTarjeta;
    }

    /**
     * Genera un número de tarjeta de 16 dígitos aleatorio.
     *
     * @return Una cadena de 16 dígitos que simula un número de tarjeta.
     */
    private String generarNumeroTarjeta() {
        Random random = new Random();
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 16; i++) {
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
     * Obtiene el saldo disponible en la cuenta asociada a la tarjeta.
     *
     * @return El saldo disponible.
     */
    @Override
    public double getSaldoDisponible() {
        return cuenta.getSaldo();
    }

    /**
     * Obtiene una descripción del método de pago, mostrando el tipo de tarjeta y los últimos 4 dígitos.
     *
     * @return Una cadena descriptiva del método de pago.
     */
    @Override
    public String getDescripcion() {
        return "Tarjeta " + tipoTarjeta + " terminada en " + numeroTarjeta.substring(numeroTarjeta.length() -4 );
    }

    @Override
    public String toString(){
        return  getDescripcion();
    }
}
