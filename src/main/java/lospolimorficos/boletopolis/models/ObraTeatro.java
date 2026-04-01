package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class ObraTeatro extends Evento{

    private String companiaTeatro;
    private String director;
    private int numActos;

    public ObraTeatro(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, EstadoEvento estado, Recinto recinto, Duration duracion, String companiaTeatro, String director, int numActos) {
        super(nombre, descripcion, ciudad, fechaYHora, estado, recinto, duracion);
        this.companiaTeatro = companiaTeatro;
        this.director = director;
        this.numActos = numActos;
    }
}
