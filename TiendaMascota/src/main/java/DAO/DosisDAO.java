package DAO;

import Modelo.DosisDTO;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public interface DosisDAO {
    public boolean agregarDosis(DosisDTO dosis, String tipoVacuna, String id); 
    public List<DosisDTO> listarDosisPorVacunaYMascota(String tipoVacuna, String id);
    public boolean eliminarDosis(DosisDTO dosis, String tipoVacuna, String id);
    public boolean actualizarDosis(DosisDTO dosis, String tipoVacuna, String id);
}