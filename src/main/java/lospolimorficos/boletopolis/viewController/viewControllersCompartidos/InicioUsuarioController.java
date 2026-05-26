package lospolimorficos.boletopolis.viewController.viewControllersCompartidos;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Controlador encargado de la vista
 * principal de inicio del usuario.
 *
 * Gestiona los accesos rápidos
 * hacia las diferentes secciones
 * del sistema.
 */
public class InicioUsuarioController {

    @FXML
    private StackPane contentPane;

    /**
     * Establece el contenedor principal
     * donde se cargarán dinámicamente
     * las vistas.
     *
     * @param contentPane Panel principal.
     */
    public void setContentPane(StackPane contentPane) {

        this.contentPane = contentPane;
    }

    /**
     * Navega hacia la vista de eventos.
     */
    @FXML
    private void mostrarEventos() {

        cargarVista(
                "/lospolimorficos/boletopolis/view.userViews/eventosUsuario.fxml"
        );
    }

    /**
     * Navega hacia la vista de compras.
     */
    @FXML
    private void mostrarCompras() {

        cargarVista(
                "/lospolimorficos/boletopolis/view.userViews/detallesComprasUsuarios.fxml"
        );
    }

    /**
     * Navega hacia la vista de perfil.
     */
    @FXML
    private void mostrarPerfil() {

        cargarVista(
                "/lospolimorficos/boletopolis/view.userViews/perfilUsuario.fxml"
        );
    }

    /**
     * Carga dinámicamente una vista
     * dentro del panel principal.
     *
     * @param rutaFXML Ruta del archivo FXML.
     */
    private void cargarVista(String rutaFXML) {

        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(rutaFXML)
                    );
            Parent vista = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(vista);
        } catch (IOException e) {e.printStackTrace();
        }
    }
}


