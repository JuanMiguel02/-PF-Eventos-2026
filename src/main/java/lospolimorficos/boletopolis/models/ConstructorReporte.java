package lospolimorficos.boletopolis.models;

import java.awt.image.BufferedImage;
import java.util.List;

//Interfaz para construcción de reportes
public interface ConstructorReporte {
    void iniciarDocumento(String rutaArchivo);
    void agregarTitulo(String titulo);
    void agregarSubtitulo(String subtitulo);
    void agregarTexto(String texto);
    void agregarTabla(List<String[]> datos);
    void agregarGrafico(BufferedImage imagen);
    void finalizarDocumento();

}
