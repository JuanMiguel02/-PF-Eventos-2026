package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;

/**
 * Interfaz que define una fábrica para la creación de objetos {@link Evento}.
 * Permite la creación de eventos de manera abstracta, desacoplando el proceso
 * de instanciación de la lógica de negocio.
 */
public interface EventoFactory {
    /**
     * Crea una nueva instancia de {@link Evento} con los parámetros especificados.
     *
     * @param nombre El nombre del evento.
     * @param descripcion La descripción detallada del evento.
     * @param ciudad La ciudad donde se realizará el evento.
     * @param fechaYHora La fecha y hora específicas en que tendrá lugar el evento.
     * @param recinto El recinto o lugar físico donde se llevará a cabo el evento.
     * @return Una nueva instancia de {@link Evento}.
     */
    Evento crearEvento(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto);

}
