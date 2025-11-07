package Vista;

import javax.swing.*;

public final class SimpleMenuBar {

    private static final String MENU_TEXTO = "Sistema";
    private static final String ITEM_VOLVER = "Volver al menú general";

    private SimpleMenuBar() {}

    // Llama esto DESPUÉS de initComponents() en cada JFrame
    public static void attachTo(JFrame frame) {
        JMenuBar bar = frame.getJMenuBar();
        boolean nuevaBarra = false;
        if (bar == null) {
            bar = new JMenuBar();
            frame.setJMenuBar(bar);
            nuevaBarra = true;
        }

        // Busca o crea el menú "Sistema"
        JMenu menuSistema = null;
        for (int i = 0; i < bar.getMenuCount(); i++) {
            JMenu m = bar.getMenu(i);
            if (m != null && MENU_TEXTO.equals(m.getText())) {
                menuSistema = m;
                break;
            }
        }
        if (menuSistema == null) {
            menuSistema = new JMenu(MENU_TEXTO);
            bar.add(menuSistema);
        }

        // Evita duplicar el item si ya existe
        if (!tieneItem(menuSistema, ITEM_VOLVER)) {
            JMenuItem volver = new JMenuItem(ITEM_VOLVER);
            volver.addActionListener(e -> {
                new MenuGeneral().setVisible(true);
                frame.dispose();
            });
            menuSistema.add(volver);
        }

        if (nuevaBarra) {
            frame.revalidate();
            frame.repaint();
        }
    }

    private static boolean tieneItem(JMenu menu, String texto) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem it = menu.getItem(i);
            if (it != null && texto.equals(it.getText())) {
                return true;
            }
        }
        return false;
    }
}