package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import lospolimorficos.boletopolis.models.Compra;
import lospolimorficos.boletopolis.models.Entrada;
import lospolimorficos.boletopolis.models.ServicioAdicional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiciosAdicionalesController {

    @FXML
    private Label lblInfoCompra;
    @FXML
    private FlowPane flowEntradas;
    @FXML
    private FlowPane flowServicios;
    @FXML
    private Label lblTotalResumen;

    private Compra compra;
    private final List<CheckBox> checkBoxesServicios = new ArrayList<>();
    private Runnable onConfirmar;

    public void setCompra(Compra compra, Runnable onConfirmar) {
        this.compra = compra;
        this.onConfirmar = onConfirmar;
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
        lblInfoCompra.setText(String.format("Cliente: %s | Entradas: %d",
                compra.getCliente().getNombreCompleto(),
                compra.getCantidadEntradas()));
        actualizarPrecioTemporal();
    }

    private void configurarServicios() {
        flowServicios.getChildren().clear();
        checkBoxesServicios.clear();

        for (ServicioAdicional servicio : ServicioAdicional.values()) {
            CheckBox cb = new CheckBox(servicio.name() + " ($" + servicio.getPrecio() + ")");
            cb.setUserData(servicio);
            
            if (compra.getServicios().contains(servicio)) {
                cb.setSelected(true);
            }
            
            cb.selectedProperty().addListener((obs, wasSelected, isSelected) -> actualizarPrecioTemporal());
            
            checkBoxesServicios.add(cb);
            flowServicios.getChildren().add(cb);
        }
    }

    private void actualizarPrecioTemporal() {
        double totalEntradas = compra.calcularTotalEntradas();
        double totalServicios = 0;
        for (CheckBox cb : checkBoxesServicios) {
            if (cb.isSelected()) {
                totalServicios += ((ServicioAdicional) cb.getUserData()).getPrecio();
            }
        }
        lblTotalResumen.setText(String.format("Total: $%.2f", totalEntradas + totalServicios));
    }

    @FXML
    private void confirmarServicios() {
        compra.getServicios().clear();
        for (CheckBox cb : checkBoxesServicios) {
            if (cb.isSelected()) {
                compra.agregarServicio((ServicioAdicional) cb.getUserData());
            }
        }
        if (onConfirmar != null) {
            onConfirmar.run();
        }
        ((Stage) lblTotalResumen.getScene().getWindow()).close();
    }
}
