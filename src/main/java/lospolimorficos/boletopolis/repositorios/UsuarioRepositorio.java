package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Admin;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.models.Usuario;

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

    private void cargarDatosEjemplo(){

        Admin admin1 = new Admin("Sancho", "Panza", "3123213","sancho@boletopolis.com", "412321312", "123456");
        Cliente cliente1 = new Cliente("Paco", "Jones", "42132131", "paquito@gmail.com", "654321", "42132131");
        Cliente cliente2 = new Cliente("Pedro", "El Escamoso", "3124531", "pedrito@gmail.com", "5432142", "3124531");

        registrarUsuario(admin1);
        registrarUsuario(cliente1);
        registrarUsuario(cliente2);

    }

}
