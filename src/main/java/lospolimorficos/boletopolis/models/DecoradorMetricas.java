package lospolimorficos.boletopolis.models;

import java.util.List;

public class DecoradorMetricas extends DecoradorReporte {

    private final EstrategiaMetrica estrategiaMetrica;

    public DecoradorMetricas(Reporte reporte, EstrategiaMetrica estrategiaMetrica) {
        super(reporte);
        this.estrategiaMetrica = estrategiaMetrica;
    }

    @Override
    public void construirReporte(ConstructorReporte constructorReporte) {

        reporte.construirReporte(constructorReporte);

        constructorReporte.agregarSubtitulo("Metricas");
        estrategiaMetrica.generarSeccion(constructorReporte);

    }
}
