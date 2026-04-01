package lospolimorficos.boletopolis.models;

public class Escenario {
    private final PosicionEscenario posicion;

    public Escenario(PosicionEscenario posicion) {
        this.posicion = posicion;
    }

    public PosicionEscenario getPosicion() {
        return posicion;
    }
}
