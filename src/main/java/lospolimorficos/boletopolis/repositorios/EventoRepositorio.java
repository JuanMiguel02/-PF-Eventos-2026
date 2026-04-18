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

    public List<MetricaEvento> obtenerTopEventos(int limite){

        return eventos.stream()
                .map(evento -> {

                    int capacidad = evento.getCapacidad();
                    int vendidos = compras.obtenerVentasEvento(evento); //esto viene del repositorio de compras

                    double ocupacion = capacidad == 0 ? 0 : (double) vendidos / capacidad * 100;
                    double ganancia = compras.calcularGananciaPorEveneto(evento); //viene de los ingresos del repositorio de compras

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
        Concierto concierto = new Concierto("Concierto de los Deftones", "Gira Nuevo Álbum - 2026", Ciudad.ARMENIA, LocalDateTime.now(), EstadoEvento.PUBLICADO, recinto, Duration.ofHours(2), "Deftones", "Rock Alternativo");
        concierto.setRutaImagen("/lospolimorficos/boletopolis/imagenes/DeftonesConciertoEjemplo.jpg");
        registrarEvento(concierto);
    }
}
