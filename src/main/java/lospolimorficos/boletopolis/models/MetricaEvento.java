package lospolimorficos.boletopolis.models;

/**
 * Record que representa las métricas clave de un evento para propósitos de reporte.
 *
 * @param nombre El nombre del evento.
 * @param ocupacion El porcentaje de ocupación del evento (valor entre 0 y 100).
 * @param ganancia La ganancia total generada por el evento.
 */
public record MetricaEvento(
        String nombre,
        double ocupacion,
        double ganancia
) {
}
