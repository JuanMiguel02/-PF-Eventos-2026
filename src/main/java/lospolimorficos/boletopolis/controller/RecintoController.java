package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;

import java.util.List;

/**
 * Controlador para la gestión de recintos en el sistema Boletopolis.
 * Proporciona métodos para gestionar recintos, incluyendo la creación de zonas,
 * cálculo de capacidad, filtrado, registro, eliminación y actualización.
 * También se encarga de sincronizar los cambios en los recintos con los eventos asociados.
 * Interactúa con {@link RecintoRepositorio} y {@link EventoRepositorio}.
 */
public class RecintoController {

    private final RecintoRepositorio recintoRepositorio =
            RecintoRepositorio.getInstancia();

    private final EventoRepositorio eventoRepositorio =
            EventoRepositorio.getInstancia();

    /**
     * Genera un número de zona basado en el tipo de zona seleccionado y las plantillas existentes.
     * Este método es útil para asignar identificadores únicos o secuenciales a las zonas.
     *
     * @param plantillas Una lista de {@link PlantillaZona} que representan las configuraciones de zona.
     * @param tipoSeleccionado El {@link TipoZona} para el cual se desea generar el número.
     * @return Un valor long que representa el número de zonas del tipo seleccionado.
     */
    public long generarNumeroZona(List<PlantillaZona> plantillas,
                                  TipoZona tipoSeleccionado) {
        // Paso 1: Filtrar las plantillas de zona para contar cuántas son del tipo seleccionado.
        return plantillas.stream()
                .filter(z -> z.tipoZona() == tipoSeleccionado)
                .count(); // Devolver el conteo de zonas que coinciden con el tipo.
    }

    /**
     * Calcula la capacidad total de un recinto sumando las capacidades de todas sus zonas.
     *
     * @param plantillas Una lista de {@link PlantillaZona} que definen las zonas del recinto.
     * @return La capacidad total calculada del recinto.
     */
    public int calcularCapacidadTotal(List<PlantillaZona> plantillas) {
        // Paso 1: Inicializar la capacidad total a cero.
        int numero = 0;

        // Paso 2: Iterar sobre cada plantilla de zona.
        for (PlantillaZona plantilla : plantillas) {
            // Paso 2.1: Sumar la capacidad de la plantilla actual a la capacidad total.
            numero += plantilla.calcularCapacidad();
        }
        // Paso 3: Devolver la capacidad total.
        return numero;
    }

    /**
     * Filtra una lista de recintos basándose en un criterio de búsqueda.
     * El filtro se aplica al nombre del recinto, ID del recinto, dirección, ciudad y capacidad.
     *
     * @param recintos La lista original de recintos a filtrar.
     * @param filtro El texto de búsqueda para filtrar los recintos.
     * @return Una nueva lista de recintos que coinciden con el filtro. Si el filtro es nulo o vacío,
     *         se devuelve la lista original.
     */
    public List<Recinto> filtrarRecintos(List<Recinto> recintos,
                                         String filtro) {
        // Paso 1: Verificar si el filtro es nulo o vacío. Si lo es, no se aplica ningún filtro y se devuelve la lista original.
        if (filtro == null || filtro.isEmpty()) {
            return recintos;
        }

        // Paso 2: Convertir el filtro a minúsculas para realizar una búsqueda insensible a mayúsculas y minúsculas.
        String filtroLimpio = filtro.toLowerCase();

        // Paso 3: Filtrar la lista de recintos utilizando un stream.
        return recintos.stream()
                // Paso 3.1: Para cada recinto, verificar si alguna de sus propiedades (nombre, ID, dirección,
                // ciudad, capacidad) contiene el texto del filtro.
                .filter(recinto ->
                        recinto.getNombre().toLowerCase().contains(filtroLimpio)
                                || recinto.getIdRecinto().toString().contains(filtroLimpio)
                                || recinto.getDireccion().toLowerCase().contains(filtroLimpio)
                                || recinto.getCiudad().toString().toLowerCase().contains(filtroLimpio)
                                || String.valueOf(recinto.getCapacidad()).contains(filtroLimpio))
                // Paso 3.2: Recolectar los recintos filtrados en una nueva lista.
                .toList();
    }

    /**
     * Registra un nuevo recinto en el sistema.
     *
     * @param recinto El objeto {@link Recinto} a registrar.
     * @return {@code true} si el recinto fue registrado exitosamente, {@code false} en caso contrario.
     */
    public boolean registrarRecinto(Recinto recinto) {
        return recintoRepositorio.registrarRecinto(recinto);
    }

