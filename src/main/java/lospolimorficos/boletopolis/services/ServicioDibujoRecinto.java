package lospolimorficos.boletopolis.services;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.*;
import lospolimorficos.boletopolis.plantillas.PlantillaZona;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de renderizar visualmente un recinto en un AnchorPane.
 */
public class ServicioDibujoRecinto {

    private final AnchorPane panelMapa;
    private final double centroX;
    private final double centroY;

    public ServicioDibujoRecinto(AnchorPane panelMapa) {
        this.panelMapa = panelMapa;
        this.centroX = panelMapa.getPrefWidth() > 0 ? panelMapa.getPrefWidth() / 2 : 500;
        this.centroY = panelMapa.getPrefHeight() > 0 ? panelMapa.getPrefHeight() / 2 : 400;
    }

    /**
     * Renderiza un recinto completo usando objetos del modelo.
     * @param escenario Escenario del recinto.
     * @param zonas Lista de zonas a dibujar.
     */
    public void renderizar(Escenario escenario, List<Zona> zonas) {
        panelMapa.getChildren().clear();

        double[] datosEscenario = dibujarEscenario(escenario != null ? escenario.getPosicion() : null);
        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        Map<PosicionZona, Integer> contadorZonas = new HashMap<>();

        for (Zona zona : zonas) {
            int index = contadorZonas.getOrDefault(zona.getPosicionZona(), 0);
            contadorZonas.put(zona.getPosicionZona(), index + 1);

            int maxFila = zona.getAsientos().stream().mapToInt(Asiento::getFila).max().orElse(0);
            int maxColumna = zona.getAsientos().stream().mapToInt(Asiento::getNumero).max().orElse(0);

            double[] base = calcularPosicionBaseZona(zona.getPosicionZona(), escX, escY, escW, escH, index);
            dibujarZona(zona.getNombre(), zona.getTipoZona(), maxFila, maxColumna, base[0], base[1]);
        }
    }

    /**
     * Renderiza un recinto completo usando plantillas (para previsualización).
     * @param posicionEscenario Posición del escenario.
     * @param plantillas Lista de plantillas de zona.
     */
    public void renderizarPlantillas(PosicionEscenario posicionEscenario, List<PlantillaZona> plantillas) {
        panelMapa.getChildren().clear();

        double[] datosEscenario = dibujarEscenario(posicionEscenario);
        double escX = datosEscenario[0];
        double escY = datosEscenario[1];
        double escW = datosEscenario[2];
        double escH = datosEscenario[3];

        Map<PosicionZona, Integer> contadorZonas = new HashMap<>();

        for (PlantillaZona pZona : plantillas) {
            int index = contadorZonas.getOrDefault(pZona.getPosicionZona(), 0);
            contadorZonas.put(pZona.getPosicionZona(), index + 1);

            double[] base = calcularPosicionBaseZona(pZona.getPosicionZona(), escX, escY, escW, escH, index);
            dibujarZona(pZona.getNombre(), pZona.getTipoZona(), pZona.getFilas(), pZona.getColumnas(), base[0], base[1]);
        }
    }

    /**
     * Obtiene las coordenadas y dimensiones del escenario sin dibujarlo.
     */
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

    private double[] dibujarEscenario(PosicionEscenario posicion) {
        double[] datos = obtenerDatosEscenarioSilencioso(posicion);
        double escX = datos[0];
        double escY = datos[1];
        double escW = datos[2];
        double escH = datos[3];

        if (posicion != null) {
            Rectangle rect = new Rectangle(escW, escH);
            rect.setStyle("-fx-fill: #575252;");
            rect.setLayoutX(escX);
            rect.setLayoutY(escY);
            panelMapa.getChildren().add(rect);
        }
        return datos;
    }

    public double[] calcularPosicionBaseZona(PosicionZona posicion, double escX, double escY, double escW, double escH, int index) {
        double baseX = escX + escW / 2;
        double baseY = escY + escH / 2;
        double offset = 60;
        double separacion = 120;

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

    private void dibujarZona(String nombre, TipoZona tipo, int filas, int columnas, double baseX, double baseY) {
        double ancho = columnas * 12;
        double alto = filas * 12;
        double inicioX = baseX - ancho / 2;
        double inicioY = baseY - alto / 2;

        // Ajuste de límites (clonado de CreacionRecintoController)
        if (inicioX < 0) inicioX = 5;
        if (inicioY < 0) inicioY = 25;
        if (inicioX + ancho > panelMapa.getPrefWidth()) inicioX = panelMapa.getPrefWidth() - ancho - 5;
        if (inicioY + alto > panelMapa.getPrefHeight()) inicioY = panelMapa.getPrefHeight() - alto - 5;

        // Nombre
        Label label = new Label(nombre);
        double finalInicioX = inicioX;
        label.widthProperty().addListener((obs, oldVal, newVal) -> {
            double labelX = (finalInicioX + ancho / 2) - newVal.doubleValue() / 2;
            label.setLayoutX(Math.max(5, Math.min(labelX, panelMapa.getPrefWidth() - newVal.doubleValue() - 5)));
        });
        label.setLayoutX(inicioX);
        label.setLayoutY(inicioY - 20);
        panelMapa.getChildren().add(label);

        // Asientos
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Rectangle r = new Rectangle(10, 10);
                r.setStyle(tipo.getEstilo());
                r.setLayoutX(inicioX + (c * 12));
                r.setLayoutY(inicioY + (f * 12));
                panelMapa.getChildren().add(r);
            }
        }
    }
}
