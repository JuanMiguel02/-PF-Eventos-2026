package lospolimorficos.boletopolis.models;

import javax.xml.datatype.Duration;
import java.time.LocalDateTime;

public class ObraTeatroFactory {
    public class ObraTeatroFactory implements EventoFactory{
        @Override
        public Evento crearEvento(String nombre, String descripcion, Ciudad ciudad,
                                  String fechaYHora, Recinto recinto, Duration duracion) {

            LocalDateTime fecha = LocalDateTime.parse(fechaYHora);

            return new ObraTeatro(
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
