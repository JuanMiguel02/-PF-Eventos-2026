package lospolimorficos.boletopolis.models;

public final class CuentaEmpresa {
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

}
