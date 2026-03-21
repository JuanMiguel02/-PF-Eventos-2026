package lospolimorficos.boletopolis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL fxml = HelloApplication.class.getResource("/lospolimorficos/boletopolis/views/adminViews/dashboardAdmin.fxml");
        if (fxml == null) {
            throw new IllegalStateException("FXML no encontrado: /lospolimorficos.boletopolis/views.adminViews/dashboardAdmin.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxml);
        Scene scene = new Scene(fxmlLoader.load(), 1200, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
