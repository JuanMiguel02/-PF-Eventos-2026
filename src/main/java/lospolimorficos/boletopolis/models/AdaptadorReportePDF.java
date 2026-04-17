package lospolimorficos.boletopolis.models;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

//Adaptador que traduce el reporte a la API de PDFBOX
public class AdaptadorReportePDF implements ConstructorReporte {

    private PDDocument documento;
    private PDPageContentStream contenido;
    private PDPage pagina;
    private float posicionY;
    private String rutaArchivo;

    @Override
    public void iniciarDocumento(String rutaArchivo) {
        try{
            this.rutaArchivo = rutaArchivo;
            documento = new PDDocument();
            pagina = new PDPage();
            documento.addPage(pagina);

            contenido = new PDPageContentStream(documento, pagina);
            contenido.beginText();
            contenido.setLeading(14.5f);
            contenido.newLineAtOffset(50,750);

            posicionY = 750;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    @Override
    public void agregarGrafico(BufferedImage imagen) {
        try{
            contenido.endText();

            File temp = new File("temp.png");
            ImageIO.write(imagen, "png", temp);

            PDImageXObject pdImagen = PDImageXObject.createFromFileByContent(temp, documento);
            contenido.drawImage(pdImagen, 50, posicionY - 200, 400, 200);

            posicionY -= 250;

            contenido.beginText();
            contenido.newLineAtOffset(50, posicionY);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void finalizarDocumento() {
        try{
            contenido.endText();
            contenido.clip();
            documento.save(rutaArchivo);
            documento.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
