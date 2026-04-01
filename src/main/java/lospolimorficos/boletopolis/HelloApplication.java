package lospolimorficos.boletopolis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class HelloApplication extends Application {

    private double x =0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("/lospolimorficos/boletopolis/views/adminViews/dashboardAdmin.fxml")));

        Scene scene = new Scene(root, 1200,720);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();

        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

            stage.setOpacity(0.8);
        });

        root.setOnMouseReleased(event -> {
            stage.setOpacity(1);
        });

        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setTitle("Sistema de Gestión de Bicicletas");
        stage.setScene(scene);
        stage.show();
    }
}
