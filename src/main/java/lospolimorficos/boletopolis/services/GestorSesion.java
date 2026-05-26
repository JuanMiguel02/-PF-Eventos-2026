package lospolimorficos.boletopolis.services;

import lospolimorficos.boletopolis.models.Usuario;

public class GestorSesion {
    private static GestorSesion instancia;

    private Usuario usuarioActual;

    /**
     * Constructor privado para aplicar Singleton.
     */
    private GestorSesion() {
    }

    /**
     * Obtiene la única instancia activa del gestor de sesión.
     *
     * @return instancia única de {@link GestorSesion}.
     */
    public static GestorSesion getInstancia() {

        if (instancia == null) {
            instancia = new GestorSesion();
        }

        return instancia;
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     *
     * @return usuario actual del sistema.
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Define el usuario autenticado actualmente.
     *
     * @param usuarioActual usuario que inició sesión.
     */
    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    /**
     * Cierra la sesión eliminando el usuario actual.
     */
    public void cerrarSesion() {
        usuarioActual = null;
    }

    }

