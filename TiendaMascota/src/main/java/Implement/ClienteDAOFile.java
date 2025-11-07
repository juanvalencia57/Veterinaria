package Implement;

import java.util.List;
import java.util.Scanner;
import java.io.*;
import javax.swing.JOptionPane;
import Modelo.ClienteDTO;
import java.util.ArrayList;
import java.util.Arrays;
import DAO.ClienteDAO;

/**
 *
 * @author USUARIO
 */
public class ClienteDAOFile implements ClienteDAO {

    private static final String DELIMITADOR_ARCHIVO = ",";
    private static final String DELIM_MASCOTAS = ";";
    private static final String FILE_NAME = "Cliente.txt";
    private BufferedWriter escritorBuffer;
    private BufferedReader lectorBuffer;
    private FileWriter escritorArchivo;
    private FileReader lectorArchivo;
    private File archivoMascota;
    private File archivoCliente;

    public ClienteDAOFile() {
        archivoCliente = new File(FILE_NAME);
        try {
            // Si el archivo no existe, lo crea vacío
            if (!archivoCliente.exists()) {
                archivoCliente.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo crear el archivo Cliente.txt");
        }
    }

    // Convierte la lista de mascotas a cadena separada por ; para guardar en txt
    private String mascotasToString(List<String> mascotas) {
        if (mascotas == null || mascotas.isEmpty()) return "";
        return String.join(DELIM_MASCOTAS, mascotas);
    }

    // Convierte en lista lo leído del archivo (campo de mascotas)
    private List<String> stringToMascotas(String str) {
        if (str == null || str.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(str.split(DELIM_MASCOTAS)));
    }

    @Override
    public boolean almacenarCliente(ClienteDTO cliente) {
        StringBuilder sb = new StringBuilder();
        sb.append(cliente.getIdentificacion()).append(DELIMITADOR_ARCHIVO);
        sb.append(cliente.getNombres()).append(DELIMITADOR_ARCHIVO);
        sb.append(cliente.getDireccionContacto()).append(DELIMITADOR_ARCHIVO);
        sb.append(cliente.getNumeroContacto()).append(DELIMITADOR_ARCHIVO);
        sb.append(mascotasToString(cliente.getMascotas())); // campo de mascotas (puede ser vacío)
        try (BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoCliente, true))) {
            escritorBuffer.write(sb.toString());
            escritorBuffer.newLine();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar cliente");
        }
        return false;
    }

    @Override
    public ClienteDTO consultarCliente(String identificacion) {
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoCliente))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] parametros = linea.split(DELIMITADOR_ARCHIVO, -1);
                // Siempre esperamos 5 campos (último es mascotas, puede estar vacío)
                if (parametros.length >= 4 && parametros[0].equals(identificacion)) {
                    List<String> mascotas = parametros.length >=5 ? stringToMascotas(parametros[4]) : new ArrayList<>();
                    return new ClienteDTO(
                            parametros[0], // identificacion
                            parametros[1], // nombres
                            parametros[2], // direccion
                            parametros[3], // numero
                            mascotas
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ClienteDTO> listarCliente() {
        archivoCliente = new File(FILE_NAME);
        String linea;
        List<ClienteDTO> clientes = new ArrayList<>();
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoCliente))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] parametros = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (parametros.length >= 4) {
                    try {
                        List<String> mascotas = parametros.length >=5 ? stringToMascotas(parametros[4]) : new ArrayList<>();
                        ClienteDTO cliente = new ClienteDTO(
                                parametros[0], // identificacion
                                parametros[1], // nombres
                                parametros[2], // direccion
                                parametros[3], // numero
                                mascotas
                        );
                        clientes.add(cliente);
                    } catch (Exception ex) {
                        continue;
                    }
                }
            }
            return clientes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean eliminarCliente(String identificacion) {
        archivoCliente = new File(FILE_NAME);
        File archivoTemporal = new File("temporal_cliente.txt");
        boolean eliminado = false;
        String linea;

        try (
            BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoCliente));
            BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoTemporal, false))
        ) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] parametros = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (parametros.length >= 4 && parametros[0].equals(identificacion)) {
                    eliminado = true; // no escribir esta línea
                } else {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error eliminando cliente");
            return false;
        }

        // Ahora reemplaza el original con el temporal si se eliminó alguno
        if (eliminado) {
            try (
                BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoTemporal));
                BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoCliente, false))
            ) {
                while ((linea = lectorBuffer.readLine()) != null) {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error finalizando eliminación cliente");
            }
            archivoTemporal.delete();
            return true;
        } else {
            archivoTemporal.delete();
        }
        return false;
    }

    @Override
    public boolean actualizarCliente(ClienteDTO cliente) {
        // Eliminar primero, luego agregar (manteniendo lista de mascotas)
        if (eliminarCliente(cliente.getIdentificacion())) {
            return almacenarCliente(cliente);
        }
        return false;
    }
}