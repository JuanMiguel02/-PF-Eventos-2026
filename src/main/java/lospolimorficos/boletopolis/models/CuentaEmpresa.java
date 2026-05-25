package lospolimorficos.boletopolis.models;

/**
 * Representa la cuenta bancaria de la empresa.
 * Implementa el patrón Singleton para asegurar que solo exista una instancia de la cuenta de la empresa.
 */
public final class CuentaEmpresa {

    private static final String NUM_CUENTA = "123456789";

    private double saldo;
    private static CuentaEmpresa instancia;

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private CuentaEmpresa(){}

    /**
     * Obtiene la única instancia de {@code CuentaEmpresa}.
     * Si la instancia no ha sido creada, la inicializa.
     *
     * @return La instancia de {@code CuentaEmpresa}.
     */
    public static CuentaEmpresa getInstancia(){
        if(instancia == null){
            instancia = new CuentaEmpresa();
        }
        return instancia;
    }

    /**
     * Recibe un pago, incrementando el saldo de la cuenta de la empresa.
     *
     * @param monto El monto a recibir.
     */
    public void recibirPago(double monto){
        saldo += monto;
    }

    /**
     * Obtiene el saldo actual de la cuenta de la empresa.
     *
     * @return El saldo actual.
     */
    public double getSaldo(){
        return saldo;
    }

    /**
     * Devuelve un pago, decrementando el saldo de la cuenta de la empresa.
     *
     * @param monto El monto a devolver.
     * @return {@code true} si la devolución fue exitosa (saldo suficiente), {@code false} en caso contrario.
     */
    public boolean devolverPago(double monto){

        if(saldo >= monto){
            saldo -= monto;
            return true;
        }

        return false;
    }

    /**
     * Obtiene el número de cuenta de la empresa.
     *
     * @return El número de cuenta.
     */
    public String getNumCuenta(){
        return NUM_CUENTA;
    }

}
