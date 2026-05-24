package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public final class EventoRepositorio {

    private final ObservableList<Evento> eventos = FXCollections.observableArrayList();
    private final CompraRepositorio compras;
    private static EventoRepositorio instancia;

    private EventoRepositorio() {
        this.compras = CompraRepositorio.getInstancia();
        cargarDatosEjemplo();
    }

    public static EventoRepositorio getInstancia() {
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

    public boolean existeConflicto(Recinto recinto, LocalDateTime fechaYHora) {
        return eventos.stream()
                .anyMatch(evento ->
                        evento.getRecinto()
                                .getIdRecinto()
                                .equals(recinto.getIdRecinto()) &&
                                evento.getFechaYHora().equals(fechaYHora));

    }

    public List<MetricaEvento> obtenerTopEventos(int limite){

        return eventos.stream()
                .map(evento -> {

                    int capacidad = evento.getCapacidad();
                    int vendidos = compras.obtenerVentasEvento(evento); //esto viene del repositorio de compras

                    double ocupacion = capacidad == 0 ? 0 : (double) vendidos / capacidad * 100;
                    double ganancia = compras.calcularGananciaPorEvento(evento); //viene de los ingresos del repositorio de compras

                    return new MetricaEvento(evento.getNombre(), ocupacion, ganancia);
                }).sorted(Comparator.comparingDouble(MetricaEvento::ocupacion).reversed())
                .limit(limite)
                .toList();

    }

    public int contarEventos(){
        return eventos.size();
    }

    private void cargarDatosEjemplo(){
        Recinto recinto = RecintoRepositorio.getInstancia().getPrimerRecinto();
        try {
            Recinto recintoCopia = recinto.copiar();
            Concierto concierto = new Concierto("Concierto de los Deftones", "Gira Nuevo Álbum - 2026", Ciudad.ARMENIA, LocalDateTime.now(), recintoCopia,"Deftones", "Rock Alternativo");
            concierto.setEstado(EstadoEvento.PUBLICADO);
            concierto.setRutaImagen("/lospolimorficos/boletopolis/imagenes/DeftonesConciertoEjemplo.jpg");
            registrarEvento(concierto);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
