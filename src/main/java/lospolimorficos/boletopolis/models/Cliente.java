package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario implements EventoObserver{

    private final List<String> notificaciones = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final List<MetodoPago> metodosPago = new ArrayList<>();
    private final List<CuentaSimulada> cuentas = new ArrayList<>();

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

    public void agregarMetodoPago(MetodoPago metodo){
        metodosPago.add(metodo);
    }

    public void agregarCuenta(CuentaSimulada cuenta){
        cuentas.add(cuenta);
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
