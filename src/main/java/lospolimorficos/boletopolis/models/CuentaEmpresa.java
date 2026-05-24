package lospolimorficos.boletopolis.models;

public final class CuentaEmpresa {

    private static final String NUM_CUENTA = "123456789";

    private double saldo;
    private static CuentaEmpresa instancia;

    private CuentaEmpresa(){}

    public static CuentaEmpresa getInstancia(){
        if(instancia == null){
            instancia = new CuentaEmpresa();
        }
        return instancia;
    }

    public void recibirPago(double monto){
        saldo += monto;
    }

    public double getSaldo(){
        return saldo;
    }

    public boolean devolverPago(double monto){

        if(saldo >= monto){
            saldo -= monto;
            return true;
        }

        return false;
    }

    public String getNumCuenta(){
        return NUM_CUENTA;
    }

}
