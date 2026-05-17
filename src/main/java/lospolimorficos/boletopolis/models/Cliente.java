package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario implements EventoObserver{
    private String numDocumento;
    private List<String> notificaciones = new ArrayList<>();

    public Cliente(String nombre, String apellido, String correo, String numTelefono, String contrasena, String numDocumento) {
        super(nombre, apellido, correo, numTelefono, contrasena);
        this.numDocumento = numDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    @Override
    public void actualizarEvento(Evento evento, EstadoEvento nuevoEstado) {
        String mensaje = "El evento " + evento.getNombre() +
                " ahora está en estado: " + nuevoEstado;
        notificaciones.add(mensaje);
        System.out.println("Notificación para " + getNombreCompleto() + ": " + mensaje);
    }

    public List<String> getNotificaciones() {
        return this.notificaciones;
    }
}
