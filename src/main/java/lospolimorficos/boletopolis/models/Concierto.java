package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Concierto extends Evento {
    private String artista;
    private String generoMusical;

    public Concierto(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto, String artista, String generoMusical) {
        super(nombre, descripcion, ciudad, fechaYHora, recinto);
        this.artista = artista;
        this.generoMusical = generoMusical;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getGeneroMusical() {
        return generoMusical;
    }

    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
    }
}
