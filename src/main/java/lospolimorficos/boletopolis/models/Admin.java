package lospolimorficos.boletopolis.models;

/**
 * Representa un administrador del sistema Boletopolis.
 * Extiende la clase {@link Usuario}, heredando sus propiedades y comportamientos básicos.
 */
public class Admin extends Usuario{

    /**
     * Constructor para crear un nuevo Administrador.
     *
     * @param nombre El nombre del administrador.
     * @param apellido El apellido del administrador.
     * @param documento El número de documento de identidad del administrador.
     * @param correo La dirección de correo electrónico del administrador.
     * @param numTelefono El número de teléfono del administrador.
     * @param contrasena La contraseña del administrador.
     */
    public Admin(String nombre, String apellido, String documento, String correo, String numTelefono, String contrasena) {
        super(nombre, apellido, documento, correo, numTelefono, contrasena);
    }
}
