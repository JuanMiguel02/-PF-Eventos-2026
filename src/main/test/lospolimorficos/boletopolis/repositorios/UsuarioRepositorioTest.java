package lospolimorficos.boletopolis.repositorios;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRepositorioTest {
    private UsuarioRepositorio usuarioRepositorio;

    @BeforeEach
    void setUp() {

        usuarioRepositorio = UsuarioRepositorio.getInstancia();

        // Limpiar datos de ejemplo
        usuarioRepositorio.getUsuarios().clear();
    }

    @Test
    void registrarUsuario_DeberiaRegistrarCorrectamente() {

        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        boolean resultado = usuarioRepositorio.registrarUsuario(cliente);

        assertTrue(resultado);

        assertEquals(1, usuarioRepositorio.contarUsuarios());
        assertTrue(usuarioRepositorio.getUsuarios().contains(cliente));
    }

    @Test
    void registrarUsuario_NoDeberiaRegistrarCorreoDuplicado() {

        Cliente cliente1 = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        Cliente cliente2 = crearClienteEjemplo(
                "Pedro",
                "juan@gmail.com"
        );

        usuarioRepositorio.registrarUsuario(cliente1);

        boolean resultado = usuarioRepositorio.registrarUsuario(cliente2);

        assertFalse(resultado);

        assertEquals(1, usuarioRepositorio.contarUsuarios());
    }

    @Test
    void eliminarUsuario_DeberiaEliminarCorrectamente() {

        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        usuarioRepositorio.registrarUsuario(cliente);

        boolean resultado = usuarioRepositorio.eliminarUsuario(cliente);

        assertTrue(resultado);

        assertEquals(0, usuarioRepositorio.contarUsuarios());
    }

    @Test
    void actualizarUsuario_DeberiaActualizarCorrectamente() {

        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        usuarioRepositorio.registrarUsuario(cliente);

        cliente.setNombre("Carlos");

        boolean resultado = usuarioRepositorio.actualizarUsuario(cliente);

        assertTrue(resultado);

        Cliente actualizado = (Cliente) usuarioRepositorio
                        .getUsuarios()
                        .getFirst();

        assertEquals("Carlos", actualizado.getNombre());
    }

    @Test
    void actualizarUsuario_DeberiaLanzarExcepcionSiNoExiste() {

        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioRepositorio.actualizarUsuario(cliente)
        );
    }

    @Test
    void getClientes_DeberiaRetornarSoloClientes() {

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

        usuarioRepositorio.registrarUsuario(cliente);
        usuarioRepositorio.registrarUsuario(admin);

        ObservableList<Cliente> clientes =
                usuarioRepositorio.getClientes();

        assertEquals(1, clientes.size());

        assertTrue(clientes.getFirst() instanceof Cliente);
    }

    @Test
    void existeUsuario_DeberiaRetornarTrueSiExiste() {

        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        usuarioRepositorio.registrarUsuario(cliente);

        boolean existe = usuarioRepositorio.existeUsuario("juan@gmail.com");

        assertTrue(existe);
    }

    @Test
    void existeUsuario_DeberiaRetornarFalseSiNoExiste() {

        boolean existe =
                usuarioRepositorio.existeUsuario(
                        "noexiste@gmail.com"
                );

        assertFalse(existe);
    }

    @Test
    void buscarUsuario_DeberiaBuscarPorCorreo() {

        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        usuarioRepositorio.registrarUsuario(cliente);

        Cliente encontrado =
                usuarioRepositorio.buscarUsuario(
                        "juan@gmail.com"
                );

        assertNotNull(encontrado);

        assertEquals(
                cliente.getCorreo(),
                encontrado.getCorreo()
        );
    }

    @Test
    void buscarUsuario_DeberiaBuscarPorDocumento() {

        Cliente cliente = crearClienteEjemplo(
                "Juan",
                "juan@gmail.com"
        );

        usuarioRepositorio.registrarUsuario(cliente);

        Cliente encontrado = usuarioRepositorio.buscarUsuario(cliente.getDocumento());

        assertNotNull(encontrado);

        assertEquals(
                cliente.getDocumento(),
                encontrado.getDocumento()
        );
    }

    @Test
    void contarUsuarios_DeberiaContarCorrectamente() {

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

        assertEquals(2, usuarioRepositorio.contarUsuarios());
    }

    private Cliente crearClienteEjemplo(String nombre, String correo) {

        Cliente cliente = new Cliente(
                nombre,
                "Perez",
                "123456",
                correo,
                "3001234567",
                "1234"
        );

        CuentaSimulada cuenta = new CuentaSimulada(cliente, 100000);

        MetodoPago metodoPago = new PagoTarjeta(cuenta, "Débito");

        cliente.agregarCuenta(cuenta);
        cliente.agregarMetodoPago(metodoPago);

        return cliente;
    }
}