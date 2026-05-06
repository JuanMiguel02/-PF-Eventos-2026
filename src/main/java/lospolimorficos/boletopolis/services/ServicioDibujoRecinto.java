package lospolimorficos.boletopolis.services;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fachada encargada de orquestar el renderizado visual de un recinto.
 */
public class ServicioDibujoRecinto {

    private final ServicioLayoutRecinto layout;
    private final ServicioRenderizadorRecinto renderizador;
    private final ServicioEstadoAsientos gestorEstados;
    private final ServicioInteraccionAsientos interaccion;

    public ServicioDibujoRecinto(AnchorPane panelMapa) {
        this.layout = new ServicioLayoutRecinto(panelMapa);
        this.renderizador = new ServicioRenderizadorRecinto(panelMapa);
        this.gestorEstados = new ServicioEstadoAsientos();
        this.interaccion = new ServicioInteraccionAsientos(new ArrayList<>());
    }

    public void setOnAsientoChanged(Runnable onAsientoChanged) {
        this.interaccion.setOnAsientoChanged(onAsientoChanged);
    }

    public List<Asiento> getAsientosSeleccionados() {
        return interaccion.getAsientosSeleccionados();
    }

    public void limpiarSeleccion() {
        interaccion.limpiarSeleccion();
    }

    public void actualizarCentros() {
        layout.actualizarCentros();
    }

    public void setStrategy(InteraccionStrategy strategy) {
        interaccion.setStrategy(strategy);
    }

    public void setInteractivo(boolean interactivo) {
        interaccion.setInteractivo(interactivo);
    }

    public void renderizar(Escenario escenario, List<Zona> zonas) {
        layout.ajustarDimensionesPanel(escenario, zonas);
        renderizador.limpiarPanel();

        PosicionEscenario posEsc = escenario != null ? escenario.posicion() : null;
        double[] datosEscenario = layout.obtenerDatosEscenarioSilencioso(posEsc);
        renderizador.dibujarEscenario(datosEscenario, posEsc);

        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        Map<PosicionZona, Integer> contadorZonas = new HashMap<>();

        for (Zona zona : zonas) {
            int index = contadorZonas.getOrDefault(zona.getPosicionZona(), 0);
            contadorZonas.put(zona.getPosicionZona(), index + 1);

            double[] base = layout.calcularPosicionBaseZona(zona.getPosicionZona(), escX, escY, escW, escH, index);
            int filas = zona.getAsientos().stream().mapToInt(Asiento::getFila).max().orElse(0);
            int columnas = zona.getAsientos().stream().mapToInt(Asiento::getNumero).max().orElse(0);

            dibujarZonaGenerica(zona.getNombre(), zona.getTipoZona(), filas, columnas, base[0], base[1], zona.getAsientos());
        }
    }

    private void dibujarZonaGenerica(String nombre, TipoZona tipo, int filas, int columnas, double baseX, double baseY, List<Asiento> asientos) {
        double ancho = columnas * 12;
        double alto = filas * 12;
        double inicioX = Math.max(5, baseX - ancho / 2);
        double inicioY = Math.max(25, baseY - alto / 2);

        renderizador.dibujarEtiquetaZona(nombre, inicioX, inicioY, ancho);

        if (asientos != null && !asientos.isEmpty()) {
            for (Asiento asiento : asientos) {
                Rectangle r = renderizador.crearRectanguloAsiento(tipo, inicioX, inicioY, asiento.getFila() - 1, asiento.getNumero() - 1);
                gestorEstados.actualizarVisualAsiento(r, asiento, tipo, interaccion.getAsientosSeleccionados().contains(asiento));
                interaccion.configurarAsiento(r, asiento, tipo, gestorEstados);
                renderizador.agregarAlPanel(r);
            }
        } else {
            for (int f = 0; f < filas; f++) {
                for (int c = 0; c < columnas; c++) {
                    Rectangle r = renderizador.crearRectanguloAsiento(tipo, inicioX, inicioY, f, c);
                    renderizador.instalarTooltipSimple(r, f, c);
                    renderizador.agregarAlPanel(r);
                }
            }
        }
    }

    public void renderizarPlantillas(PosicionEscenario posicionEscenario, List<PlantillaZona> plantillas) {
        layout.ajustarDimensionesPanelPlantillas(posicionEscenario, plantillas);
        renderizador.limpiarPanel();

        double[] datosEscenario = layout.obtenerDatosEscenarioSilencioso(posicionEscenario);
        renderizador.dibujarEscenario(datosEscenario, posicionEscenario);

        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        Map<PosicionZona, Integer> contadorZonas = new HashMap<>();

        for (PlantillaZona pZona : plantillas) {
            int index = contadorZonas.getOrDefault(pZona.getPosicionZona(), 0);
            contadorZonas.put(pZona.getPosicionZona(), index + 1);

            double[] base = layout.calcularPosicionBaseZona(pZona.getPosicionZona(), escX, escY, escW, escH, index);
            dibujarZonaGenerica(pZona.getNombre(), pZona.getTipoZona(), pZona.getFilas(), pZona.getColumnas(), base[0], base[1], null);
        }
    }

    public double[] calcularPosicionBaseZona(PosicionZona posicion, double escX, double escY, double escW, double escH, int index) {
        return layout.calcularPosicionBaseZona(posicion, escX, escY, escW, escH, index);
    }

    public double[] obtenerDatosEscenarioSilencioso(PosicionEscenario posicion) {
        return layout.obtenerDatosEscenarioSilencioso(posicion);
    }
}
