package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Concierto extends Evento{
    private String artista;
    private String generoMusical;

    public Concierto(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto, Duration duracion, String artista, String generoMusical) {
        super(nombre, descripcion, ciudad, fechaYHora, recinto, duracion);
        this.artista = artista;
        this.generoMusical = generoMusical;
    }
}
