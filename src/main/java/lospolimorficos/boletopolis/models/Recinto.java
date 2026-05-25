package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa un recinto donde se pueden celebrar eventos.
 * Contiene información sobre su ubicación, capacidad, zonas y escenario.
 * Implementa {@link Cloneable} para permitir la creación de copias del recinto.
 */
public class Recinto implements Cloneable {

    private final UUID idRecinto;
    private String nombre;
    private String direccion;
    private Ciudad ciudad;
    private List<Zona> zonas;
    private Escenario escenario;
    private int capacidad;

    /**
     * Constructor para crear un nuevo Recinto.
     *
     * @param nombre El nombre del recinto.
     * @param direccion La dirección física del recinto.
     * @param ciudad La ciudad donde se encuentra el recinto.
     */
    public Recinto(String nombre, String direccion, Ciudad ciudad) {
        this.idRecinto = UUID.randomUUID();
        this.direccion = direccion;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.zonas = new ArrayList<>();
        this.capacidad = 0;
        this.escenario = null;
    }

    /**
     * Calcula la ocupación actual del recinto sumando la ocupación de todas sus zonas.
     *
     * @return El número total de asientos ocupados en el recinto.
     */
    public int getOcupacion(){
        return zonas.stream()
                .mapToInt(Zona::calcularOcupacion)
                .sum();
    }

    /**
     * Calcula la capacidad total del recinto sumando la capacidad de todas sus zonas.
     *
     * @return La capacidad total de asientos del recinto.
     */

    public int getCapacidad() {
        return zonas.stream()
                .mapToInt(Zona::getCapacidad)
                .sum();
    }

    /**
     * Obtiene el nombre del recinto.
     *
     * @return El nombre del recinto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Agrega una zona a la lista de zonas del recinto.
     *
     * @param zona La {@link Zona} a agregar.
     */
    public void agregarZona(Zona zona) {
        this.zonas.add(zona);
    }

    /**
     * Obtiene el ID único del recinto.
     *
     * @return El UUID del recinto.
     */
    public UUID getIdRecinto() {
        return idRecinto;
    }

    /**
     * Obtiene la dirección del recinto.
     *
     * @return La dirección del recinto.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Obtiene la ciudad donde se encuentra el recinto.
     *
     * @return La {@link Ciudad} del recinto.
     */
    public Ciudad getCiudad() {
        return ciudad;
    }

    /**
     * Obtiene la lista de zonas del recinto.
     *
     * @return Una lista de {@link Zona}s.
     */
    public List<Zona> getZonas() {
        return zonas;
    }

    /**
     * Establece el escenario del recinto.
     *
     * @param escenario El {@link Escenario} a establecer.
     */
    public void setEscenario(Escenario escenario) {
        this.escenario = escenario;
    }

    /**
     * Establece el nombre del recinto.
     *
     * @param nombre El nuevo nombre del recinto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la dirección del recinto.
     *
     * @param direccion La nueva dirección del recinto.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Establece la ciudad donde se encuentra el recinto.
     *
     * @param ciudad La nueva {@link Ciudad} del recinto.
     */
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Establece la lista de zonas del recinto.
     *
     * @param zonas La nueva lista de {@link Zona}s.
     */
    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    /**
     * Obtiene el escenario del recinto.
     *
     * @return El {@link Escenario} del recinto.
     */
    public Escenario getEscenario() {
        return escenario;
    }

    /**
     * Establece la capacidad total del recinto.
     *
     * @param capacidad La nueva capacidad.
     */
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * Crea una copia profunda del recinto, incluyendo sus zonas y asientos.
     *
     * @return Una nueva instancia de Recinto que es una copia del objeto actual.
     * @throws CloneNotSupportedException Si la clonación no es soportada.
     */
    public Recinto copiar () throws CloneNotSupportedException {
        Recinto copia = (Recinto) super.clone();
        copia.setEscenario(this.escenario); // Asumiendo que Escenario es inmutable o se copia por referencia si es adecuado.
        copia.setCapacidad(this.capacidad);
        copia.zonas = new ArrayList<>();
        for (Zona zona : this.zonas) {
            copia.agregarZona(zona.copiar()); // Copia profunda de las zonas.
        }
        return copia;
    }

    /**
     * Devuelve una representación en cadena del recinto.
     *
     * @return Una cadena que describe el nombre y la dirección del recinto.
     */
    @Override
    public String toString() {
        return "Recinto: "
                + nombre +
                ", dirección: " + direccion;
    }
}
