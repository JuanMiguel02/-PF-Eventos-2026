package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.services.ServicioInteraccionAsientos;

public interface InteraccionStrategy {

    void onClick(Asiento asiento, ServicioInteraccionAsientos servicio);
    boolean esInteractuable(Asiento asiento);

}
