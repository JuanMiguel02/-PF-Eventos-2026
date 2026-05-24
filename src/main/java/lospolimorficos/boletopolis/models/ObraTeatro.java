package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;

public class ObraTeatro extends Evento{

    private String companiaTeatro;
    private String director;
    private int numActos;

    public ObraTeatro(String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto, String companiaTeatro, String director, int numActos) {
        super(nombre, descripcion, ciudad, fechaYHora, recinto);
        this.companiaTeatro = companiaTeatro;
        this.director = director;
        this.numActos = numActos;
    }

    public String getCompaniaTeatro() {
        return companiaTeatro;
    }

    public void setCompaniaTeatro(String companiaTeatro) {
        this.companiaTeatro = companiaTeatro;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getNumActos() {
        return numActos;
    }

    public void setNumActos(int numActos) {
        this.numActos = numActos;
    }
}
