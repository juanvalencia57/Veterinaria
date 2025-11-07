package Controlador;

import Modelo.AccesorioDTO;
import Negocio.AccesorioNegocio;
import Vista.TablaAccesorio;
import javax.swing.JOptionPane;

public class AccesorioControlador {

    private static final String DECIMAL_REGEX = "^[0-9]+(\\.[0-9]+)?$";
    private final AccesorioNegocio accesorioNegocio = new AccesorioNegocio();

    public void almacenarAccesorio(
            String id,
            String nombre,
            String tipo,
            String precioStr,
            String cantidadStr
    ) {
        // Validaciones básicas
        if (!id.matches("[0-9]+")) {
            datosErroneos("El ID debe ser numérico");
            return;
        }
        if (id.isEmpty() || nombre.isEmpty() || tipo.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            datosErroneos("Complete todos los campos para almacenar un accesorio.");
            return;
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+")) {
            datosErroneos("El nombre solo puede contener letras y espacios");
            return;
        }
        if (!tipo.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+")) {
            datosErroneos("El tipo solo puede contener letras y espacios");
            return;
        }
        if (!precioStr.matches(DECIMAL_REGEX)) {
            datosErroneos("El precio debe ser un número válido (usa punto para decimales)");
            return;
        }
        if (!cantidadStr.matches("[0-9]+")) {
            datosErroneos("La cantidad debe ser un número entero positivo.");
            return;
        }

        double precio = Double.parseDouble(precioStr);
        int cantidad = Integer.parseInt(cantidadStr);

        if (precio < 0) {
            datosErroneos("El precio no puede ser negativo");
            return;
        }
        if (cantidad < 0) {
            datosErroneos("La cantidad no puede ser negativa");
            return;
        }

        if (accesorioNegocio.consultarAccesorio(id) != null) {
            int respuesta = JOptionPane.showConfirmDialog(
                    null,
                    "El accesorio ya existe. ¿Desea actualizarlo?",
                    "Accesorio existente",
                    JOptionPane.YES_NO_OPTION
            );
            if (respuesta == JOptionPane.YES_OPTION) {
                actualizarAccesorio(id, nombre, tipo, precioStr, cantidadStr);
            } else {
                datosErroneos("No se almacenó el accesorio porque el ID ya existe.");
            }
            return;
        }

        AccesorioDTO accesorio = new AccesorioDTO(id, nombre, tipo, precio, cantidad);
        if (accesorioNegocio.almacenarAccesorio(accesorio)) {
            operacionExitosa("Se ha almacenado el accesorio correctamente");
        } else {
            datosErroneos("No se pudo almacenar el accesorio.");
        }
    }

    public void consultarAccesorio(String id) {
        if (!id.matches("[0-9]+")) {
            datosErroneos("El ID debe ser un número");
            return;
        }
        AccesorioDTO accesorio = accesorioNegocio.consultarAccesorio(id);
        if (accesorio == null) {
            operacionExitosa("El número de ID ingresado no coincide con ningún accesorio");
        } else {
            operacionExitosa("Datos del accesorio:\n\n" + accesorio);
        }
    }

    public void eliminarAccesorio(String id) {
        if (!id.matches("[0-9]+")) {
            datosErroneos("El ID debe ser un número");
            return;
        }
        if (accesorioNegocio.eliminarAccesorio(id)) {
            operacionExitosa("Se ha eliminado el accesorio");
        } else {
            JOptionPane.showMessageDialog(null, "Ningún accesorio coincide con el ID ingresado",
                    "Accesorio no encontrado", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarAccesorio(
            String id,
            String nombre,
            String tipo,
            String precioStr,
            String cantidadStr
    ) {
        if (!id.matches("[0-9]+")) {
            datosErroneos("El ID debe ser numérico");
            return;
        }
        if (id.isEmpty() || nombre.isEmpty() || tipo.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            datosErroneos("Complete todos los campos para actualizar un accesorio.");
            return;
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+")) {
            datosErroneos("El nombre solo puede contener letras y espacios");
            return;
        }
        if (!tipo.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+")) {
            datosErroneos("El tipo solo puede contener letras y espacios");
            return;
        }
        if (!precioStr.matches(DECIMAL_REGEX)) {
            datosErroneos("El precio debe ser un número válido (usa punto para decimales)");
            return;
        }
        if (!cantidadStr.matches("[0-9]+")) {
            datosErroneos("La cantidad debe ser un número entero positivo.");
            return;
        }
        double precio = Double.parseDouble(precioStr);
        int cantidad = Integer.parseInt(cantidadStr);
        if (precio < 0) {
            datosErroneos("El precio no puede ser negativo");
            return;
        }
        if (cantidad < 0) {
            datosErroneos("La cantidad no puede ser negativa");
            return;
        }

        AccesorioDTO accesorio = new AccesorioDTO(id, nombre, tipo, precio, cantidad);
        if (accesorioNegocio.actualizarAccesorio(accesorio)) {
            operacionExitosa("Se ha actualizado el accesorio");
        } else {
            datosErroneos("No se ha podido actualizar el accesorio. ¿Está registrado?");
        }
    }

    public void listarAccesorios() {
        TablaAccesorio ventana = new TablaAccesorio();
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