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
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Adaptador que traduce el reporte a la API de PDFBOX para generar documentos PDF.
 */
public class AdaptadorReportePDF implements ConstructorReporte {

    private PDDocument documento;
    private PDPageContentStream contenido;
    private PDPage pagina;
    private float posicionY;
    private String rutaArchivo;
    private boolean textoAbierto = false;

    /**
     * Inicia un nuevo documento PDF en la ruta especificada.
     * @param rutaArchivo Ruta donde se guardará el archivo.
     */
    @Override
    public void iniciarDocumento(String rutaArchivo) {
        try{
            this.rutaArchivo = rutaArchivo;
            documento = new PDDocument();
            pagina = new PDPage();
            documento.addPage(pagina);

            contenido = new PDPageContentStream(documento, pagina);
            contenido.beginText();
            textoAbierto = true;
            contenido.setLeading(14.5f);
            contenido.newLineAtOffset(50,750);

            posicionY = 750;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega un título al documento PDF.
     * @param titulo Texto del título.
     */
    @Override
    public void agregarTitulo(String titulo) {
        try{
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contenido.showText(titulo);
            contenido.newLine();
            posicionY -= 20;

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega un subtítulo al documento PDF.
     * @param subtitulo Texto del subtítulo.
     */
    @Override
    public void agregarSubtitulo(String subtitulo) {
        try{
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 13);
            contenido.showText(subtitulo);
            contenido.newLine();
            posicionY -= 18;

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega un bloque de texto al documento PDF.
     * @param texto Contenido textual.
     */
    @Override
    public void agregarTexto(String texto) {
        try{
            contenido.setFont(PDType1Font.HELVETICA, 12);
            contenido.showText(texto);
            contenido.newLine();
            posicionY -= 20;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega una tabla de datos al documento PDF.
     * @param datos Lista de arreglos de String que representan las filas de la tabla.
     */
    @Override
    public void agregarTabla(List<String[]> datos) {
        try{
            for(String[] fila : datos){
                String linea = String.join(" | ", fila);
                contenido.showText(linea);
                contenido.newLine();
                posicionY -= 15;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega una imagen al documento PDF.
     * @param imagen Imagen en formato BufferedImage.
     */
    @Override
    public void agregarImagen(BufferedImage imagen) {
        try{
            if(textoAbierto){
                contenido.endText();
                textoAbierto = false;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", baos);

            PDImageXObject pdImagen = PDImageXObject.createFromByteArray(documento, baos.toByteArray(), "Imagen");
            contenido.drawImage(pdImagen, 50, posicionY - 300, 500, 300);

            posicionY -= 320;

            contenido.beginText();
            textoAbierto = true;
            contenido.newLineAtOffset(50, posicionY);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finaliza y guarda el documento PDF.
     */
    @Override
    public void finalizarDocumento() {
        try{
            if(textoAbierto){
                contenido.endText();
                textoAbierto = false;
            }

            contenido.close();
            documento.save(rutaArchivo);
            documento.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Genera y agrega un gráfico de barras al documento PDF a partir de un mapa de datos.
     * @param titulo Título del gráfico.
     * @param datos Mapa con las categorías y sus valores numéricos.
     */
    @Override
    public void agregarGrafico(String titulo, Map<String, Number> datos) {
        if (datos == null || datos.isEmpty()) {
            agregarTexto("No hay datos para el gráfico: " + titulo);
            return;
        }

        try {
            BarChart<String, Number> grafico =
                    ServicioGeneradorGraficos.crearBarChart(titulo, datos);

            VBox contenedor = new VBox(grafico);
            contenedor.setStyle("-fx-background-color: white; -fx-padding: 20;");
            contenedor.setPrefSize(900, 700);

            // Forzar renderizado para que los listeners de ServicioGeneradorGraficos se activen
            new Scene(contenedor);
            contenedor.applyCss();
            contenedor.layout();

            SnapshotParameters params = new SnapshotParameters();
            params.setTransform(javafx.scene.transform.Transform.scale(1.5, 1.5)); // Escala moderada para evitar pixelado

            WritableImage captura = contenedor.snapshot(params, null);
            BufferedImage imagen = SwingFXUtils.fromFXImage(captura, null);

            agregarImagen(imagen);

        } catch (Exception e) {
            throw new RuntimeException("Error generando gráfico en PDF", e);
        }
    }

}
