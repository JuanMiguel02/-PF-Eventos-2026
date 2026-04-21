package lospolimorficos.boletopolis.plantillas;

import lospolimorficos.boletopolis.models.PosicionZona;
import lospolimorficos.boletopolis.models.TipoZona;

public class PlantillaZona {
    private String nombre;
    private TipoZona tipoZona;
    private PosicionZona posicionZona;
    private int filas;
    private int columnas;
    private double precioBase;

    public PlantillaZona(String nombre, PosicionZona posicion, TipoZona tipoZona, int filas, int columnas, double precioBase) {
        this.nombre = nombre;
        this.posicionZona = posicion;
        this.tipoZona = tipoZona;
        this.filas = filas;
        this.columnas = columnas;
        this.precioBase = precioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public TipoZona getTipoZona() {
        return tipoZona;
    }

    public PosicionZona getPosicionZona() {
        return posicionZona;
    }

    public int calcularCapacidad() {
        return filas * columnas;
    }
}
