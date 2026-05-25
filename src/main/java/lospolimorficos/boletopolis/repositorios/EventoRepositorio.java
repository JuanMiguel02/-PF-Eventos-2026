package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Repositorio para la gestión de objetos {@link Evento}.
 * Implementa el patrón Singleton para asegurar una única instancia global.
 * Proporciona métodos para almacenar, recuperar, actualizar y eliminar eventos,
 * así como para obtener métricas y verificar conflictos de horario.
 */
public final class EventoRepositorio {

    private final ObservableList<Evento> eventos = FXCollections.observableArrayList();
    private final CompraRepositorio compras;
    private static EventoRepositorio instancia;

    /**
     * Constructor privado para implementar el patrón Singleton.
     * Inicializa el repositorio de compras y carga datos de ejemplo.
     */
    private EventoRepositorio() {
        this.compras = CompraRepositorio.getInstancia();
        cargarDatosEjemplo();
    }

    /**
     * Obtiene la única instancia de {@code EventoRepositorio}.
     * Si la instancia no ha sido creada, la inicializa y también carga los datos de ejemplo del {@link CompraRepositorio}.
     *
     * @return La instancia de {@code EventoRepositorio}.
     */
    public static EventoRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new EventoRepositorio();
            // Cargar datos de ejemplo de compras después de inicializar EventoRepositorio
            // para asegurar que los eventos ya existan.
            CompraRepositorio.getInstancia().cargarDatosEjemplo();
        }
        return instancia;
    }

    /**
     * Registra un nuevo evento en el repositorio.
     *
     * @param evento El objeto {@link Evento} a registrar.
     * @return {@code true} si el evento fue añadido exitosamente, {@code false} en caso contrario.
     */
    public boolean registrarEvento(Evento evento) {
        return eventos.add(evento);
    }

    /**
     * Obtiene la lista observable de todos los eventos registrados.
     *
     * @return Una {@link ObservableList} de objetos {@link Evento}.
     */
    public ObservableList<Evento> getEventos() {
        return eventos;
    }

    /**
     * Elimina un evento del repositorio.
     *
     * @param evento El objeto {@link Evento} a eliminar.
     * @return {@code true} si el evento fue eliminado exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarEvento(Evento evento) {
        return eventos.remove(evento);
    }

    /**
     * Actualiza un evento existente en el repositorio.
     * Busca el evento por su ID y lo reemplaza con la versión actualizada.
     *
     * @param eventoActualizado El objeto {@link Evento} con la información actualizada.
     * @return {@code true} si el evento fue actualizado exitosamente, {@code false} si el evento no se encontró.
     */
    public boolean actualizarEvento(Evento eventoActualizado) {
        // Paso 1: Iterar sobre la lista de eventos para encontrar el evento a actualizar.
        for (int i = 0; i < eventos.size(); i++) {
            // Paso 1.1: Comparar el ID del evento actual con el ID del evento actualizado.
            if (eventos.get(i).getIdEvento().equals(eventoActualizado.getIdEvento())) {
                // Paso 1.2: Si los IDs coinciden, reemplazar el evento existente con la versión actualizada.
                eventos.set(i, eventoActualizado);
                // Paso 1.3: Devolver true indicando que la actualización fue exitosa.
                return true;
            }
        }
        // Paso 2: Si el bucle termina y el evento no fue encontrado, devolver false.
        return false;
    }

    /**
     * Verifica si existe un conflicto de horario para un recinto y una fecha/hora específicas.
     * Un conflicto ocurre si ya existe un evento en el mismo recinto a la misma fecha y hora.
     *
     * @param recinto El {@link Recinto} a verificar.
     * @param fechaYHora La {@link LocalDateTime} a verificar.
     * @return {@code true} si existe un conflicto, {@code false} en caso contrario.
     */
    public boolean existeConflicto(Recinto recinto, LocalDateTime fechaYHora) {
        return eventos.stream()
                .anyMatch(evento ->
                        evento.getRecinto()
                                .getIdRecinto()
                                .equals(recinto.getIdRecinto()) && // Comprobar si el ID del recinto coincide.
                                evento.getFechaYHora().equals(fechaYHora)); // Comprobar si la fecha y hora coinciden.
    }

    /**
     * Obtiene una lista de los eventos principales (top eventos) basándose en su ocupación.
     *
     * @param limite El número máximo de eventos a devolver.
     * @return Una lista de objetos {@link MetricaEvento} ordenados por ocupación de forma descendente.
     */
    public List<MetricaEvento> obtenerTopEventos(int limite){
        return eventos.stream()
                .map(evento -> {
                    // Paso 1: Calcular la capacidad total del evento.
                    int capacidad = evento.getCapacidad();
                    // Paso 2: Obtener el número de entradas vendidas para el evento desde el repositorio de compras.
                    int vendidos = compras.obtenerVentasEvento(evento);

                    // Paso 3: Calcular el porcentaje de ocupación. Si la capacidad es 0, la ocupación es 0.
                    double ocupacion = capacidad == 0 ? 0 : (double) vendidos / capacidad * 100;
                    // Paso 4: Calcular la ganancia total del evento desde el repositorio de compras.
                    double ganancia = compras.calcularGananciaPorEvento(evento);

                    // Paso 5: Crear y devolver un objeto MetricaEvento con los datos calculados.
                    return new MetricaEvento(evento.getNombre(), ocupacion, ganancia);
                })
                .sorted(Comparator.comparingDouble(MetricaEvento::ocupacion).reversed()) // Ordenar los eventos por ocupación de forma descendente.
                .limit(limite) // Limitar la cantidad de eventos al número especificado.
                .toList(); // Recolectar los resultados en una lista.
    }

    /**
     * Cuenta el número total de eventos registrados en el repositorio.
     *
     * @return El número total de eventos.
     */
    public int contarEventos(){
        return eventos.size();
    }

    /**
     * Carga datos de ejemplo en el repositorio de eventos.
     * Crea dos conciertos de ejemplo y los registra.
     */
    private void cargarDatosEjemplo(){
        // Paso 1: Obtener instancias de recintos de ejemplo desde el RecintoRepositorio.
        Recinto recinto = RecintoRepositorio.getInstancia().getPrimerRecinto();
        Recinto recinto2 = RecintoRepositorio.getInstancia().getRecintos().get(1);
        try {
            // Paso 2: Crear una copia del primer recinto para el primer concierto.
            Recinto recintoCopia = recinto.copiar();
            // Paso 3: Crear el primer concierto de ejemplo.
            Concierto concierto = new Concierto("Concierto de los Deftones", "Gira Aniversario - 2026", Ciudad.ARMENIA, LocalDateTime.now(), recintoCopia,"Deftones", "Metal Alternativo");
            // Paso 4: Establecer el estado del concierto como PUBLICADO.
            concierto.setEstado(EstadoEvento.PUBLICADO);
            // Paso 5: Establecer la ruta de la imagen del concierto.
            concierto.setRutaImagen("/lospolimorficos/boletopolis/imagenes/DeftonesConciertoEjemplo.jpg");
            // Paso 6: Registrar el primer concierto.
            registrarEvento(concierto);

            // Paso 7: Crear una copia del segundo recinto para el segundo concierto.
            Recinto recintoCopia2 = recinto2.copiar();
            // Paso 8: Crear el segundo concierto de ejemplo.
            Concierto concierto2 = new Concierto("Concierto de TheStrokes", "Gira Nuevo Álbum - 2026", Ciudad.PEREIRA, LocalDateTime.now(), recintoCopia2,"The Strokes", "Rock Alternativo");
            // Paso 9: Establecer el estado del concierto como PUBLICADO.
            concierto2.setEstado(EstadoEvento.PUBLICADO);
            // Paso 10: Establecer la ruta de la imagen del concierto.
            concierto2.setRutaImagen("/lospolimorficos/boletopolis/imagenes/StrokesConciertoEjemplo.jpg");
            // Paso 11: Permitir reembolsos para este concierto.
            concierto2.setPermiteReembolso(true);
            // Paso 12: Registrar el segundo concierto.
            registrarEvento(concierto2);

        } catch (CloneNotSupportedException e) {
            // Manejar la excepción si la clonación no es soportada.
            throw new RuntimeException(e);
        }
    }
}
