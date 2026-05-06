package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.services.ServicioInteraccionAsientos;

public class CompraInteraccionStrategy implements InteraccionStrategy {
    @Override
    public void onClick(Asiento asiento, ServicioInteraccionAsientos servicio) {
        if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {
            servicio.toggleSeleccionCompra(asiento);
            servicio.notifyAsientoChanged();
        }
    }

    @Override
    public boolean esInteractuable(Asiento asiento) {
        return asiento.getEstado() == EstadoAsiento.DISPONIBLE;
    }
}
