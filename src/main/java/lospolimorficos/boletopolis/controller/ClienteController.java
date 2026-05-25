package lospolimorficos.boletopolis.controller;

import javafx.collections.ObservableList;
import lospolimorficos.boletopolis.models.Cliente;
import lospolimorficos.boletopolis.models.CuentaSimulada;
import lospolimorficos.boletopolis.models.MetodoPago;
import lospolimorficos.boletopolis.models.PagoTarjeta;
import lospolimorficos.boletopolis.repositorios.UsuarioRepositorio;

import java.util.List;

/**
 * Controlador para la gestión de clientes en el sistema.
 * Proporciona métodos para filtrar, buscar, registrar, eliminar y actualizar información de clientes.
 * Interactúa con {@link UsuarioRepositorio} para la persistencia de datos.
 */
public class ClienteController {

    private final UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstancia();

    /**
     * Filtra una lista de clientes basándose en un criterio de búsqueda.
     * El filtro se aplica a nombre completo, documento, correo, ID de usuario y número de teléfono.
     *
     * @param clientes La lista original de clientes a filtrar.
     * @param filtro El texto de búsqueda para filtrar los clientes.
     * @return Una nueva lista de clientes que coinciden con el filtro. Si el filtro es nulo o vacío,
     *         se devuelve la lista original.
     */
    public List<Cliente> filtrarClientes(List<Cliente> clientes, String filtro){
        // Paso 1: Verificar si el filtro es nulo o vacío. Si lo es, no se aplica ningún filtro y se devuelve la lista original.
        if(filtro == null || filtro.isEmpty()){
            return clientes;
        }
        // Paso 2: Convertir el filtro a minúsculas para realizar una búsqueda insensible a mayúsculas y minúsculas.
        String filtroLimpio = filtro.toLowerCase();
        // Paso 3: Filtrar la lista de clientes utilizando un stream.
        return clientes.stream()
                // Paso 3.1: Para cada cliente, verificar si alguna de sus propiedades (nombre completo, documento,
                // correo, ID de usuario, número de teléfono) contiene el texto del filtro.
                .filter(cliente -> cliente.getNombreCompleto().toLowerCase().contains(filtroLimpio)
                                || cliente.getDocumento().contains(filtroLimpio)
                                || cliente.getCorreo().toLowerCase().contains(filtroLimpio)
                                || cliente.getIdUsuario().toString().contains(filtroLimpio)
                                || cliente.getNumTelefono().contains(filtroLimpio)
                )
                // Paso 3.2: Recolectar los clientes filtrados en una nueva lista.
                .toList();
    }

    /**
     * Busca un cliente por su identificador único (puede ser ID, correo o documento).
     *
     * @param busqueda El identificador del cliente a buscar.
     * @return El objeto {@link Cliente} si se encuentra, o {@code null} si no.
     */
    public Cliente buscarCliente(String busqueda){
        // Delega la búsqueda al repositorio de usuarios.
        return usuarioRepositorio.buscarUsuario(busqueda);
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * Al registrar un cliente, se le asigna una {@link CuentaSimulada} inicial con un saldo predefinido
     * y un método de pago de tarjeta simulado.
     *
     * @param cliente El objeto {@link Cliente} a registrar.
     * @return {@code true} si el cliente fue registrado exitosamente, {@code false} en caso contrario.
     */
    public boolean registrarCliente(Cliente cliente){
        // Paso 1: Crear una cuenta simulada para el nuevo cliente con un saldo inicial.
        CuentaSimulada cuentaPrincipal = new CuentaSimulada(cliente, 500000000);
        // Paso 2: Agregar la cuenta simulada al cliente.
        cliente.agregarCuenta(cuentaPrincipal);
        // Paso 3: Crear un método de pago de tarjeta simulado asociado a la cuenta.
        MetodoPago tarjeta = new PagoTarjeta(cuentaPrincipal, "VISA");
        // Paso 4: Agregar el método de pago al cliente.
        cliente.agregarMetodoPago(tarjeta);

        // Paso 5: Intentar registrar el cliente en el repositorio de usuarios y devolver el resultado.
        return usuarioRepositorio.registrarUsuario(cliente);
    }

    /**
     * Elimina un cliente del sistema.
     *
     * @param cliente El objeto {@link Cliente} a eliminar.
     * @return {@code true} si el cliente fue eliminado exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarCliente(Cliente cliente){
        // Delega la eliminación del cliente al repositorio de usuarios.
        return usuarioRepositorio.eliminarUsuario(cliente);
    }

    /**
     * Actualiza la información de un cliente existente en el sistema.
     *
     * @param clienteActualizado El objeto {@link Cliente} con la información actualizada.
     * @return {@code true} si el cliente fue actualizado exitosamente, {@code false} en caso contrario.
     */
    public boolean actualizarCliente(Cliente clienteActualizado){
        // Delega la actualización del cliente al repositorio de usuarios.
        return usuarioRepositorio.actualizarUsuario(clienteActualizado);
    }

    /**
     * Obtiene una lista observable de todos los clientes registrados en el sistema.
     *
     * @return Una {@link ObservableList} de objetos {@link Cliente}.
     */
    public ObservableList<Cliente> getClientes(){
        // Delega la obtención de la lista de clientes al repositorio de usuarios.
        return usuarioRepositorio.getClientes();
    }

}
