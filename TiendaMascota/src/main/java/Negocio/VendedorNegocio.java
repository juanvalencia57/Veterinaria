package Negocio;

import DAO.VendedorDAO;
import Modelo.VendedorDTO;
import Implement.VendedorDAOFile;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class VendedorNegocio {
    private VendedorDAO vendedorDAO = new VendedorDAOFile();

    public VendedorNegocio() { 
    }

    public boolean almacenarVendedor(VendedorDTO vendedor) {
        // Validación por identificacion (nunca por nombre)
        if (consultarVendedor(vendedor.getIdentificacion()) == null) {
            return vendedorDAO.almacenarVendedor(vendedor);
        }
        return false;
    }

    public List<VendedorDTO> listarVendedor() {
        return vendedorDAO.listarVendedor();
    }

    public VendedorDTO consultarVendedor(String identificacion) {
        return vendedorDAO.consultarVendedor(identificacion);
    }

    public boolean eliminarVendedor(String identificacion) {
        if (consultarVendedor(identificacion) != null) {
            return vendedorDAO.eliminarVendedor(identificacion);
        }
        return false;
    }

    public boolean actualizarVendedor(VendedorDTO vendedor) {
        if (consultarVendedor(vendedor.getIdentificacion()) != null) {
            return vendedorDAO.actualizarVendedor(vendedor);
        }
        return false;
    }
}