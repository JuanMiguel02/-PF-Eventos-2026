package lospolimorficos.boletopolis.models;

import lospolimorficos.boletopolis.repositorios.CompraRepositorio;

public class DecoradorVentasPeriodo extends DecoradorReporte{

    private final CompraRepositorio compraRepositorio;

    public DecoradorVentasPeriodo(Reporte reporte, CompraRepositorio compraRepositorio) {
        super(reporte);
        this.compraRepositorio = compraRepositorio;
    }

    @Override
    public void construirReporte(ConstructorReporte constructorReporte) {

        reporte.construirReporte(constructorReporte);
        constructorReporte.agregarSubtitulo("Ventas por periodo:");
        //Falta

    }
}
