package lospolimorficos.boletopolis.models;

/**
 * Enumeración que define los tipos de formato de archivo a los que se puede exportar un reporte.
 * Cada tipo de exportación tiene asociada una extensión de archivo.
 */
public enum TipoExportacion {
    /**
     * Formato de documento portátil (Portable Document Format).
     */
    PDF(".pdf"),
    /**
     * Formato de hoja de cálculo de Microsoft Excel (Open XML Spreadsheet).
     */
    EXCEL(".xlsx");

    private final String extension;

    /**
     * Constructor para {@code TipoExportacion}.
     *
     * @param extension La extensión de archivo asociada a este tipo de exportación (ej. ".pdf", ".xlsx").
     */
    TipoExportacion(String extension) {
        this.extension = extension;
    }

    /**
     * Obtiene la extensión de archivo asociada a este tipo de exportación.
     *
     * @return Una cadena de texto que representa la extensión del archivo.
     */
    public String getExtension() {
        return extension;
    }

}
