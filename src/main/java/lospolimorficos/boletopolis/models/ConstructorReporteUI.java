package lospolimorficos.boletopolis.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.services.ServicioGeneradorGraficos;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Implementación de {@link ConstructorReporte} que construye un reporte visualmente
 * en una interfaz de usuario JavaFX. Este constructor permite añadir títulos, subtítulos,
 * texto, tablas, imágenes y gráficos a un contenedor {@link VBox}.
 */
public class ConstructorReporteUI implements ConstructorReporte{

    /**
     * Contenedor principal de JavaFX donde se añadirán todos los elementos del reporte.
     */
    private VBox contenedor;

    /**
     * Obtiene la vista principal del reporte construida.
     *
     * @return El {@link VBox} que contiene todos los elementos visuales del reporte.
     */
    public VBox getVista(){
        return contenedor;
    }

    /**
     * Inicializa el documento del reporte creando un nuevo {@link VBox} como contenedor principal.
     * La ruta del archivo no se utiliza en esta implementación ya que el reporte es visual.
     *
     * @param rutaArchivo La ruta del archivo (no utilizada en esta implementación UI).
     */
    @Override
    public void iniciarDocumento(String rutaArchivo) {
        // Paso 1: Inicializar el contenedor VBox que albergará todos los elementos del reporte.
        // Se establece un espaciado de 10 píxeles entre los elementos.
        contenedor = new VBox(10);
    }

    /**
     * Agrega un título al reporte. El título se muestra con un estilo de fuente grande y en negrita.
     *
     * @param titulo El texto del título a agregar.
     */
    @Override
    public void agregarTitulo(String titulo) {
        // Paso 1: Crear una nueva etiqueta (Label) con el texto del título.
        Label tituloLabel = new Label(titulo);
        // Paso 2: Aplicar estilos CSS a la etiqueta para que se vea como un título principal.
        // Se define un tamaño de fuente de 18px y un peso de fuente en negrita.
        tituloLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        // Paso 3: Añadir la etiqueta del título al contenedor principal del reporte.
        contenedor.getChildren().add(tituloLabel);
    }

    /**
     * Agrega un subtítulo al reporte. El subtítulo se muestra con un estilo de fuente ligeramente más pequeño y en negrita.
     *
     * @param subtitulo El texto del subtítulo a agregar.
     */
    @Override
    public void agregarSubtitulo(String subtitulo) {
        // Paso 1: Crear una nueva etiqueta (Label) con el texto del subtítulo.
        Label subtituloLabel = new Label(subtitulo);
        // Paso 2: Aplicar estilos CSS a la etiqueta para que se vea como un subtítulo.
        // Se define un tamaño de fuente de 14px y un peso de fuente en negrita.
        subtituloLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        // Paso 3: Añadir la etiqueta del subtítulo al contenedor principal del reporte.
        contenedor.getChildren().add(subtituloLabel);
    }

    /**
     * Agrega un párrafo de texto simple al reporte.
     *
     * @param texto El contenido del texto a agregar.
     */
    @Override
    public void agregarTexto(String texto) {
        // Paso 1: Crear una nueva etiqueta (Label) con el texto proporcionado.
        // Esta etiqueta se añade directamente al contenedor sin estilos adicionales,
        // asumiendo que es texto de contenido general.
        contenedor.getChildren().add(new Label(texto));
    }

