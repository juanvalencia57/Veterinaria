package Controlador;

import Modelo.ConsultaDTO;
import Implement.MascotaDAOFile;
import Modelo.MascotaDTO;
import Negocio.ConsultaNegocio;
import Vista.TablaConsulta;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controlador para la gestión de consultas veterinarias.
 */
public class ConsultaControlador {

    private final ConsultaNegocio consultaNegocio = new ConsultaNegocio();
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    // Registrar una consulta veterinaria
    public void registrarConsulta(String idMascota, String fechaStr, String sintomas, String tratamiento) {
        if (idMascota.isEmpty() || fechaStr.isEmpty() || sintomas.isEmpty() || tratamiento.isEmpty()) {
            datosErroneos("Debe completar todos los campos para registrar una consulta.");
            return;
        }
        if (!idMascota.matches("[0-9A-Za-z]+")) {
            datosErroneos("El ID de mascota sólo puede contener números y letras.");
            return;
        }
        if (!fechaStr.matches("[0-9\\-/]+")) {
            datosErroneos("La fecha debe tener el formato yyyy-MM-dd.");
            return;
        }

        Date fecha;
        try {
            fecha = new SimpleDateFormat(DATE_FORMAT).parse(fechaStr);
        } catch (Exception e) {
            datosErroneos("Formato de fecha inválido. Use yyyy-MM-dd.");
            return;
        }

        MascotaDTO mascota = new MascotaDAOFile().consultarMascota(idMascota);
        if (mascota == null) {
            datosErroneos("No existe una mascota registrada con ese ID.");
            return;
        }
        String nombreMascota = mascota.getNombre();

        ConsultaDTO consulta = new ConsultaDTO(idMascota, nombreMascota, fecha, sintomas, tratamiento);

        // Cambia aquí: ya no consultar por fecha, sino por ID solamente
        List<ConsultaDTO> consultasMascota = consultaNegocio.consultarConsulta(idMascota);
        boolean consultaExisteParaFecha = false;
        for (ConsultaDTO c : consultasMascota) {
            String fechaConsulta = new SimpleDateFormat(DATE_FORMAT).format(c.getFecha());
            if (fechaConsulta.equals(fechaStr)) {
                consultaExisteParaFecha = true;
                break;
            }
        }

        if (consultaExisteParaFecha) {
            int respuesta = JOptionPane.showConfirmDialog(
                null,
                "Ya existe una consulta registrada para esta mascota en esa fecha. ¿Desea actualizarla?",
                "Consulta existente",
                JOptionPane.YES_NO_OPTION
            );
            if (respuesta == JOptionPane.YES_OPTION) {
                actualizarConsulta(idMascota, fechaStr, sintomas, tratamiento);
            } else {
                datosErroneos("No se almacenó la consulta porque ya existe una igual.");
            }
            return;
        }

        if (consultaNegocio.registrarConsulta(idMascota, nombreMascota, fecha, sintomas, tratamiento)) {
            operacionExitosa("Consulta almacenada correctamente.");
        } else {
            datosErroneos("No se pudo almacenar la consulta.");
        }
    }

    // Consultar todas las consultas de una mascota por su ID
    public void consultarConsulta(String idMascota) {
        if (idMascota.isEmpty()) {
            datosErroneos("Debe ingresar el ID de mascota.");
            return;
        }
        if (!idMascota.matches("[0-9A-Za-z]+")) {
            datosErroneos("El ID de mascota sólo puede contener números y letras.");
            return;
        }

        List<ConsultaDTO> consultas = consultaNegocio.consultarConsulta(idMascota);
        if (consultas == null || consultas.isEmpty()) {
            operacionExitosa("No se encontraron consultas para la mascota con ID: " + idMascota);
        } else {
            // Mostrar en TablaConsulta si existe el método para mostrar por lista
            TablaConsulta tabla = new TablaConsulta();
            tabla.llenarTablaPorMascota(consultas); // Este método recibe solo las consultas de esa mascota
            tabla.setVisible(true);
        }
    }

    // Actualizar una consulta: requiere todos los datos
    public void actualizarConsulta(String idMascota, String fechaStr, String sintomas, String tratamiento) {
        if (idMascota.isEmpty() || fechaStr.isEmpty() || sintomas.isEmpty() || tratamiento.isEmpty()) {
            datosErroneos("Debe completar todos los campos para actualizar la consulta.");
            return;
        }
        Date fecha;
        try {
            fecha = new SimpleDateFormat(DATE_FORMAT).parse(fechaStr);
        } catch (Exception e) {
            datosErroneos("Formato de fecha inválido. Use yyyy-MM-dd.");
            return;
        }
        MascotaDTO mascota = new MascotaDAOFile().consultarMascota(idMascota);
        if (mascota == null) {
            datosErroneos("No existe una mascota registrada con ese ID.");
            return;
        }
        String nombreMascota = mascota.getNombre();
        if (consultaNegocio.actualizarConsulta(idMascota, nombreMascota, fecha, sintomas, tratamiento)) {
            operacionExitosa("Consulta actualizada correctamente.");
        } else {
            datosErroneos("No se encontró ninguna consulta para los datos ingresados.");
        }
    }

    // Eliminar una consulta específica por mascota y fecha
    public void eliminarConsulta(String idMascota, String fechaStr) {
        if (idMascota.isEmpty() || fechaStr.isEmpty()) {
            datosErroneos("Debe ingresar el ID de mascota y la fecha.");
            return;
        }
        if (!idMascota.matches("[0-9A-Za-z]+")) {
            datosErroneos("El ID de mascota sólo puede contener números y letras.");
            return;
        }
        if (!fechaStr.matches("[0-9\\-/]+")) {
            datosErroneos("La fecha debe tener el formato yyyy-MM-dd.");
            return;
        }
        if (consultaNegocio.eliminarConsulta(idMascota, fechaStr)) {
            operacionExitosa("Consulta eliminada correctamente.");
        } else {
            datosErroneos("No se encontró ninguna consulta para los datos ingresados.");
        }
    }

    // Listar todas las consultas registradas
    public void listarConsultas() {
        TablaConsulta ventana = new TablaConsulta();
        ventana.llenarTabla();
        ventana.setVisible(true);
    }

    // Mensajes
    public void operacionExitosa(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
    }
    public void datosErroneos(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
    }
}