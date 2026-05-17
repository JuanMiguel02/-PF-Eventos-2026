package lospolimorficos.boletopolis.models;

public interface EventoObserver {
    void actualizarEvento(Evento evento, EstadoEvento nuevoEstado);
}
