package DAO;

import Modelo.ConsultaDTO;
import java.util.List;

public interface ConsultaDAO {
    boolean almacenarConsulta(ConsultaDTO consulta);
    List<ConsultaDTO> consultarConsulta(String idMascota);
    boolean eliminarConsulta(String idMascota, String fechaStr);
    boolean actualizarConsulta(ConsultaDTO consulta);
    List<ConsultaDTO> listarConsultas();
}