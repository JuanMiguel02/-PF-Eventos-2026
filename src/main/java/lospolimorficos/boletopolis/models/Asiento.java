package lospolimorficos.boletopolis.models;

/**
 * <p><b>Asiento</b></p>
 *
 * <p>Representa un asiento individual dentro de una zona de un recinto.
 * Contiene información sobre su identificación, ubicación (fila y número)
 * y su estado actual (DISPONIBLE, RESERVADO, VENDIDO, BLOQUEADO).</p>
 *
 * <p>Esta clase es {@link Cloneable} para permitir la creación de copias de asientos.</p>
 */
public class Asiento implements Cloneable {
    /**
     * Identificador único del asiento, generado a partir de su fila y número (ej. "A1", "B15").
     */
    private final String idAsiento;
    /**
     * Número de fila del asiento (base 1).
     */
    private final int fila;
    /**
     * Número de asiento dentro de la fila (base 1).
     */
    private final int numero;
    /**
     * El estado actual del asiento, definido por la enumeración {@link EstadoAsiento}.
     */
    private EstadoAsiento estado;

    /**
     * Constructor para crear una nueva instancia de Asiento.
     * Inicializa el asiento como DISPONIBLE y genera su ID único.
     *
     * @param fila El número de fila del asiento.
     * @param numero El número de asiento dentro de la fila.
     */
    public Asiento(int fila, int numero) {
        this.idAsiento = generarId(fila, numero);
        this.fila = fila;
        this.numero = numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    /**
     * Genera un identificador único para el asiento combinando la letra de la fila
     * (A, B, C...) con el número del asiento.
     *
     * @param fila El número de fila del asiento (base 1).
     * @param numero El número de asiento dentro de la fila (base 1).
     * @return Una cadena de texto que representa el ID único del asiento.
     */
    private String generarId(int fila, int numero){
        char letraFila = (char) ('A' + fila-1);
        return letraFila + String.valueOf(numero);
    }

    /**
     * Obtiene el identificador único del asiento.
     * @return El ID del asiento.
     */
    public String getIdAsiento() {
        return idAsiento;
    }

    /**
     * Obtiene el número de fila del asiento.
     * @return El número de fila.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Obtiene el número de asiento dentro de la fila.
     * @return El número de asiento.
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Obtiene el estado actual del asiento.
     * @return El {@link EstadoAsiento} actual.
     */
    public EstadoAsiento getEstado() {
        return estado;
    }

    /**
     * Establece un nuevo estado para el asiento.
     * @param estado El nuevo {@link EstadoAsiento} a asignar.
     */
    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }


    /**
     * Crea y devuelve una copia superficial de este objeto Asiento.
     * El estado del asiento también se copia.
     *
     * @return Una nueva instancia de {@link Asiento} que es una copia del original.
     * @throws CloneNotSupportedException Si la clonación no es soportada (aunque esta clase la implementa).
     */
    public Asiento copiar() throws CloneNotSupportedException {
        Asiento copia = (Asiento) super.clone();
        copia.setEstado(this.estado); // Asegura que el estado también se copie
        return copia;
    }
}
