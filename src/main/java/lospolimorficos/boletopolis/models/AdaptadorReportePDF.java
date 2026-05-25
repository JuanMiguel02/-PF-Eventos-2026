package lospolimorficos.boletopolis.models;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.services.ServicioGeneradorGraficos;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Adaptador que implementa la interfaz {@link ConstructorReporte} para generar documentos PDF
 * utilizando la librería Apache PDFBox. Este adaptador permite añadir títulos, subtítulos, texto,
 * tablas, imágenes y gráficos a un documento PDF.
 */
public class AdaptadorReportePDF implements ConstructorReporte {

    /**
     * El documento PDF principal al que se añadirán los elementos.
     */
    private PDDocument documento;
    /**
     * El flujo de contenido para escribir en la página actual del PDF.
     */
    private PDPageContentStream contenido;
    /**
     * La página actual del documento PDF.
     */
    private PDPage pagina;
    /**
     * La posición actual en el eje Y de la página para el siguiente elemento.
     */
    private float posicionY;
    /**
     * La ruta de archivo donde se guardará el documento PDF.
     */
    private String rutaArchivo;
    /**
     * Bandera que indica si el flujo de texto está abierto para escribir.
     */
    private boolean textoAbierto = false;

    /**
     * Inicia un nuevo documento PDF y prepara la primera página para la escritura.
     *
     * @param rutaArchivo La ruta completa donde se guardará el archivo PDF.
     */
    @Override
    public void iniciarDocumento(String rutaArchivo) {
        try{
            // Paso 1: Almacenar la ruta del archivo para guardar el documento al finalizar.
            this.rutaArchivo = rutaArchivo;
            // Paso 2: Crear un nuevo documento PDF.
            documento = new PDDocument();
            // Paso 3: Crear una nueva página en blanco.
            pagina = new PDPage();
            // Paso 4: Añadir la página al documento.
            documento.addPage(pagina);

            // Paso 5: Crear un nuevo flujo de contenido para escribir en la página.
            contenido = new PDPageContentStream(documento, pagina);
            // Paso 6: Iniciar el modo de texto para poder escribir texto.
            contenido.beginText();
            // Paso 7: Establecer la bandera de texto abierto a true.
            textoAbierto = true;
            // Paso 8: Establecer el interlineado para el texto.
            contenido.setLeading(14.5f);
            // Paso 9: Establecer la posición inicial del cursor de texto en la página.
            contenido.newLineAtOffset(50,750);

            // Paso 10: Inicializar la posición Y actual para el seguimiento de la ubicación de los elementos.
            posicionY = 750;

        } catch (IOException e) {
            // En caso de error de E/S, lanzar una RuntimeException.
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega un título al documento PDF. El título se formatea con una fuente más grande y en negrita.
     *
     * @param titulo El texto del título a agregar.
     */
    @Override
    public void agregarTitulo(String titulo) {
        try{
            // Paso 1: Establecer la fuente y el tamaño para el título.
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 16);
            // Paso 2: Mostrar el texto del título.
            contenido.showText(titulo);
            // Paso 3: Mover el cursor a la siguiente línea.
            contenido.newLine();
            // Paso 4: Ajustar la posición Y para el siguiente elemento.
            posicionY -= 20;

        }catch (IOException e) {
            // En caso de error de E/S, lanzar una RuntimeException.
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega un subtítulo al documento PDF. El subtítulo se formatea con una fuente ligeramente más pequeña y en negrita.
     *
     * @param subtitulo El texto del subtítulo a agregar.
     */
    @Override
    public void agregarSubtitulo(String subtitulo) {
        try{
            // Paso 1: Establecer la fuente y el tamaño para el subtítulo.
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 13);
            // Paso 2: Mostrar el texto del subtítulo.
            contenido.showText(subtitulo);
            // Paso 3: Mover el cursor a la siguiente línea.
            contenido.newLine();
            // Paso 4: Ajustar la posición Y para el siguiente elemento.
            posicionY -= 18;

        }catch (IOException e) {
            // En caso de error de E/S, lanzar una RuntimeException.
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega un bloque de texto simple al documento PDF.
     *
     * @param texto El contenido textual a agregar.
     */
    @Override
    public void agregarTexto(String texto) {
        try{
            // Paso 1: Establecer la fuente y el tamaño para el texto normal.
            contenido.setFont(PDType1Font.HELVETICA, 12);
            // Paso 2: Mostrar el texto.
            contenido.showText(texto);
            // Paso 3: Mover el cursor a la siguiente línea.
            contenido.newLine();
            // Paso 4: Ajustar la posición Y para el siguiente elemento.
            posicionY -= 20;
        }catch (IOException e) {
            // En caso de error de E/S, lanzar una RuntimeException.
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega una tabla de datos al documento PDF. La tabla se representa como texto plano
     * con un encabezado y líneas de separación.
     *
     * @param datos    Una lista de arrays de String, donde cada array representa una fila de la tabla.
     * @param columnas Una lista de String que contiene los nombres de las columnas de la tabla.
     */
    @Override
    public void agregarTabla(List<String[]> datos, List<String> columnas) {
        try{
            // Paso 1: Establecer la fuente para los encabezados de la tabla.
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
            // Paso 2: Unir los nombres de las columnas con " | " para formar el encabezado.
            String encabezado = String.join(" | ", columnas);
            // Paso 3: Mostrar el encabezado en el PDF.
            contenido.showText(encabezado);
            // Paso 4: Mover el cursor a la siguiente línea.
            contenido.newLine();
            // Paso 5: Ajustar la posición Y.
            posicionY -=15;

            // Paso 6: Establecer la fuente para las líneas de separación.
            contenido.setFont(PDType1Font.HELVETICA, 12);
            // Paso 7: Mostrar una línea de separación.
            contenido.showText("----------------------------------");
            // Paso 8: Mover el cursor a la siguiente línea.
            contenido.newLine();
            // Paso 9: Ajustar la posición Y.
            posicionY -=15;

            // Paso 10: Iterar sobre cada fila de datos.
            for(String[] fila : datos){
                // Paso 10.1: Unir los elementos de la fila con " | " para formar una línea de texto.
                String linea = String.join(" | ", fila);
                // Paso 10.2: Mostrar la línea de datos en el PDF.
                contenido.showText(linea);
                // Paso 10.3: Mover el cursor a la siguiente línea.
                contenido.newLine();
                // Paso 10.4: Ajustar la posición Y.
                posicionY -= 15;
            }
        } catch (IOException e) {
            // En caso de error de E/S, lanzar una RuntimeException.
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega una imagen al documento PDF. La imagen se inserta en la posición actual
     * y se ajusta la posición Y para el siguiente elemento.
     *
     * @param imagen La imagen de tipo {@link BufferedImage} a agregar.
     */
    @Override
    public void agregarImagen(BufferedImage imagen) {
        try{
            // Paso 1: Si el flujo de texto está abierto, cerrarlo antes de insertar la imagen.
            if(textoAbierto){
                contenido.endText();
                textoAbierto = false;
            }

            // Paso 2: Convertir la BufferedImage a un array de bytes en formato PNG.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", baos);

            // Paso 3: Crear un objeto de imagen PDF a partir de los bytes.
            PDImageXObject pdImagen = PDImageXObject.createFromByteArray(documento, baos.toByteArray(), "Imagen");
            // Paso 4: Dibujar la imagen en el PDF en una posición y tamaño específicos.
            // Se resta 300 a posicionY para colocar la imagen por encima del texto actual.
            contenido.drawImage(pdImagen, 50, posicionY - 300, 500, 300);

            // Paso 5: Ajustar la posición Y para el siguiente elemento, considerando la altura de la imagen.
            posicionY -= 320;

            // Paso 6: Reiniciar el modo de texto en la nueva posición Y.
            contenido.beginText();
            textoAbierto = true;
            contenido.newLineAtOffset(50, posicionY);

        } catch (Exception e) {
            // En caso de error, lanzar una RuntimeException.
            throw new RuntimeException(e);
        }
    }

    /**
     * Finaliza la construcción del documento PDF y lo guarda en la ruta de archivo especificada.
     */
    @Override
    public void finalizarDocumento() {
        try{
            // Paso 1: Si el flujo de texto está abierto, cerrarlo antes de finalizar el documento.
            if(textoAbierto){
                contenido.endText();
                textoAbierto = false;
            }

            // Paso 2: Cerrar el flujo de contenido.
            contenido.close();
            // Paso 3: Guardar el documento PDF en la ruta especificada.
            documento.save(rutaArchivo);
            // Paso 4: Cerrar el documento PDF para liberar recursos.
            documento.close();
        }catch (Exception e) {
            // En caso de error, lanzar una RuntimeException.
            throw new RuntimeException(e);
        }
    }

    /**
     * Genera un gráfico de barras a partir de los datos proporcionados, lo convierte en una imagen
     * y lo agrega al documento PDF.
     *
     * @param titulo El título del gráfico.
     * @param datos Un mapa con las categorías (String) y sus valores numéricos (Number) para el gráfico.
     */
    @Override
    public void agregarGrafico(String titulo, Map<String, Number> datos) {
        // Paso 1: Verificar si no hay datos para el gráfico. Si no hay, se agrega un mensaje de texto
        // y se finaliza la ejecución del método.
        if (datos == null || datos.isEmpty()) {
            agregarTexto("No hay datos para el gráfico: " + titulo);
            return;
        }

        try {
            // Paso 2: Crear un gráfico de barras utilizando el ServicioGeneradorGraficos.
            BarChart<String, Number> grafico =
                    ServicioGeneradorGraficos.crearBarChart(titulo, datos);

            // Paso 3: Crear un contenedor VBox para el gráfico y aplicar estilos para asegurar un fondo blanco
            // y un padding adecuado para la captura de pantalla.
            VBox contenedor = new VBox(grafico);
            contenedor.setStyle("-fx-background-color: white; -fx-padding: 20;");
            // Paso 3.1: Establecer un tamaño preferido para el contenedor del gráfico.
            contenedor.setPrefSize(900, 700);

            // Paso 4: Forzar el renderizado del contenedor y el gráfico. Esto es crucial para que
            // JavaFX calcule correctamente el layout y los listeners de ServicioGeneradorGraficos se activen,
            // permitiendo una captura de pantalla precisa.
            new Scene(contenedor); // Se necesita una Scene para que el layout se calcule.
            contenedor.applyCss(); // Aplicar CSS.
            contenedor.layout(); // Calcular el layout.

            // Paso 5: Crear parámetros para la captura de pantalla, incluyendo una escala para mejorar la resolución.
            SnapshotParameters params = new SnapshotParameters();
            params.setTransform(javafx.scene.transform.Transform.scale(1.5, 1.5)); // Escala moderada para evitar pixelado

            // Paso 6: Realizar una captura de pantalla del contenedor del gráfico.
            WritableImage captura = contenedor.snapshot(params, null);
            // Paso 7: Convertir la imagen capturada (WritableImage) a BufferedImage.
            BufferedImage imagen = SwingFXUtils.fromFXImage(captura, null);

            // Paso 8: Agregar la imagen del gráfico al documento PDF utilizando el método existente.
            agregarImagen(imagen);

        } catch (Exception e) {
            // Paso 9: En caso de error durante la generación o inserción del gráfico, lanzar una RuntimeException.
            throw new RuntimeException("Error generando gráfico en PDF", e);
        }
    }

}
