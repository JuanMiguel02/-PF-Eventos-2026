package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Evento;

public final class EventoRepositorio {

    private ObservableList<Evento> eventos = FXCollections.observableArrayList();
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
}
