package lospolimorficos.boletopolis.models;

public class EstadoFactory {

    public static EstadoAsientoState crearEstadoAsiento(EstadoAsiento estado){
        return switch (estado){
            case DISPONIBLE -> new DisponibleState();
            case RESERVADO -> new ReservadoState();
            case VENDIDO -> new VendidoState();
            case BLOQUEADO -> new BloqueadoState();
        };
    }

}
