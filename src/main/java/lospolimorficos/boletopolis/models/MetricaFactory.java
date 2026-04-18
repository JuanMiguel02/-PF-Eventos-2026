package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.CompraRepositorio;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;

public class MetricaFactory {

    public static EstrategiaMetrica crearMetrica(TipoSeccionReporte tipo, CompraRepositorio compraRepositorio, EventoRepositorio eventoRepositorio){
        switch (tipo){
            case TOP_EVENTOS -> {
                return new MetricaTopEventos(eventoRepositorio);
            }
            default -> {
                return null;
            }
        }
    }

}
