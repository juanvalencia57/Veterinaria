package Implement;

import Modelo.AccesorioDTO;
import DAO.AccesorioDAO;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import javax.swing.JOptionPane;

public class AccesorioDAOFile implements AccesorioDAO {
    private static final String DELIMITADOR_ARCHIVO = ",";
    private static final String FILE_NAME = "Accesorio.txt";
    private BufferedWriter escritorBuffer;
    private BufferedReader lectorBuffer;
    private FileWriter escritorArchivo;
    private FileReader lectorArchivo;
    private File archivoAccesorio;

    public AccesorioDAOFile() {
        archivoAccesorio = new File(FILE_NAME);
        try {
            // Si el archivo no existe, lo crea vacío
            if (!archivoAccesorio.exists()) {
                archivoAccesorio.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo crear el archivo Accesorio.txt");
        }
    }

    @Override
    public boolean almacenarAccesorio(AccesorioDTO accesorio) {
        StringBuilder sb = new StringBuilder();
        sb.append(accesorio.getId()).append(DELIMITADOR_ARCHIVO);
        sb.append(accesorio.getNombre()).append(DELIMITADOR_ARCHIVO);
        sb.append(accesorio.getTipo()).append(DELIMITADOR_ARCHIVO);
        sb.append(accesorio.getPrecio()).append(DELIMITADOR_ARCHIVO);
        sb.append(accesorio.getCantidad());

        try (BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            escritorBuffer.write(sb.toString());
            escritorBuffer.newLine();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el accesorio");
        }
        return false;
    }

    @Override
    public AccesorioDTO consultarAccesorio(String id) {
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (params.length >= 5 && params[0].equals(id)) {
                    return new AccesorioDTO(
                        params[0], // id
                        params[1], // nombre
                        params[2], // tipo
                        Double.parseDouble(params[3]), // precio
                        Integer.parseInt(params[4]) // cantidad
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AccesorioDTO> listarAccesorios() {
        List<AccesorioDTO> accesorios = new ArrayList<>();
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (params.length >= 5) {
                    try {
                        AccesorioDTO accesorio = new AccesorioDTO(
                            params[0],
                            params[1],
                            params[2],
                            Double.parseDouble(params[3]),
                            Integer.parseInt(params[4])
                        );
                        accesorios.add(accesorio);
                    } catch (Exception ex) { continue; }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accesorios;
    }

    @Override
    public boolean eliminarAccesorio(String id) {
        File archivoTemporal = new File("Accesorio_temp.txt");
        boolean eliminado = false;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME));
             BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoTemporal, false))) {
            String linea;
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (params.length >= 5 && params[0].equals(id)) {
                    eliminado = true;
                } else {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar accesorio");
            e.printStackTrace();
        }
        // Reemplaza el archivo original por el temporal
        if (eliminado) {
            try (
                BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoTemporal));
                BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(FILE_NAME))
            ) {
                String linea;
                while ((linea = lectorBuffer.readLine()) != null) {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al finalizar eliminación");
            }
            archivoTemporal.delete();
            return true;
        }
        archivoTemporal.delete();
        return false;
    }

    @Override
    public boolean actualizarAccesorio(AccesorioDTO accesorio) {
        if (eliminarAccesorio(accesorio.getId())) {
            return almacenarAccesorio(accesorio);
        }
        return false;
    }
}