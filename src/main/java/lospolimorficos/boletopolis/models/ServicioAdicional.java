package lospolimorficos.boletopolis.models;

public enum ServicioAdicional {
    PARQUEADERO(50000),
    COMIDA(30000),
    MERCHANDISING(80000);

    private final double precio;

    ServicioAdicional(double precio){
        this.precio = precio;
    }

    public double getPrecio(){
        return this.precio;
    }
}
