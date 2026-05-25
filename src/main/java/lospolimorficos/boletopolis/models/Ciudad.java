package lospolimorficos.boletopolis.models;

/**
 * Enumeración que representa las ciudades disponibles en el sistema.
 * Cada ciudad tiene un nombre asociado.
 */
public enum Ciudad {
    /**
     * Ciudad de Armenia.
     */
    ARMENIA("Armenia"),
    /**
     * Ciudad de Pereira.
     */
    PEREIRA("Pereira"),
    ;

    private final String nombre;

    /**
     * Constructor para {@code Ciudad}.
     *
     * @param nombre El nombre de la ciudad.
     */
    Ciudad(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el nombre de la ciudad.
     *
     * @return El nombre de la ciudad como String.
     */
    @Override
    public String toString() {
        return nombre;

    }
}
