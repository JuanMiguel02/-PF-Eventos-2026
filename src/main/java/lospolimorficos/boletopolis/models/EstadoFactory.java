package lospolimorficos.boletopolis.models;

/**
 * <p><b>EstadoFactory</b></p>
 *
 * <p>Esta clase es una fábrica simple para crear instancias de {@link EstadoAsientoState}
 * basándose en un {@link EstadoAsiento} dado. Implementa el patrón Factory Method
 * para encapsular la lógica de creación de objetos de estado.</p>
 */
public class EstadoFactory {

    /**
     * <p><b>Crea y devuelve una instancia concreta de {@link EstadoAsientoState}
     * que corresponde al {@link EstadoAsiento} proporcionado.</b></p>
     *
     * <p>Este método utiliza una expresión {@code switch} para mapear cada valor
     * de la enumeración {@link EstadoAsiento} a su implementación de estado visual
     * correspondiente.</p>
     *
     * <p><b>Pasos:</b></p>
     * <ol>
     *     <li>Recibe un {@link EstadoAsiento} como parámetro.</li>
     *     <li>Utiliza una expresión {@code switch} para:
     *         <ul>
     *             <li>Si el estado es {@link EstadoAsiento#DISPONIBLE}, devuelve una nueva instancia de {@link DisponibleState}.</li>
     *             <li>Si el estado es {@link EstadoAsiento#RESERVADO}, devuelve una nueva instancia de {@link ReservadoState}.</li>
     *             <li>Si el estado es {@link EstadoAsiento#VENDIDO}, devuelve una nueva instancia de {@link VendidoState}.</li>
     *             <li>Si el estado es {@link EstadoAsiento#BLOQUEADO}, devuelve una nueva instancia de {@link BloqueadoState}.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param estado El {@link EstadoAsiento} para el cual se desea crear una instancia de estado visual.
     * @return Una implementación concreta de {@link EstadoAsientoState} que representa el estado visual.
     */
    public static EstadoAsientoState crearEstadoAsiento(EstadoAsiento estado){
        return switch (estado){
            case DISPONIBLE -> new DisponibleState();
            case RESERVADO -> new ReservadoState();
            case VENDIDO -> new VendidoState();
            case BLOQUEADO -> new BloqueadoState();
        };
    }

}
