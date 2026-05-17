package lospolimorficos.boletopolis.models;

import java.time.LocalDateTime;

public class FiltroReporte {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public FiltroReporte(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
}
