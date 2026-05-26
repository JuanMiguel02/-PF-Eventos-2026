package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de eventos en el sistema Boletopolis.
 * Proporciona métodos para crear, filtrar, registrar, eliminar y actualizar eventos.
 * Interactúa con {@link EventoRepositorio} y utiliza el patrón Factory para la creación de eventos.
 */
public class EventoController {

    private final EventoRepositorio eventoRepositorio = EventoRepositorio.getInstancia();

    /**
     * Crea un nuevo evento basándose en el tipo especificado y sus atributos.
     * Utiliza el patrón Factory para instanciar el tipo de evento correcto.
     *
     * @param tipo El tipo de evento a crear (e.g., "ObraTeatro", "Conferencia", "Concierto").
     * @param especificos Un mapa de String a String que contiene atributos específicos del tipo de evento.
     * @param nombre El nombre del evento.
     * @param descripcion La descripción del evento.
     * @param ciudad La ciudad donde se realizará el evento.
     * @param fechaYHora La fecha y hora del evento.
     * @param recinto El recinto donde se realizará el evento.
     * @return Una instancia del {@link Evento} creado.
     * @throws IllegalArgumentException Si el tipo de evento no es válido.
     */
    public Evento crearEvento(String tipo, Map<String, String> especificos, String nombre, String descripcion, Ciudad ciudad, LocalDateTime fechaYHora, Recinto recinto){
        // Paso 1: Declarar una variable de tipo EventoFactory.
        EventoFactory fabrica;

        // Paso 2: Utilizar un switch para determinar qué fábrica de eventos instanciar según el 'tipo' proporcionado.
        switch (tipo){
            case "ObraTeatro" -> fabrica = new ObraTeatroFactory(
                    especificos.get("compania"), // Obtener la compañía de los atributos específicos.
                    especificos.get("director"), // Obtener el director de los atributos específicos.
                    Integer.parseInt(especificos.get("numActos")) // Convertir el número de actos a entero.
            );
            case "Conferencia" -> fabrica = new ConferenciaFactory(
                    especificos.get("ponente"), // Obtener el ponente de los atributos específicos.
                    especificos.get("tema"), // Obtener el tema de los atributos específicos.
                    especificos.get("institucion") // Obtener la institución de los atributos específicos.
            );
            case "Concierto" -> fabrica = new ConciertoFactory(
                    especificos.get("artista"), // Obtener el artista de los atributos específicos.
                    especificos.get("generoMusical") // Obtener el género musical de los atributos específicos.
            );
            default -> throw new IllegalArgumentException("Tipo no válido"); // Lanzar excepción si el tipo no es reconocido.
        }
        // Paso 3: Utilizar la fábrica creada para construir y devolver el objeto Evento.
        return fabrica.crearEvento(nombre, descripcion, ciudad, fechaYHora, recinto);
    }

    /**
     * Filtra una lista de eventos basándose en un criterio de búsqueda.
     * El filtro se aplica al nombre del evento, ciudad, nombre del recinto y estado del evento.
     *
     * @param eventos La lista original de eventos a filtrar.
     * @param filtro El texto de búsqueda para filtrar los eventos.
     * @return Una nueva lista de eventos que coinciden con el filtro. Si el filtro es nulo o vacío,
     *         se devuelve la lista original.
     */
    public List<Evento> filtrarEventos(List<Evento> eventos, String filtro){
        // Paso 1: Verificar si el filtro es nulo o vacío. Si lo es, no se aplica ningún filtro y se devuelve la lista original.
        if(filtro == null || filtro.isEmpty()){
            return eventos;
        }
        // Paso 2: Convertir el filtro a minúsculas para realizar una búsqueda insensible a mayúsculas y minúsculas.
        String filtroLimpio = filtro.toLowerCase();
        // Paso 3: Filtrar la lista de eventos utilizando un stream.
        return eventos.stream()
                // Paso 3.1: Para cada evento, verificar si alguna de sus propiedades (nombre, ciudad,
                // nombre del recinto, estado) contiene el texto del filtro.
                .filter(evento -> evento.getNombre().toLowerCase().contains(filtroLimpio)
                        ||  evento.getCiudad().toString().toLowerCase().contains(filtroLimpio)
                        || evento.getRecinto().getNombre().toLowerCase().contains(filtroLimpio)
                        || evento.getEstado().toString().toLowerCase().contains(filtroLimpio))
                // Paso 3.2: Recolectar los eventos filtrados en una nueva lista.
                .toList();
    }

    public List<Evento> filtrarPorNombreYTipo(List<Evento> eventos, String filtroTexto, String tipoSubclase) {
        // Si no hay filtros activos, retornamos la lista completa de inmediato
        String textoLimpio = (filtroTexto == null) ? "" : filtroTexto.trim().toLowerCase();
        boolean filtrarTipo = (tipoSubclase != null && !tipoSubclase.equalsIgnoreCase("Todos"));

        return eventos.stream()
                .filter(evento -> {
                    // Filtro 1: El nombre debe contener la cadena de búsqueda
                    return evento.getNombre().toLowerCase().contains(textoLimpio);
                })
                .filter(evento -> {
                    // Filtro 2: Coincidencia por subclase (si no se seleccionó "Todos")
                    if (!filtrarTipo) {
                        return true;
                    }

                    // Obtenemos el nombre simple de la clase (ej: "Concierto", "ObraTeatro", "Conferencia")
                    String nombreClaseConcreta = evento.getClass().getSimpleName().toLowerCase();

                    // Evaluamos si el nombre de la clase contiene la palabra clave (ej: "teatro" calza en "obrateatro")
                    return nombreClaseConcreta.contains(tipoSubclase.toLowerCase());
                })
                .toList();
    }

    /**
     * Registra un nuevo evento en el sistema.
     *
     * @param evento El objeto {@link Evento} a registrar.
     * @return {@code true} si el evento fue registrado exitosamente, {@code false} en caso contrario.
     */
    public boolean registrarEvento(Evento evento) {
        return eventoRepositorio.registrarEvento(evento);
    }

    /**
     * Elimina un evento del sistema.
     *
     * @param evento El objeto {@link Evento} a eliminar.
     * @return {@code true} si el evento fue eliminado exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarEvento(Evento evento) {
        return eventoRepositorio.eliminarEvento(evento);
    }

    /**
     * Actualiza la información de un evento existente en el sistema.
     *
     * @param eventoActualizado El objeto {@link Evento} con la información actualizada.
     * @return {@code true} si el evento fue actualizado exitosamente, {@code false} en caso contrario.
     */
    public boolean actualizarEvento(Evento eventoActualizado) {
        return eventoRepositorio.actualizarEvento(eventoActualizado);
    }

    /**
     * Obtiene una lista observable de todos los eventos registrados en el sistema.
     *
     * @return Una {@link ObservableList} de objetos {@link Evento}.
     */
    public ObservableList<Evento> getEventos() {
        return eventoRepositorio.getEventos();
    }

    /**
     * Verifica si existe un conflicto de horario para un recinto y una fecha/hora específicas.
     *
     * @param recinto El {@link Recinto} a verificar.
     * @param fechaYHora La {@link LocalDateTime} a verificar.
     * @return {@code true} si existe un conflicto, {@code false} en caso contrario.
     */
    public boolean existeConflicto(Recinto recinto, LocalDateTime fechaYHora) {
        return eventoRepositorio.existeConflicto(recinto, fechaYHora);
    }
}
