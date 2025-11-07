package Controlador;

import Negocio.VentaNegocio;
import Modelo.VentaDTO;
import Modelo.DetalleDTO;
import Negocio.AccesorioNegocio;
import javax.swing.JOptionPane;
import java.util.List;

public class VentaControlador {
    private final VentaNegocio ventaNegocio = new VentaNegocio();

    public void registrarVenta(String idVenta, String idCliente, String idVendedor, String idMascota, List<DetalleDTO> detallesAccesorio) {
        // Validación de stock de accesorios (stock insuficiente y sin stock)
        AccesorioNegocio accesorioNegocio = new AccesorioNegocio();
        if (detallesAccesorio != null) {
            for (DetalleDTO detalle : detallesAccesorio) {
                // Si tu método es getId(), cámbialo así: detalle.getId(), según tu DTO.
                String errorStock = accesorioNegocio.validarStockAccesorio(detalle.getIdAccesorio(), detalle.getCantidad());
                if (errorStock != null) {
                    JOptionPane.showMessageDialog(
                            null,
                            errorStock,
                            "Stock insuficiente",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
        }

        // Validaciones de idVenta
        idVenta = (idVenta == null) ? "" : idVenta.trim();
        if (!idVenta.matches("[0-9]*")) {
            datosErroneos("El ID debe ser un número sin puntos ni comas");
            return;
        }
        if (idVenta.isEmpty()) {
            datosErroneos("Debe ingresar una identificación");
            return;
        }

        VentaDTO venta = new VentaDTO(
                idVenta,
                idCliente,
                idVendedor,
                (idMascota == null || idMascota.trim().isEmpty()) ? null : idMascota.trim(),
                detallesAccesorio,
                null,
                0.0
        );

        int resultado = ventaNegocio.registrarVenta(venta);

        if (resultado == 1) {
            JOptionPane.showMessageDialog(null,
                    "Venta registrada exitosamente.",
                    "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else if (resultado == -1) {
            JOptionPane.showMessageDialog(null,
                    "No se puede vender la mascota: ya ha sido vendida.",
                    "Error en Venta", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "No se pudo registrar la venta. Revise los datos ingresados o si ya existe una venta con ese ID.",
                    "Error en Venta", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<VentaDTO> listarVentas() {
        return ventaNegocio.listarVentas();
    }

    public VentaDTO consultarVenta(String idVenta) {
        VentaDTO venta = ventaNegocio.consultarVenta(idVenta);
        if (venta != null) {
            JOptionPane.showMessageDialog(null,
                    venta.toString(),
                    "Consulta de Venta",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(null,
                    "No se encontró la venta con ID: " + idVenta,
                    "Venta no encontrada",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return venta;
    }

    public boolean eliminarVenta(String idVenta) {
        boolean eliminado = ventaNegocio.eliminarVenta(idVenta);
        if (eliminado) {
            JOptionPane.showMessageDialog(null, "Venta eliminada correctamente.", "Venta Eliminada", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar la venta (ID incorrecto o inexistente).", "Error al Eliminar", JOptionPane.ERROR_MESSAGE);
        }
        return eliminado;
    }

    public void actualizarVenta(String idVenta, String idCliente, String idVendedor, String idMascota, List<DetalleDTO> detallesAccesorio) {
        // Validación de stock de accesorios antes de actualizar
        AccesorioNegocio accesorioNegocio = new AccesorioNegocio();
        if (detallesAccesorio != null) {
            for (DetalleDTO detalle : detallesAccesorio) {
                String errorStock = accesorioNegocio.validarStockAccesorio(detalle.getIdAccesorio(), detalle.getCantidad());
                if (errorStock != null) {
                    JOptionPane.showMessageDialog(
                            null,
                            errorStock,
                            "Stock insuficiente",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
        }

        VentaDTO venta = new VentaDTO(
                idVenta,
                idCliente,
                idVendedor,
                (idMascota == null || idMascota.trim().isEmpty()) ? null : idMascota.trim(),
                detallesAccesorio,
                null,
                0.0
        );
        int resultado = ventaNegocio.actualizarVenta(venta);
        if (resultado == 1) {
            JOptionPane.showMessageDialog(null,
                    "Venta actualizada exitosamente.",
                    "Venta Actualizada", JOptionPane.INFORMATION_MESSAGE);
        } else if (resultado == -1) {
            JOptionPane.showMessageDialog(null,
                    "No se puede vender la mascota: ya ha sido vendida.",
                    "Error en Venta", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "No se pudo actualizar la venta. Verifique el ID, que la venta exista y que los datos sean válidos.",
                    "Error al Actualizar Venta", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void datosErroneos(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
    }
}