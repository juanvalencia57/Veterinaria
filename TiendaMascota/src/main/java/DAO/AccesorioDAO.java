package DAO;

import Modelo.AccesorioDTO;
import java.util.List;

public interface AccesorioDAO {
    boolean almacenarAccesorio(AccesorioDTO accesorio);
    AccesorioDTO consultarAccesorio(String id);
    List<AccesorioDTO> listarAccesorios();
    boolean eliminarAccesorio(String id);
    boolean actualizarAccesorio(AccesorioDTO accesorio);
}