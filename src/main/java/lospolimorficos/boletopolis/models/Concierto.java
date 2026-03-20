package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Concierto extends Evento{
    private String artista;
    private String generoMusical;

    public Concierto(String descripcion, String nombre, Ciudad ciudad, LocalDateTime fechaYHora, EstadoEvento estado, Recinto recinto, Duration duracion, String artista, String generoMusical) {
        super(descripcion, nombre, ciudad, fechaYHora, estado, recinto, duracion);
        this.artista = artista;
        this.generoMusical = generoMusical;
    }
}
