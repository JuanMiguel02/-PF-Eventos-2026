package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;

/**
 * Servicio que gestiona la lógica de procesamiento de pagos y reembolsos.
 * Interactúa con los {@link MetodoPago} de los clientes y la {@link CuentaEmpresa}.
 */
public class ServicioPago {

    private final CuentaEmpresa cuentaEmpresa;

    /**
     * Constructor para {@code ServicioPago}.
     * Obtiene la instancia única de {@link CuentaEmpresa}.
     */
    public ServicioPago(){
        this.cuentaEmpresa = CuentaEmpresa.getInstancia();
    }

    /**
     * Procesa un pago. Intenta realizar el cargo al método de pago del cliente.
     * Si es exitoso, la empresa recibe el monto y el estado del pago y la compra se actualizan.
     *
     * @param pago El objeto {@link Pago} a procesar.
     * @return {@code true} si el pago fue aprobado, {@code false} si fue rechazado.
     */
    public boolean procesarPago(Pago pago){
        // Paso 1: Intentar realizar el cargo al método de pago del cliente.
        boolean aprobado = pago.getMetodoPago().pagar(pago.getMonto());

        // Paso 2: Si el pago fue aprobado por el método de pago del cliente.
        if(aprobado){
            // Paso 2.1: La cuenta de la empresa recibe el monto del pago.
            cuentaEmpresa.recibirPago(pago.getMonto());
            // Paso 2.2: Se actualiza el estado del pago a APROBADO.
            pago.setEstadoPago(EstadoPago.APROBADO);
            // Paso 2.3: Se actualiza el estado de la compra asociada a PAGADA.
            pago.getCompra().setEstadoCompra(EstadoCompra.PAGADA);
            // Paso 2.4: Se devuelve true indicando el éxito del procesamiento.
            return true;
        }
        // Paso 3: Si el pago no fue aprobado, se actualiza el estado del pago a RECHAZADO.
        pago.setEstadoPago(EstadoPago.RECHAZADO);
        // Paso 4: Se devuelve false indicando que el procesamiento falló.
        return false;
    }

    /**
     * Procesa el reembolso de un pago.
     * Verifica si el evento permite reembolso y si el pago original fue aprobado.
     * Si la empresa tiene fondos suficientes, devuelve el monto al cliente y actualiza los estados.
     *
     * @param pago El objeto {@link Pago} a reembolsar.
     * @return {@code true} si el reembolso fue exitoso, {@code false} en caso contrario.
     */
    public boolean reembolsarPago(Pago pago){
        // Paso 1: Obtener el evento asociado a la compra del pago.
        Evento evento = pago.getCompra().getEvento();
        // Paso 2: Verificar si el evento permite reembolsos. Si no, el reembolso no es posible.
        if(!evento.permiteReembolso()){
            return false;
        }
        // Paso 3: Verificar si el pago original fue APROBADO. Solo se pueden reembolsar pagos aprobados.
        if(pago.getEstadoPago() != EstadoPago.APROBADO){
            return false;
        }
        // Paso 4: Intentar que la cuenta de la empresa devuelva el monto. Esto verifica si hay fondos suficientes.
        boolean empresaTieneFondos = cuentaEmpresa.devolverPago(pago.getMonto());

        // Paso 5: Si la empresa no tiene fondos suficientes para el reembolso, el proceso falla.
        if(!empresaTieneFondos){
            return false;
        }

        // Paso 6: Si la empresa tiene fondos, el método de pago del cliente recibe el reembolso.
        pago.getMetodoPago().reembolsar(pago.getMonto());
        // Paso 7: Se actualiza el estado del pago a REEMBOLSADO.
        pago.setEstadoPago(EstadoPago.REEMBOLSADO);
        // Paso 8: Se actualiza el estado de la compra asociada a REEMBOLSADA.
        pago.getCompra().setEstadoCompra(EstadoCompra.REEMBOLSADA);
        // Paso 9: Se devuelve true indicando el éxito del reembolso.
        return true;
    }
}
