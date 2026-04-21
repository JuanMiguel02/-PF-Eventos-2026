package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

public class ClienteController {

    private final UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstancia();

    public boolean registrarCliente(Cliente cliente){
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
