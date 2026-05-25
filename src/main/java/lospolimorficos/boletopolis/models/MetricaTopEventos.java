package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.EventoRepositorio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación de la estrategia de métrica para generar un reporte de los eventos con mayor ocupación.
 * Esta clase se encarga de obtener los eventos principales por ocupación y formatearlos
 * para su presentación en un reporte, incluyendo un gráfico y una tabla.
 */
public class MetricaTopEventos implements EstrategiaMetrica {

    private final EventoRepositorio eventoRepositorio;

    /**
     * Constructor de MetricaTopEventos.
     *
     * @param eventoRepositorio El repositorio de eventos utilizado para obtener los datos de los eventos.
     */
    public MetricaTopEventos(EventoRepositorio eventoRepositorio) {
        this.eventoRepositorio = eventoRepositorio;
    }


    /**
     * Genera la sección del reporte correspondiente a los eventos con mayor ocupación.
     * Este método añade un subtítulo, un gráfico y una tabla al constructor del reporte.
     *
     * @param constructor El constructor de reporte al que se añadirán los elementos.
     */
    @Override
    public void generarSeccion(ConstructorReporte constructor) {
        // Paso 1: Agregar un subtítulo descriptivo a la sección del reporte.
        constructor.agregarSubtitulo("Top 5 Eventos por ocupación");

        // Paso 2: Obtener la lista de los 5 eventos con mayor ocupación desde el repositorio.
        List<MetricaEvento> topEventos = eventoRepositorio.obtenerTopEventos(5);

        // Paso 3: Verificar si no hay eventos disponibles. Si la lista está vacía,
        // se añade un mensaje indicando que no hay eventos y se finaliza la ejecución del método.
        if(topEventos.isEmpty()){
            constructor.agregarTexto("No hay eventos disponibles para mostrar.");
            return;
        }

        // Paso 4: Inicializar estructuras de datos para el gráfico y la tabla.
        // 'datos' almacenará la información para el gráfico (nombre del evento y ocupación).
        // 'tabla' almacenará las filas de la tabla (nombre, ocupación y ganancia).
        // 'columnas' define los encabezados de la tabla.
        Map<String, Number> datos = new LinkedHashMap<>();
        List<String[]> tabla = new ArrayList<>();
        List<String> columnas = List.of("Nombre", "Ocupación (%)", "Ganancia");

        // Paso 5: Iterar sobre cada evento en la lista de los top eventos.
        for(MetricaEvento evento : topEventos){
            // Paso 5.1: Redondear la ocupación del evento a dos decimales para una mejor presentación.
            double eventoRedondeado = Math.round(evento.ocupacion() * 100.0) / 100.0;

            // Paso 5.2: Añadir los datos del evento al mapa 'datos' para el gráfico.
            datos.put(evento.nombre(), eventoRedondeado);
            // Paso 5.3: Añadir una nueva fila a la lista 'tabla' con el nombre, ocupación formateada y ganancia formateada.
            tabla.add(new String[]{evento.nombre(), String.format("%.2f", evento.ocupacion()), String.format("%.2f", evento.ganancia())});
        }
        // Paso 6: Agregar el gráfico al constructor del reporte utilizando los datos recopilados.
        constructor.agregarGrafico("Top 5 Eventos por ocupación", datos);
        // Paso 7: Agregar la tabla al constructor del reporte utilizando los datos y las columnas definidas.
        constructor.agregarTabla(tabla, columnas);

    }
}
