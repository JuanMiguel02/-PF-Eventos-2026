package lospolimorficos.boletopolis.models;

public interface MetodoPago {

    boolean pagar(double monto);
    void reembolsar(double monto);
    double getSaldoDisponible();
    String getDescripcion();

}