    /**
     * Agrega una tabla al reporte. La tabla se construye dinámicamente con las columnas y datos proporcionados.
     *
     * @param datos Una lista de arrays de String, donde cada array representa una fila de la tabla.
     * @param columnas Una lista de String que contiene los nombres de las columnas de la tabla.
     */
    @Override
    public void agregarTabla(List<String[]> datos, List<String> columnas) {
        // Paso 1: Crear una nueva instancia de TableView para mostrar los datos tabulares.
        TableView<ObservableList<String>> tabla = new TableView<>();
        // Paso 2: Deshabilitar la edición de la tabla, ya que es solo para visualización de reportes.
        tabla.setEditable(false);

        // Paso 3: Validar si las columnas o los datos son nulos o si los datos están vacíos.
        // Si alguna de estas condiciones se cumple, no se puede construir la tabla, por lo que se retorna.
        if(columnas == null || datos == null || datos.isEmpty()) return;

        // Paso 4: Iterar sobre la lista de nombres de columnas para crear las TableColumn correspondientes.
        for(int i = 0; i < columnas.size(); i++){
            final int indiceColumna = i; // Se usa una variable final para el lambda.

            // Paso 4.1: Crear una nueva TableColumn con el nombre de la columna actual.
            TableColumn<ObservableList<String>, String> columna = new TableColumn<>(columnas.get(i));
            // Paso 4.2: Definir cómo se obtendrá el valor para cada celda de esta columna.
            // Se usa un SimpleStringProperty para enlazar el valor de la celda con el elemento
            // correspondiente en la ObservableList que representa la fila.
            columna.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(indiceColumna)));
            // Paso 4.3: Añadir la columna creada a la tabla.
            tabla.getColumns().add(columna);
        }

        // Paso 5: Preparar los datos para las filas de la tabla.
        // Se crea una ObservableList de ObservableList<String> para almacenar todas las filas.
        ObservableList<ObservableList<String>> filas = FXCollections.observableArrayList();

        // Paso 6: Iterar sobre los datos proporcionados (List<String[]>) para convertirlos en el formato
        // requerido por la TableView (ObservableList<String> por fila).
        for(String[] fila : datos){
            // Paso 6.1: Convertir cada array de String (fila) en una ObservableList<String>.
            ObservableList<String> filaObservable = FXCollections.observableArrayList(fila);
            // Paso 6.2: Añadir la fila observable a la lista de filas de la tabla.
            filas.add(filaObservable);
        }

        // Paso 7: Establecer los datos de las filas en la tabla.
        tabla.setItems(filas);
        // Paso 8: Configurar la política de redimensionamiento de columnas para que se ajusten al ancho disponible.
        tabla.columnResizePolicyProperty().setValue(TableView.CONSTRAINED_RESIZE_POLICY);
        // Paso 9: Añadir la tabla completa al contenedor principal del reporte.
        contenedor.getChildren().add(tabla);
    }

    /**
     * Agrega una imagen al reporte. La imagen se redimensiona para ajustarse a un ancho fijo
     * manteniendo su relación de aspecto.
     *
     * @param imagen La imagen de tipo {@link BufferedImage} a agregar.
     */
    @Override
    public void agregarImagen(BufferedImage imagen) {
        // Paso 1: Verificar si la imagen proporcionada es nula. Si lo es, no se hace nada.
        if(imagen == null) return;

        // Paso 2: Convertir la imagen de BufferedImage (AWT) a Image (JavaFX).
        Image imagenFx = SwingFXUtils.toFXImage(imagen, null);
        // Paso 3: Crear un ImageView para mostrar la imagen en la interfaz de usuario.
        ImageView imageView = new ImageView(imagenFx);

        // Paso 4: Establecer el ancho deseado para la imagen.
        imageView.setFitWidth(400);
        // Paso 5: Mantener la relación de aspecto de la imagen al redimensionarla.
        imageView.setPreserveRatio(true);
        // Paso 6: Aplicar un estilo para centrar la imagen (aunque esto puede depender del layout del VBox).
        imageView.setStyle("-fx-alignment: center;");
        // Paso 7: Añadir el ImageView al contenedor principal del reporte.
        contenedor.getChildren().add(imageView);
    }

    /**
     * Agrega un gráfico de barras al reporte. El gráfico se genera utilizando un servicio externo
     * y se añade al contenedor.
     *
     * @param titulo El título del gráfico.
     * @param datos Un mapa donde las claves son las categorías y los valores son los datos numéricos.
     */
    @Override
    public void agregarGrafico(String titulo, Map<String, Number> datos) {
        // Paso 1: Utilizar el ServicioGeneradorGraficos para crear un BarChart a partir del título y los datos.
        BarChart<String, Number> grafico = ServicioGeneradorGraficos.crearBarChart(titulo, datos);
        // Paso 2: Añadir el gráfico generado al contenedor principal del reporte.
        contenedor.getChildren().add(grafico);
    }

    /**
     * Finaliza la construcción del documento. Si el contenedor está vacío al finalizar,
     * se añade un mensaje indicando que el reporte está vacío.
     */
    @Override
    public void finalizarDocumento() {
        // Paso 1: Verificar si el contenedor VBox no tiene ningún elemento hijo.
        // Esto significa que no se añadió ningún contenido al reporte.
        if (contenedor.getChildren().isEmpty()) {
            // Paso 2: Si el contenedor está vacío, añadir una etiqueta indicando que el reporte está vacío.
            contenedor.getChildren().add(new Label("Reporte vacío"));
        }
    }
}
