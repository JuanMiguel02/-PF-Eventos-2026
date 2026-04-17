package lospolimorficos.boletopolis.models;

public enum TipoExportacion {
    PDF("pdf"),
    EXCEL("xlsx");

    private final String extension;

    TipoExportacion(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

}
