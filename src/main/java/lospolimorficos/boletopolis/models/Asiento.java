package lospolimorficos.boletopolis.models;

public class Asiento implements Cloneable {
    private String idAsiento;
    private int fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(int fila, int numero) {
        this.idAsiento = generarId(fila, numero);
        this.fila = fila;
        this.numero = numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    private String generarId(int fila, int numero){
        char letraFila = (char) ('A' + fila-1);
        return letraFila + String.valueOf(numero);
    }

    public String getIdAsiento() {
        return idAsiento;
    }

    public int getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    public Asiento copiar() throws CloneNotSupportedException {
        Asiento copia = (Asiento) super.clone();
        copia.setEstado(this.estado);
        return copia;
    }
}
