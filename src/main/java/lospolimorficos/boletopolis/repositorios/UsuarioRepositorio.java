package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.models.Usuario;

import java.util.stream.Collectors;

public final class UsuarioRepositorio {

    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private static UsuarioRepositorio instancia;

    private UsuarioRepositorio() {}

    public static UsuarioRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioRepositorio();
        }
        return instancia;
    }

    public boolean registrarUsuario(Usuario usuario) {
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
}
