package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zona implements Cloneable {

    private static Map<TipoZona, Integer> contadores = new HashMap<>();

    private String idZona;
    private double precioZona;
    private String nombre;
    private TipoZona tipoZona;
    private PosicionZona posicionZona;
    private int capacidad;
    private List<Asiento> asientos;

    public Zona(String nombre, int capacidad, TipoZona tipoZona, PosicionZona posicion, double precioZona) {
        this.idZona = generarId(tipoZona);
        this.capacidad = capacidad;
        this.tipoZona = tipoZona;
        this.posicionZona = posicion;
        this.nombre = nombre;
        this.precioZona = precioZona;
        this.asientos = new ArrayList<>();
    }

    public int calcularOcupacion(){
        return asientos.stream()
                .filter(a -> a.getEstado() != EstadoAsiento.DISPONIBLE && a.getEstado() != EstadoAsiento.BLOQUEADO)
                .toArray().length;
    }

    private String generarId(TipoZona tipoZona) {
        int numero = contadores.getOrDefault(tipoZona, 0) + 1;
        contadores.put(tipoZona, numero);
        return tipoZona.name() + "-" + numero;
    }

    public void agregarAsiento(Asiento asiento) {
        this.asientos.add(asiento);
    }

    public String getIdZona() {
        return idZona;
    }

    public double getPrecioZona() {
        return precioZona;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoZona getTipoZona() {
        return tipoZona;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public PosicionZona getPosicionZona() {
        return posicionZona;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setPrecioZona(double precioZona) {
        this.precioZona = precioZona;
    }

    public Zona copiar() throws CloneNotSupportedException {
        Zona copia = (Zona) super.clone();
        copia.asientos = new ArrayList<>();
        for (Asiento asiento : this.asientos) {
            copia.agregarAsiento(asiento.copiar());
        }
        return copia;
    }
}
