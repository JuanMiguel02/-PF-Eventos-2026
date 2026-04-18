package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.EventoRepositorio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetricaTopEventos implements EstrategiaMetrica {

    private final EventoRepositorio eventoRepositorio;

    public MetricaTopEventos(EventoRepositorio eventoRepositorio) {
        this.eventoRepositorio = eventoRepositorio;
    }

    @Override
    public ResultadoMetrica calcularMetrica() {

        List<MetricaEvento> topEventos = eventoRepositorio.obtenerTopEventos(5);
        Map<String, Number> datosGrafico = new LinkedHashMap<>();
        List<String[]> datosTabla = new ArrayList<>();

        datosTabla.add(new String[]{"Evento", "Ocupación (%)", "Ganancia ($)"});

        for(MetricaEvento evento : topEventos){
            datosGrafico.put(evento.nombre(), evento.ocupacion());

            datosTabla.add(new String[]{
                    evento.nombre(),
                    String.format("%.2f", evento.ocupacion()),
                    String.format("%.2f", evento.ganancia())
            });
        }

        return new ResultadoMetrica("Top 5 eventos por ocupación", datosGrafico, datosTabla);
    }
}
