package DAO;

import Modelo.VentaDTO;
import java.util.List;

public interface VentaDAO {
    boolean registrarVenta(VentaDTO venta);
    List<VentaDTO> listarVentas();
    VentaDTO consultarVenta(String idVenta);
    boolean eliminarVenta(String idVenta);
    boolean actualizarVenta(VentaDTO venta);
}