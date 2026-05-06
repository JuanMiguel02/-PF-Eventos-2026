package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.services.ServicioInteraccionAsientos;

public class AdminRecintoInteraccionStrategy implements InteraccionStrategy {

    @Override
    public void onClick(Asiento asiento, ServicioInteraccionAsientos servicio) {
        EstadoAsiento actual = asiento.getEstado();
        EstadoAsiento nuevo = (actual == EstadoAsiento.BLOQUEADO) ? EstadoAsiento.DISPONIBLE : EstadoAsiento.BLOQUEADO;
        asiento.setEstado(nuevo);
        servicio.notifyAsientoChanged();
    }

    @Override
    public boolean esInteractuable(Asiento asiento) {
        return true;
    }
}
