package Negocio;

import DAO.MascotaDAO;
import DAO.VacunaDAO;
import Modelo.MascotaDTO;
import Modelo.VacunaDTO;
import Modelo.DosisDTO;
import Implement.MascotaDAOFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Lógica de negocio para la gestión de vacunas y dosis asociadas a mascotas.
 */
public class VacunaNegocio {

    private MascotaDAO mascotaDAO;
    private VacunaDAO vacunaDAO;

    public VacunaNegocio() {
        this.mascotaDAO = new MascotaDAOFile(); // Inicializa DAO de mascota correctamente
        // Si tienes VacunaDAO, inicialízalo aquí, por defecto puedes dejarlo nulo.
        // this.vacunaDAO = new VacunaDAOFile();
    }

    /**
     * Agrega (o actualiza) una vacuna y dosis para una mascota específica.
     */
    public boolean agregarVacuna(String idMascota, String tipoVacuna, Date fecha, double cantidad) {
        // Busca la mascota
        MascotaDTO mascota = mascotaDAO.consultarMascota(idMascota);
        if (mascota == null) return false;

        List<VacunaDTO> vacunas = mascota.getVacunas();
        if (vacunas == null) {
            vacunas = new ArrayList<>();
            mascota.setVacunas(vacunas);
        }

        // Busca si la vacuna ya existe en la mascota
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

        // Agrega la dosis a la vacuna
        vacuna.getDosis().add(new DosisDTO(fecha, cantidad));

        // Guarda el cambio en la mascota
        return mascotaDAO.actualizarMascota(mascota);
    }

    /**
     * Consulta la lista de vacunas de una mascota por su id.
     */
    public List<VacunaDTO> listarVacunasPorMascota(String idMascota) {
        MascotaDTO mascota = mascotaDAO.consultarMascota(idMascota);
        if (mascota != null && mascota.getVacunas() != null) {
            return mascota.getVacunas();
        }
        return new ArrayList<>();
    }

    /**
     * Elimina una vacuna específica de una mascota.
     */
    public boolean eliminarVacuna(String tipoVacuna, String idMascota) {
        MascotaDTO mascota = mascotaDAO.consultarMascota(idMascota);
        if (mascota == null || mascota.getVacunas() == null) return false;

        boolean eliminada = mascota.getVacunas().removeIf(vacuna -> vacuna.getTipo().equalsIgnoreCase(tipoVacuna));
        if (eliminada) {
            return mascotaDAO.actualizarMascota(mascota);
        }
        return false;
    }

    /**
     * Actualiza los datos de una vacuna en una mascota.
     */
    public boolean actualizarVacuna(VacunaDTO vacunaAct, String idMascota) {
        MascotaDTO mascota = mascotaDAO.consultarMascota(idMascota);
        if (mascota == null || mascota.getVacunas() == null) return false;
        
        boolean actualizada = false;
        List<VacunaDTO> vacunas = mascota.getVacunas();
        for (int i = 0; i < vacunas.size(); i++) {
            VacunaDTO vacuna = vacunas.get(i);
            if (vacuna.getTipo().equalsIgnoreCase(vacunaAct.getTipo())) {
                vacunas.set(i, vacunaAct);
                actualizada = true;
                break;
            }
        }
        if (actualizada) {
            return mascotaDAO.actualizarMascota(mascota);
        }
        return false;
    }
}