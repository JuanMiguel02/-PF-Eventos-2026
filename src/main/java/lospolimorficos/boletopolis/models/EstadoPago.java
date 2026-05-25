package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los posibles estados en los que puede encontrarse un pago.
 */
public enum EstadoPago {
    /**
     * El pago ha sido iniciado pero aún no se ha confirmado su procesamiento.
     */
    PENDIENTE,
    /**
     * El pago ha sido procesado y confirmado exitosamente.
     */
    APROBADO,
    /**
     * El pago ha sido rechazado, por ejemplo, por fondos insuficientes o error en los datos.
     */
    RECHAZADO,
    /**
     * El pago ha sido reembolsado al cliente.
     */
    REEMBOLSADO
}
