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
 * Adaptador que implementa la interfaz {@link ConstructorReporte} para generar documentos de Excel (XLSX)
 * utilizando la librería Apache POI. Este adaptador permite añadir títulos, subtítulos, texto, tablas,
 * imágenes y gráficos a una hoja de cálculo de Excel.
 */
public class AdaptadorReporteExcel implements ConstructorReporte {

    private Workbook libro;
    private Sheet hoja;
    private int filaActual;
    private String rutaArchivo;

    /**
     * Inicia un nuevo libro de Excel y crea una hoja de reporte.
     *
     * @param rutaArchivo La ruta completa donde se guardará el archivo de Excel.
     */
    @Override
    public void iniciarDocumento(String rutaArchivo) {
        // Paso 1: Crear un nuevo libro de trabajo de Excel en formato XLSX.
        libro = new XSSFWorkbook();
        // Paso 2: Crear una nueva hoja dentro del libro de trabajo con el nombre "Reporte".
        hoja = libro.createSheet("Reporte");
        // Paso 3: Inicializar el contador de fila actual a 0, que indica la primera fila del documento.
        filaActual = 0;
        // Paso 4: Almacenar la ruta del archivo para su uso posterior al finalizar el documento.
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Agrega un título al documento de Excel. El título se formatea con una fuente más grande y en negrita.
     *
     * @param titulo El texto del título a agregar.
     */
    @Override
    public void agregarTitulo(String titulo) {
        // Paso 1: Crear una nueva fila en la posición actual y luego incrementar el contador de fila.
        Row fila = hoja.createRow(filaActual++);
        // Paso 2: Crear una celda en la primera columna (índice 0) de la fila actual.
        Cell celda = fila.createCell(0);
        // Paso 3: Establecer el valor de la celda con el texto del título.
        celda.setCellValue(titulo);

        // Paso 4: Crear un estilo de celda para el título.
        CellStyle estiloTitulo = libro.createCellStyle();
        // Paso 5: Crear una fuente para el título.
        Font fuenteTitulo = libro.createFont();
        // Paso 5.1: Establecer la fuente en negrita.
        fuenteTitulo.setBold(true);
        // Paso 5.2: Establecer el tamaño de la fuente a 16 puntos.
        fuenteTitulo.setFontHeightInPoints((short) 16);
        // Paso 6: Asignar la fuente creada al estilo del título.
        estiloTitulo.setFont(fuenteTitulo);
        // Paso 7: Aplicar el estilo del título a la celda.
        celda.setCellStyle(estiloTitulo);
    }

    /**
     * Agrega un subtítulo al documento de Excel. El subtítulo se formatea con una fuente ligeramente más pequeña y en negrita.
     *
     * @param subtitulo El texto del subtítulo a agregar.
     */
    @Override
    public void agregarSubtitulo(String subtitulo) {
        // Paso 1: Crear una nueva fila en la posición actual y luego incrementar el contador de fila.
        Row fila = hoja.createRow(filaActual++);
        // Paso 2: Crear una celda en la primera columna (índice 0) de la fila actual.
        Cell celda = fila.createCell(0);
        // Paso 3: Establecer el valor de la celda con el texto del subtítulo.
        celda.setCellValue(subtitulo);

        // Paso 4: Crear un estilo de celda para el subtítulo.
        CellStyle estiloSubtitulo = libro.createCellStyle();
        // Paso 5: Crear una fuente para el subtítulo.
        Font fuenteTitulo = libro.createFont();
        // Paso 5.1: Establecer la fuente en negrita.
        fuenteTitulo.setBold(true);
        // Paso 5.2: Establecer el tamaño de la fuente a 13 puntos.
        fuenteTitulo.setFontHeightInPoints((short) 13);
        // Paso 6: Asignar la fuente creada al estilo del subtítulo.
        estiloSubtitulo.setFont(fuenteTitulo);
        // Paso 7: Aplicar el estilo del subtítulo a la celda.
        celda.setCellStyle(estiloSubtitulo);
    }

    /**
     * Agrega una línea de texto simple al documento de Excel.
     *
     * @param texto El contenido textual a agregar.
     */
    @Override
    public void agregarTexto(String texto) {
        // Paso 1: Crear una nueva fila en la posición actual y luego incrementar el contador de fila.
        Row fila = hoja.createRow(filaActual++);
        // Paso 2: Crear una celda en la primera columna (índice 0) de la fila actual.
        Cell celda = fila.createCell(0);
        // Paso 3: Establecer el valor de la celda con el texto proporcionado.
        celda.setCellValue(texto);
    }

    /**
     * Agrega una tabla de datos al documento de Excel. Incluye encabezados de columna y ajusta automáticamente
     * el ancho de las columnas.
     *
     * @param datos    Una lista de arrays de String, donde cada array representa una fila de la tabla.
     * @param columnas Una lista de String que contiene los nombres de las columnas de la tabla.
     */
    @Override
    public void agregarTabla(List<String[]> datos, List<String> columnas) {
        // Paso 1: Validar si las columnas o los datos son nulos o si los datos están vacíos.
        // Si alguna de estas condiciones se cumple, no se puede construir la tabla, por lo que se retorna.
        if(columnas == null || datos == null || datos.isEmpty()) return;

        // Paso 2: Crear una fila para los encabezados de la tabla y luego incrementar el contador de fila.
        Row filaEncabezado = hoja.createRow(filaActual++);

        // Paso 3: Crear un estilo de celda para los encabezados.
        CellStyle estiloEncabezado = libro.createCellStyle();
        // Paso 4: Crear una fuente para los encabezados.
        Font fuenteEncabezado = libro.createFont();
        // Paso 4.1: Establecer la fuente en negrita.
        fuenteEncabezado.setBold(true);
        // Paso 5: Asignar la fuente creada al estilo del encabezado.
        estiloEncabezado.setFont(fuenteEncabezado);

        // Paso 6: Iterar sobre la lista de nombres de columnas para crear las celdas de encabezado.
        for(int i = 0; i < columnas.size(); i++){
            // Paso 6.1: Crear una celda para el encabezado en la columna actual.
            Cell celdaEncabezado = filaEncabezado.createCell(i);
            // Paso 6.2: Establecer el valor de la celda con el nombre de la columna.
            celdaEncabezado.setCellValue(columnas.get(i));
            // Paso 6.3: Aplicar el estilo de encabezado a la celda.
            celdaEncabezado.setCellStyle(estiloEncabezado);
        }
        // Paso 7: Iterar sobre la lista de datos para crear las filas de datos de la tabla.
        for(String[] fila : datos){
            // Paso 7.1: Crear una nueva fila en Excel para los datos y luego incrementar el contador de fila.
            Row filaExcel = hoja.createRow(filaActual++);
            // Paso 7.2: Iterar sobre los elementos del array de String (fila) para crear las celdas de datos.
            for(int i = 0; i < fila.length; i++){
                // Paso 7.2.1: Crear una celda en la columna actual.
                Cell celda = filaExcel.createCell(i);
                // Paso 7.2.2: Establecer el valor de la celda con el dato correspondiente.
                celda.setCellValue(fila[i]);
            }
        }
        // Paso 8: Ajustar automáticamente el ancho de todas las columnas para que el contenido sea visible.
        for(int i = 0; i < columnas.size(); i++){
            hoja.autoSizeColumn(i);
        }
    }

    /**
     * Agrega una imagen al documento de Excel. La imagen se inserta como un objeto incrustado.
     * Se añade un espacio adicional antes y después de la imagen para una mejor presentación.
     *
     * @param imagen La imagen de tipo {@link BufferedImage} a agregar.
     */
    @Override
    public void agregarImagen(BufferedImage imagen) {
        try{
            // Paso 1: Añadir un espacio adicional (2 filas) antes de la imagen para separarla del contenido anterior.
            filaActual += 2;

            // Paso 2: Convertir la BufferedImage a un array de bytes en formato PNG.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", baos);
            byte[] imagenBytes = baos.toByteArray();

            // Paso 3: Añadir la imagen al libro de trabajo y obtener su índice.
            int imagenPosicion = libro.addPicture(imagenBytes, Workbook.PICTURE_TYPE_PNG);

            // Paso 4: Obtener el ayudante de creación para dibujar objetos.
            CreationHelper ayudador = libro.getCreationHelper();
            // Paso 5: Obtener el objeto de dibujo de la hoja.
            Drawing<?> dibujo = hoja.createDrawingPatriarch();
            // Paso 6: Crear un ancla de cliente para posicionar la imagen.
            ClientAnchor anchor = ayudador.createClientAnchor();
            // Paso 6.1: Establecer la columna inicial de la imagen a 0.
            anchor.setCol1(0);
            // Paso 6.2: Establecer la fila inicial de la imagen a la fila actual.
            anchor.setRow1(filaActual);

            // Paso 7: Crear la imagen en Excel utilizando el ancla y la posición de la imagen.
            Picture imagenExcel = dibujo.createPicture(anchor, imagenPosicion);
            // Paso 8: Redimensionar la imagen para que se ajuste a su tamaño original.
            imagenExcel.resize();

            // Paso 9: Desplazar el contador de fila actual basándose en un tamaño aproximado de la imagen (60 filas).
            // Esto asegura que el contenido posterior no se superponga con la imagen.
            filaActual += 60;

        }catch (Exception e){
            // Paso 10: En caso de error, lanzar una RuntimeException con un mensaje descriptivo.
            throw new RuntimeException("Error insertando imagen en Excel", e);
        }
    }


    /**
     * Finaliza la construcción del documento de Excel y lo guarda en la ruta de archivo especificada.
     */
    @Override
    public void finalizarDocumento() {
        try(FileOutputStream salida = new FileOutputStream(rutaArchivo)){
            // Paso 1: Escribir el contenido del libro de trabajo en el archivo de salida.
            libro.write(salida);
            // Paso 2: Cerrar el libro de trabajo para liberar recursos.
            libro.close();
        }catch (Exception e){
            // Paso 3: En caso de error, lanzar una RuntimeException.
            throw new RuntimeException(e);
        }
    }

    /**
     * Genera un gráfico de barras a partir de los datos proporcionados, lo convierte en una imagen
     * y lo agrega al documento de Excel.
     *
     * @param titulo El título del gráfico.
     * @param datos Un mapa con las categorías (String) y sus valores numéricos (Number) para el gráfico.
     */
    @Override
    public void agregarGrafico(String titulo, Map<String, Number> datos) {
        // Paso 1: Verificar si no hay datos para el gráfico. Si no hay, se agrega un mensaje de texto
        // y se finaliza la ejecución del método.
        if(datos == null || datos.isEmpty()){
            agregarTexto("No hay datos para el gráfico: " + titulo);
            return;
        }
        try{
            // Paso 2: Crear un gráfico de barras utilizando el ServicioGeneradorGraficos.
            BarChart<String, Number> grafico = ServicioGeneradorGraficos.crearBarChart(titulo, datos);

            // Paso 3: Crear un contenedor VBox para el gráfico y aplicar estilos para asegurar un fondo blanco
            // y un padding adecuado para la captura de pantalla.
            VBox contenedor = new VBox(grafico);
            contenedor.setStyle("-fx-background-color: white; -fx-padding: 20;");
            // Paso 3.1: Establecer un tamaño preferido para el contenedor del gráfico.
            contenedor.setPrefSize(1000, 750);

            // Paso 4: Forzar el renderizado del contenedor y el gráfico. Esto es crucial para que
            // JavaFX calcule correctamente el layout y los listeners de ServicioGeneradorGraficos se activen,
            // permitiendo una captura de pantalla precisa.
            new Scene(contenedor); // Se necesita una Scene para que el layout se calcule.
            contenedor.applyCss(); // Aplicar CSS.
            contenedor.layout(); // Calcular el layout.

            // Paso 5: Crear parámetros para la captura de pantalla, incluyendo una escala para mejorar la resolución.
            SnapshotParameters params = new SnapshotParameters();
            params.setTransform(javafx.scene.transform.Transform.scale(1.5, 1.5));

            // Paso 6: Realizar una captura de pantalla del contenedor del gráfico.
            WritableImage captura = contenedor.snapshot(params, null);
            // Paso 7: Convertir la imagen capturada (WritableImage) a BufferedImage.
            BufferedImage imagen = SwingFXUtils.fromFXImage(captura, null);
            // Paso 8: Agregar la imagen del gráfico al documento de Excel utilizando el método existente.
            agregarImagen(imagen);
        } catch (Exception e) {
            // Paso 9: En caso de error durante la generación o inserción del gráfico, lanzar una RuntimeException.
            throw new RuntimeException("Error generando gráfico en Excel", e);
        }
    }
}
