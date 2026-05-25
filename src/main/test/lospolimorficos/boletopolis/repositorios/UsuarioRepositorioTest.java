package lospolimorficos.boletopolis.repositorios;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para {@link UsuarioRepositorio}.
 * Verifica el correcto funcionamiento de las operaciones CRUD y de consulta de usuarios.
 */
class UsuarioRepositorioTest {
    private UsuarioRepositorio usuarioRepositorio;

    /**
     * Configuración inicial para cada prueba.
     * Se obtiene una nueva instancia del repositorio y se limpia la lista de usuarios.
     */
    @BeforeEach
    void setUp() {
        usuarioRepositorio = UsuarioRepositorio.getInstancia();
        // Limpiar los datos de ejemplo para asegurar un estado inicial limpio en cada prueba.
        usuarioRepositorio.getUsuarios().clear();
    }

    /**
     * Verifica que un usuario se registre correctamente en el repositorio.
     * Se espera que el tamaño del repositorio aumente en uno y el usuario esté presente.
     */
    @Test
    void registrarUsuario_DeberiaRegistrarCorrectamente() {
        // Paso 1: Crear un cliente de ejemplo.
        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        // Paso 2: Intentar registrar el cliente.
        boolean resultado = usuarioRepositorio.registrarUsuario(cliente);

        // Paso 3: Afirmar que el registro fue exitoso.
        assertTrue(resultado);

        // Paso 4: Afirmar que el repositorio contiene un usuario.
        assertEquals(1, usuarioRepositorio.contarUsuarios());
        // Paso 5: Afirmar que el cliente registrado está en la lista de usuarios.
        assertTrue(usuarioRepositorio.getUsuarios().contains(cliente));
    }

    /**
     * Verifica que no se pueda registrar un usuario si ya existe otro con el mismo correo electrónico.
     * Se espera que el método devuelva false y el número de usuarios no cambie.
     */
    @Test
    void registrarUsuario_NoDeberiaRegistrarCorreoDuplicado() {
        // Paso 1: Crear dos clientes con el mismo correo electrónico.
        Cliente cliente1 = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        Cliente cliente2 = crearClienteEjemplo(
                "Pedro",
                "juan@gmail.com"
        );

        // Paso 2: Registrar el primer cliente.
        usuarioRepositorio.registrarUsuario(cliente1);

        // Paso 3: Intentar registrar el segundo cliente (con correo duplicado).
        boolean resultado = usuarioRepositorio.registrarUsuario(cliente2);

        // Paso 4: Afirmar que el registro del segundo cliente no fue exitoso.
        assertFalse(resultado);

        // Paso 5: Afirmar que el número de usuarios en el repositorio sigue siendo 1.
        assertEquals(1, usuarioRepositorio.contarUsuarios());
    }

    /**
     * Verifica que un usuario se elimine correctamente del repositorio.
     * Se espera que el método devuelva true y el usuario ya no esté presente.
     */
    @Test
    void eliminarUsuario_DeberiaEliminarCorrectamente() {
        // Paso 1: Crear un cliente de ejemplo y registrarlo.
        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );
        usuarioRepositorio.registrarUsuario(cliente);

        // Paso 2: Intentar eliminar el cliente.
        boolean resultado = usuarioRepositorio.eliminarUsuario(cliente);

        // Paso 3: Afirmar que la eliminación fue exitosa.
        assertTrue(resultado);

