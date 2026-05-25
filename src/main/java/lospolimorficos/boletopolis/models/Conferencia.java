package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;

/**
 * Representa un evento de tipo Conferencia.
 * Extiende la clase abstracta {@link Evento} y añade atributos específicos de una conferencia
 * como el ponente, el tema y la institución.
 */
public class Conferencia extends Evento{

    private String ponente;
    private String tema;
    private String institucion;

    /**
     * Constructor para crear una nueva Conferencia.
     *
     * @param nombre El nombre de la conferencia.
     * @param descripcion La descripción de la conferencia.
     * @param ciudad La ciudad donde se realizará la conferencia.
     * @param fechaYHora La fecha y hora de la conferencia.
     * @param recinto El {@link Recinto} donde se llevará a cabo la conferencia.
     * @param ponente El nombre del ponente de la conferencia.
     * @param tema El tema principal de la conferencia.
     * @param institucion La institución organizadora o patrocinadora de la conferencia.
     */
    public Conferencia(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto, String ponente, String tema, String institucion) {
        super(nombre, descripcion, ciudad, fechaYHora, recinto);
        this.ponente = ponente;
        this.tema = tema;
        this.institucion = institucion;
    }

    /**
     * Obtiene el nombre del ponente de la conferencia.
     *
     * @return El nombre del ponente.
     */
    public String getPonente() {
        return ponente;
    }

    /**
     * Establece el nombre del ponente de la conferencia.
     *
     * @param ponente El nuevo nombre del ponente.
     */
    public void setPonente(String ponente) {
        this.ponente = ponente;
    }

    /**
     * Obtiene el tema principal de la conferencia.
     *
     * @return El tema de la conferencia.
     */
    public String getTema() {
        return tema;
    }

    /**
     * Establece el tema principal de la conferencia.
     *
     * @param tema El nuevo tema de la conferencia.
     */
    public void void setTema(String tema) {
        this.tema = tema;
    }

    /**
     * Obtiene la institución organizadora o patrocinadora de la conferencia.
     *
     * @return La institución de la conferencia.
     */
    public String getInstitucion() {
        return institucion;
    }

    /**
     * Establece la institución organizadora o patrocinadora de la conferencia.
     *
     * @param institucion La nueva institución de la conferencia.
     */
    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }
}
