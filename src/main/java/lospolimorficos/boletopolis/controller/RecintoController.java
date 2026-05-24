package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import lospolimorficos.boletopolis.repositorios.EventoRepositorio;
import lospolimorficos.boletopolis.repositorios.RecintoRepositorio;

import java.util.List;

public class RecintoController {

    private final RecintoRepositorio recintoRepositorio =
            RecintoRepositorio.getInstancia();

    private final EventoRepositorio eventoRepositorio =
            EventoRepositorio.getInstancia();

    public long generarNumeroZona(List<PlantillaZona> plantillas,
                                  TipoZona tipoSeleccionado) {

        return plantillas.stream()
                .filter(z -> z.getTipoZona() == tipoSeleccionado)
                .count();
    }

    public int calcularCapacidadTotal(List<PlantillaZona> plantillas) {
        int numero = 0;

        for (PlantillaZona plantilla : plantillas) {
            numero += plantilla.calcularCapacidad();
        }

        return numero;
    }

    public List<Recinto> filtrarRecintos(List<Recinto> recintos,
                                         String filtro) {

        if (filtro == null || filtro.isEmpty()) {
            return recintos;
        }

        String filtroLimpio = filtro.toLowerCase();

        return recintos.stream()
                .filter(recinto ->
                        recinto.getNombre().toLowerCase().contains(filtroLimpio)
                                || recinto.getIdRecinto().toString().contains(filtroLimpio)
                                || recinto.getDireccion().toLowerCase().contains(filtroLimpio)
                                || recinto.getCiudad().toString().toLowerCase().contains(filtroLimpio)
                                || String.valueOf(recinto.getCapacidad()).contains(filtroLimpio))
                .toList();
    }

    public boolean registrarRecinto(Recinto recinto) {
        return recintoRepositorio.registrarRecinto(recinto);
    }

    public boolean eliminarRecinto(Recinto recinto) {
        return recintoRepositorio.eliminarRecinto(recinto);
    }

    public boolean actualizarRecinto(Recinto recintoActualizado) {

        boolean actualizado = recintoRepositorio.actualizarRecinto(recintoActualizado);

        if (actualizado) {
            sincronizarEventos(recintoActualizado);
        }

        return actualizado;
    }

    /**
     * Sincroniza cambios estructurales del recinto
     * con los eventos que usan copias del recinto.
     */
    private void sincronizarEventos(Recinto recintoActualizado) {

        for (Evento evento : eventoRepositorio.getEventos()) {

            Recinto recintoEvento = evento.getRecinto();

            // Verificar que el evento use este recinto
            if (!recintoEvento.getIdRecinto()
                    .equals(recintoActualizado.getIdRecinto())) {
                continue;
            }

            // Sincronizar zonas
            for (Zona zonaEvento : recintoEvento.getZonas()) {

                Zona zonaBase = recintoActualizado.getZonas()
                        .stream()
                        .filter(z -> z.getIdZona()
                                .equals(zonaEvento.getIdZona()))
                        .findFirst()
                        .orElse(null);

                if (zonaBase == null) {
                    continue;
                }

                // Sincronizar propiedades de zona
                zonaEvento.setNombre(zonaBase.getNombre());
                zonaEvento.setPrecioZona(zonaBase.getPrecioZona());

                // Sincronizar asientos
                for (Asiento asientoEvento : zonaEvento.getAsientos()) {

                    Asiento asientoBase = zonaBase.getAsientos()
                            .stream()
                            .filter(a -> a.getIdAsiento()
                                    .equals(asientoEvento.getIdAsiento()))
                            .findFirst()
                            .orElse(null);

                    if (asientoBase == null) {
                        continue;
                    }

                    // SOLO sincronizar bloqueos
                    if (asientoBase.getEstado() == EstadoAsiento.BLOQUEADO) {

                        asientoEvento.setEstado(EstadoAsiento.BLOQUEADO);

                    } else if (asientoEvento.getEstado() == EstadoAsiento.BLOQUEADO) {

                        // Si el asiento ya no está bloqueado en el recinto,
                        // volverlo disponible SOLO si no estaba vendido/reservado
                        asientoEvento.setEstado(EstadoAsiento.DISPONIBLE);
                    }
                }
            }

            eventoRepositorio.actualizarEvento(evento);
        }
    }

    public ObservableList<Recinto> getRecintos() {
        return recintoRepositorio.getRecintos();
    }
}