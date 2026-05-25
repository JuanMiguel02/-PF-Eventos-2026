package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaRecinto;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import lospolimorficos.boletopolis.services.GeneradorRecinto;

import java.util.ArrayList;
import java.util.List;


/**
 * Repositorio para la gestión de objetos {@link Recinto}.
 * Implementa el patrón Singleton para asegurar una única instancia global.
 * Proporciona métodos para almacenar, recuperar, actualizar y eliminar recintos.
 * Carga datos de ejemplo al inicializarse.
 */
public final class RecintoRepositorio {

    private final ObservableList<Recinto> recintos = FXCollections.observableArrayList();
    private static RecintoRepositorio instancia;

    /**
     * Constructor privado para implementar el patrón Singleton.
     * Carga datos de ejemplo al ser instanciado.
     */
    private RecintoRepositorio() {
        cargarDatosEjemplo();
    }

    /**
     * Obtiene la única instancia de {@code RecintoRepositorio}.
     * Si la instancia no ha sido creada, la inicializa.
     *
     * @return La instancia de {@code RecintoRepositorio}.
     */
    public static RecintoRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new RecintoRepositorio();
        }
        return instancia;
    }

    /**
     * Registra un nuevo recinto en el repositorio.
     *
     * @param recinto El objeto {@link Recinto} a registrar.
     * @return {@code true} si el recinto fue añadido exitosamente, {@code false} en caso contrario.
     */
    public boolean registrarRecinto(Recinto recinto) {
        return recintos.add(recinto);
    }

    /**
     * Elimina un recinto del repositorio.
     *
     * @param recinto El objeto {@link Recinto} a eliminar.
     * @return {@code true} si el recinto fue eliminado exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarRecinto(Recinto recinto) {
        return recintos.remove(recinto);
    }

    /**
     * Actualiza un recinto existente en el repositorio.
     * Busca el recinto por su ID y lo reemplaza con la versión actualizada.
     *
     * @param recintoActualizado El objeto {@link Recinto} con la información actualizada.
     * @return {@code true} si el recinto fue actualizado exitosamente.
     * @throws IllegalArgumentException Si el recinto a actualizar no se encuentra.
     */
    public boolean actualizarRecinto(Recinto recintoActualizado) {
        // Paso 1: Iterar sobre la lista de recintos para encontrar el recinto a actualizar.
        for(int i = 0; i < recintos.size(); i++) {
            // Paso 1.1: Comparar el ID del recinto actual con el ID del recinto actualizado.
            if(recintos.get(i).getIdRecinto().equals(recintoActualizado.getIdRecinto())) {
                // Paso 1.2: Si los IDs coinciden, reemplazar el recinto existente con la versión actualizada.
                recintos.set(i, recintoActualizado);
                // Paso 1.3: Devolver true indicando que la actualización fue exitosa.
                return true;
            }
        }
        // Paso 2: Si el bucle termina y el recinto no fue encontrado, lanzar una excepción.
        throw new IllegalArgumentException("Recinto no encontrado");
    }

    /**
     * Obtiene la lista observable de todos los recintos registrados.
     *
     * @return Una {@link ObservableList} de objetos {@link Recinto}.
     */
    public ObservableList<Recinto> getRecintos() {
        return recintos;
    }

    /**
     * Cuenta el número total de recintos registrados en el repositorio.
     *
     * @return El número total de recintos.
     */
    public int contarRecintos(){
        return recintos.size();
    }

    /**
     * Carga datos de ejemplo en el repositorio de recintos.
     * Crea dos recintos predefinidos con sus respectivas zonas y escenarios.
     */
    private void cargarDatosEjemplo(){
        // Paso 1: Definir plantillas de zonas para el primer recinto.
        PlantillaZona recintoZona1 = new PlantillaZona("VIP-1", PosicionZona.SUR, TipoZona.VIP, 2, 7, 60000);
        PlantillaZona recintoZona2 = new PlantillaZona("VIP-2", PosicionZona.NORTE, TipoZona.VIP, 2, 7, 60000);
        PlantillaZona recintoZona3 = new PlantillaZona("GENERAL-1", PosicionZona.ESTE, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona recintoZona4 = new PlantillaZona("GENERAL-2", PosicionZona.OESTE, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona recintoZona5 = new PlantillaZona("GENERAL-3", PosicionZona.SUR, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona recintoZona6= new PlantillaZona("GENERAL-4", PosicionZona.NORTE, TipoZona.GENERAL, 8, 8, 30000);

        // Paso 2: Crear una lista con las plantillas de zonas del primer recinto.
        List<PlantillaZona> plantillaZonas = new ArrayList<>();
        plantillaZonas.add(recintoZona1);
        plantillaZonas.add(recintoZona2);
        plantillaZonas.add(recintoZona3);
        plantillaZonas.add(recintoZona4);
        plantillaZonas.add(recintoZona5);
        plantillaZonas.add(recintoZona6);

        // Paso 3: Crear una plantilla de recinto para el primer recinto.
        PlantillaRecinto plantillaRecinto = new PlantillaRecinto("Estadio de Pacho", plantillaZonas);
        // Paso 4: Generar el primer recinto utilizando el GeneradorRecinto.
        Recinto recinto = GeneradorRecinto.generarRecinto(plantillaRecinto, "Calle 123, Col. Centro", Ciudad.ARMENIA);
        // Paso 5: Establecer el escenario para el primer recinto.
        recinto.setEscenario(new Escenario(PosicionEscenario.CENTRO));
        // Paso 6: Registrar el primer recinto en el repositorio.
        registrarRecinto(recinto);

        // Paso 7: Definir plantillas de zonas para el segundo recinto.
        PlantillaZona recinto2Zona1 = new PlantillaZona("PREFERENCIAL-1", PosicionZona.SUR, TipoZona.PREFERENCIAL, 2, 5, 70000);
        PlantillaZona recinto2Zona2 = new PlantillaZona("GENERAL-1", PosicionZona.SUR, TipoZona.GENERAL, 4, 6, 50000);
        PlantillaZona recinto2Zona3 = new PlantillaZona("GENERAL-2", PosicionZona.SUR, TipoZona.GENERAL, 8, 8, 30000);
        PlantillaZona recinto2Zona4 = new PlantillaZona("VIP-2", PosicionZona.ESTE, TipoZona.VIP, 6, 2, 80000);
        PlantillaZona recinto2Zona5 = new PlantillaZona("VIP-3", PosicionZona.OESTE, TipoZona.VIP, 6, 2, 80000);
        PlantillaZona recinto2Zona6 = new PlantillaZona("GENERAL-3", PosicionZona.ESTE, TipoZona.GENERAL, 8, 8, 50000);
        PlantillaZona recinto2Zona7 = new PlantillaZona("GENERAL-4", PosicionZona.OESTE, TipoZona.GENERAL, 8, 8, 50000);

        // Paso 8: Crear una lista con las plantillas de zonas del segundo recinto.
        List<PlantillaZona> plantillaZonas2 = new ArrayList<>();
        plantillaZonas2.add(recinto2Zona1);
        plantillaZonas2.add(recinto2Zona2);
        plantillaZonas2.add(recinto2Zona3);
        plantillaZonas2.add(recinto2Zona4);
        plantillaZonas2.add(recinto2Zona5);
        plantillaZonas2.add(recinto2Zona6);
        plantillaZonas2.add(recinto2Zona7);

        // Paso 9: Crear una plantilla de recinto para el segundo recinto.
        PlantillaRecinto plantillaRecinto2 = new PlantillaRecinto("Estadio Casablanca", plantillaZonas2);
        // Paso 10: Generar el segundo recinto utilizando el GeneradorRecinto.
        Recinto recinto2 = GeneradorRecinto.generarRecinto(plantillaRecinto2, "Calle 18, Carrera #14", Ciudad.PEREIRA);
        // Paso 11: Establecer el escenario para el segundo recinto.
        recinto2.setEscenario(new Escenario(PosicionEscenario.ARRIBA));
        // Paso 12: Registrar el segundo recinto en el repositorio.
        registrarRecinto(recinto2);
    }

    /**
     * Obtiene el primer recinto de la lista de recintos.
     * Útil para propósitos de prueba o cuando se sabe que siempre hay al menos un recinto.
     *
     * @return El primer objeto {@link Recinto} en la lista.
     */
    public Recinto getPrimerRecinto(){
        return recintos.getFirst();
    }

}
