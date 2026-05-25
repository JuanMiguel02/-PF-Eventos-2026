package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los diferentes tipos de zonas disponibles en un recinto.
 * Cada tipo de zona tiene asociado un estilo visual para su representación en la interfaz de usuario.
 */
public enum TipoZona {
    /**
     * Zona VIP, generalmente con las mejores ubicaciones y servicios.
     */
    VIP("-fx-fill: gold;"),
    /**
     * Zona General, la más común y accesible.
     */
    GENERAL("-fx-fill: lightblue;"),
    /**
     * Zona Preferencial, con ubicaciones intermedias o beneficios adicionales.
     */
    PREFERENCIAL("-fx-fill: lightgray;");

    private final String estilo;

    /**
     * Constructor para {@code TipoZona}.
     *
     * @param estilo El estilo CSS asociado a este tipo de zona, utilizado para la representación visual.
     */
    TipoZona(String estilo) {
        this.estilo = estilo;
    }

    /**
     * Obtiene el estilo CSS asociado a este tipo de zona.
     *
     * @return Una cadena de texto que representa el estilo CSS.
     */
    public String getEstilo() {
        return estilo;
    }
}
