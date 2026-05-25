package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los posibles estados en los que puede encontrarse una entrada.
 */
public enum EstadoEntrada {
    /**
     * La entrada está activa y lista para ser utilizada.
     */
    ACTIVA,
    /**
     * La entrada ha sido utilizada para acceder al evento.
     */
    USADA,
    /**
     * La entrada ha sido cancelada y ya no es válida.
     */
    CANCELADA
}
