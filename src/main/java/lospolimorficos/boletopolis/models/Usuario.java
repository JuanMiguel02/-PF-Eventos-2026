package lospolimorficos.boletopolis.models;

import java.util.UUID;

/**
 * Clase abstracta base para representar a un usuario en el sistema Boletopolis.
 * Contiene información básica de identificación y contacto.
 */
public abstract class Usuario {
    private final UUID idUsuario;
    private String nombre;
    private String apellido;
    private String documento;
    private String correo;
    private String numTelefono;
    private String contrasena;

    /**
     * Constructor para crear un nuevo Usuario.
     *
     * @param nombre El nombre del usuario.
     * @param apellido El apellido del usuario.
     * @param documento El número de documento de identidad del usuario.
     * @param correo La dirección de correo electrónico del usuario.
     * @param numTelefono El número de teléfono del usuario.
     * @param contrasena La contraseña del usuario.
     */
    public Usuario(String nombre, String apellido, String documento, String correo, String numTelefono, String contrasena) {
        this.idUsuario = UUID.randomUUID();
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.correo = correo;
        this.numTelefono = numTelefono;
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el ID único del usuario.
     *
     * @return El UUID del usuario.
     */
    public UUID getIdUsuario() {
        return idUsuario;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return El apellido del usuario.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Obtiene la dirección de correo electrónico del usuario.
     *
     * @return El correo del usuario.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     *
     * @return El número de teléfono del usuario.
     */
    public String getNumTelefono() {
        return numTelefono;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return La contraseña del usuario.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Obtiene el nombre completo del usuario (nombre y apellido).
     *
     * @return El nombre completo del usuario.
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nuevo nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param apellido El nuevo apellido del usuario.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Establece la dirección de correo electrónico del usuario.
     *
     * @param correo La nueva dirección de correo electrónico.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Establece el número de teléfono del usuario.
     *
     * @param numTelefono El nuevo número de teléfono.
     */
    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasena La nueva contraseña.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el número de documento de identidad del usuario.
     *
     * @return El documento del usuario.
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Establece el número de documento de identidad del usuario.
     *
     * @param documento El nuevo número de documento.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
