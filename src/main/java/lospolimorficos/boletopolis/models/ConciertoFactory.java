package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class ConciertoFactory implements EventoFactory {

    private String artista;
    private String generoMusical;

    public ConciertoFactory(String artista, String generoMusical) {
        this.artista = artista;
        this.generoMusical = generoMusical;
    }

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
