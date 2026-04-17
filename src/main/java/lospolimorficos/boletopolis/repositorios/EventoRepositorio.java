package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Evento;
import lospolimorficos.boletopolis.models.Recinto;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public boolean existeConflicto(Recinto recinto, LocalDateTime fechaYHora, Duration duracion) {
        LocalDateTime finNuevo = fechaYHora.plus(duracion);

        for (Evento evento : getEventos()) {
            if (evento.getRecinto().getIdRecinto().equals(recinto.getIdRecinto())) {
                LocalDateTime inicioExistente = evento.getFechaYHora();
                LocalDateTime finExistente = evento.getFechaYHora().plus(evento.getDuracion());

                // Se solapan si (inicio1 < fin2) Y (inicio2 < fin1)
                if (fechaYHora.isBefore(finExistente) && inicioExistente.isBefore(finNuevo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int contarEventos(){
        return eventos.size();
    }
}