    /**
     * Elimina un recinto del sistema.
     *
     * @param recinto El objeto {@link Recinto} a eliminar.
     * @return {@code true} si el recinto fue eliminado exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarRecinto(Recinto recinto) {
        return recintoRepositorio.eliminarRecinto(recinto);
    }

    /**
     * Actualiza la información de un recinto existente en el sistema.
     * Después de la actualización, se sincronizan los cambios con los eventos que utilizan este recinto.
     *
     * @param recintoActualizado El objeto {@link Recinto} con la información actualizada.
     * @return {@code true} si el recinto fue actualizado exitosamente, {@code false} en caso contrario.
     */
    public boolean actualizarRecinto(Recinto recintoActualizado) {
        // Paso 1: Intentar actualizar el recinto en el repositorio.
        boolean actualizado = recintoRepositorio.actualizarRecinto(recintoActualizado);

        // Paso 2: Si el recinto fue actualizado exitosamente, sincronizar los cambios con los eventos.
        if (actualizado) {
            sincronizarEventos(recintoActualizado);
        }
        // Paso 3: Devolver el resultado de la actualización.
        return actualizado;
    }

    /**
     * Sincroniza los cambios estructurales de un recinto (zonas y asientos)
     * con los eventos que utilizan copias de este recinto.
     * Esto asegura que las modificaciones en el recinto base se reflejen en los eventos asociados.
     *
     * @param recintoActualizado El {@link Recinto} que ha sido actualizado y cuyos cambios deben sincronizarse.
     */
    private void sincronizarEventos(Recinto recintoActualizado) {
        // Paso 1: Iterar sobre todos los eventos registrados en el sistema.
        for (Evento evento : eventoRepositorio.getEventos()) {
            // Paso 1.1: Obtener el recinto asociado al evento actual.
            Recinto recintoEvento = evento.getRecinto();

            // Paso 1.2: Verificar si el evento utiliza el recinto que ha sido actualizado.
            // Si no es el mismo recinto, se salta al siguiente evento.
            if (!recintoEvento.getIdRecinto()
                    .equals(recintoActualizado.getIdRecinto())) {
                continue;
            }

            // Paso 1.3: Sincronizar las zonas del recinto del evento con las del recinto actualizado.
            for (Zona zonaEvento : recintoEvento.getZonas()) {
                // Paso 1.3.1: Buscar la zona correspondiente en el recinto actualizado por su ID.
                Zona zonaBase = recintoActualizado.getZonas()
                        .stream()
                        .filter(z -> z.getIdZona()
                                .equals(zonaEvento.getIdZona()))
                        .findFirst()
                        .orElse(null);

                // Paso 1.3.2: Si la zona no se encuentra en el recinto actualizado (posiblemente eliminada), se salta.
                if (zonaBase == null) {
                    continue;
                }

                // Paso 1.3.3: Sincronizar las propiedades de la zona (nombre y precio).
                zonaEvento.setNombre(zonaBase.getNombre());
                zonaEvento.setPrecioZona(zonaBase.getPrecioZona());

                // Paso 1.3.4: Sincronizar los asientos dentro de la zona.
                for (Asiento asientoEvento : zonaEvento.getAsientos()) {
                    // Paso 1.3.4.1: Buscar el asiento correspondiente en la zona base por su ID.
                    Asiento asientoBase = zonaBase.getAsientos()
                            .stream()
                            .filter(a -> a.getIdAsiento()
                                    .equals(asientoEvento.getIdAsiento()))
                            .findFirst()
                            .orElse(null);

                    // Paso 1.3.4.2: Si el asiento no se encuentra en la zona base, se salta.
                    if (asientoBase == null) {
                        continue;
                    }

                    // Paso 1.3.4.3: Sincronizar SOLO los estados de bloqueo de los asientos.
                    // Si el asiento está bloqueado en el recinto base, se bloquea en el evento.
                    if (asientoBase.getEstado() == EstadoAsiento.BLOQUEADO) {
                        asientoEvento.setEstado(EstadoAsiento.BLOQUEADO);
                    }
                    // Si el asiento estaba bloqueado en el evento pero ya no lo está en el recinto base,
                    // se vuelve disponible SOLO si no estaba vendido o reservado.
                    else if (asientoEvento.getEstado() == EstadoAsiento.BLOQUEADO) {
                        asientoEvento.setEstado(EstadoAsiento.DISPONIBLE);
                    }
                }
            }
            // Paso 1.4: Actualizar el evento en el repositorio después de sincronizar su recinto.
            eventoRepositorio.actualizarEvento(evento);
        }
    }

    /**
     * Obtiene una lista observable de todos los recintos registrados en el sistema.
     *
     * @return Una {@link ObservableList} de objetos {@link Recinto}.
     */
    public ObservableList<Recinto> getRecintos() {
        return recintoRepositorio.getRecintos();
    }
}
