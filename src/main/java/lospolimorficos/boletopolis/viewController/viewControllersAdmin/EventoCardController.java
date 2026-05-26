package lospolimorficos.boletopolis.viewController.viewControllersAdmin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lospolimorficos.boletopolis.models.Evento;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class EventoCardController {

    @FXML
    private ImageView ivImagen;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblDescripcion;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblLugar;
    @FXML
    private Label lblReembolsable;

    private Evento evento;
    private Consumer<Evento> onComprarAction;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setEvento(Evento evento, Consumer<Evento> onComprarAction) {
        this.evento = evento;
        this.onComprarAction = onComprarAction;

        lblNombre.setText(evento.getNombre());
        lblDescripcion.setText(evento.getDescripcion());
        lblFecha.setText(evento.getFechaYHora().format(formatter));
        lblLugar.setText(evento.getCiudad().toString() + ", " + evento.getRecinto().getNombre());

        lblReembolsable.setVisible(evento.permiteReembolso());
        lblReembolsable.setManaged(evento.permiteReembolso());

        if (evento.getRutaImagen() != null && !evento.getRutaImagen().isEmpty()) {
            try {
                String ruta = evento.getRutaImagen();
                Image imagen;
                //Imágenes internas
                if(ruta.startsWith("/")){
                    var recurso = getClass().getResource(ruta);
                    if(recurso != null){
                        imagen = new Image(recurso.toExternalForm());
                    }else{
                        throw new Exception("Recurso no encontrado: " + ruta);
                    }
                }else{ //Imágenes externas
                    imagen = new Image(ruta);
                }
                ivImagen.setImage(imagen);
                System.out.println("SET EVENTO");
                System.out.println(lblNombre);
                System.out.println(evento.getNombre());
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen en la tarjeta: " + e.getMessage());
            }
        }
    }

    @FXML
    private void comprarEntrada() {
        if (onComprarAction != null) {
            onComprarAction.accept(evento);
        }
    }
}
