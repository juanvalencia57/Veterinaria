package Negocio;
import DAO.ClienteDAO;
import Modelo.ClienteDTO;
import Implement.ClienteDAOFile;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class ClienteNegocio {
    private ClienteDAO clienteDAO = new ClienteDAOFile();
    
    public ClienteNegocio() { }

    // Almacenar solo si no existe ya un cliente con esa identificación
    public boolean almacenarCliente(ClienteDTO cliente) {
        if (consultarCliente(cliente.getIdentificacion()) == null) {
            return clienteDAO.almacenarCliente(cliente);
        }
        return false;
    }

    public List<ClienteDTO> listarCliente() {
        return clienteDAO.listarCliente();
    }
    
    public ClienteDTO consultarCliente(String identificacion) {
        return clienteDAO.consultarCliente(identificacion);
    }
    
    public boolean eliminarCliente(String identificacion) {
        // Puede agregar validación de negocio: NO eliminar si cliente tiene mascotas asociadas
        ClienteDTO cliente = consultarCliente(identificacion);
        if (cliente != null) {
            // Validación de negocio: No eliminar si tiene mascotas asociadas
            if (cliente.getMascotas() != null && !cliente.getMascotas().isEmpty()) {
                // Puede lanzar excepción o retornar false según política de la app
                return false;
            }
            return clienteDAO.eliminarCliente(identificacion);
        }
        return false;
    }
    
    public boolean actualizarCliente(ClienteDTO cliente) {
        // Siempre conserva la lista de mascotas anterior!
        ClienteDTO clienteViejo = consultarCliente(cliente.getIdentificacion());
        if (clienteViejo != null) {
            // Si no recibe la lista de mascotas, usa la del anterior
            if (cliente.getMascotas() == null || cliente.getMascotas().isEmpty()) {
                cliente.setMascotas(clienteViejo.getMascotas());
            }
            return clienteDAO.actualizarCliente(cliente);
        }
        return false;
    }

    // Métodos útiles añadidos (para manipular mascotas de un cliente):

    // Añadir una mascota al cliente (y actualizar en archivo)
    public boolean agregarMascotaACliente(String identificacion, String idMascota) {
        ClienteDTO cliente = consultarCliente(identificacion);
        if (cliente != null) {
            if (cliente.getMascotas() == null) cliente.setMascotas(new java.util.ArrayList<>());
            if (!cliente.getMascotas().contains(idMascota)) {
                cliente.getMascotas().add(idMascota);
                return clienteDAO.actualizarCliente(cliente);
            }
        }
        return false;
    }

    // Remover mascota de cliente (por venta o cesión)
    public boolean removerMascotaDeCliente(String identificacion, String idMascota) {
        ClienteDTO cliente = consultarCliente(identificacion);
        if (cliente != null && cliente.getMascotas() != null && cliente.getMascotas().contains(idMascota)) {
            cliente.getMascotas().remove(idMascota);
            return clienteDAO.actualizarCliente(cliente);
        }
        return false;
    }
}