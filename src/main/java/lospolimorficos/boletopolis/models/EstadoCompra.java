package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los posibles estados en los que puede encontrarse una compra.
 */
public enum EstadoCompra {
    /**
     * La compra ha sido iniciada pero aún no se ha procesado el pago.
     */
    CREADA,
    /**
     * La compra ha sido confirmada, generalmente después de la selección de asientos y antes del pago.
     */
    CONFIRMADA,
    /**
     * El pago de la compra ha sido procesado exitosamente.
     */
    PAGADA,
    /**
     * La compra ha sido cancelada por el usuario o el sistema.
     */
    CANCELADA,
    /**
     * La compra ha sido reembolsada al cliente.
     */
    REEMBOLSADA
}
