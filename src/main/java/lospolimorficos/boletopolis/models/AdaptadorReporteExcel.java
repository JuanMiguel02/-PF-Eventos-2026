package lospolimorficos.boletopolis.models;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.services.ServicioGeneradorGraficos;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Adaptador que traduce el reporte a la API de Apache POI para generar documentos de Excel (XLSX).
 */
public class AdaptadorReporteExcel implements ConstructorReporte {

    private Workbook libro;
    private Sheet hoja;
    private int filaActual;
    private String rutaArchivo;

    /**
     * Inicia un nuevo libro de Excel y crea una hoja de reporte.
     * @param rutaArchivo Ruta donde se guardará el archivo.
     */
    @Override
    public void iniciarDocumento(String rutaArchivo) {
        libro = new XSSFWorkbook();
        hoja = libro.createSheet("Reporte");
        filaActual = 0;
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Agrega un título a la hoja de Excel con formato en negrita.
     * @param titulo Texto del título.
     */
    @Override
    public void agregarTitulo(String titulo) {
        Row fila = hoja.createRow(filaActual++);
        Cell celda = fila.createCell(0);
        celda.setCellValue(titulo);

        CellStyle estiloTitulo = libro.createCellStyle();
        Font fuenteTitulo = libro.createFont();
        fuenteTitulo.setBold(true);
        fuenteTitulo.setFontHeightInPoints((short) 16);
        estiloTitulo.setFont(fuenteTitulo);
        celda.setCellStyle(estiloTitulo);
    }

    /**
     * Agrega un subtítulo a la hoja de Excel con formato en negrita.
     * @param subtitulo Texto del subtítulo.
     */
    @Override
    public void agregarSubtitulo(String subtitulo) {
        Row fila = hoja.createRow(filaActual++);
        Cell celda = fila.createCell(0);
        celda.setCellValue(subtitulo);

        CellStyle estiloSubtitulo = libro.createCellStyle();
        Font fuenteTitulo = libro.createFont();
        fuenteTitulo.setBold(true);
        fuenteTitulo.setFontHeightInPoints((short) 13);
        estiloSubtitulo.setFont(fuenteTitulo);
        celda.setCellStyle(estiloSubtitulo);
    }

    /**
     * Agrega una fila de texto a la hoja de Excel.
     * @param texto Contenido textual.
     */
    @Override
    public void agregarTexto(String texto) {
        Row fila = hoja.createRow(filaActual++);
        Cell celda = fila.createCell(0);
        celda.setCellValue(texto);
    }

    /**
     * Agrega una tabla de datos a la hoja de Excel.
     * @param datos Lista de arreglos de String que representan las filas y celdas.
     */
    @Override
    public void agregarTabla(List<String[]> datos) {
        for(String[] fila : datos){
            Row filaExcel = hoja.createRow(filaActual++);
            for(int i = 0; i < fila.length; i++){
                Cell celda = filaExcel.createCell(i);
                celda.setCellValue(fila[i]);
            }
        }
    }

    /**
     * Agrega una imagen a la hoja de Excel con un espacio de separación.
     * @param imagen Imagen en formato BufferedImage.
     */
    @Override
    public void agregarImagen(BufferedImage imagen) {
        try{
            // Espacio adicional antes de la imagen para separarla de la tabla o texto anterior
            filaActual += 2;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", baos);
            byte[] imagenBytes = baos.toByteArray();

            int imagenPosicion = libro.addPicture(imagenBytes, Workbook.PICTURE_TYPE_PNG);

            CreationHelper ayudador = libro.getCreationHelper();
            Drawing<?> dibujo = hoja.createDrawingPatriarch();
            ClientAnchor anchor = ayudador.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(filaActual);

            Picture imagenExcel = dibujo.createPicture(anchor, imagenPosicion);
            imagenExcel.resize();

            // Desplazar filaActual basándonos en el tamaño aproximado de la imagen (aprox 15 filas)
            filaActual += 20;

        }catch (Exception e){
            throw new RuntimeException("Error insertando imagen en Excel", e);
        }
    }


    /**
     * Finaliza y guarda el archivo de Excel.
     */
    @Override
    public void finalizarDocumento() {
        try(FileOutputStream salida = new FileOutputStream(rutaArchivo)){
            libro.write(salida);
            libro.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Genera y agrega un gráfico de barras a la hoja de Excel.
     * @param titulo Título del gráfico.
     * @param datos Mapa con las categorías y sus valores numéricos.
     */
    @Override
    public void agregarGrafico(String titulo, Map<String, Number> datos) {
        if(datos == null || datos.isEmpty()){
            agregarTexto("No hay datos para el gráfico: " + titulo);
            return;
        }
        try{
            BarChart<String, Number> grafico = ServicioGeneradorGraficos.crearBarChart(titulo, datos);

            VBox contenedor = new VBox(grafico);
            contenedor.setStyle("-fx-background-color: white; -fx-padding: 20;");
            contenedor.setPrefSize(1000, 750);

            // Forzar renderizado para que los listeners de ServicioGeneradorGraficos se activen
            new Scene(contenedor);
            contenedor.applyCss();
            contenedor.layout();

            SnapshotParameters params = new SnapshotParameters();
            params.setTransform(javafx.scene.transform.Transform.scale(1.5, 1.5));

            WritableImage captura = contenedor.snapshot(params, null);
            BufferedImage imagen = SwingFXUtils.fromFXImage(captura, null);
            agregarImagen(imagen);
        } catch (Exception e) {
            throw new RuntimeException("Error generando gráfico en Excel", e);
        }
    }
}
