package lospolimorficos.boletopolis.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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


public class ConstructorReporteUI implements ConstructorReporte{

    private VBox contenedor;

    public VBox getVista(){
        return contenedor;
    }

    @Override
    public void iniciarDocumento(String rutaArchivo) {
        contenedor = new VBox(10);
    }

    @Override
    public void agregarTitulo(String titulo) {
        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        contenedor.getChildren().add(tituloLabel);
    }

    @Override
    public void agregarSubtitulo(String subtitulo) {
        Label subtituloLabel = new Label(subtitulo);
        subtituloLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        contenedor.getChildren().add(subtituloLabel);
    }

    @Override
    public void agregarTexto(String texto) {
        contenedor.getChildren().add(new Label(texto));
    }

    @Override
    public void agregarTabla(List<String[]> datos) {
        TableView<ObservableList<String>> tabla = new TableView<>();

        if(datos == null || datos.isEmpty()) return;

        int numColumnas = datos.getFirst().length;

        //crear columnas dinámicamente
        for(int i=0; i<numColumnas; i++){
            final int indiceColumna = i;

            TableColumn<ObservableList<String>, String> columna = new TableColumn<>("Columna " + (i+1));
            columna.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(indiceColumna)));
            tabla.getColumns().add(columna);
        }

        //Agregar filas
        ObservableList<ObservableList<String>> filas = FXCollections.observableArrayList();

        for(String[] fila : datos){
            ObservableList<String> filaObservable = FXCollections.observableArrayList(fila);
            filas.add(filaObservable);
        }

        tabla.setItems(filas);
        tabla.columnResizePolicyProperty().setValue(TableView.CONSTRAINED_RESIZE_POLICY);
        contenedor.getChildren().add(tabla);
    }

    @Override
    public void agregarImagen(BufferedImage imagen) {

        if(imagen == null) return;

        Image imagenFx = SwingFXUtils.toFXImage(imagen, null);
        ImageView imageView = new ImageView(imagenFx);

        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-alignment: center;");
        contenedor.getChildren().add(imageView);

    }

    @Override
    public void agregarGrafico(String titulo, Map<String, Number> datos) {

        BarChart<String, Number> grafico = ServicioGeneradorGraficos.crearBarChart(titulo, datos);
        contenedor.getChildren().add(grafico);
    }

    @Override
    public void finalizarDocumento() {
        if (contenedor.getChildren().isEmpty()) {
            contenedor.getChildren().add(new Label("Reporte vacío"));
        }
    }
}
