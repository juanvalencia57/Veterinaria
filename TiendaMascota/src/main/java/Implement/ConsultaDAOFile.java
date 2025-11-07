package Implement;

import DAO.ConsultaDAO;
import Modelo.ConsultaDTO;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class ConsultaDAOFile implements ConsultaDAO {

    private static final String FILE_NAME = "Consulta.txt";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DELIM = ",";
     private BufferedWriter escritorBuffer;
    private BufferedReader lectorBuffer;
    private FileWriter escritorArchivo;
    private FileReader lectorArchivo;
    private File archivoMascota;
    private SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public ConsultaDAOFile() {
        File archivoConsulta = new File(FILE_NAME);
        try {
            if (!archivoConsulta.exists()) {
                archivoConsulta.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo crear el archivo Consulta.txt");
        }
    }

    @Override
    public boolean almacenarConsulta(ConsultaDTO consulta) {
        StringBuilder sb = new StringBuilder();
        sb.append(consulta.getIdMascota()).append(DELIM);
        sb.append(consulta.getNombreMascota()).append(DELIM);
        sb.append(sdf.format(consulta.getFecha())).append(DELIM);
        sb.append(consulta.getSintomas()).append(DELIM);
        sb.append(consulta.getTratamiento());
        try (BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            escritorBuffer.write(sb.toString());
            escritorBuffer.newLine();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la consulta");
        }
        return false;
    }

    // Cambiado: recibe solo id y retorna todas las consultas de esa mascota
    @Override
    public List<ConsultaDTO> consultarConsulta(String idMascota) {
        List<ConsultaDTO> resultado = new ArrayList<>();
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIM, -1);
                if (params.length >= 5 && params[0].equals(idMascota)) {
                    Date fecha = sdf.parse(params[2]);
                    ConsultaDTO consulta = new ConsultaDTO(
                        params[0], // ID Mascota
                        params[1], // Nombre Mascota
                        fecha,
                        params[3], // Síntomas
                        params[4]  // Tratamiento
                    );
                    resultado.add(consulta);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    // Buscar consulta específica por id y fecha (puedes definir consultarConsultaPorFecha si lo necesitas)

    @Override
    public boolean eliminarConsulta(String idMascota, String fechaStr) {
        File archivoTemporal = new File("consulta_temp.txt");
        boolean eliminado = false;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME));
             BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoTemporal, false))) {
            String linea;
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIM, -1);
                if (params.length >= 5 && params[0].equals(idMascota) && params[2].equals(fechaStr)) {
                    eliminado = true;
                } else {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar consulta");
            e.printStackTrace();
        }
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
        return false;
    }

    @Override
    public boolean actualizarConsulta(ConsultaDTO consultaNueva) {
        if (eliminarConsulta(consultaNueva.getIdMascota(), sdf.format(consultaNueva.getFecha()))) {
            return almacenarConsulta(consultaNueva);
        }
        return false;
    }

    @Override
    public List<ConsultaDTO> listarConsultas() {
        List<ConsultaDTO> resultado = new ArrayList<>();
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIM, -1);
                if (params.length >= 5) {
                    Date fecha = sdf.parse(params[2]);
                    ConsultaDTO consulta = new ConsultaDTO(
                        params[0], params[1], fecha, params[3], params[4]
                    );
                    resultado.add(consulta);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
}