package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;

public interface ConciertoFactory implements EventoFactory {
    @Override
    public Evento crearEvento(String nombre, String descripcion, Ciudad ciudad,
                              String fechaYHora, Recinto recinto, Duration duracion){
        LocalDateTime fecha=LocalDateTime.parse(fechaYHora);
        return new Concierto(
                nombre,
                descripcion,
                ciudad,
                fecha,
                EstadoEvento.PUBLICADO,
                recinto,
                duracion
        );
    }
}
