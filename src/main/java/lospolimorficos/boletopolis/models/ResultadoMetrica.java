package lospolimorficos.boletopolis.models;

import java.util.List;
import java.util.Map;

public class ResultadoMetrica {
    private final String titulo;
    private final Map<String, Number> datos;
    private final List<String[]> tabla;

    public ResultadoMetrica(String titulo, Map<String, Number> datos, List<String[]> tabla) {
        this.titulo = titulo;
        this.datos = datos;
        this.tabla = tabla;
    }

    public String getTitulo() { return titulo; }
    public Map<String, Number> getDatos() { return datos; }
    public List<String[]> getTabla() { return tabla; }
}
