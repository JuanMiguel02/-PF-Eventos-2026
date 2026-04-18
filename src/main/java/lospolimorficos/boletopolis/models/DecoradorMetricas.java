package lospolimorficos.boletopolis.models;

import java.util.List;

public class DecoradorMetricas extends DecoradorReporte {

    private final List<EstrategiaMetrica> metricas;

    public DecoradorMetricas(Reporte reporte, List<EstrategiaMetrica> metricas) {
        super(reporte);
        this.metricas = metricas;
    }

    @Override
    public void construirReporte(ConstructorReporte constructorReporte) {

        reporte.construirReporte(constructorReporte);

        if(metricas == null || metricas.isEmpty()) return;

        constructorReporte.agregarSubtitulo("Métricas:");

        for(EstrategiaMetrica metrica : metricas){

            ResultadoMetrica resultado = metrica.calcularMetrica();
            if(resultado == null) continue;

            constructorReporte.agregarSubtitulo(resultado.getTitulo());

            if(resultado.getDatos() != null && !resultado.getDatos().isEmpty()){
                constructorReporte.agregarGrafico(
                        resultado.getTitulo(),
                        resultado.getDatos()
                );
            }else{
                constructorReporte.agregarTexto("No hay datos para mostrar.");
            }
            if(resultado.getTabla() != null && !resultado.getTabla().isEmpty()){
                constructorReporte.agregarTabla(resultado.getTabla());
            }

        }

    }
}
