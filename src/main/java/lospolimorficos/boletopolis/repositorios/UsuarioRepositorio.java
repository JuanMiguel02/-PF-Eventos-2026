package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import java.util.stream.Collectors;

public final class UsuarioRepositorio {

    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private static UsuarioRepositorio instancia;

    private UsuarioRepositorio() {
        cargarDatosEjemplo();
    }

    public static UsuarioRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioRepositorio();
        }
        return instancia;
    }

    public boolean registrarUsuario(Usuario usuario) {
        if(existeUsuario(usuario.getCorreo())){
            return false;
        }
        return usuarios.add(usuario);
    }

    public boolean eliminarUsuario(Usuario usuario) {
        return usuarios.remove(usuario);
    }

    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        for(int i = 0; i < usuarios.size(); i++) {
            if(usuarios.get(i).getCorreo().equals(usuarioActualizado.getCorreo())) {

                usuarios.set(i, usuarioActualizado);
                return true;
            }
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    public ObservableList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ObservableList<Cliente> getClientes() {
        return usuarios.stream()
                .filter(u -> u instanceof Cliente)
                .map(u -> (Cliente) u)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public boolean existeUsuario(String correo){
        for(Usuario usuario : this.usuarios){
            if(usuario.getCorreo().equals(correo)){
                return true;
            }
        }
        return false;
    }

    public Cliente buscarUsuario(String busqueda){
        return (Cliente) usuarios.stream()
                .filter(usuario ->
                        usuario.getCorreo().equals(busqueda)
                        ||  usuario.getDocumento().equals(busqueda))
                .findFirst()
                .orElse(null);
    }

    public int contarUsuarios(){
        return usuarios.size();
    }

    private void cargarDatosEjemplo() {

        Admin admin1 = new Admin(
                "Sancho",
                "Panza",
                "3123213",
                "sancho@boletopolis.com",
                "412321312",
                "123456"
        );

        registrarUsuario(admin1);

        crearClienteEjemplo(
                "Paco",
                "Jones",
                "42132131",
                "paquito@gmail.com",
                "654321",
                400000000,
                "TARJETA"
        );

        crearClienteEjemplo(
                "Pedro",
                "El Escamoso",
                "3124531",
                "pedrito@gmail.com",
                "5432142",
                100000000,
                "NEQUI"
        );

        crearClienteEjemplo(
                "Ana",
                "Martinez",
                "987654",
                "ana@gmail.com",
                "3001112233",
                5000000,
                "TARJETA"
        );

        crearClienteEjemplo(
                "Laura",
                "Gomez",
                "741852",
                "laura@gmail.com",
                "3015558899",
                800000,
                "NEQUI"
        );

        crearClienteEjemplo(
                "Tom",
                "Yorke",
                "9876122",
                "tom@gmail.com",
                "3201412233",
                8000000,
                "TARJETA"
        );

        crearClienteEjemplo(
                "Armando",
                "Casas",
                "42139123",
                "casas@gmail.com",
                "301578879",
                850000,
                "NEQUI"
        );

        crearClienteEjemplo(
                "Julian",
                "Casablancas",
                "09172321",
                "casablancas@gmail.com",
                "301578000",
                9050000,
                "NEQUI"
        );
    }

    private void crearClienteEjemplo(String nombre, String apellido, String documento, String correo, String telefono, double saldoInicial, String tipoPago) {

        Cliente cliente = new Cliente(nombre, apellido, documento, correo, telefono, documento);
        CuentaSimulada cuenta = new CuentaSimulada(cliente, saldoInicial);

        cliente.agregarCuenta(cuenta);

        MetodoPago metodoPago;

        if(tipoPago.equals("TARJETA")) {
            metodoPago = new PagoTarjeta(cuenta, "Débito");
        } else {
            metodoPago = new PagoNequi(cuenta, telefono);
        }

        cliente.agregarMetodoPago(metodoPago);

        registrarUsuario(cliente);
    }

}
