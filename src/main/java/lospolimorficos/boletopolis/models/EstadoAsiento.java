package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los posibles estados en los que puede encontrarse un asiento.
 */
public enum EstadoAsiento {
    /**
     * El asiento está libre y puede ser seleccionado.
     */
    DISPONIBLE,
    /**
     * El asiento ha sido reservado temporalmente.
     */
    RESERVADO,
    /**
     * El asiento ha sido comprado y ya no está disponible.
     */
    VENDIDO,
    /**
     * El asiento ha sido bloqueado por la administración y no puede ser vendido ni reservado.
     */
    BLOQUEADO
}
