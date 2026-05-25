package lospolimorficos.boletopolis.models;

/**
 * Clase abstracta base para los decoradores de reportes.
 * Implementa la interfaz {@link Reporte} y contiene una referencia al objeto {@link Reporte}
 * que está decorando. Esta clase forma parte del patrón Decorator, permitiendo añadir
 * responsabilidades adicionales a un objeto de reporte de forma dinámica.
 */
public abstract class DecoradorReporte implements Reporte{

    /**
     * La instancia de {@link Reporte} que está siendo decorada.
     * Todas las operaciones se delegan a este objeto base, y los decoradores
     * pueden añadir funcionalidad antes o después de la llamada al objeto base.
     */
    protected final Reporte reporte;

    /**
     * Constructor para la clase {@code DecoradorReporte}.
     *
     * @param reporte El objeto {@link Reporte} que se va a decorar.
     */
    public DecoradorReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    // La implementación del método construirReporte se deja a las subclases concretas
    // para que definan cómo se añade la funcionalidad de decoración.
}
