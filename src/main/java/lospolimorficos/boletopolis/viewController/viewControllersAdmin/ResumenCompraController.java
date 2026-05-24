package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import lospolimorficos.boletopolis.models.Compra;
import lospolimorficos.boletopolis.models.Entrada;
import lospolimorficos.boletopolis.models.ServicioAdicional;

import java.io.IOException;

public class ResumenCompraController {

    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblInfoCompra;
    @FXML
    private FlowPane flowEntradas;
    @FXML
    private VBox vboxServicios;
    @FXML
    private FlowPane flowServicios;
    @FXML
    private Label lblTotalResumen;

    private Compra compra;

    public void setCompra(Compra compra) {
        setCompra(compra, "¡Compra Exitosa!");
    }

    public void setCompra(Compra compra, String titulo) {
        this.compra = compra;
        lblTitulo.setText(titulo);
        actualizarInfo();

        flowEntradas.getChildren().clear();
        for (Entrada entrada : compra.getEntradas()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lospolimorficos/boletopolis/views/adminViews/entradaCard.fxml"));
                Parent card = loader.load();
                EntradaCardController controller = loader.getController();
                controller.setEntrada(entrada, compra.getEvento());
                flowEntradas.getChildren().add(card);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        configurarServicios();
    }

    private void actualizarInfo() {
        lblInfoCompra.setText(String.format("Cliente: %s | Entradas: %d | Pago: %s",
                compra.getCliente().getNombreCompleto(),
                compra.getCantidadEntradas(),
                compra.getPago() != null ? compra.getPago().getMetodoPago().getDescripcion() : "N/A"));
        lblTotalResumen.setText(String.format("Total: $%.2f", compra.getTotalCompra()));
    }

    private void configurarServicios() {
        flowServicios.getChildren().clear();

        if (compra.getServicios() == null || compra.getServicios().isEmpty()) {
            vboxServicios.setVisible(false);
            vboxServicios.setManaged(false);
            return;
        }

        for (ServicioAdicional servicio : compra.getServicios()) {
            Label lbl = new Label("- " + servicio.name() + " ($" + servicio.getPrecio() + ")");
            lbl.setStyle("-fx-font-size: 14;");
            flowServicios.getChildren().add(lbl);
        }
    }
}
