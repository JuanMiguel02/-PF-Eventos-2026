package lospolimorficos.boletopolis.models;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Implementación de {@link EventoFactory} para la creación de objetos {@link Conferencia}.
 * Esta fábrica se encarga de instanciar conferencias con sus atributos específicos.
 */
public class ConferenciaFactory implements EventoFactory {

    private String ponente;
    private String tema;
    private String institucion;

    /**
     * Constructor para {@code ConferenciaFactory}.
     *
     * @param ponente El nombre del ponente de la conferencia.
     * @param tema El tema principal de la conferencia.
     * @param institucion La institución organizadora o patrocinadora de la conferencia.
     */
    public ConferenciaFactory(String ponente, String tema, String institucion) {
        this.ponente = ponente;
        this.tema = tema;
        this.institucion = institucion;
    }

    /**
     * Crea una nueva instancia de {@link Conferencia} con los parámetros generales del evento
     * y los atributos específicos de la conferencia definidos en la fábrica.
     *
     * @param nombre El nombre del evento.
     * @param descripcion La descripción detallada del evento.
     * @param ciudad La ciudad donde se realizará el evento.
     * @param fechaYHora La fecha y hora específicas en que tendrá lugar el evento.
     * @param recinto El recinto o lugar físico donde se llevará a cabo el evento.
     * @return Una nueva instancia de {@link Conferencia}.
     */
    @Override
    public Evento crearEvento(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto) {
        return new Conferencia(
                nombre,
                descripcion,
                ciudad,
                fechaYHora,
                recinto,
                ponente,
                tema,
                institucion
        );
    }

    /**
     * Obtiene el tema de la conferencia.
     *
     * @return El tema de la conferencia.
     */
    public String getTema() {
        return tema;
    }

    /**
     * Establece el tema de la conferencia.
     *
     * @param tema El nuevo tema de la conferencia.
     */
    public void setTema(String tema) {
        this.tema = tema;
    }

    /**
     * Obtiene la institución de la conferencia.
     *
     * @return La institución de la conferencia.
     */
    public String getInstitucion() {
        return institucion;
    }

    /**
     * Establece la institución de la conferencia.
     *
     * @param institucion La nueva institución de la conferencia.
     */
    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    /**
     * Obtiene el ponente de la conferencia.
     *
     * @return El ponente de la conferencia.
     */
    public String getPonente() {
        return ponente;
    }

    /**
     * Establece el ponente de la conferencia.
     *
     * @param ponente El nuevo ponente de la conferencia.
     */
    public void setPonente(String ponente) {
        this.ponente = ponente;
    }
}
