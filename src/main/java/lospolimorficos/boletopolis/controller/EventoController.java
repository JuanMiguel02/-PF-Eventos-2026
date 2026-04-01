package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Evento;
import lospolimorficos.boletopolis.models.Recinto;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public boolean existeConflicto(Recinto recinto, LocalDateTime fechaYHora, Duration duracion) {
        LocalDateTime inicioNuevo = fechaYHora;
        LocalDateTime finNuevo = fechaYHora.plus(duracion);

        for (Evento evento : getEventos()) {
            if (evento.getRecinto().getIdRecinto().equals(recinto.getIdRecinto())) {
                LocalDateTime inicioExistente = evento.getFechaYHora();
                LocalDateTime finExistente = evento.getFechaYHora().plus(evento.getDuracion());

                // Se solapan si (inicio1 < fin2) Y (inicio2 < fin1)
                if (inicioNuevo.isBefore(finExistente) && inicioExistente.isBefore(finNuevo)) {
                    return true;
                }
            }
        }
        return false;
    }
}
