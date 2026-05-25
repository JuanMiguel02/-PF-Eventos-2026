package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Representa un evento de tipo Concierto.
 * Extiende la clase abstracta {@link Evento} y añade atributos específicos de un concierto
 * como el artista y el género musical.
 */
public class Concierto extends Evento {
    private String artista;
    private String generoMusical;

    /**
     * Constructor para crear un nuevo Concierto.
     *
     * @param nombre El nombre del concierto.
     * @param descripcion La descripción del concierto.
     * @param ciudad La ciudad donde se realizará el concierto.
     * @param fechaYHora La fecha y hora del concierto.
     * @param recinto El {@link Recinto} donde se llevará a cabo el concierto.
     * @param artista El nombre del artista o banda que se presentará.
     * @param generoMusical El género musical del concierto.
     */
    public Concierto(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto, String artista, String generoMusical) {
        super(nombre, descripcion, ciudad, fechaYHora, recinto);
        this.artista = artista;
        this.generoMusical = generoMusical;
    }

    /**
     * Obtiene el nombre del artista o banda del concierto.
     *
     * @return El nombre del artista.
     */
    public String getArtista() {
        return artista;
    }

    /**
     * Establece el nombre del artista o banda del concierto.
     *
     * @param artista El nuevo nombre del artista.
     */
    public void setArtista(String artista) {
        this.artista = artista;
    }

    /**
     * Obtiene el género musical del concierto.
     *
     * @return El género musical.
     */
    public String getGeneroMusical() {
        return generoMusical;
    }

    /**
     * Establece el género musical del concierto.
     *
     * @param generoMusical El nuevo género musical.
     */
    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
    }
}
