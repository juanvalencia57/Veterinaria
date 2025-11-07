package Negocio;

import DAO.VentaDAO;
import DAO.AccesorioDAO;
import DAO.MascotaDAO;
import DAO.ClienteDAO;
import Modelo.VentaDTO;
import Modelo.DetalleDTO;
import Modelo.AccesorioDTO;
import Modelo.MascotaDTO;
import Modelo.ClienteDTO;
import Implement.VentaDAOFile;
import Implement.AccesorioDAOFile;
import Implement.MascotaDAOFile;
import Implement.ClienteDAOFile;

import java.util.Date;
import java.util.List;

public class VentaNegocio {
    private final VentaDAO ventaDAO = new VentaDAOFile();
    private final AccesorioDAO accesorioDAO = new AccesorioDAOFile();
    private final MascotaDAO mascotaDAO = new MascotaDAOFile();
    private final ClienteDAO clienteDAO = new ClienteDAOFile();

    public VentaNegocio() {}

    // Retorna:
    //  1 = éxito
    //  0 = datos no válidos o venta repetida
    // -1 = mascota ya vendida
    public int registrarVenta(VentaDTO venta) {
        if (venta == null || venta.getIdVenta() == null || venta.getIdVenta().trim().isEmpty())
            return 0;
        if (ventaDAO.consultarVenta(venta.getIdVenta()) != null)
            return 0;

        int validacion = validarYAplicarInventarioYRelaciones(venta, null);
        if (validacion != 1) return validacion;

        boolean resultado = ventaDAO.registrarVenta(venta);
        return resultado ? 1 : 0;
    }

    public List<VentaDTO> listarVentas() {
        return ventaDAO.listarVentas();
    }

    public VentaDTO consultarVenta(String idVenta) {
        if (idVenta == null || idVenta.trim().isEmpty()) return null;
        return ventaDAO.consultarVenta(idVenta);
    }

    public boolean eliminarVenta(String idVenta) {
        if (idVenta == null || idVenta.trim().isEmpty()) return false;
        return ventaDAO.eliminarVenta(idVenta);
    }

    // Retorna: 1 = éxito, 0 = error, -1 = mascota ya vendida
    public int actualizarVenta(VentaDTO nuevaVenta) {
        if (nuevaVenta == null || nuevaVenta.getIdVenta() == null || nuevaVenta.getIdVenta().trim().isEmpty())
            return 0;
        VentaDTO ventaAnterior = ventaDAO.consultarVenta(nuevaVenta.getIdVenta());
        if (ventaAnterior == null) return 0;

        revertirEfectosVentaAnterior(ventaAnterior);

        int validacion = validarYAplicarInventarioYRelaciones(nuevaVenta, ventaAnterior);
        if (validacion != 1) return validacion;

        boolean resultado = ventaDAO.actualizarVenta(nuevaVenta);
        return resultado ? 1 : 0;
    }

    // 1 = OK, -1 = mascota vendida, 0 = error normal
    private int validarYAplicarInventarioYRelaciones(VentaDTO venta, VentaDTO ventaAnterior) {
        double total = 0.0;

        ClienteDTO cliente = clienteDAO.consultarCliente(venta.getIdCliente());
        if (cliente == null) return 0;

        // Mascota (opcional)
        if (venta.getIdMascota() != null && !venta.getIdMascota().isEmpty()) {
            MascotaDTO mascota = mascotaDAO.consultarMascota(venta.getIdMascota());
            if (mascota == null) return 0;
            if (!mascota.asignarDueñoSiDisponible(venta.getIdCliente())) return -1;
            mascotaDAO.actualizarMascota(mascota);

            if (ventaAnterior == null || !venta.getIdMascota().equals(ventaAnterior.getIdMascota())) {
                if (!cliente.getMascotas().contains(mascota.getId()))
                    cliente.getMascotas().add(mascota.getId());
                clienteDAO.actualizarCliente(cliente);
            }

            total += mascota.getPrecio();
        }

        // Accesorios
        if (venta.getDetallesAccesorio() != null) {
            for (DetalleDTO d : venta.getDetallesAccesorio()) {
                if (d.getCantidad() < 1) return 0;
                AccesorioDTO acc = accesorioDAO.consultarAccesorio(d.getIdAccesorio());
                if (acc == null) return 0;
                if (acc.getCantidad() < d.getCantidad()) return 0;
                d.setPrecioUnitario(acc.getPrecio());
                acc.setCantidad(acc.getCantidad() - d.getCantidad());
                accesorioDAO.actualizarAccesorio(acc);
                total += d.getCantidad() * d.getPrecioUnitario();
            }
        }

        venta.setTotal(total);
        venta.setFecha(new Date());
        return 1;
    }

    private void revertirEfectosVentaAnterior(VentaDTO ventaAnterior) {
        if (ventaAnterior.getDetallesAccesorio() != null) {
            for (DetalleDTO det : ventaAnterior.getDetallesAccesorio()) {
                AccesorioDTO acc = accesorioDAO.consultarAccesorio(det.getIdAccesorio());
                if (acc != null) {
                    acc.setCantidad(acc.getCantidad() + det.getCantidad());
                    accesorioDAO.actualizarAccesorio(acc);
                }
            }
        }
        if (ventaAnterior.getIdMascota() != null && !ventaAnterior.getIdMascota().isEmpty()) {
            MascotaDTO mascota = mascotaDAO.consultarMascota(ventaAnterior.getIdMascota());
            if (mascota != null) {
                mascota.setIdDueño(null);
                mascotaDAO.actualizarMascota(mascota);
            }
            ClienteDTO cliente = clienteDAO.consultarCliente(ventaAnterior.getIdCliente());
            if (cliente != null && cliente.getMascotas() != null) {
                cliente.getMascotas().remove(ventaAnterior.getIdMascota());
                clienteDAO.actualizarCliente(cliente);
            }
        }
    }
}