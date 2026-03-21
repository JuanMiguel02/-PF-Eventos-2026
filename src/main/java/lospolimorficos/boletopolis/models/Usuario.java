package lospolimorficos.boletopolis.models;

import java.util.UUID;

public abstract class Usuario {
    private final UUID idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String numTelefono;
    private String contrasena;

    public Usuario(String nombre, String apellido, String correo, String numTelefono, String contrasena) {
        this.idUsuario = UUID.randomUUID();
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.numTelefono = numTelefono;
        this.contrasena = contrasena;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
