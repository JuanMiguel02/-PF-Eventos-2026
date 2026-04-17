package lospolimorficos.boletopolis.models;

public abstract class DecoradorReporte implements Reporte{

    protected final Reporte reporte;

    public DecoradorReporte(Reporte reporte) {
        this.reporte = reporte;
    }

}
