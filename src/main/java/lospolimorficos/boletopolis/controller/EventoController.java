package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Evento;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;

public class EventoController {
    private final EventoRepositorio eventoRepositorio = EventoRepositorio.getInstance();

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
}
