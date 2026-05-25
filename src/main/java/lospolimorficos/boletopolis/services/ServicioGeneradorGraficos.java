package lospolimorficos.boletopolis.services;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;


import java.util.Map;

/**
 * Servicio encargado de generar gráficos de barras utilizando JavaFX.
 * Proporciona métodos estáticos para crear y configurar {@link BarChart}
 * con estilos personalizados y etiquetas de valor.
 */
public class ServicioGeneradorGraficos {

    /**
     * Crea un gráfico de barras (BarChart) a partir de un título y un conjunto de datos.
     * El gráfico se configura con estilos visuales, colores dinámicos para las barras
     * y etiquetas de valor encima de cada barra.
     *
     * @param titulo El título que se mostrará en el gráfico.
     * @param datos Un {@link Map} donde las claves son las categorías (String) para el eje X
     *              y los valores son los datos numéricos (Number) para el eje Y.
     * @return Una instancia de {@link BarChart} configurada y lista para ser mostrada.
     */
    public static BarChart<String, Number> crearBarChart(String titulo, Map<String, Number> datos) {
        // Paso 1: Crear los ejes del gráfico.
        // CategoryAxis para el eje X (categorías como nombres de eventos, meses, etc.).
        CategoryAxis ejeX = new CategoryAxis();
        // NumberAxis para el eje Y (valores numéricos como ventas, ocupación, etc.).
        NumberAxis ejeY = new NumberAxis();

        // Paso 2: Crear una nueva instancia de BarChart con los ejes definidos.
        BarChart<String, Number> grafico = new BarChart<>(ejeX, ejeY);

        // Paso 3: Establecer el título del gráfico.
        grafico.setTitle(titulo);
        // Paso 4: Establecer el tamaño preferido del gráfico.
        grafico.setPrefSize(800, 600);

        // Paso 5: Configuración general del gráfico.
        // Deshabilitar animaciones para una renderización más rápida y consistente.
        grafico.setAnimated(false);
        // Ocultar la leyenda, ya que cada barra tendrá su propia etiqueta de valor.
        grafico.setLegendVisible(false);
        // Establecer el espacio entre categorías en el eje X.
        grafico.setCategoryGap(30);
        // Establecer el espacio entre las barras dentro de una misma categoría (si hubiera varias series).
        grafico.setBarGap(10);

        // Paso 6: Aplicar estilos CSS al contenedor del gráfico.
        grafico.setStyle(
                "-fx-background-color: white;" + // Fondo blanco.
                        "-fx-padding: 30;" +           // Relleno interno.
                        "-fx-border-color: #d0d0d0;" +  // Color del borde.
                        "-fx-border-width: 1;" +        // Ancho del borde.
                        "-fx-border-radius: 10;"        // Radio del borde para esquinas redondeadas.
        );

        // Paso 7: Estilo de los ejes.
        // Aplicar estilos CSS a las etiquetas de los ejes para fuente, tamaño, peso y color.
        ejeX.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        ejeY.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        // Paso 8: Establecer las etiquetas de los ejes.
        ejeX.setLabel("Categoría");
        ejeY.setLabel("Valor");

        // Paso 9: Quitar ruido visual del gráfico.
        // Mostrar líneas de cuadrícula horizontales para facilitar la lectura de valores.
        grafico.setHorizontalGridLinesVisible(true);
        // Ocultar líneas de cuadrícula verticales para un aspecto más limpio.
        grafico.setVerticalGridLinesVisible(false);

        // Paso 10: Crear una serie de datos para el gráfico.
        XYChart.Series<String, Number> serie = new XYChart.Series<>();

        // Paso 11: Definir una paleta de colores vivos para las barras.
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

        // Paso 12: Iterar sobre los datos proporcionados para añadir cada punto al gráfico.
        int colorIndex = 0;
        for (Map.Entry<String, Number> entry : datos.entrySet()) {
            // Crear un objeto XYChart.Data para cada par clave-valor.
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            // Seleccionar un color de la paleta de forma cíclica.
            String color = colores[colorIndex % colores.length];

            // Paso 13: Añadir un listener a la propiedad 'node' de cada dato.
            // Este listener se activa cuando el nodo visual de la barra es creado por JavaFX.
            data.nodeProperty().addListener((obs, oldNode, node) -> {
                if (node != null) {
                    // Paso 13.1: Aplicar el color de fondo y el radio del borde a la barra.
                    node.setStyle("-fx-bar-fill: " + color + "; -fx-background-radius: 5 5 0 0;");

                    // Paso 13.2: Añadir una etiqueta de valor encima de cada barra.
                    if (node instanceof StackPane) {
                        StackPane bar = (StackPane) node;
                        // Crear una etiqueta con el valor numérico del dato.
                        Label label = new Label(data.getYValue().toString());
                        // Aplicar estilos a la etiqueta.
                        label.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #2c3e50;");
                        // Desplazar la etiqueta hacia arriba para que quede encima de la barra.
                        label.setTranslateY(-15);
                        // Añadir la etiqueta al StackPane de la barra.
                        bar.getChildren().add(label);
                    }
                }
            });

            // Paso 14: Añadir el dato a la serie.
            serie.getData().add(data);
            // Incrementar el índice de color para la siguiente barra.
            colorIndex++;
        }

        // Paso 15: Añadir la serie de datos al gráfico.
        grafico.getData().add(serie);

        // Paso 16: Devolver el gráfico de barras configurado.
        return grafico;
    }
}
