package lospolimorficos.boletopolis.models;

import java.time.LocalDate;
import java.util.List;

/**
 * Reporte personalizado para clientes.
 *
 * Permite consultar el historial de compras
 * utilizando filtros por:
 *
 * - Fecha
 * - Evento
 * - Estado
 */
public class ReporteCliente implements Reporte {

    private final Cliente cliente;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private Evento eventoFiltro;

    private EstadoCompra estadoFiltro;

    /**
     * Constructor del reporte.
     *
     * @param cliente Cliente dueño del reporte.
     */
    public ReporteCliente(
            Cliente cliente
    ) {

        this.cliente = cliente;
    }

    /**
     * Configura filtro de fechas.
     *
     * @param fechaInicio Fecha inicial.
     * @param fechaFin Fecha final.
     */
    public void setFiltroFecha(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {

        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    /**
     * Configura filtro por evento.
     *
     * @param evento Evento a consultar.
     */
    public void setFiltroEvento(Evento evento) {

        this.eventoFiltro = evento;
    }

    /**
     * Configura filtro por estado.
     *
     * @param estado Estado de la compra.
     */
    public void setFiltroEstado(EstadoCompra estado) {

        this.estadoFiltro = estado;
    }

    /**
     * Construye el reporte del usuario.
     *
     * @param constructorReporte Constructor del reporte.
     */
    @Override
    public void construirReporte(
            ConstructorReporte constructorReporte
    ) {

        // Fecha actual
        LocalDate fechaActual = LocalDate.now();

        // =========================
        // TÍTULO
        // =========================

        constructorReporte.agregarTitulo(
                "Historial de Compras - "
                        + cliente.getNombre()
                        + " "
                        + cliente.getApellido()
                        + " | Fecha: "
                        + fechaActual
        );

        // =========================
        // FILTROS
        // =========================

        constructorReporte.agregarTexto(
                "Filtros Aplicados:"
        );

        if (fechaInicio != null && fechaFin != null) {

            constructorReporte.agregarTexto(
                    "- Fecha: "
                            + fechaInicio
                            + " hasta "
                            + fechaFin
            );
        }

        if (eventoFiltro != null) {

            constructorReporte.agregarTexto(
                    "- Evento: "
                            + eventoFiltro.getNombre()
            );
        }

        if (estadoFiltro != null) {

            constructorReporte.agregarTexto(
                    "- Estado: "
                            + estadoFiltro
            );
        }

        constructorReporte.agregarTexto(" ");

        // =========================
        // COMPRAS CLIENTE
        // =========================

        List<Compra> comprasCliente =
                cliente.getCompras();

        double totalGastado = 0;

        int totalCompras = 0;

        for (Compra compra : comprasCliente) {

            boolean cumpleFiltro = true;

            // =========================
            // FILTRO FECHA
            // =========================

            if (fechaInicio != null &&
                    fechaFin != null) {

                LocalDate fechaCompra =
                        compra.getFechaCompra()
                                .toLocalDate();

                if (fechaCompra.isBefore(fechaInicio) ||
                        fechaCompra.isAfter(fechaFin)) {

                    cumpleFiltro = false;
                }
            }

            // =========================
            // FILTRO EVENTO
            // =========================

            if (eventoFiltro != null &&
                    !compra.getEvento()
                            .equals(eventoFiltro)) {

                cumpleFiltro = false;
            }

            // =========================
            // FILTRO ESTADO
            // =========================

            if (estadoFiltro != null &&
                    compra.getEstadoCompra()
                            != estadoFiltro) {

                cumpleFiltro = false;
            }

            // =========================
            // AGREGAR COMPRA
            // =========================

            if (cumpleFiltro) {

                totalCompras++;

                totalGastado +=
                        compra.getTotalCompra();

                constructorReporte.agregarTexto(
                        "Compra #" + totalCompras
                );

                constructorReporte.agregarTexto(
                        "Evento: "
                                + compra.getEvento()
                                .getNombre()
                );

                constructorReporte.agregarTexto(
                        "Fecha: "
                                + compra.getFechaCompra()
                );

                constructorReporte.agregarTexto(
                        "Estado: "
                                + compra.getEstadoCompra()
                );

                constructorReporte.agregarTexto(
                        "Método de Pago: "
                                + compra.getPago().getMetodoPago()
                );

                constructorReporte.agregarTexto(
                        "Total Pagado: $"
                                + String.format(
                                "%.2f",
                                compra.getTotalCompra()
                        )
                );

                constructorReporte.agregarTexto(
                        "Cantidad Entradas: "
                                + compra.getEntradas().size()
                );

                // =========================
                // ENTRADAS
                // =========================

                constructorReporte.agregarTexto(
                        "Entradas Compradas:"
                );

                for (Entrada entrada :
                        compra.getEntradas()) {

                    constructorReporte.agregarTexto(
                            "- Zona: "
                                    + entrada.getZona()
                                    .getNombre()
                                    + " | Asiento: "
                                    + entrada.getAsiento()
                                    .getIdAsiento()
                                    + " | Precio: $"
                                    + String.format(
                                    "%.2f",
                                    entrada.getPrecioFinal()
                            )
                    );
                }

                constructorReporte.agregarTexto(
                        "----------------------------------"
                );
            }
        }

        // =========================
        // RESUMEN FINAL
        // =========================

        constructorReporte.agregarTexto(" ");

        constructorReporte.agregarTexto(
                "Resumen General"
        );

        constructorReporte.agregarTexto(
                "Total Compras: "
                        + totalCompras
        );

        constructorReporte.agregarTexto(
                "Total Gastado: $"
                        + String.format(
                        "%.2f",
                        totalGastado
                )
        );
    }
}