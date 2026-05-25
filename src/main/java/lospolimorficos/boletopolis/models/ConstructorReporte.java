package lospolimorficos.boletopolis.models;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Interfaz para la construcción de reportes.
 * Define los métodos que cualquier constructor de reportes debe implementar
 * para generar diferentes secciones de un documento de reporte.
 */
public interface ConstructorReporte {
    /**
     * Inicia un nuevo documento de reporte en la ruta de archivo especificada.
     *
     * @param rutaArchivo La ruta completa donde se guardará el archivo del reporte.
     */
    void iniciarDocumento(String rutaArchivo);

    /**
     * Agrega un título principal al documento del reporte.
     *
     * @param titulo El texto del título a agregar.
     */
    void agregarTitulo(String titulo);

    /**
     * Agrega un subtítulo a una sección del documento del reporte.
     *
     * @param subtitulo El texto del subtítulo a agregar.
     */
    void agregarSubtitulo(String subtitulo);

    /**
     * Agrega un párrafo de texto al documento del reporte.
     *
     * @param texto El contenido del texto a agregar.
     */
    void agregarTexto(String texto);

    /**
     * Agrega una tabla al documento del reporte.
     *
     * @param datos Una lista de arrays de String, donde cada array representa una fila de la tabla.
     * @param columnas Una lista de String que contiene los nombres de las columnas de la tabla.
     */
    void agregarTabla(List<String[]> datos,List<String> columnas);

    /**
     * Agrega una imagen al documento del reporte.
     *
     * @param image La imagen de tipo BufferedImage a agregar.
     */
    void agregarImagen(BufferedImage image);

    /**
     * Finaliza la construcción del documento del reporte, guardando todos los cambios.
     */
    void finalizarDocumento();

    /**
     * Agrega un gráfico al documento del reporte.
     *
     * @param titulo El título del gráfico.
     * @param datos Un mapa donde las claves son las etiquetas de los datos (e.g., nombres de eventos)
     *              y los valores son los datos numéricos correspondientes (e.g., ocupación, ganancia).
     */
    void agregarGrafico(String titulo, Map<String, Number> datos);
}
