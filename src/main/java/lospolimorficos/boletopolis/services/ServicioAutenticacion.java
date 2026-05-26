package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.Usuario;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

public class ServicioAutenticacion {
    private final UsuarioRepositorio usuarioRepositorio;

    /**
     * Constructor del servicio de autenticación.
     */
    public ServicioAutenticacion() {
        this.usuarioRepositorio =
                UsuarioRepositorio.getInstancia();
    }

    /**
     * Autentica un usuario usando correo y contraseña.
     *
     * @param correo correo ingresado.
     * @param password contraseña ingresada.
     * @return usuario autenticado o null si las credenciales son incorrectas.
     */
    public Usuario iniciarSesion(
            String correo,
            String password
    ) {

        for (Usuario usuario :
                usuarioRepositorio.getUsuarios()) {

            if (usuario.getCorreo().equals(correo)
                    && usuario.getContrasena().equals(password)) {

                return usuario;
            }
        }

        return null;
    }
}
