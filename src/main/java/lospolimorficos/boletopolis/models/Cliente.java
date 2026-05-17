package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario implements EventoObserver{
    private List<String> notificaciones = new ArrayList<>();
    private List<Compra> compras = new ArrayList<>();

    public Cliente(String nombre, String apellido,String documento, String correo, String numTelefono, String contrasena) {
        super(nombre, apellido,documento, correo, numTelefono, contrasena);
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

    public List<Compra> getCompras(){
        return this.compras;
    }

    public void agregarCompra(Compra compra){
        this.compras.add(compra);
    }
}
