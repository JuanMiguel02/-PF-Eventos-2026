package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

public interface EventoFactory {
    Evento crearEvento(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto, Duration duracion);

}
