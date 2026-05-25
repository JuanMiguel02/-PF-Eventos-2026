package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Implementación de {@link EventoFactory} para la creación de objetos {@link Concierto}.
 * Esta fábrica se encarga de instanciar conciertos con sus atributos específicos.
 */
public class ConciertoFactory implements EventoFactory {

    private String artista;
    private String generoMusical;

    /**
     * Constructor para {@code ConciertoFactory}.
     *
     * @param artista El nombre del artista o banda del concierto.
     * @param generoMusical El género musical del concierto.
     */
    public ConciertoFactory(String artista, String generoMusical) {
        this.artista = artista;
        this.generoMusical = generoMusical;
    }

    /**
     * Crea una nueva instancia de {@link Concierto} con los parámetros generales del evento
     * y los atributos específicos del concierto definidos en la fábrica.
     *
     * @param nombre El nombre del evento.
     * @param descripcion La descripción detallada del evento.
     * @param ciudad La ciudad donde se realizará el evento.
     * @param fechaYHora La fecha y hora específicas en que tendrá lugar el evento.
     * @param recinto El recinto o lugar físico donde se llevará a cabo el evento.
     * @return Una nueva instancia de {@link Concierto}.
     */
    @Override
    public Evento crearEvento(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto) {
        return new Concierto(
                nombre,
                descripcion,
                ciudad,
                fechaYHora,
                recinto,
                artista,
                generoMusical
        );
    }
}
