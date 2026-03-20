package lospolimorficos.boletopolis.models;

public class Cliente extends Usuario{

    public Cliente(String nombre, String apellido, String correo, String numTelefono, String contrasena) {
        super(nombre, apellido, correo, numTelefono, contrasena, RolUsuario.CLIENTE);
    }
}
