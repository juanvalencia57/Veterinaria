package Negocio;
import DAO.MascotaDAO;
import Modelo.MascotaDTO;
import Modelo.VacunaDTO;
import Modelo.DosisDTO;
import Implement.MascotaDAOFile;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

public class MascotaNegocio {
    private MascotaDAO mascotaDAO = new MascotaDAOFile();

    public MascotaNegocio() {}

    public boolean almacenarMascota(MascotaDTO mascota) {
        // Almacena toda la mascota, con vacunas y dosis si existen
        if (consultarMascota(mascota.getId()) == null) {
            mascotaDAO.almacenarMascota(mascota);
            return true;
        }
        return false;
    }

    public List<MascotaDTO> listarMascota() {
        return mascotaDAO.listarMascota();
    }

    public MascotaDTO consultarMascota(String id) {
        return mascotaDAO.consultarMascota(id);
    }

    public boolean eliminarMascota(String id) {
        if (consultarMascota(id) != null) {
            mascotaDAO.eliminarMascota(id);
            return true;
        }
        return false;
    }

    public boolean actualizarMascota(MascotaDTO mascota) {
        if (consultarMascota(mascota.getId()) != null) {
            return mascotaDAO.actualizarMascota(mascota);
        }
        return false;
    }

    // Nuevo: método para agregar vacuna y dosis directamente en negocio
    public boolean agregarVacuna(String idMascota, String tipoVacuna, Date fecha, double cantidad) {
        MascotaDTO mascota = consultarMascota(idMascota);
        if (mascota == null) return false;

        List<VacunaDTO> vacunas = mascota.getVacunas();
        if (vacunas == null) {
            vacunas = new ArrayList<>();
            mascota.setVacunas(vacunas);
        }

        // Busca si ya existe la vacuna, si no la crea
        VacunaDTO vacuna = null;
        for (VacunaDTO v : vacunas) {
            if (v.getTipo().equalsIgnoreCase(tipoVacuna)) {
                vacuna = v;
                break;
            }
        }
        if (vacuna == null) {
            vacuna = new VacunaDTO(tipoVacuna, new ArrayList<>());
            vacunas.add(vacuna);
        }
        // Agrega la dosis
        vacuna.getDosis().add(new DosisDTO(fecha, cantidad));
        // Actualiza la mascota
        return mascotaDAO.actualizarMascota(mascota);
    }

    // NUEVO: métodos claros para asignar o eliminar dueño (para futuras extensiones)
    public boolean asignarDueño(String idMascota, String idDueño) {
        MascotaDTO mascota = consultarMascota(idMascota);
        if (mascota == null) return false;
        mascota.setIdDueño(idDueño);
        return mascotaDAO.actualizarMascota(mascota);
    }

    public boolean quitarDueño(String idMascota) {
        MascotaDTO mascota = consultarMascota(idMascota);
        if (mascota == null) return false;
        mascota.setIdDueño(null);
        return mascotaDAO.actualizarMascota(mascota);
    }
    public class MascotaYaVendidaException extends Exception {
    public MascotaYaVendidaException(String idMascota) {
        super("La mascota con ID '" + idMascota + "' ya fue vendida.");
    }
}
}