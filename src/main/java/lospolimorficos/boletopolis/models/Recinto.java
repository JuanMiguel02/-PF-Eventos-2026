package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recinto implements Cloneable {

    private final UUID idRecinto;
    private String nombre;
    private String direccion;
    private Ciudad ciudad;
    private List<Zona> zonas;
    private Escenario escenario;
    private int capacidad;

    public Recinto(String nombre, String direccion, Ciudad ciudad) {
        this.idRecinto = UUID.randomUUID();
        this.direccion = direccion;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.zonas = new ArrayList<>();
        this.capacidad = 0;
        this.escenario = null;
    }

    public int getOcupacion(){
        return zonas.stream()
                .mapToInt(Zona::calcularOcupacion)
                .sum();
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarZona(Zona zona) {
        this.zonas.add(zona);
    }

    public UUID getIdRecinto() {
        return idRecinto;
    }

    public String getDireccion() {
        return direccion;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void setEscenario(Escenario escenario) {
        this.escenario = escenario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    public Escenario getEscenario() {
        return escenario;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Recinto copiar () throws CloneNotSupportedException {
        Recinto copia = (Recinto) super.clone();
        copia.setEscenario(this.escenario);
        copia.setCapacidad(this.capacidad);
        copia.zonas = new ArrayList<>();
        for (Zona zona : this.zonas) {
            copia.agregarZona(zona.copiar());
        }
        return copia;
    }
}
