package DAO;

import Modelo.VacunaDTO;
import java.util.List;

public interface VacunaDAO {
    public boolean almacenarVacuna(VacunaDTO vacuna, String id); // Usa 'id' de la mascota, igual al MascotaDAO
    public List<VacunaDTO> listarVacunasPorMascota(String id);   // Vacunas del perro con ese id (id de mascota)
    public boolean eliminarVacuna(String tipoVacuna, String id);
    public boolean actualizarVacuna(VacunaDTO vacuna, String id);
}