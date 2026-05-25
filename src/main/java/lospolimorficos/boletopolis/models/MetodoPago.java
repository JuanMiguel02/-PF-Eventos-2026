package lospolimorficos.boletopolis.models;

/**
 * Interfaz que define el contrato para cualquier método de pago en el sistema.
 * Proporciona operaciones para realizar pagos, reembolsos y consultar el saldo disponible.
 */
public interface MetodoPago {

    /**
     * Intenta realizar un pago por el monto especificado.
     *
     * @param monto El monto a pagar.
     * @return {@code true} si el pago fue exitoso, {@code false} en caso contrario.
     */
    boolean pagar(double monto);

    /**
     * Realiza un reembolso por el monto especificado.
     *
     * @param monto El monto a reembolsar.
     */
    void reembolsar(double monto);

    /**
     * Obtiene el saldo disponible asociado a este método de pago.
     *
     * @return El saldo disponible.
     */
    double getSaldoDisponible();

    /**
     * Obtiene una descripción textual del método de pago.
     *
     * @return Una cadena que describe el método de pago.
     */
    String getDescripcion();

}
