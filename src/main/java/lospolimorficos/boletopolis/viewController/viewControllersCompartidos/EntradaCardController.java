package lospolimorficos.boletopolis.viewController.viewControllersCompartidos;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lospolimorficos.boletopolis.models.Entrada;
import lospolimorficos.boletopolis.models.Evento;

import java.time.format.DateTimeFormatter;

public class EntradaCardController {

    @FXML
    private Label lblEvento;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblLugar;
    @FXML
    private Label lblZona;
    @FXML
    private Label lblAsiento;
    @FXML
    private Label lblPrecio;
    @FXML
    private Label lblIdEntrada;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setEntrada(Entrada entrada, Evento evento) {
        lblEvento.setText(evento.getNombre());
        lblFecha.setText(evento.getFechaYHora().format(formatter));
        lblLugar.setText(evento.getRecinto().getNombre() + ", " + evento.getCiudad());
        lblZona.setText("Zona: " + entrada.getZona().getNombre());
        lblAsiento.setText("Asiento: " + entrada.getAsiento().getIdAsiento());
        lblPrecio.setText(String.format("$%.2f", entrada.getPrecioFinal()));
        lblIdEntrada.setText("ID: " + entrada.getIdEntrada().toString().substring(0, 8));
    }
}
