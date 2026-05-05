package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class ConferenciaFactory implements EventoFactory {

    private String ponente;
    private String tema;
    private String institucion;

    public ConferenciaFactory(String ponente, String tema, String institucion) {
        this.ponente = ponente;
        this.tema = tema;
        this.institucion = institucion;
    }

    @Override
    public Evento crearEvento(String nombre, String descripcion, Ciudad ciudad,
                              LocalDateTime fechaYHora, EstadoEvento estadoEvento, Recinto recinto, Duration duracion) {

        return new Conferencia(
                nombre,
                descripcion,
                ciudad,
                fechaYHora,
                estadoEvento,
                recinto,
                duracion,
                ponente,
                tema,
                institucion
        );
    }
}
