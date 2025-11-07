package Controlador;

import Modelo.VendedorDTO;
import Vista.TablaVendedor;
import Negocio.VendedorNegocio;
import javax.swing.JOptionPane;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class VendedorControlador {

    VendedorNegocio vendedorNegocio = new VendedorNegocio();

    public void almacenarVendedor(String identificacion, String nombres, char genero) {
        // Validación de nombres
        if (nombres == null || !nombres.matches("[a-zA-Z ]+")) {
            datosErroneos("El nombre sólo puede contener letras y espacios");
            return;
        }
        // Validación de identificación
        if (identificacion == null || !identificacion.matches("[0-9]+")) {
            datosErroneos("La identificación debe ser un número sin puntos ni comas");
            return;
        }
        // Validación de género
        if (!(genero == 'M' || genero == 'F' || genero == 'm' || genero == 'f')) {
            datosErroneos("Por favor ingrese el género 'F' para femenino o 'M' para masculino");
            return;
        }
        // Validación de campos vacíos
        if (identificacion.isEmpty() || nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe completar todos los campos para almacenar un vendedor.");
            return;
        }
        
        VendedorDTO vendedor = new VendedorDTO(identificacion, nombres, genero);

        // Siempre compara por identificacion, no por nombre
        if (vendedorNegocio.consultarVendedor(identificacion) != null) {
            int respuesta = JOptionPane.showConfirmDialog(
                null,
                "El vendedor ya se encuentra registrado, ¿desea actualizar la información con los datos ingresados?",
                "Vendedor existente",
                JOptionPane.YES_NO_OPTION
            );
            if (respuesta == JOptionPane.YES_OPTION) {
                actualizarVendedor(identificacion, nombres, genero);
            } else {
                datosErroneos("No se almacenó el vendedor porque la identificación ya existe.");
            }
            return;
        }

        if (vendedorNegocio.almacenarVendedor(vendedor)) {
            operacionExitosa("Se ha almacenado el vendedor correctamente");
        } else {
            datosErroneos("No se pudo almacenar el vendedor. Inténtelo de nuevo.");
        }
    }

    public void consultarVendedor(String identificacion) {
        if (identificacion == null || !identificacion.matches("[0-9]+")) {
            datosErroneos("La identificación debe ser un número sin puntos ni comas");
            return;
        }
        if (identificacion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una identificación.");
            return;
        }
        VendedorDTO vendedor = vendedorNegocio.consultarVendedor(identificacion);
        if (vendedor == null) {
            operacionExitosa("El número de identificación ingresado no coincide con ninguno registrado.");
        } else {
            operacionExitosa("Datos vendedor:\n\n" + vendedor.toString());
        }
    }

    public void eliminarVendedor(String identificacion) {
        if (identificacion == null || !identificacion.matches("[0-9]+")) {
            datosErroneos("La identificación debe ser un número sin puntos ni comas");
            return;
        }

        if (vendedorNegocio.eliminarVendedor(identificacion)) {
            operacionExitosa("Se ha eliminado el vendedor");
        } else {
            JOptionPane.showMessageDialog(null, "Ningún vendedor coincide con la identificación ingresada",
                    "Vendedor no encontrado", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarVendedor(String identificacion, String nombres, char genero) {
        // Validación igual a almacenar
        if (nombres == null || !nombres.matches("[a-zA-Z ]+")) {
            datosErroneos("El nombre sólo puede contener letras y espacios");
            return;
        }
        if (identificacion == null || !identificacion.matches("[0-9]+")) {
            datosErroneos("La identificación debe ser un número sin puntos ni comas");
            return;
        }
        if (!(genero == 'M' || genero == 'F' || genero == 'm' || genero == 'f')) {
            datosErroneos("Por favor ingrese el género 'F' para femenino o 'M' para masculino");
            return;
        }
        if (identificacion.isEmpty() || nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe completar todos los campos para actualizar un vendedor.");
            return;
        }

        VendedorDTO vendedor = new VendedorDTO(identificacion, nombres, genero);

        if (vendedorNegocio.actualizarVendedor(vendedor)) {
            operacionExitosa("Se ha actualizado el vendedor");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido actualizar el vendedor\n"
                    + "Por favor verifique si el vendedor se encuentra registrado");
        }
    }

    public void listarVendedores() {
        // Corregido: enviar lista de vendedores a la tabla
        List<VendedorDTO> lista = vendedorNegocio.listarVendedor();
        TablaVendedor ventana = new TablaVendedor();
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