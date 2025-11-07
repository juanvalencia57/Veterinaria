package Negocio;

import DAO.AccesorioDAO;
import Modelo.AccesorioDTO;
import Implement.AccesorioDAOFile;
import java.util.List;

public class AccesorioNegocio {
    private final AccesorioDAO accesorioDAO = new AccesorioDAOFile();

    public boolean almacenarAccesorio(AccesorioDTO accesorio) {
        if (consultarAccesorio(accesorio.getId()) == null) {
            accesorioDAO.almacenarAccesorio(accesorio);
            return true;
        }
        return false;
    }

    public List<AccesorioDTO> listarAccesorios() {
        return accesorioDAO.listarAccesorios();
    }

    public AccesorioDTO consultarAccesorio(String id) {
        return accesorioDAO.consultarAccesorio(id);
    }

    public boolean eliminarAccesorio(String id) {
        if (consultarAccesorio(id) != null) {
            accesorioDAO.eliminarAccesorio(id);
            return true;
        }
        return false;
    }

    public boolean actualizarAccesorio(AccesorioDTO accesorio) {
        if (consultarAccesorio(accesorio.getId()) != null) {
            return accesorioDAO.actualizarAccesorio(accesorio);
        }
        return false;
    }
    public String validarStockAccesorio(String idAccesorio, int cantidadDeseada) {
        AccesorioDTO accesorio = consultarAccesorio(idAccesorio);
        if (accesorio == null) {
            return "No existe el accesorio con ID " + idAccesorio + ".";
        }
        int stock = accesorio.getCantidad(); // o accesorio.getCantidad()
        if (stock == 0) {
            return "No hay stock disponible para el accesorio " + idAccesorio + ".";
        }
        if (stock - cantidadDeseada < 0) {
            return "No hay suficiente stock para el accesorio " + idAccesorio + 
                ". Solo hay " + stock + " unidades disponibles. Ingresa una cantidad menor.";
        }
        return null; // Hay stock suficiente
    }
}
