package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;

public class ServicioPago {

    private final CuentaEmpresa cuentaEmpresa;

    public ServicioPago(){
        this.cuentaEmpresa = CuentaEmpresa.getInstancia();
    }

    public boolean procesarPago(Pago pago){
        boolean aprobado = pago.getMetodoPago().pagar(pago.getMonto());

        if(aprobado){
            cuentaEmpresa.recibirPago(pago.getMonto());
            pago.setEstadoPago(EstadoPago.APROBADO);
            pago.getCompra().setEstadoCompra(EstadoCompra.PAGADA);
            return true;
        }
        pago.setEstadoPago(EstadoPago.RECHAZADO);
        return false;
    }

    public boolean reembolsarPago(Pago pago){
        Evento evento = pago.getCompra().getEvento();
        if(!evento.permiteReembolso()){
            return false;
        }
        if(pago.getEstadoPago() != EstadoPago.APROBADO){
            return false;
        }
        boolean empresaTieneFondos = cuentaEmpresa.devolverPago(pago.getMonto());

        if(!empresaTieneFondos){
            return false;
        }

        pago.getMetodoPago().reembolsar(pago.getMonto());
        pago.setEstadoPago(EstadoPago.REEMBOLSADO);
        pago.getCompra().setEstadoCompra(EstadoCompra.REEMBOLSADA);
        return true;
    }
}
