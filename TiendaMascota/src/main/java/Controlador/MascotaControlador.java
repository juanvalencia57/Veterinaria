package Controlador;

import Modelo.MascotaDTO;
import Modelo.VacunaDTO;
import Negocio.MascotaNegocio;
import Vista.TablaMascota;

import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MascotaControlador {

    MascotaNegocio mascotaNegocio = new MascotaNegocio();
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public void almacenarMascota(
            String nombre,
            String raza,
            int edad,
            String tipo,
            String id,
            double peso,
            String fechaIngreso,
            String lugarOrigen,
            String genero,
            double precio
    ) {
        // Normalización mínima (sin cambiar estructura)
        nombre = nombre == null ? "" : nombre.trim();
        raza = raza == null ? "" : raza.trim();
        tipo = tipo == null ? "" : tipo.trim();
        id = id == null ? "" : id.trim();
        fechaIngreso = fechaIngreso == null ? "" : fechaIngreso.trim();
        lugarOrigen = lugarOrigen == null ? "" : lugarOrigen.trim();
        genero = genero == null ? "" : genero.trim();

        // Validaciones estándar (mismo orden y mensajes; ajustes mínimos)
        if (!nombre.matches("[a-zA-Z ]*")) {
            datosErroneos("El nombre sólo puede contener letras y espacios");
            return;
        }
        if (nombre.isEmpty()) {
            datosErroneos("Debe ingresar el nombre de la mascota");
            return;
        }
        if (!raza.matches("[a-zA-Z ]*")) {
            datosErroneos("La raza sólo puede contener letras y espacios");
            return;
        }
        if (raza.isEmpty()) {
            datosErroneos("Debe ingresar la raza de la mascota");
            return;
        }
        if (!tipo.matches("[a-zA-Z ]*")) {
            datosErroneos("El tipo sólo puede contener letras y espacios");
            return;
        }
        if (tipo.isEmpty()) {
            datosErroneos("Debe ingresar el tipo de animal que es su mascota");
            return;
        }
        if (!lugarOrigen.matches("[a-zA-Z ]*")) {
            datosErroneos("El lugar de origen sólo puede contener letras y espacios");
            return;
        }
        if (lugarOrigen.isEmpty()) {
            datosErroneos("Debe ingresar el lugar de origen de la mascota");
            return;
        }
        // Fecha estricta yyyy-MM-dd (cambio mínimo: regex y parseo estricto)
        if (!fechaIngreso.matches("\\d{4}-\\d{2}-\\d{2}")) {
            datosErroneos("La fecha de ingreso debe tener el formato yyyy-MM-dd");
            return;
        }
        if (fechaIngreso.isEmpty()) {
            datosErroneos("Debe ingresar la Fecha en que ingreso la mascota");
            return;
        }
        if (!(genero.equalsIgnoreCase("M") || genero.equalsIgnoreCase("F"))) {
            datosErroneos("Por favor ingrese en el género \"F\" para femenino o \"M\" para masculino");
            return;
        }
        if (genero.isEmpty()) {
            datosErroneos("Debe ingresar el genero de la mascota");
            return;
        }
        if (!id.matches("[0-9]*")) {
            datosErroneos("El ID debe ser un número sin puntos ni comas");
            return;
        }
        if (id.isEmpty()) {
            datosErroneos("Debe ingresar un ID para la mascota");
            return;
        }

        if (edad < 0) {
            datosErroneos("La edad no puede ser negativa");
            return;
        }
        if (peso <= 0) {
            datosErroneos("El peso debe ser mayor que cero");
            return;
        }
        if (precio < 0) {
            datosErroneos("El precio no puede ser negativo");
            return;
        }
        if (nombre.isEmpty() || raza.isEmpty() || tipo.isEmpty() || id.isEmpty()
                || lugarOrigen.isEmpty() || genero.isEmpty() || fechaIngreso.isEmpty()) {
            datosErroneos("Debe de completar todos los campos para poder ingresar una mascota.");
            return;
        }

        char generoChar = genero.charAt(0);
        Date fechaIngresoDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            sdf.setLenient(false); // valida estrictamente yyyy-MM-dd
            fechaIngresoDate = sdf.parse(fechaIngreso);
        } catch (Exception e) {
            datosErroneos("Formato de fecha inválido. Use yyyy-MM-dd.");
            return;
        }

        MascotaDTO mascota = new MascotaDTO(
                nombre, raza, edad, tipo, id, peso,
                fechaIngresoDate, lugarOrigen, generoChar, precio,
                new java.util.ArrayList<>(), // Vacunas vacías al registrar
                null // idDueño null al registrar
        );

        if (mascotaNegocio.consultarMascota(id) != null) {
            int respuesta = JOptionPane.showConfirmDialog(
                    null,
                    "La mascota ya se encuentra registrada, ¿desea actualizar la información con los datos ingresados?",
                    "Mascota existente",
                    JOptionPane.YES_NO_OPTION
            );
            if (respuesta == JOptionPane.YES_OPTION) {
                actualizarMascota(nombre, raza, edad, tipo, id, peso, fechaIngreso, lugarOrigen, genero, precio);
            } else {
                datosErroneos("No se almacenó la mascota porque el ID ya existe.");
            }
            return;
        }

        if (mascotaNegocio.almacenarMascota(mascota)) {
            operacionExitosa("Se ha almacenado la mascota correctamente");
        } else {
            datosErroneos("No se pudo almacenar la mascota.");
        }
    }

    public void consultarMascota(String id) {
        id = id == null ? "" : id.trim();
        if (!id.matches("[0-9]*")) {
            datosErroneos("El ID debe ser un número sin puntos ni comas");
            return;
        }
        if (id.isEmpty()) {
            datosErroneos("Debe de ingresar un ID.");
            return;
        }
        MascotaDTO mascota = mascotaNegocio.consultarMascota(id);
        if (mascota == null) {
            operacionExitosa("El número de ID ingresado no coincide con ninguno de los ids registrados");
        } else {
            StringBuilder info = new StringBuilder();
            info.append("Datos mascotales:\n\n").append(mascota.toString());

            List<VacunaDTO> vacunas = mascota.getVacunas();
            if (vacunas != null && !vacunas.isEmpty()) {
                info.append("\n\nVacunas y dosis:\n");
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                for (VacunaDTO v : vacunas) {
                    info.append("Vacuna: ").append(v.getTipo()).append("\n");
                    if (v.getDosis() != null && !v.getDosis().isEmpty()) {
                        for (Modelo.DosisDTO d : v.getDosis()) {
                            info.append("   - Fecha: ").append(sdf.format(d.getFecha()))
                                    .append(", Cantidad: ").append(d.getCantidad()).append("\n");
                        }
                    } else {
                        info.append("   (Sin dosis registradas)\n");
                    }
                }
            }
            // Mostrar dueño si existe
            if (mascota.getIdDueño() != null && !mascota.getIdDueño().isEmpty()) {
                info.append("\n\nDueño actual (ID cliente): ").append(mascota.getIdDueño());
            } else {
                info.append("\n\nMascota SIN DUEÑO (disponible para venta)");
            }
            operacionExitosa(info.toString());
        }
    }

    public void eliminarMascota(String id) {
        id = id == null ? "" : id.trim();
        if (!id.matches("[0-9]*")) {
            datosErroneos("El id debe ser un número sin puntos ni comas");
            return;
        }

        if (mascotaNegocio.eliminarMascota(id)) {
            operacionExitosa("Se ha eliminado la mascota");
        } else {
            JOptionPane.showMessageDialog(null, "Ninguna mascota coincide con el número de id ingresado",
                    "Mascota no encontrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarMascota(
            String nombre,
            String raza,
            int edad,
            String tipo,
            String id,
            double peso,
            String fechaIngreso,
            String lugarOrigen,
            String genero,
            double precio
    ) {
        // Normalización mínima
        nombre = nombre == null ? "" : nombre.trim();
        raza = raza == null ? "" : raza.trim();
        tipo = tipo == null ? "" : tipo.trim();
        id = id == null ? "" : id.trim();
        fechaIngreso = fechaIngreso == null ? "" : fechaIngreso.trim();
        lugarOrigen = lugarOrigen == null ? "" : lugarOrigen.trim();
        genero = genero == null ? "" : genero.trim();

        // Validaciones estándar (mismo orden y mensajes; ajustes mínimos)
        if (nombre.isEmpty() || raza.isEmpty() || tipo.isEmpty() || id.isEmpty()
                || lugarOrigen.isEmpty() || genero.isEmpty() || fechaIngreso.isEmpty()) {
            datosErroneos("Debe de completar todos los campos para poder actualizar una mascota.");
            return;
        }
        if (!nombre.matches("[a-zA-Z ]*")) {
            datosErroneos("El nombre sólo puede contener letras y espacios");
            return;
        }
        if (nombre.isEmpty()) {
            datosErroneos("Debe ingresar el nombre de la mascota");
            return;
        }
        if (!raza.matches("[a-zA-Z ]*")) {
            datosErroneos("La raza sólo puede contener letras y espacios");
            return;
        }
        if (raza.isEmpty()) {
            datosErroneos("Debe ingresar la raza de la mascota");
            return;
        }
        if (!tipo.matches("[a-zA-Z ]*")) {
            datosErroneos("El tipo sólo puede contener letras y espacios");
            return;
        }
        if (tipo.isEmpty()) {
            datosErroneos("Debe ingresar que tipo de animal es su mascota");
            return;
        }
        if (!lugarOrigen.matches("[a-zA-Z ]*")) {
            datosErroneos("El lugar de origen sólo puede contener letras y espacios");
            return;
        }
        if (lugarOrigen.isEmpty()) {
            datosErroneos("Debe ingresar el lugar de origen de la mascota");
            return;
        }
        if (!fechaIngreso.matches("\\d{4}-\\d{2}-\\d{2}")) {
            datosErroneos("La fecha de ingreso debe tener el formato yyyy-MM-dd");
            return;
        }
        if (fechaIngreso.isEmpty()) {
            datosErroneos("Debe ingresar la fecha de ingreso de la mascota");
            return;
        }
        if (!(genero.equalsIgnoreCase("M") || genero.equalsIgnoreCase("F"))) {
            datosErroneos("Por favor ingrese en el género \"F\" para femenino o \"M\" para masculino");
            return;
        }
        if (genero.isEmpty()) {
            datosErroneos("Debe ingresar el genero de la mascota");
            return;
        }
        if (!id.matches("[0-9]*")) {
            datosErroneos("El id debe ser un número sin puntos ni comas");
            return;
        }
        if (id.isEmpty()) {
            datosErroneos("Debe ingresar una identificación");
            return;
        }

        if (edad < 0) {
            datosErroneos("La edad no puede ser negativa");
            return;
        }
        if (peso <= 0) {
            datosErroneos("El peso debe ser mayor que cero");
            return;
        }
        if (precio < 0) {
            datosErroneos("El precio no puede ser negativo");
            return;
        }

        char generoChar = genero.charAt(0);
        Date fechaIngresoDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            sdf.setLenient(false); // valida estrictamente yyyy-MM-dd
            fechaIngresoDate = sdf.parse(fechaIngreso);
        } catch (Exception e) {
            datosErroneos("Formato de fecha inválido. Use yyyy-MM-dd.");
            return;
        }

        // Mantén las vacunas existentes y el dueño al actualizar
        MascotaDTO actual = mascotaNegocio.consultarMascota(id);
        List<VacunaDTO> vacunasExistentes = actual != null && actual.getVacunas() != null
                ? actual.getVacunas() : new java.util.ArrayList<>();
        String idDueñoExistente = actual != null ? actual.getIdDueño() : null;

        MascotaDTO mascota = new MascotaDTO(
                nombre, raza, edad, tipo, id, peso,
                fechaIngresoDate, lugarOrigen, generoChar, precio,
                vacunasExistentes,
                idDueñoExistente // importante para no perder el dueño
        );

        if (mascotaNegocio.actualizarMascota(mascota)) {
            operacionExitosa("Se ha actualizado la mascota");
        } else {
            datosErroneos("No se ha podido actualizar la mascota\nPor favor verifique si la mascota se encuentra registrada");
        }
    }

    public void listarMascotas() {
        TablaMascota ventana = new TablaMascota();
        ventana.llenarTabla();
        ventana.setVisible(true);
    }

    public void operacionExitosa(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    public void datosErroneos(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
    }
}