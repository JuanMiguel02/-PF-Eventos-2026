package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.services.ServicioInteraccionAsientos;

public class AdminEventoInteraccionStrategy implements InteraccionStrategy {

    @Override
    public void onClick(Asiento asiento, ServicioInteraccionAsientos servicio) {
        EstadoAsiento actual = asiento.getEstado();
        EstadoAsiento[] estados = EstadoAsiento.values();
        int siguienteIndex = (actual.ordinal() + 1) % estados.length;
        EstadoAsiento nuevo = estados[siguienteIndex];
        asiento.setEstado(nuevo);
        servicio.notifyAsientoChanged();
    }

    @Override
    public boolean esInteractuable(Asiento asiento) {
        return true;
    }
}
