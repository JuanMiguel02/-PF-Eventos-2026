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

    public String getPonente() {
        return ponente;
    }

    public void setPonente(String ponente) {
        this.ponente = ponente;
    }
}
