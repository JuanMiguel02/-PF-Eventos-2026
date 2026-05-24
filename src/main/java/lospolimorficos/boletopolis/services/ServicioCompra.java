package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServicioCompra {

    private final ServicioPago servicioPago;

    public ServicioCompra() {
        this.servicioPago = new ServicioPago();
    }

    public boolean reembolsarCompra(Compra compra) {

        if (compra == null || compra.getPago() == null) {
            return false;
        }

        boolean exito = servicioPago.reembolsarPago(compra.getPago());

        if (exito) {

            compra.setEstadoCompra(EstadoCompra.REEMBOLSADA);

            // Liberar asientos
            for (Entrada entrada : compra.getEntradas()) {

                entrada.setEstado(EstadoEntrada.CANCELADA);

                if (entrada.getAsiento() != null) {
                    entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
                }
            }
        }

        return exito;
    }

    public Compra realizarCompra(Cliente cliente, Evento evento, List<Asiento> asientos, Map<Asiento, Zona> zonaAsientoMap, MetodoPago metodoPago) {

        List<Entrada> entradas = new ArrayList<>();
        double total = 0;

        // Crear entradas y calcular total
        for (Asiento asiento : asientos) {

            Zona zona = zonaAsientoMap.get(asiento);

            total += zona.getPrecioZona();

            Entrada entrada = new Entrada(
                    zona,
                    asiento,
                    zona.getPrecioZona(),
                    EstadoEntrada.ACTIVA
            );

            entradas.add(entrada);
        }

        // Crear compra
        Compra compra = new Compra(cliente, evento);
        compra.setEntradas(entradas);
        compra.setEstadoCompra(EstadoCompra.CREADA);

        // Crear pago
        Pago pago = new Pago(compra, metodoPago, total);

        // Procesar pago usando el servicio
        boolean pagoExitoso = servicioPago.procesarPago(pago);

        if (!pagoExitoso) {

            compra.setEstadoCompra(EstadoCompra.CANCELADA);

            return null;
        }

        // Asociar pago
        compra.setPago(pago);

        // Cambiar estado compra
        compra.setEstadoCompra(EstadoCompra.PAGADA);

        // Marcar asientos vendidos
        for (Asiento asiento : asientos) {
            asiento.setEstado(EstadoAsiento.VENDIDO);
        }

        // Asociar compra al cliente
        cliente.agregarCompra(compra);

        return compra;
    }
}