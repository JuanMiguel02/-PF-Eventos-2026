package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;

public class Conferencia extends Evento{

    private String ponente;
    private String tema;
    private String institucion;

    public Conferencia(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto, String ponente, String tema, String institucion) {
        super(nombre, descripcion, ciudad, fechaYHora, recinto);
        this.ponente = ponente;
        this.tema = tema;
        this.institucion = institucion;
    }

    public String getPonente() {
        return ponente;
    }

    public void setPonente(String ponente) {
        this.ponente = ponente;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }
}
