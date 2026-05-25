package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los posibles estados en los que puede encontrarse un evento.
 */
public enum EstadoEvento {
    /**
     * El evento está en fase de creación y no es visible públicamente.
     */
    BORRADOR,
    /**
     * El evento ha sido publicado y está disponible para la venta de entradas.
     */
    PUBLICADO,
    /**
     * El evento ha sido temporalmente pausado, la venta de entradas puede estar suspendida.
     */
    PAUSADO,
    /**
     * El evento ha sido cancelado.
     */
    CANCELADO,
    /**
     * El evento ha finalizado.
     */
    FINALIZADO
}
