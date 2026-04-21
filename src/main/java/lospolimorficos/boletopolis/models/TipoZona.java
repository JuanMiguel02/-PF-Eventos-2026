package lospolimorficos.boletopolis.models;

public enum TipoZona {
    VIP("-fx-fill: gold;"),
    GENERAL("-fx-fill: lightblue;"),
    PREFERENCIAL("-fx-fill: lightgray;");

    private String estilo;

    TipoZona(String estilo) {
        this.estilo = estilo;
    }

    public String getEstilo() {
        return estilo;
    }
}
