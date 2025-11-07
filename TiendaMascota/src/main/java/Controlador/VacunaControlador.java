package Controlador;

import Modelo.VacunaDTO;
import Modelo.DosisDTO;
import Negocio.VacunaNegocio;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.List;

/**
 * Controlador para operaciones de vacunas y dosis en mascotas.
 * Recibe siempre el id de la mascota como argumento.
 */
public class VacunaControlador {

    private VacunaNegocio vacunaNegocio = new VacunaNegocio();

    /**
     * Agrega una vacuna y dosis a la mascota indicada por id.
     */
    public void agregarVacuna(String idMascota, String tipoVacuna, Date fecha, double cantidad) {
        // Validaciones
        if (idMascota == null || idMascota.trim().isEmpty()) {
            datosErroneos("Debe proporcionar un id de mascota.");
            return;
        }
        if (tipoVacuna == null || tipoVacuna.trim().isEmpty()) {
            datosErroneos("Debe indicar el tipo de vacuna.");
            return;
        }
        if (fecha == null) {
            datosErroneos("Debe indicar la fecha de la dosis.");
            return;
        }
        if (cantidad <= 0) {
            datosErroneos("La cantidad debe ser mayor que cero.");
            return;
        }

        boolean exito = vacunaNegocio.agregarVacuna(idMascota, tipoVacuna, fecha, cantidad);
        if (exito) {
            operacionExitosa("Vacuna registrada correctamente en la mascota.");
        } else {
            datosErroneos("No se pudo registrar la vacuna. Verifica el ID de la mascota y que exista en el sistema.");
        }
    }

    /**
     * Lista todas las vacunas y dosis de una mascota por su id.
     */
    public void consultarVacunasPorMascota(String idMascota) {
        if (idMascota == null || idMascota.trim().isEmpty()) {
            datosErroneos("Debe proporcionar un id de mascota.");
            return;
        }
        List<VacunaDTO> vacunas = vacunaNegocio.listarVacunasPorMascota(idMascota);
        StringBuilder info = new StringBuilder();
        if (vacunas == null || vacunas.isEmpty()) {
            info.append("No hay vacunas registradas para esta mascota.");
        } else {
            info.append("Vacunas y dosis de la mascota ").append(idMascota).append(":\n");
            for (VacunaDTO vacuna : vacunas) {
                info.append("- Vacuna: ").append(vacuna.getTipo()).append("\n");
                List<DosisDTO> dosisList = vacuna.getDosis();
                if (dosisList != null && !dosisList.isEmpty()) {
                    for (DosisDTO dosis : dosisList) {
                        info.append("     Fecha: ").append(dosis.getFecha())
                            .append(", Cantidad: ").append(dosis.getCantidad())
                            .append("\n");
                    }
                } else {
                    info.append("     (Sin dosis registradas)\n");
                }
            }
        }
        operacionExitosa(info.toString());
    }

    /**
     * Elimina una vacuna específica de una mascota.
     */
    public void eliminarVacuna(String idMascota, String tipoVacuna) {
        if (idMascota == null || idMascota.trim().isEmpty()) {
            datosErroneos("Debe proporcionar un id de mascota.");
            return;
        }
        if (tipoVacuna == null || tipoVacuna.trim().isEmpty()) {
            datosErroneos("Debe indicar el tipo de vacuna a eliminar.");
            return;
        }
        boolean exito = vacunaNegocio.eliminarVacuna(tipoVacuna, idMascota);
        if (exito) {
            operacionExitosa("Vacuna eliminada correctamente.");
        } else {
            datosErroneos("No se pudo eliminar la vacuna. Verifique los datos.");
        }
    }

    /**
     * Actualiza los datos de una vacuna en una mascota.
     */
    public void actualizarVacuna(String idMascota, VacunaDTO vacuna) {
        if (idMascota == null || idMascota.trim().isEmpty()) {
            datosErroneos("Debe proporcionar un id de mascota.");
            return;
        }
        if (vacuna == null) {
            datosErroneos("Debe proporcionar los datos de la vacuna.");
            return;
        }
        boolean exito = vacunaNegocio.actualizarVacuna(vacuna, idMascota);
        if (exito) {
            operacionExitosa("Vacuna actualizada correctamente.");
        } else {
            datosErroneos("No se pudo actualizar la vacuna. Verifique los datos.");
        }
    }

    /**
     * Métodos de mensajes
     */
    public void operacionExitosa(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    public void datosErroneos(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
    }
}