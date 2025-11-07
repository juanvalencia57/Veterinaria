package Controlador;

import Modelo.ClienteDTO;
import Negocio.ClienteNegocio;
import Vista.TablaCliente;
import javax.swing.JOptionPane;
import java.util.List;


public class ClienteControlador {

    ClienteNegocio clienteNegocio = new ClienteNegocio();

    public void almacenarCliente(String identificacion, String nombres, String direccionContacto, String numeroContacto) {
        if (!identificacion.matches("[0-9]*")) {
            datosErroneos("El ID debe ser un número sin puntos ni comas");
            return;
        }
        if (identificacion.isEmpty()) {
            datosErroneos("Debe ingresar una identificación");
            return;
        }
        if (!nombres.matches("[a-zA-Z ]*")) {
            datosErroneos("El nombre sólo puede contener letras y espacios");
            return;
        }
        if (nombres.isEmpty()) {
            datosErroneos("Debe ingresar el nombre del cliente");
            return;
        }
        if (direccionContacto.trim().isEmpty()) {
            datosErroneos("Debe ingresar la dirección de contacto");
            return;
        }
        if (!numeroContacto.matches("[0-9]*")) {
            datosErroneos("El número de contacto sólo puede contener números");
            return;
        }
        if (numeroContacto.isEmpty()) {
            datosErroneos("Debe ingresar el número de contacto");
            return;
        }

        // Intenta conservar mascotas si se está actualizando, si no, nueva
        ClienteDTO clienteActual = clienteNegocio.consultarCliente(identificacion);
        List<String> mascotasCliente = (clienteActual != null && clienteActual.getMascotas() != null)
                ? clienteActual.getMascotas()
                : new java.util.ArrayList<>();
        ClienteDTO cliente = new ClienteDTO(identificacion, nombres, direccionContacto, numeroContacto, mascotasCliente);

        if (clienteActual != null) {
            // Cliente ya existe, pregunta por actualizar
            if (JOptionPane.showConfirmDialog(
                    null,
                    "El cliente ya se encuentra registrado, ¿desea actualizar la información con los datos ingresados?",
                    "Cliente existente",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (clienteNegocio.actualizarCliente(cliente)) {
                    operacionExitosa("Se ha actualizado el cliente correctamente");
                } else {
                    datosErroneos("No se pudo actualizar el cliente.");
                }
            } else {
                datosErroneos("No se almacenó el cliente porque la identificación ya existe.");
            }
        } else {
            // Cliente nuevo
            if (clienteNegocio.almacenarCliente(cliente)) {
                operacionExitosa("Se ha almacenado el cliente correctamente");
            } else {
                datosErroneos("No se pudo almacenar el cliente.");
            }
        }
    }

    public void consultarCliente(String identificacion) {
        if (!identificacion.matches("[0-9]*")) {
            datosErroneos("El ID debe ser un número sin puntos ni comas");
            return;
        }
        if (identificacion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe de ingresar un ID.");
            return;
        }
        ClienteDTO cliente = clienteNegocio.consultarCliente(identificacion);
        if (cliente == null) {
            operacionExitosa("El número de ID ingresado no coincide con ninguno de los ids registrados");
        } else {
            // Muestra las mascotas asociadas en el mensaje
            String mascotasInfo = (cliente.getMascotas() != null && !cliente.getMascotas().isEmpty())
                    ? "\nMascotas asociadas: " + String.join(", ", cliente.getMascotas())
                    : "\n(No tiene mascotas registradas)";
            operacionExitosa("Datos del cliente:\n\n" + cliente.toString() + mascotasInfo);
        }
    }

    public void eliminarCliente(String identificacion) {
        if (!identificacion.matches("[0-9]*")) {
            datosErroneos("El id debe ser un número sin puntos ni comas");
            return;
        }
        ClienteDTO cliente = clienteNegocio.consultarCliente(identificacion);
        if (cliente != null && cliente.getMascotas() != null && !cliente.getMascotas().isEmpty()) {
            datosErroneos("No puede eliminar un cliente que tiene mascotas registradas.\nDebe transferir o eliminar las mascotas primero.");
            return;
        }
        if (clienteNegocio.eliminarCliente(identificacion)) {
            operacionExitosa("Se ha eliminado el cliente");
        } else {
            JOptionPane.showMessageDialog(null, "Ningún cliente coincide con el número de id ingresado",
                    "Cliente no encontrado", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarCliente(String identificacion, String nombres, String direccionContacto, String numeroContacto) {
        if (!identificacion.matches("[0-9]*")) {
            datosErroneos("El ID debe ser un número sin puntos ni comas");
            return;
        }
        if (identificacion.isEmpty()) {
            datosErroneos("Debe ingresar una identificación");
            return;
        }
        if (!nombres.matches("[a-zA-Z ]*")) {
            datosErroneos("El nombre sólo puede contener letras y espacios");
            return;
        }
        if (nombres.isEmpty()) {
            datosErroneos("Debe ingresar el nombre del cliente");
            return;
        }
        if (direccionContacto.trim().isEmpty()) {
            datosErroneos("Debe ingresar la dirección de contacto");
            return;
        }
        if (!numeroContacto.matches("[0-9]*")) {
            datosErroneos("El número de contacto sólo puede contener números");
            return;
        }
        if (numeroContacto.isEmpty()) {
            datosErroneos("Debe ingresar el número de contacto");
            return;
        }

        ClienteDTO clienteActual = clienteNegocio.consultarCliente(identificacion);
        List<String> mascotasCliente = (clienteActual != null && clienteActual.getMascotas() != null)
                ? clienteActual.getMascotas()
                : new java.util.ArrayList<>();
        ClienteDTO cliente = new ClienteDTO(identificacion, nombres, direccionContacto, numeroContacto, mascotasCliente);

        if (clienteNegocio.actualizarCliente(cliente)) {
            operacionExitosa("Se ha actualizado el cliente");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido actualizar el cliente\n"
                    + "Por favor verifique si el cliente se encuentra registrado");
        }
    }

    public void listarClientes() {
        TablaCliente ventana = new TablaCliente();
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