package lospolimorficos.boletopolis.models;

public enum Ciudad {
    ARMENIA("Armenia"),
    PEREIRA("Pereira"),
    ;

    private String nombre;

    Ciudad(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;

    }
}
