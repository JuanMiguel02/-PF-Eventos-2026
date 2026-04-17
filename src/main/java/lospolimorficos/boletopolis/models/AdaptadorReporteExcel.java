package lospolimorficos.boletopolis.models;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

//Adaptador que traduce el reporte a la API de Apache POI
public class AdaptadorReporteExcel implements ConstructorReporte {

    public static final String EXTENSION = ".xlsx";
    private Workbook libro;
    private Sheet hoja;
    private int filaActual;
    private String rutaArchivo;

    @Override
    public void iniciarDocumento(String rutaArchivo) {
        libro = new XSSFWorkbook();
        hoja = libro.createSheet("Reporte");
        filaActual = 0;
        this.rutaArchivo = rutaArchivo;
    }

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

    @Override
    public void agregarTexto(String texto) {
        Row fila = hoja.createRow(filaActual++);
        Cell celda = fila.createCell(0);
        celda.setCellValue(texto);
    }

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

    @Override
    public void agregarImagen(BufferedImage imagen) {
        //más tarde
    }


    @Override
    public void finalizarDocumento() {
        try(FileOutputStream salida = new FileOutputStream(rutaArchivo)){
            libro.write(salida);
            libro.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