        // Paso 4: Afirmar que el repositorio está vacío.
        assertEquals(0, usuarioRepositorio.contarUsuarios());
    }

    /**
     * Verifica que un usuario se actualice correctamente en el repositorio.
     * Se espera que el nombre del usuario en el repositorio refleje el cambio.
     */
    @Test
    void actualizarUsuario_DeberiaActualizarCorrectamente() {
        // Paso 1: Crear un cliente de ejemplo y registrarlo.
        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );
        usuarioRepositorio.registrarUsuario(cliente);

        // Paso 2: Modificar el nombre del cliente.
        cliente.setNombre("Carlos");

        // Paso 3: Actualizar el cliente en el repositorio.
        boolean resultado = usuarioRepositorio.actualizarUsuario(cliente);

        // Paso 4: Afirmar que la actualización fue exitosa.
        assertTrue(resultado);

        // Paso 5: Recuperar el usuario actualizado del repositorio y afirmar que su nombre es "Carlos".
        Cliente actualizado = (Cliente) usuarioRepositorio
                        .getUsuarios()
                        .getFirst();
        assertEquals("Carlos", actualizado.getNombre());
    }

    /**
     * Verifica que el método {@code actualizarUsuario()} lance una {@link IllegalArgumentException}
     * si se intenta actualizar un usuario que no existe en el repositorio.
     */
    @Test
    void actualizarUsuario_DeberiaLanzarExcepcionSiNoExiste() {
        // Paso 1: Crear un cliente de ejemplo que no será registrado.
        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        // Paso 2: Afirmar que se lanza una IllegalArgumentException al intentar actualizar un usuario no existente.
        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioRepositorio.actualizarUsuario(cliente)
        );
    }

    /**
     * Verifica que el método {@code getClientes()} retorne solo los usuarios que son instancias de {@link Cliente}.
     * Se espera que la lista contenga solo un cliente y que sea de tipo Cliente.
     */
    @Test
    void getClientes_DeberiaRetornarSoloClientes() {
        // Paso 1: Crear un cliente y un administrador.
        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );
        Admin admin = new Admin(
                "Admin",
                "Root",
                "123",
                "admin@gmail.com",
                "300",
                "1234"
        );

        // Paso 2: Registrar ambos usuarios.
        usuarioRepositorio.registrarUsuario(cliente);
        usuarioRepositorio.registrarUsuario(admin);

        // Paso 3: Obtener la lista de clientes.
        ObservableList<Cliente> clientes =
                usuarioRepositorio.getClientes();

        // Paso 4: Afirmar que la lista de clientes contiene solo un elemento.
        assertEquals(1, clientes.size());
        // Paso 5: Afirmar que el elemento en la lista es una instancia de Cliente.
        assertTrue(clientes.getFirst() instanceof Cliente);
    }

    /**
     * Verifica que el método {@code existeUsuario()} retorne true si existe un usuario con el correo dado.
     */
    @Test
    void existeUsuario_DeberiaRetornarTrueSiExiste() {
        // Paso 1: Crear un cliente de ejemplo y registrarlo.
        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );
        usuarioRepositorio.registrarUsuario(cliente);

        // Paso 2: Verificar si existe un usuario con el correo "juan@gmail.com".
        boolean existe = usuarioRepositorio.existeUsuario("juan@gmail.com");

        // Paso 3: Afirmar que el usuario existe.
        assertTrue(existe);
    }

    /**
     * Verifica que el método {@code existeUsuario()} retorne false si no existe un usuario con el correo dado.
     */
    @Test
    void existeUsuario_DeberiaRetornarFalseSiNoExiste() {
        // Paso 1: Verificar si existe un usuario con un correo que no ha sido registrado.
        boolean existe =
                usuarioRepositorio.existeUsuario(
                        "noexiste@gmail.com"
                );

        // Paso 2: Afirmar que el usuario no existe.
        assertFalse(existe);
    }

    /**
     * Verifica que el método {@code buscarUsuario()} busque correctamente por correo electrónico.
     */
    @Test
    void buscarUsuario_DeberiaBuscarPorCorreo() {
        // Paso 1: Crear un cliente de ejemplo y registrarlo.
        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );
        usuarioRepositorio.registrarUsuario(cliente);

        // Paso 2: Buscar el usuario por su correo electrónico.
        Cliente encontrado =
                usuarioRepositorio.buscarUsuario(
                        "juan@gmail.com"
                );

        // Paso 3: Afirmar que el usuario fue encontrado.
        assertNotNull(encontrado);
        // Paso 4: Afirmar que el correo del usuario encontrado coincide con el correo buscado.
        assertEquals(
                cliente.getCorreo(),
                encontrado.getCorreo()
        );
    }

    /**
     * Verifica que el método {@code buscarUsuario()} busque correctamente por número de documento.
     */
    @Test
    void buscarUsuario_DeberiaBuscarPorDocumento() {
        // Paso 1: Crear un cliente de ejemplo y registrarlo.
        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );
        usuarioRepositorio.registrarUsuario(cliente);

        // Paso 2: Buscar el usuario por su número de documento.
        Cliente encontrado = usuarioRepositorio.buscarUsuario(cliente.getDocumento());

        // Paso 3: Afirmar que el usuario fue encontrado.
        assertNotNull(encontrado);
        // Paso 4: Afirmar que el documento del usuario encontrado coincide con el documento buscado.
        assertEquals(
                cliente.getDocumento(),
                encontrado.getDocumento()
        );
    }

    /**
     * Verifica que el método {@code contarUsuarios()} devuelva el número correcto de usuarios.
     */
    @Test
    void contarUsuarios_DeberiaContarCorrectamente() {
        // Paso 1: Registrar dos clientes de ejemplo.
        usuarioRepositorio.registrarUsuario(
                crearClienteEjemplo(
                        "Juan",
                        "juan@gmail.com"
                )
        );
        usuarioRepositorio.registrarUsuario(
                crearClienteEjemplo(
                        "Pedro",
                        "pedro@gmail.com"
                )
        );

        // Paso 2: Afirmar que el conteo de usuarios es 2.
        assertEquals(2, usuarioRepositorio.contarUsuarios());
    }

    /**
     * Método auxiliar para crear un objeto {@link Cliente} de ejemplo con una cuenta simulada y un método de pago.
     *
     * @param nombre El nombre del cliente.
     * @param correo El correo electrónico del cliente.
     * @return Una nueva instancia de {@link Cliente}.
     */
    private Cliente crearClienteEjemplo(String nombre, String correo) {
        // Paso 1: Crear una nueva instancia de Cliente.
        Cliente cliente = new Cliente(
                nombre,
                "Perez",
                "123456",
                correo,
                "3001234567",
                "1234"
        );

        // Paso 2: Crear una CuentaSimulada para el cliente.
        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        // Paso 3: Crear un método de pago (PagoTarjeta) para el cliente.
        MetodoPago metodoPago = new PagoTarjeta(cuenta, "Débito");

        // Paso 4: Asociar la cuenta y el método de pago al cliente.
        cliente.agregarCuenta(cuenta);
        cliente.agregarMetodoPago(metodoPago);

        // Paso 5: Devolver el cliente creado.
        return cliente;
    }
}
