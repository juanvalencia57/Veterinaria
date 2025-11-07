package Implement;

import Modelo.MascotaDTO;
import Modelo.VacunaDTO;
import Modelo.DosisDTO;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import DAO.MascotaDAO;

public class MascotaDAOFile implements MascotaDAO {
    private static final String DELIMITADOR_ARCHIVO = ",";
    private static final String FILE_NAME = "Mascota.txt";
    private static final String VACUNA_DELIM = ";";
    private static final String DOSIS_DELIM = ",";
    private static final String DOSIS_DATA_DELIM = "|";
    private static final String DOSIS_FECHA_CANT_DELIM = ":";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private BufferedWriter escritorBuffer;
    private BufferedReader lectorBuffer;
    private FileWriter escritorArchivo;
    private FileReader lectorArchivo;
    private File archivoMascota;

    public MascotaDAOFile() {
        archivoMascota = new File(FILE_NAME);
        try {
            // Si el archivo no existe, lo crea vacío
            if (!archivoMascota.exists()) {
                archivoMascota.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo crear el archivo Mascota.txt");
        }
    }

    private String vacunasToString(List<VacunaDTO> vacunas) {
        if (vacunas == null || vacunas.isEmpty()) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vacunas.size(); i++) {
            VacunaDTO vacuna = vacunas.get(i);
            sb.append(vacuna.getTipo());
            sb.append(DOSIS_DATA_DELIM);
            List<DosisDTO> dosis = vacuna.getDosis();
            if (dosis != null && !dosis.isEmpty()) {
                for (int j = 0; j < dosis.size(); j++) {
                    DosisDTO d = dosis.get(j);
                    sb.append(sdf.format(d.getFecha()));
                    sb.append(DOSIS_FECHA_CANT_DELIM);
                    sb.append(d.getCantidad());
                    if (j < dosis.size() - 1) sb.append(DOSIS_DELIM);
                }
            }
            if (i < vacunas.size() - 1) sb.append(VACUNA_DELIM);
        }
        return sb.toString();
    }

    // Convierte cadena del archivo a lista de vacunas/dosis
    private List<VacunaDTO> stringToVacunas(String vacunasStr) {
        List<VacunaDTO> vacunas = new ArrayList<>();
        if (vacunasStr == null || vacunasStr.isEmpty()) return vacunas;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        String[] arrVacunas = vacunasStr.split(VACUNA_DELIM);
        for (String vstr : arrVacunas) {
            String[] tipoYDosis = vstr.split("\\" + DOSIS_DATA_DELIM, 2);
            if (tipoYDosis.length == 0) continue;
            String tipo = tipoYDosis[0];
            List<DosisDTO> dosisList = new ArrayList<>();
            if (tipoYDosis.length == 2 && !tipoYDosis[1].isEmpty()) {
                String[] dosisArr = tipoYDosis[1].split(DOSIS_DELIM);
                for (String dstr : dosisArr) {
                    String[] fechaCant = dstr.split(DOSIS_FECHA_CANT_DELIM);
                    if (fechaCant.length == 2) {
                        try {
                            Date fecha = sdf.parse(fechaCant[0]);
                            DosisDTO d = new DosisDTO(fecha, Double.parseDouble(fechaCant[1]));
                            dosisList.add(d);
                        } catch (Exception e) { continue; }
                    }
                }
            }
            vacunas.add(new VacunaDTO(tipo, dosisList));
        }
        return vacunas;
    }

    @Override
    public boolean almacenarMascota(MascotaDTO mascota) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        StringBuilder sb = new StringBuilder();
        sb.append(mascota.getNombre()).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getRaza()).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getEdad()).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getTipo()).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getId()).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getPeso()).append(DELIMITADOR_ARCHIVO);
        sb.append(sdf.format(mascota.getFechaIngreso())).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getLugarOrigen()).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getGenero()).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getPrecio()).append(DELIMITADOR_ARCHIVO);
        sb.append(vacunasToString(mascota.getVacunas())).append(DELIMITADOR_ARCHIVO);
        sb.append(mascota.getIdDueño() == null ? "" : mascota.getIdDueño()); // NUEVO: ID del dueño

        try (BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            escritorBuffer.write(sb.toString());
            escritorBuffer.newLine();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la mascota");
        }
        return false;
    }

    @Override
    public MascotaDTO consultarMascota(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                // ⬇️ Cambiamos >=11 a >=12 para considerar ID dueño
                if (params.length >= 12 && params[4].equals(id)) {
                    Date fechaIngreso = sdf.parse(params[6]);
                    return new MascotaDTO(
                        params[0], // nombre
                        params[1], // raza
                        Integer.parseInt(params[2]),
                        params[3],
                        params[4],
                        Double.parseDouble(params[5]),
                        fechaIngreso,
                        params[7],
                        params[8].charAt(0),
                        Double.parseDouble(params[9]),
                        stringToVacunas(params[10]),
                        params[11].isEmpty() ? null : params[11]  // idDueño
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MascotaDTO> listarMascota() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        List<MascotaDTO> mascotas = new ArrayList<>();
        String linea;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (params.length >= 12) {
                    try {
                        Date fechaIngreso = sdf.parse(params[6]);
                        MascotaDTO mascota = new MascotaDTO(
                            params[0],
                            params[1],
                            Integer.parseInt(params[2]),
                            params[3],
                            params[4],
                            Double.parseDouble(params[5]),
                            fechaIngreso,
                            params[7],
                            params[8].charAt(0),
                            Double.parseDouble(params[9]),
                            stringToVacunas(params[10]),
                            params[11].isEmpty() ? null : params[11] // idDueño
                        );
                        mascotas.add(mascota);
                    } catch (Exception ex) { continue; }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mascotas;
    }

    @Override
    public boolean eliminarMascota(String id) {
        File archivoTemporal = new File("temporal.txt");
        boolean eliminado = false;
        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(FILE_NAME));
             BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoTemporal, false))) {
            String linea;
            while ((linea = lectorBuffer.readLine()) != null) {
                String[] params = linea.split(DELIMITADOR_ARCHIVO, -1);
                if (params.length >= 12 && params[4].equals(id)) {
                    eliminado = true;
                } else {
                    escritorBuffer.write(linea);
                    escritorBuffer.newLine();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar mascota");
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
        return false;
    }

    @Override
    public boolean actualizarMascota(MascotaDTO mascota) {
        if (eliminarMascota(mascota.getId())) {
            return almacenarMascota(mascota);
        }
        return false;
    }
}