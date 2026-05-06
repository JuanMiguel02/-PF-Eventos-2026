package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class EventoController {

    private final EventoRepositorio eventoRepositorio = EventoRepositorio.getInstance();

    public Evento crearEvento(String tipo, Map<String, String> especificos, String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, EstadoEvento estado, Recinto recinto, Duration duracion){

        EventoFactory fabrica;

        switch (tipo){
            case "ObraTeatro" -> fabrica = new ObraTeatroFactory(
                    especificos.get("compania"),
                    especificos.get("director"),
                    Integer.parseInt(especificos.get("numActos"))
            );
            case "Conferencia" -> fabrica = new ConferenciaFactory(
                    especificos.get("ponente"),
                    especificos.get("tema"),
                    especificos.get("institucion")
            );
            case "Concierto" -> fabrica = new ConciertoFactory(
                    especificos.get("artista"),
                    especificos.get("generoMusical")
            );
            default -> throw new IllegalArgumentException("Tipo no válido");
        }
        return fabrica.crearEvento(nombre, descripcion, ciudad, fechaYHora, recinto, duracion);
    }


    public boolean registrarEvento(Evento evento) {
        return eventoRepositorio.registrarEvento(evento);
    }

    public boolean eliminarEvento(Evento evento) {
        return eventoRepositorio.eliminarEvento(evento);
    }

    public boolean actualizarEvento(Evento eventoActualizado) {
        return eventoRepositorio.actualizarEvento(eventoActualizado);
    }

    public ObservableList<Evento> getEventos() {
        return eventoRepositorio.getEventos();
    }

    public boolean existeConflicto(Recinto recinto, LocalDateTime fechaYHora, Duration duracion) {
        return eventoRepositorio.existeConflicto(recinto, fechaYHora, duracion);
    }
}
