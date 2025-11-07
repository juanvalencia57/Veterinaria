package Negocio;

import DAO.ConsultaDAO;
import Implement.ConsultaDAOFile;
import Modelo.ConsultaDTO;

import java.util.Date;
import java.util.List;

/**
 * Lógica de negocio para la gestión de consultas veterinarias.
 */
public class ConsultaNegocio {

    private ConsultaDAO consultaDAO;

    public ConsultaNegocio() {
        this.consultaDAO = new ConsultaDAOFile();
    }

    /**
     * Registrar una consulta médica para una mascota.
     */
    public boolean registrarConsulta(String idMascota, String nombreMascota, Date fecha, String sintomas, String tratamiento) {
        ConsultaDTO consulta = new ConsultaDTO(idMascota, nombreMascota, fecha, sintomas, tratamiento);
        return consultaDAO.almacenarConsulta(consulta);
    }

    /**
     * Consultar TODAS las consultas de una mascota por su ID.
     */
    public List<ConsultaDTO> consultarConsulta(String idMascota) {
        return consultaDAO.consultarConsulta(idMascota);
    }

    /**
     * Listar todas las consultas registradas.
     */
    public List<ConsultaDTO> listarConsultas() {
        return consultaDAO.listarConsultas();
    }

    /**
     * Eliminar una consulta por id y fecha.
     */
    public boolean eliminarConsulta(String idMascota, String fechaStr) {
        return consultaDAO.eliminarConsulta(idMascota, fechaStr);
    }

    /**
     * Actualizar una consulta existente por id y fecha.
     */
    public boolean actualizarConsulta(String idMascota, String nombreMascota, Date fecha, String sintomas, String tratamiento) {
        ConsultaDTO consultaNueva = new ConsultaDTO(idMascota, nombreMascota, fecha, sintomas, tratamiento);
        return consultaDAO.actualizarConsulta(consultaNueva);
    }
}