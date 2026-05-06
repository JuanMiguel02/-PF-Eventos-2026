package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de gestionar el layout y posicionamiento de los elementos del recinto.
 */
public class ServicioLayoutRecinto {

    private double centroX;
    private double centroY;
    private final AnchorPane panelMapa;

    public ServicioLayoutRecinto(AnchorPane panelMapa) {
        this.panelMapa = panelMapa;
        actualizarCentros();
    }

    public void actualizarCentros() {
        this.centroX = panelMapa.getPrefWidth() > 0 ? panelMapa.getPrefWidth() / 2 : 500;
        this.centroY = panelMapa.getPrefHeight() > 0 ? panelMapa.getPrefHeight() / 2 : 400;
    }

    public double getCentroX() {
        return centroX;
    }

    public double getCentroY() {
        return centroY;
    }

    public double[] obtenerDatosEscenarioSilencioso(PosicionEscenario posicion) {
        double escW = 150;
        double escH = 40;
        double escX = centroX - escW / 2;
        double escY = centroY - escH / 2;

        if (posicion != null) {
            switch (posicion) {
                case ARRIBA -> escY -= 200;
                case ABAJO -> escY += 200;
                case IZQUIERDA -> escX -= 250;
                case DERECHA -> escX += 250;
            }
        }
        return new double[]{escX, escY, escW, escH};
    }

    public double[] calcularPosicionBaseZona(PosicionZona posicion, double escX, double escY, double escW, double escH, int index) {
        double baseX = escX + escW / 2;
        double baseY = escY + escH / 2;
        double offset = 60;
        double separacion = 100;

        double dx = 0;
        double dy = 0;

        switch (posicion) {
            case NORTE -> dy -= (escH / 2) + offset + (index * separacion);
            case SUR -> dy += (escH / 2) + offset + (index * separacion);
            case ESTE -> dx += (escW / 2) + offset + (index * separacion);
            case OESTE -> dx -= (escW / 2) + offset + (index * separacion);
        }

        return new double[]{baseX + dx, baseY + dy};
    }

    public void ajustarDimensionesPanel(Escenario escenario, List<Zona> zonas) {
        double[] bounds = calcularBounds(escenario != null ? escenario.posicion() : null, zonas, null);
        aplicarDimensiones(bounds);
    }

    public void ajustarDimensionesPanelPlantillas(PosicionEscenario posEsc, List<PlantillaZona> plantillas) {
        double[] bounds = calcularBounds(posEsc, null, plantillas);
        aplicarDimensiones(bounds);
    }

    private double[] calcularBounds(PosicionEscenario posEsc, List<Zona> zonas, List<PlantillaZona> plantillas) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        double[] datosEsc = obtenerDatosEscenarioSilencioso(posEsc);
        minX = Math.min(minX, datosEsc[0] - 50);
        minY = Math.min(minY, datosEsc[1] - 50);
        maxX = Math.max(maxX, datosEsc[0] + datosEsc[2] + 100);
        maxY = Math.max(maxY, datosEsc[1] + datosEsc[3] + 100);

        Map<PosicionZona, Integer> contadores = new HashMap<>();

        if (zonas != null) {
            for (Zona zona : zonas) {
                int index = contadores.getOrDefault(zona.getPosicionZona(), 0);
                contadores.put(zona.getPosicionZona(), index + 1);

                double[] base = calcularPosicionBaseZona(zona.getPosicionZona(), datosEsc[0], datosEsc[1], datosEsc[2], datosEsc[3], index);
                int filas = zona.getAsientos().stream().mapToInt(Asiento::getFila).max().orElse(0);
                int columnas = zona.getAsientos().stream().mapToInt(Asiento::getNumero).max().orElse(0);

                double ancho = columnas * 12;
                double alto = filas * 12;

                minX = Math.min(minX, base[0] - (ancho / 2) - 50);
                minY = Math.min(minY, base[1] - (alto / 2) - 50);
                maxX = Math.max(maxX, base[0] + (ancho / 2) + 100);
                maxY = Math.max(maxY, base[1] + (alto / 2) + 100);
            }
        }

        if (plantillas != null) {
            for (PlantillaZona p : plantillas) {
                int index = contadores.getOrDefault(p.getPosicionZona(), 0);
                contadores.put(p.getPosicionZona(), index + 1);

                double[] base = calcularPosicionBaseZona(p.getPosicionZona(), datosEsc[0], datosEsc[1], datosEsc[2], datosEsc[3], index);
                double ancho = p.getColumnas() * 12;
                double alto = p.getFilas() * 12;

                minX = Math.min(minX, base[0] - (ancho / 2) - 50);
                minY = Math.min(minY, base[1] - (alto / 2) - 50);
                maxX = Math.max(maxX, base[0] + (ancho / 2) + 100);
                maxY = Math.max(maxY, base[1] + (alto / 2) + 100);
            }
        }

        minX = Math.min(minX, 0);
        minY = Math.min(minY, 0);
        maxX = Math.max(maxX, 800);
        maxY = Math.max(maxY, 600);

        return new double[]{minX, minY, maxX, maxY};
    }

    private void aplicarDimensiones(double[] bounds) {
        double minX = bounds[0];
        double minY = bounds[1];
        double maxX = bounds[2];
        double maxY = bounds[3];

        double finalWidth = maxX - Math.min(0, minX);
        double finalHeight = maxY - Math.min(0, minY);

        panelMapa.setPrefWidth(finalWidth);
        panelMapa.setPrefHeight(finalHeight);
        panelMapa.setMinWidth(finalWidth);
        panelMapa.setMinHeight(finalHeight);
        actualizarCentros();
    }
}
