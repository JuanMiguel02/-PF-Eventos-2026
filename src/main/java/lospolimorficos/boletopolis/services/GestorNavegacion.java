package lospolimorficos.boletopolis.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            e.printStackTrace();
        }
    }
}
