package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Usuario;

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

    public ObservableList<Usuario> getUsuarios() {
        return usuarios;
    }
}
