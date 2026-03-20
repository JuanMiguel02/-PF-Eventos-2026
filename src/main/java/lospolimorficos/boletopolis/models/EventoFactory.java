package lospolimorficos.boletopolis.models;

import java.time.Duration;

public interface EventoFactory {
    Evento crearEvento(String nombre, String descripcion, Ciudad ciudad, String fechaYHora, Recinto recinto, Duration duracion);
}
