package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Evento;

public final class EventoRepositorio {

    private final ObservableList<Evento> eventos = FXCollections.observableArrayList();
    private static EventoRepositorio instancia;

    private EventoRepositorio() {}

    public static EventoRepositorio getInstance() {
        if (instancia == null) {
            instancia = new EventoRepositorio();
        }
        return instancia;
    }
    public boolean registrarEvento(Evento evento) {
        return eventos.add(evento);
    }

    public ObservableList<Evento> getEventos() {
        return eventos;
    }
    public boolean eliminarEvento(Evento evento) {
        return eventos.remove(evento);
    }

    public boolean actualizarEvento(Evento eventoActualizado) {
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getIdEvento().equals(eventoActualizado.getIdEvento())) {
                eventos.set(i, eventoActualizado);
                return true;
            }
        }
        return false;
    }
}
