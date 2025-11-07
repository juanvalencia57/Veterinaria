package Vista;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ClosePrompt {

    private ClosePrompt() {}

    // Llama esto DESPUÉS de initComponents() y de cualquier setDefaultCloseOperation().
    public static void attachTo(JFrame frame) {
        // Evita múltiples instalaciones
        Object installed = frame.getRootPane().getClientProperty("ClosePrompt.installed");
        if (Boolean.TRUE.equals(installed)) {
            return;
        }
        frame.getRootPane().putClientProperty("ClosePrompt.installed", Boolean.TRUE);

        // Asegura que no se cierre automáticamente
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Vuelve a forzar DO_NOTHING por si alguien lo cambió
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

                Object[] options = {"Volver al menú", "Cerrar programa", "Cancelar"};
                int choice = JOptionPane.showOptionDialog(
                        frame,
                        "¿Qué deseas hacer?",
                        "Cerrar ventana",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice == 0) {
                    // Volver al menú
                    if (!(frame instanceof MenuGeneral)) {
                        new MenuGeneral().setVisible(true);
                        frame.dispose();
                    } else {
                        // Ya estás en el menú: no abras otro
                        frame.toFront();
                    }
                } else if (choice == 1) {
                    // Cerrar programa
                    System.exit(0);
                } else {
                    // Cancelar o cerrar el diálogo: no hacer nada, mantener ventana abierta
                    // No cambiamos el defaultCloseOperation, así que la ventana sigue abierta
                }
            }
        });
    }
}