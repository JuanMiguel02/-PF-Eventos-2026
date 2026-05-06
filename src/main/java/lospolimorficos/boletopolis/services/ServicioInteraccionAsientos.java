package lospolimorficos.boletopolis.services;

import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;
import lospolimorficos.boletopolis.models.Asiento;
import lospolimorficos.boletopolis.models.InteraccionStrategy;
import lospolimorficos.boletopolis.models.AdminEventoInteraccionStrategy;
import lospolimorficos.boletopolis.models.TipoZona;
import java.util.List;

public class ServicioInteraccionAsientos {

    private Runnable onAsientoChanged;
    private final List<Asiento> asientosSeleccionados;
    private InteraccionStrategy strategy;
    private boolean interactivo = false;

    public ServicioInteraccionAsientos(List<Asiento> asientosSeleccionados) {
        this.asientosSeleccionados = asientosSeleccionados;
    }

    public void setOnAsientoChanged(Runnable onAsientoChanged) {
        this.onAsientoChanged = onAsientoChanged;
    }

    public void setStrategy(InteraccionStrategy strategy) {
        this.strategy = strategy;
    }

    public void setInteractivo(boolean interactivo) {
        this.interactivo = interactivo;
    }

    public boolean isInteractivo() {
        return interactivo;
    }

    public InteraccionStrategy getStrategy() {
        return strategy;
    }

    public void notifyAsientoChanged() {
        if (onAsientoChanged != null) {
            onAsientoChanged.run();
        }
    }

    public List<Asiento> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    public void configurarAsiento(Rectangle r, Asiento asiento, TipoZona tipo, ServicioEstadoAsientos servicioEstado) {
        actualizarTooltip(r, asiento);

        if (interactivo) {
            r.setOnMouseClicked(event -> {
                strategy.onClick(asiento, this);
                
                servicioEstado.actualizarVisualAsiento(r, asiento, tipo, asientosSeleccionados.contains(asiento));
                actualizarTooltip(r, asiento);
            });

            if (strategy.esInteractuable(asiento)) {
                r.setStyle(r.getStyle() + "; -fx-cursor: hand;");
            } else {
                r.setStyle(r.getStyle() + "; -fx-cursor: default;");
            }
        }
    }

    public void actualizarTooltip(Rectangle r, Asiento asiento) {
        Tooltip.install(r, new Tooltip("Asiento: " + asiento.getIdAsiento() + "\nEstado: " + asiento.getEstado()));
    }

    public void toggleSeleccionCompra(Asiento asiento) {
        if (asientosSeleccionados.contains(asiento)) {
            asientosSeleccionados.remove(asiento);
        } else {
            asientosSeleccionados.add(asiento);
        }
    }

    public void limpiarSeleccion() {
        asientosSeleccionados.clear();
    }
}
