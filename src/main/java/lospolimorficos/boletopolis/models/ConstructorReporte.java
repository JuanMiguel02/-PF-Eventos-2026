package lospolimorficos.boletopolis.models;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

//Interfaz para construcción de reportes
public interface ConstructorReporte {
    void iniciarDocumento(String rutaArchivo);
    void agregarTitulo(String titulo);
    void agregarSubtitulo(String subtitulo);
    void agregarTexto(String texto);
    void agregarTabla(List<String[]> datos);
    void agregarImagen(BufferedImage imagen);
    void finalizarDocumento();
    void agregarGrafico(String titulo, Map<String, Number> datos);
}
