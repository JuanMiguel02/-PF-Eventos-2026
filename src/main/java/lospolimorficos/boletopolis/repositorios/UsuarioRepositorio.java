package lospolimorficos.boletopolis.repositorios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import java.util.stream.Collectors;

/**
 * Repositorio para la gestión de objetos {@link Usuario} (incluyendo {@link Cliente} y {@link Admin}).
 * Implementa el patrón Singleton para asegurar una única instancia global.
 * Proporciona métodos para almacenar, recuperar, actualizar y eliminar usuarios.
 * Carga datos de ejemplo al inicializarse.
 */
public final class UsuarioRepositorio {

    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private static UsuarioRepositorio instancia;

    /**
     * Constructor privado para implementar el patrón Singleton.
     * Carga datos de ejemplo al ser instanciado.
     */
    private UsuarioRepositorio() {
        cargarDatosEjemplo();
    }

    /**
     * Obtiene la única instancia de {@code UsuarioRepositorio}.
     * Si la instancia no ha sido creada, la inicializa.
     *
     * @return La instancia de {@code UsuarioRepositorio}.
     */
    public static UsuarioRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioRepositorio();
        }
        return instancia;
    }

    /**
     * Registra un nuevo usuario en el repositorio.
     *
     * @param usuario El objeto {@link Usuario} a registrar.
     * @return {@code true} si el usuario fue añadido exitosamente, {@code false} si ya existe un usuario con el mismo correo.
     */
    public boolean registrarUsuario(Usuario usuario) {
        // Paso 1: Verificar si ya existe un usuario con el mismo correo electrónico.
        if(existeUsuario(usuario.getCorreo())){
            return false; // Si existe, no se registra y se devuelve false.
        }
        // Paso 2: Si no existe, se añade el usuario a la lista y se devuelve true.
        return usuarios.add(usuario);
    }

    /**
     * Elimina un usuario del repositorio.
     *
     * @param usuario El objeto {@link Usuario} a eliminar.
     * @return {@code true} si el usuario fue eliminado exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarUsuario(Usuario usuario) {
        return usuarios.remove(usuario);
    }

    /**
     * Actualiza un usuario existente en el repositorio.
     * Busca el usuario por su correo electrónico y lo reemplaza con la versión actualizada.
     *
     * @param usuarioActualizado El objeto {@link Usuario} con la información actualizada.
     * @return {@code true} si el usuario fue actualizado exitosamente.
     * @throws IllegalArgumentException Si el usuario a actualizar no se encuentra.
     */
    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        // Paso 1: Iterar sobre la lista de usuarios para encontrar el usuario a actualizar.
        for(int i = 0; i < usuarios.size(); i++) {
            // Paso 1.1: Comparar el correo electrónico del usuario actual con el correo del usuario actualizado.
            if(usuarios.get(i).getCorreo().equals(usuarioActualizado.getCorreo())) {
                // Paso 1.2: Si los correos coinciden, reemplazar el usuario existente con la versión actualizada.
                usuarios.set(i, usuarioActualizado);
                // Paso 1.3: Devolver true indicando que la actualización fue exitosa.
                return true;
            }
        }
        // Paso 2: Si el bucle termina y el usuario no fue encontrado, lanzar una excepción.
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    /**
     * Obtiene la lista observable de todos los usuarios registrados (clientes y administradores).
     *
     * @return Una {@link ObservableList} de objetos {@link Usuario}.
     */
    public ObservableList<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Obtiene una lista observable de todos los clientes registrados en el repositorio.
     *
     * @return Una {@link ObservableList} de objetos {@link Cliente}.
     */
    public ObservableList<Cliente> getClientes() {
        return usuarios.stream()
                .filter(u -> u instanceof Cliente) // Filtrar solo los usuarios que son instancias de Cliente.
                .map(u -> (Cliente) u) // Convertir los usuarios filtrados a tipo Cliente.
                .collect(Collectors.toCollection(FXCollections::observableArrayList)); // Recolectar en una ObservableList.
    }

    /**
     * Verifica si ya existe un usuario con el correo electrónico dado.
     *
     * @param correo El correo electrónico a verificar.
     * @return {@code true} si ya existe un usuario con ese correo, {@code false} en caso contrario.
     */
    public boolean existeUsuario(String correo){
        for(Usuario usuario : this.usuarios){
            if(usuario.getCorreo().equals(correo)){
                return true;
            }
        }
        return false;
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo El correo electrónico del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra, o {@code null} si no.
     */
    public Usuario buscarPorCorreo(String correo) {
        return usuarios.stream()
                .filter(usuario -> usuario.getCorreo().equals(correo))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca un usuario (cliente) por su correo electrónico o número de documento.
     *
     * @param busqueda El correo electrónico o número de documento del cliente a buscar.
     * @return El objeto {@link Cliente} si se encuentra, o {@code null} si no se encuentra o no es un cliente.
     */
    public Cliente buscarUsuario(String busqueda){
        return (Cliente) usuarios.stream()
                .filter(usuario ->
                        usuario.getCorreo().equals(busqueda) // Filtrar por correo electrónico.
                        ||  usuario.getDocumento().equals(busqueda)) // O filtrar por número de documento.
                .findFirst() // Obtener el primer resultado que coincida.
                .orElse(null); // Si no se encuentra, devolver null.
    }

    /**
     * Cuenta el número total de usuarios registrados en el repositorio.
     *
     * @return El número total de usuarios.
     */
    public int contarUsuarios(){
        return usuarios.size();
    }

    /**
     * Carga datos de ejemplo en el repositorio, incluyendo un administrador y varios clientes.
     * Para cada cliente, se crea una cuenta simulada y un método de pago.
     */
    private void cargarDatosEjemplo() {
        // Paso 1: Crear y registrar un usuario administrador de ejemplo.
        Admin admin1 = new Admin(
                "Sancho",
                "Panza",
                "3123213",
                "sancho@boletopolis.com",
                "412321312",
                "123456"
        );
        registrarUsuario(admin1);

        // Paso 2: Crear y registrar varios clientes de ejemplo con diferentes configuraciones.
        crearClienteEjemplo(
                "Paco",
                "Jones",
                "123456",
                "paquito@gmail.com",
                "654321",
                400000000,
                "TARJETA"
        );

        crearClienteEjemplo(
                "Pedro",
                "El Escamoso",
                "3124531",
                "pedrito@gmail.com",
                "5432142",
                100000000,
                "NEQUI"
        );

        crearClienteEjemplo(
                "Ana",
                "Martinez",
                "987654",
                "ana@gmail.com",
                "3001112233",
                5000000,
                "TARJETA"
        );

        crearClienteEjemplo(
                "Laura",
                "Gomez",
                "741852",
                "laura@gmail.com",
                "3015558899",
                800000,
                "NEQUI"
        );

        crearClienteEjemplo(
                "Tom",
                "Yorke",
                "9876122",
                "tom@gmail.com",
                "3201412233",
                8000000,
                "TARJETA"
        );

        crearClienteEjemplo(
                "Armando",
                "Casas",
                "42139123",
                "casas@gmail.com",
                "301578879",
                850000,
                "NEQUI"
        );

        crearClienteEjemplo(
                "Julian",
                "Casablancas",
                "09172321",
                "casablancas@gmail.com",
                "301578000",
                9050000,
                "NEQUI"
        );
    }

    /**
     * Método auxiliar para crear y registrar un cliente de ejemplo con su cuenta y método de pago.
     *
     * @param nombre El nombre del cliente.
     * @param apellido El apellido del cliente.
     * @param documento El número de documento del cliente.
     * @param correo El correo electrónico del cliente.
     * @param telefono El número de teléfono del cliente.
     * @param saldoInicial El saldo inicial de la cuenta simulada del cliente.
     * @param tipoPago El tipo de método de pago a crear ("TARJETA" o "NEQUI").
     */
    private void crearClienteEjemplo(String nombre, String apellido, String documento, String correo, String telefono, double saldoInicial, String tipoPago) {
        // Paso 1: Crear una nueva instancia de Cliente.
        Cliente cliente = new Cliente(nombre, apellido, documento, correo, telefono, documento);
        // Paso 2: Crear una CuentaSimulada para el cliente con el saldo inicial.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, saldoInicial);

        // Paso 3: Asociar la cuenta al cliente.
        cliente.agregarCuenta(cuenta);

        // Paso 4: Declarar una variable para el método de pago.
        MetodoPago metodoPago;

        // Paso 5: Crear el método de pago según el tipo especificado.
        if(tipoPago.equals("TARJETA")) {
            metodoPago = new PagoTarjeta(cuenta, "Débito"); // Crear un PagoTarjeta.
        } else {
            metodoPago = new PagoNequi(cuenta, telefono); // Crear un PagoNequi.
        }

        // Paso 6: Asociar el método de pago al cliente.
        cliente.agregarMetodoPago(metodoPago);

        // Paso 7: Registrar el cliente en el repositorio.
        registrarUsuario(cliente);
    }

}
