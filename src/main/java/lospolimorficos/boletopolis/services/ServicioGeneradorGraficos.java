package lospolimorficos.boletopolis.services;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Map;

public class ServicioGeneradorGraficos {

    public static BarChart<String, Number> crearBarChart(String titulo, Map<String, Number> datos) {

        CategoryAxis ejeX = new CategoryAxis();
        NumberAxis ejeY = new NumberAxis();

        BarChart<String, Number> grafico = new BarChart<>(ejeX, ejeY);

        grafico.setTitle(titulo);
        grafico.setPrefSize(800, 600);

        // Configuración general
        grafico.setAnimated(false);
        grafico.setLegendVisible(true);
        grafico.setCategoryGap(30);
        grafico.setBarGap(10);

        grafico.setStyle(
                "-fx-background-color: white;" +
                        "-fx-padding: 30;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;"
        );

        // Estilo de ejes
        ejeX.setTickLabelRotation(30);
        ejeX.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        ejeY.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        ejeX.setLabel("Categoría");
        ejeY.setLabel("Valor");

        // Quitar ruido visual
        grafico.setHorizontalGridLinesVisible(true);
        grafico.setVerticalGridLinesVisible(false);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();

        // Colores vivos para las barras
        String[] colores = {
                "#3498db", // Azul
                "#e74c3c", // Rojo
                "#2ecc71", // Verde
                "#f1c40f", // Amarillo
                "#9b59b6", // Morado
                "#e67e22", // Naranja
                "#1abc9c", // Turquesa
                "#34495e"  // Gris oscuro
        };

        // Agregar datos
        int colorIndex = 0;
        for (Map.Entry<String, Number> entry : datos.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            String color = colores[colorIndex % colores.length];

            // Listener para aplicar estilo y etiqueta cuando el nodo se cree
            data.nodeProperty().addListener((obs, oldNode, node) -> {
                if (node != null) {
                    // 1. Color y bordes
                    node.setStyle("-fx-bar-fill: " + color + "; -fx-background-radius: 5 5 0 0;");

                    // 2. Etiqueta de valor encima
                    if (node instanceof StackPane) {
                        StackPane bar = (StackPane) node;
                        Label label = new Label(data.getYValue().toString());
                        label.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #2c3e50;");
                        label.setTranslateY(-15);
                        bar.getChildren().add(label);
                    }
                }
            });

            serie.getData().add(data);
            colorIndex++;
        }

        grafico.getData().add(serie);

        return grafico;
    }
}
