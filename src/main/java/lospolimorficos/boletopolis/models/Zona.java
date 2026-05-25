package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa una zona dentro de un recinto, con sus asientos, capacidad y precio.
 * Implementa {@link Cloneable} para permitir la creación de copias de zonas.
 */
public class Zona implements Cloneable {

    private static final Map<TipoZona, Integer> contadores = new HashMap<>();

    private final String idZona;
    private double precioZona;
    private String nombre;
    private final TipoZona tipoZona;
    private final PosicionZona posicionZona;
    private final int capacidad;
    private List<Asiento> asientos;

    /**
     * Constructor para crear una nueva Zona.
     *
     * @param nombre El nombre de la zona.
     * @param capacidad La capacidad máxima de la zona (número de asientos).
     * @param tipoZona El tipo de zona (e.g., VIP, General).
     * @param posicion La posición de la zona dentro del recinto.
     * @param precioZona El precio base de un asiento en esta zona.
     */
    public Zona(String nombre, int capacidad, TipoZona tipoZona, PosicionZona posicion, double precioZona) {
        this.idZona = generarId(tipoZona);
        this.capacidad = capacidad;
        this.tipoZona = tipoZona;
        this.posicionZona = posicion;
        this.nombre = nombre;
        this.precioZona = precioZona;
        this.asientos = new ArrayList<>();
    }

    /**
     * Calcula el número de asientos ocupados (no disponibles ni bloqueados) en la zona.
     *
     * @return El número de asientos ocupados.
     */
    public int calcularOcupacion(){
        return (int) asientos.stream()
                .filter(a -> a.getEstado() != EstadoAsiento.DISPONIBLE && a.getEstado() != EstadoAsiento.BLOQUEADO)
                .count();
    }

    /**
     * Genera un ID único para la zona basado en su tipo.
     *
     * @param tipoZona El tipo de la zona.
     * @return El ID generado para la zona.
     */
    private String generarId(TipoZona tipoZona) {
        int numero = contadores.getOrDefault(tipoZona, 0) + 1;
        contadores.put(tipoZona, numero);
        return tipoZona.name() + "-" + numero;
    }

    /**
     * Establece el nombre de la zona.
     *
     * @param nombre El nuevo nombre de la zona.
     */
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    /**
     * Agrega un asiento a la lista de asientos de la zona.
     *
     * @param asiento El asiento a agregar.
     */
    public void agregarAsiento(Asiento asiento) {
        this.asientos.add(asiento);
    }

    /**
     * Obtiene el ID único de la zona.
     *
     * @return El ID de la zona.
     */
    public String getIdZona() {
        return idZona;
    }

    /**
     * Obtiene el precio base de un asiento en esta zona.
     *
     * @return El precio de la zona.
     */
    public double getPrecioZona() {
        return precioZona;
    }

    /**
     * Obtiene el nombre de la zona.
     *
     * @return El nombre de la zona.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el tipo de zona.
     *
     * @return El tipo de zona.
     */
    public TipoZona getTipoZona() {
        return tipoZona;
    }

    /**
     * Obtiene la capacidad total de la zona (número de asientos).
     *
     * @return La capacidad de la zona.
     */
    public int getCapacidad() {
        return this.capacidad;
    }

    /**
     * Obtiene la posición de la zona dentro del recinto.
     *
     * @return La posición de la zona.
     */
    public PosicionZona getPosicionZona() {
        return posicionZona;
    }

    /**
     * Obtiene la lista de asientos de la zona.
     *
     * @return La lista de asientos.
     */
    public List<Asiento> getAsientos() {
        return asientos;
    }

    /**
     * Establece el precio base de un asiento en esta zona.
     *
     * @param precioZona El nuevo precio de la zona.
     */
    public void setPrecioZona(double precioZona) {
        this.precioZona = precioZona;
    }

    /**
     * Crea una copia profunda de la zona, incluyendo sus asientos.
     *
     * @return Una nueva instancia de Zona que es una copia del objeto actual.
     * @throws CloneNotSupportedException Si la clonación no es soportada.
     */
    public Zona copiar() throws CloneNotSupportedException {
        Zona copia = (Zona) super.clone();
        copia.asientos = new ArrayList<>();
        for (Asiento asiento : this.asientos) {
            copia.agregarAsiento(asiento.copiar());
        }
        return copia;
    }
}
