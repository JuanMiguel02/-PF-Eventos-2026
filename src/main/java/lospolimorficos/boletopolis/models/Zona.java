package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.List;

public class Zona {
    private String idZona;
    private double precioZona;
    private String nombre;
    private TipoZona tipoZona;
    private int capacidad;
    private List<Asiento> asientos;

    public Zona(String idZona, int capacidad, TipoZona tipoZona, String nombre, double precioZona) {
        this.idZona = idZona;
        this.capacidad = capacidad;
        this.tipoZona = tipoZona;
        this.nombre = nombre;
        this.precioZona = precioZona;
        this.asientos = new ArrayList<>();
    }
}
