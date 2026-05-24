package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.models.CuentaSimulada;
import lospolimorficos.boletopolis.models.MetodoPago;
import lospolimorficos.boletopolis.models.PagoTarjeta;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

import java.util.List;

public class ClienteController {

    private final UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstancia();

    public List<Cliente> filtrarClientes(List<Cliente> clientes, String filtro){
        if(filtro == null || filtro.isEmpty()){
            return clientes;
        }
        String filtroLimpio = filtro.toLowerCase();
        return clientes.stream()
                .filter(cliente -> cliente.getNombreCompleto().toLowerCase().contains(filtroLimpio)
                                || cliente.getDocumento().contains(filtroLimpio)
                                || cliente.getCorreo().toLowerCase().contains(filtroLimpio)
                                || cliente.getIdUsuario().toString().contains(filtroLimpio)
                                || cliente.getNumTelefono().contains(filtroLimpio)
                )
                .toList();
    }

    public Cliente buscarCliente(String busqueda){
        return usuarioRepositorio.buscarUsuario(busqueda);
    }

    public boolean registrarCliente(Cliente cliente){

        CuentaSimulada cuentaPrincipal = new CuentaSimulada(cliente, 500000000);
        cliente.agregarCuenta(cuentaPrincipal);
        MetodoPago tarjeta = new PagoTarjeta(cuentaPrincipal, "VISA");
        cliente.agregarMetodoPago(tarjeta);

        return usuarioRepositorio.registrarUsuario(cliente);
    }

    public boolean eliminarCliente(Cliente cliente){
        return usuarioRepositorio.eliminarUsuario(cliente);
    }

    public boolean actualizarCliente(Cliente clienteActualizado){
        return usuarioRepositorio.actualizarUsuario(clienteActualizado);
    }

    public ObservableList<Cliente> getClientes(){
        return usuarioRepositorio.getClientes();
    }

}
