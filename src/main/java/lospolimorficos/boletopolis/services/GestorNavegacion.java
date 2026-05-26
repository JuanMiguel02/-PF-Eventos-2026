package lospolimorficos.boletopolis.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GestorNavegacion {
    /**
     * Cambia la vista actual de la aplicación.
     *
     * @param stage ventana principal.
     * @param rutaFXML ruta del archivo FXML a cargar.
     */
    public static void cambiarVista(Stage stage, String rutaFXML) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    GestorNavegacion.class.getResource(rutaFXML));

            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     *   Cambia solo el panel interno (Para navegación dentro del Dashboard)
     * Reemplaza de forma segura el contenido del StackPane central sin romper el menú ni los bordes.
     *
     * @param contentPane El contenedor dinámico del Dashboard
     * @param rutaFXML Ruta de la vista interna a cargar
     */
    public static void cambiarVistaInterna(StackPane contentPane, String rutaFXML) {
        if (contentPane == null) {
            System.err.println("[GestorNavegacion] El contentPane de destino es nulo.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GestorNavegacion.class.getResource(rutaFXML));
            Parent vistaInterna = loader.load();

            // Reemplazo limpio de los nodos hijos en el lienzo central
            contentPane.getChildren().clear();
            contentPane.getChildren().add(vistaInterna);
        } catch (Exception e) {
            System.err.println("[GestorNavegacion] Error al cargar vista interna: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }
}

