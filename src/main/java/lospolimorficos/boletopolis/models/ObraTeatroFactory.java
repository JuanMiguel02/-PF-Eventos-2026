package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class ObraTeatroFactory implements EventoFactory {

    private String companiaTeatro;
    private String director;
    private int numActos;

    public ObraTeatroFactory(String companiaTeatro, String director, int numActos) {
        this.companiaTeatro = companiaTeatro;
        this.director = director;
        this.numActos = numActos;
    }

    @Override
    public Evento crearEvento(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora,  Recinto recinto) {
        return new ObraTeatro(
                nombre,
                descripcion,
                ciudad,
                fechaYHora,
                recinto,
                companiaTeatro,
                director,
                numActos
        );
    }
}
