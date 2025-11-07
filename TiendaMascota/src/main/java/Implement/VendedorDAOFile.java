package Implement;

import java.util.List;
import java.util.ArrayList;
import java.io.*;
import javax.swing.JOptionPane;
import Modelo.VendedorDTO;
import DAO.VendedorDAO;

public class VendedorDAOFile implements VendedorDAO {

    private static final String DELIMITADOR_ARCHIVO = ",";
    private static final String FILE_NAME = "Vendedor.txt";
    private BufferedWriter escritorBuffer;
    private BufferedReader lectorBuffer;
    private FileWriter escritorArchivo;
    private FileReader lectorArchivo;
    private File archivoVendedor;

    public VendedorDAOFile() {
        archivoVendedor = new File(FILE_NAME);
        try {
            if (!archivoVendedor.exists()) {
                archivoVendedor.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo crear el archivo Vendedor.txt");
        }
    }

    @Override
    public boolean almacenarVendedor(VendedorDTO vendedor) {
        StringBuilder sb = new StringBuilder();
        sb.append(vendedor.getIdentificacion().trim());
        sb.append(DELIMITADOR_ARCHIVO);
        sb.append(vendedor.getNombres().trim());
        sb.append(DELIMITADOR_ARCHIVO);
        sb.append(vendedor.getGenero());
        // ¡NO agregues delimitador extra!

        try {
            escritorArchivo = new FileWriter(archivoVendedor, true);
            escritorBuffer = new BufferedWriter(escritorArchivo);
            escritorBuffer.write(sb.toString());
            escritorBuffer.newLine();
            escritorBuffer.close();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el vendedor");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public VendedorDTO consultarVendedor(String identificacion) {
        String linea;
        try {
            lectorArchivo = new FileReader(archivoVendedor);
            lectorBuffer = new BufferedReader(lectorArchivo);
            while ((linea = lectorBuffer.readLine()) != null) {
                String parametros[] = linea.split(",");
                // Compara SOLO la identificacion, usando trim y equalsIgnoreCase
                if (parametros.length == 3 && parametros[0].trim().equalsIgnoreCase(identificacion.trim())) {
                    return new VendedorDTO(
                        parametros[0].trim(),
                        parametros[1].trim(),
                        parametros[2].trim().charAt(0)
                    );
                }
            }
            lectorBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<VendedorDTO> listarVendedor() {
        archivoVendedor = new File(FILE_NAME);
        String linea;
        List<VendedorDTO> vendedores = new ArrayList<>();
        try {
            lectorArchivo = new FileReader(archivoVendedor);
            lectorBuffer = new BufferedReader(lectorArchivo);
            while ((linea = lectorBuffer.readLine()) != null) {
                String parametros[] = linea.split(",");
                if (parametros.length == 3) {
                    try {
                        VendedorDTO vendedor = new VendedorDTO(
                            parametros[0].trim(), // identificacion
                            parametros[1].trim(), // nombres
                            parametros[2].trim().charAt(0) // genero
                        );
                        vendedores.add(vendedor);
                    } catch (Exception ex) {
                        continue;
                    }
                }
            }
            lectorBuffer.close();
            return vendedores;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean eliminarVendedor(String identificacion) {
        String linea;
        archivoVendedor = new File(FILE_NAME);
        File archivoTemporal = new File("temporal_vendedor.txt");
        boolean eliminado = false;

        try {
            lectorArchivo = new FileReader(archivoVendedor);
            lectorBuffer = new BufferedReader(lectorArchivo);

            escritorArchivo = new FileWriter(archivoTemporal, false);
            escritorBuffer = new BufferedWriter(escritorArchivo);

            while ((linea = lectorBuffer.readLine()) != null) {
                String[] parametros = linea.split(",");
                // El id está en la posición 0, compara con trim y equalsIgnoreCase
                if (parametros.length == 3 && parametros[0].trim().equalsIgnoreCase(identificacion.trim())) {
                    eliminado = true; // Encontró y elimina la línea
                    // No escribe la línea
                } else {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            }
            escritorBuffer.close();
            lectorBuffer.close();

            // Sobrescribe el archivo original con el temporal
            escritorBuffer = new BufferedWriter(new FileWriter(archivoVendedor));
            lectorBuffer = new BufferedReader(new FileReader(archivoTemporal));
            while ((linea = lectorBuffer.readLine()) != null) {
                escritorBuffer.write(linea);
                escritorBuffer.newLine();
            }
            escritorBuffer.close();
            lectorBuffer.close();
            archivoTemporal.delete();

            return eliminado;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar vendedor");
            e.printStackTrace();
        } finally {
            try {
                if (null != lectorArchivo) {
                    lectorArchivo.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error cerrando el archivo");
            }
        }
        return false;
    }

    @Override
    public boolean actualizarVendedor(VendedorDTO vendedor) {
        archivoVendedor = new File(FILE_NAME);
        String linea;
        boolean encontrado = false;
        try {
            lectorArchivo = new FileReader(archivoVendedor);
            lectorBuffer = new BufferedReader(lectorArchivo);

            while ((linea = lectorBuffer.readLine()) != null) {
                String[] parametros = linea.split(",");
                if (parametros.length == 3 && parametros[0].trim().equalsIgnoreCase(vendedor.getIdentificacion().trim())) {
                    encontrado = true;
                    break;
                }
            }
            lectorBuffer.close();

            if (encontrado) {
                eliminarVendedor(vendedor.getIdentificacion());
                almacenarVendedor(vendedor);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}