package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Conferencia extends Evento{

    private String ponente;
    private String tema;
    private String institucion;

    public Conferencia(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, EstadoEvento estado, Recinto recinto, Duration duracion, String ponente, String tema, String institucion) {
        super(nombre, descripcion, ciudad, fechaYHora, estado, recinto, duracion);
        this.ponente = ponente;
        this.tema = tema;
        this.institucion = institucion;
    }
}
