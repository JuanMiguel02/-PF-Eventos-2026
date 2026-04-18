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
    public void generarSeccion(ConstructorReporte constructor) {
        constructor.agregarSubtitulo("Top 5 Eventos por ocupación");

        List<MetricaEvento> topEventos = eventoRepositorio.obtenerTopEventos(5);

        if(topEventos.isEmpty()){
            constructor.agregarTexto("No hay eventos disponibles para mostrar.");
            return;
        }

        Map<String, Number> datos = new LinkedHashMap<>();
        List<String[]> tabla = new ArrayList<>();
        List<String> columnas = List.of("Nombre", "Ocupación", "Ganancia");

        for(MetricaEvento evento : topEventos){
            datos.put(evento.nombre(), evento.ocupacion());
            tabla.add(new String[]{evento.nombre(), String.format("%.2f", evento.ocupacion()), String.format("%.2f", evento.ganancia())});
        }
        constructor.agregarGrafico("Top 5 Eventos por ocupación", datos);
        constructor.agregarTabla(tabla, columnas);

    }
}
