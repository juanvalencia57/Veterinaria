package Implement;

import Modelo.VentaDTO;
import Modelo.DetalleDTO;
import DAO.VentaDAO;

import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * DAO de ventas basado en archivo.
 * Lógica de negocio (stock, dueño, etc) debe estar solamente en negocio/controlador,
 * aquí solo persistencia pura de objetos VentaDTO.
 */
public class VentaDAOFile implements VentaDAO {
    private static final String DELIMITADOR_ARCHIVO = ",";
    private static final String DETALLE_DELIM = ";";
    private static final String DETALLE_CAMPO_DELIM = "|";
    private static final String FILE_NAME = "Venta.txt";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private File archivoVenta;

    public VentaDAOFile() {
        archivoVenta = new File(FILE_NAME);
        try {
            if (!archivoVenta.exists()) {
                archivoVenta.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo crear el archivo Venta.txt");
        }
    }

    private String detallesToString(List<DetalleDTO> detalles) {
        if (detalles == null || detalles.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < detalles.size(); i++) {
            DetalleDTO d = detalles.get(i);
            sb.append(d.getIdAccesorio())
              .append(DETALLE_CAMPO_DELIM)
              .append(d.getCantidad())
              .append(DETALLE_CAMPO_DELIM)
              .append(d.getPrecioUnitario());
            if (i < detalles.size() - 1)
                sb.append(DETALLE_DELIM);
        }
        return sb.toString();
    }

    private List<DetalleDTO> stringToDetalles(String detallesStr) {
        List<DetalleDTO> detalles = new ArrayList<>();
        if (detallesStr == null || detallesStr.isEmpty()) return detalles;
        String[] detallesArr = detallesStr.split(DETALLE_DELIM);
        for (String dStr : detallesArr) {
            String[] campos = dStr.split("\\" + DETALLE_CAMPO_DELIM);
            if (campos.length >= 3) {
                try {
                    String idAccesorio = campos[0];
                    int cantidad = Integer.parseInt(campos[1]);
                    double precioUnitario = Double.parseDouble(campos[2]);
                    detalles.add(new DetalleDTO(idAccesorio, cantidad, precioUnitario));
                } catch (Exception e) { continue; }
            }
        }
        return detalles;
    }

    @Override
    public boolean registrarVenta(VentaDTO venta) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        StringBuilder sb = new StringBuilder();
        sb.append(venta.getIdVenta()).append(DELIMITADOR_ARCHIVO)
          .append(venta.getIdCliente()).append(DELIMITADOR_ARCHIVO)
          .append(venta.getIdVendedor()).append(DELIMITADOR_ARCHIVO)
          .append(venta.getIdMascota() == null ? "" : venta.getIdMascota()).append(DELIMITADOR_ARCHIVO)
          .append(venta.getTotal()).append(DELIMITADOR_ARCHIVO)
          .append(venta.getFecha() != null ? sdf.format(venta.getFecha()) : "").append(DELIMITADOR_ARCHIVO)
          .append(detallesToString(venta.getDetallesAccesorio()));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoVenta, true))) {
            bw.write(sb.toString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la venta");
        }
        return false;
    }

    @Override
    public VentaDTO consultarVenta(String idVenta) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoVenta))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (params.length >= 7 && params[0].equals(idVenta)) {
                    try {
                        String idCliente = params[1];
                        String idVendedor = params[2];
                        String idMascota = params[3].isEmpty() ? null : params[3];
                        double total = Double.parseDouble(params[4]);
                        Date fecha = params[5].isEmpty() ? null : sdf.parse(params[5]);
                        List<DetalleDTO> detalles = stringToDetalles(params[6]);
                        return new VentaDTO(idVenta, idCliente, idVendedor, idMascota, detalles, fecha, total);
                    } catch (Exception e) { return null; }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error consultando venta.");
        }
        return null;
    }

    @Override
    public List<VentaDTO> listarVentas() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        List<VentaDTO> ventas = new ArrayList<>();
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoVenta))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (params.length >= 7) {
                    try {
                        String idVenta = params[0];
                        String idCliente = params[1];
                        String idVendedor = params[2];
                        String idMascota = params[3].isEmpty() ? null : params[3];
                        double total = Double.parseDouble(params[4]);
                        Date fecha = params[5].isEmpty() ? null : sdf.parse(params[5]);
                        List<DetalleDTO> detalles = stringToDetalles(params[6]);
                        ventas.add(new VentaDTO(idVenta, idCliente, idVendedor, idMascota, detalles, fecha, total));
                    } catch (Exception e) { continue; }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar las ventas.");
        }
        return ventas;
    }

    @Override
    public boolean eliminarVenta(String idVenta) {
        File archivoTemporal = new File("VentaTemp.txt");
        boolean eliminado = false;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoVenta));
             BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoTemporal, false))) {
            String linea;
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (params.length >= 1 && params[0].equals(idVenta)) {
                    eliminado = true;
                } else {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar venta");
            e.printStackTrace();
        }
        // Reemplaza el archivo original por el temporal solo si eliminó alguno
        if (eliminado) {
            try (
                BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoTemporal));
                BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoVenta))
            ) {
                String linea;
                while ((linea = lectorBuffer.readLine()) != null) {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al finalizar eliminación de venta");
            }
            archivoTemporal.delete();
            return true;
        }
        archivoTemporal.delete();
        return false;
    }

    @Override
    public boolean actualizarVenta(VentaDTO venta) {
        File archivoTemporal = new File("VentaTemp.txt");
        boolean actualizado = false;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoVenta));
             BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoTemporal, false))) {
            String linea;
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                // Si se encuentra la venta a actualizar, la reemplaza
                if (params.length >= 1 && params[0].equals(venta.getIdVenta())) {
                    // Escribe la venta actualizada
                    StringBuilder sb = new StringBuilder();
                    sb.append(venta.getIdVenta()).append(DELIMITADOR_ARCHIVO)
                      .append(venta.getIdCliente()).append(DELIMITADOR_ARCHIVO)
                      .append(venta.getIdVendedor()).append(DELIMITADOR_ARCHIVO)
                      .append(venta.getIdMascota() == null ? "" : venta.getIdMascota()).append(DELIMITADOR_ARCHIVO)
                      .append(venta.getTotal()).append(DELIMITADOR_ARCHIVO)
                      .append(venta.getFecha() != null ? sdf.format(venta.getFecha()) : "").append(DELIMITADOR_ARCHIVO)
                      .append(detallesToString(venta.getDetallesAccesorio()));
                    escritorBuffer.write(sb.toString());
                    escritorBuffer.newLine();
                    actualizado = true;
                } else {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar venta");
            e.printStackTrace();
        }

        // Reemplaza el archivo original por el temporal solo si actualizó alguno
        if (actualizado) {
            try (
                BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoTemporal));
                BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoVenta))
            ) {
                String linea;
                while ((linea = lectorBuffer.readLine()) != null) {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al finalizar actualización de venta");
            }
            archivoTemporal.delete();
            return true;
        }
        archivoTemporal.delete();
        return false;
    }
}