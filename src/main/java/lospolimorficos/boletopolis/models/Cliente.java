package lospolimorficos.boletopolis.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un cliente en el sistema Boletopolis.
 * Extiende la clase {@link Usuario} y implementa {@link EventoObserver} para recibir notificaciones de eventos.
 * Un cliente puede tener compras, métodos de pago y cuentas asociadas.
 */
public class Cliente extends Usuario implements EventoObserver{

    private final List<String> notificaciones = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final List<MetodoPago> metodosPago = new ArrayList<>();
    private final List<CuentaSimulada> cuentas = new ArrayList<>();

    /**
     * Constructor para crear un nuevo Cliente.
     *
     * @param nombre El nombre del cliente.
     * @param apellido El apellido del cliente.
     * @param documento El número de documento de identidad del cliente.
     * @param correo La dirección de correo electrónico del cliente.
     * @param numTelefono El número de teléfono del cliente.
     * @param contrasena La contraseña del cliente.
     */
    public Cliente(String nombre, String apellido,String documento, String correo, String numTelefono, String contrasena) {
        super(nombre, apellido,documento, correo, numTelefono, contrasena);
    }

    /**
     * Recibe una actualización sobre el estado de un evento y la añade a la lista de notificaciones del cliente.
     *
     * @param evento El {@link Evento} que ha cambiado de estado.
     * @param nuevoEstado El nuevo {@link EstadoEvento} del evento.
     */
    @Override
    public void actualizarEvento(Evento evento, EstadoEvento nuevoEstado) {
        String mensaje = "El evento " + evento.getNombre() +
                " ahora está en estado: " + nuevoEstado;
        notificaciones.add(mensaje);
        System.out.println("Notificación para " + getNombreCompleto() + ": " + mensaje);
    }

    /**
     * Agrega un método de pago a la lista de métodos de pago del cliente.
     *
     * @param metodo El {@link MetodoPago} a agregar.
     */
    public void agregarMetodoPago(MetodoPago metodo){
        metodosPago.add(metodo);
    }

    /**
     * Agrega una cuenta simulada a la lista de cuentas del cliente.
     *
     * @param cuenta La {@link CuentaSimulada} a agregar.
     */
    public void agregarCuenta(CuentaSimulada cuenta){
        cuentas.add(cuenta);
    }

    /**
     * Obtiene la lista de métodos de pago del cliente.
     *
     * @return Una lista de {@link MetodoPago}.
     */
    public List<MetodoPago> getMetodosPago() {
        return this.metodosPago;
    }

    /**
     * Obtiene la lista de notificaciones del cliente.
     *
     * @return Una lista de mensajes de notificación (String).
     */
    public List<String> getNotificaciones() {
        return this.notificaciones;
    }

    /**
     * Obtiene la lista de compras realizadas por el cliente.
     *
     * @return Una lista de {@link Compra}.
     */
    public List<Compra> getCompras(){
        return this.compras;
    }

    /**
     * Agrega una compra a la lista de compras del cliente.
     *
     * @param compra La {@link Compra} a agregar.
     */
    public void agregarCompra(Compra compra){
        this.compras.add(compra);
    }
}
