package Vista;

import Implement.ClienteDAOFile;
import Implement.ConsultaDAOFile;
import Implement.MascotaDAOFile;
import Modelo.ClienteDTO;
import Modelo.ConsultaDTO;
import Modelo.MascotaDTO;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReportesFrame extends JFrame {

    private final JComboBox<ClienteResumen> comboClientes = new JComboBox<>();
    private final JComboBox<MascotaResumen> comboMascotas = new JComboBox<>();
    private final JButton btnRefrescar = new JButton("Refrescar");
    private final JLabel lblClientes = new JLabel("Por cliente:");
    private final JLabel lblMascotas = new JLabel("Por mascota:");

    private final ClienteDAOFile clienteDAO = new ClienteDAOFile();
    private final MascotaDAOFile mascotaDAO = new MascotaDAOFile();
    private final ConsultaDAOFile consultaDAO = new ConsultaDAOFile();

    public ReportesFrame() {
        setTitle("Reportes - Relaciones Cliente/Mascota y Consultas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 300);
        setLocationRelativeTo(null);
        setLayout(new java.awt.GridBagLayout());

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fila 0: Título
        JLabel titulo = new JLabel("Reportes del sistema", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(18f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        // Fila 1: Label clientes
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(lblClientes, gbc);

        // Fila 1: combo clientes
        gbc.gridx = 1;
        add(comboClientes, gbc);

        // Fila 2: Label mascotas
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblMascotas, gbc);

        // Fila 2: combo mascotas
        gbc.gridx = 1;
        add(comboMascotas, gbc);

        // Fila 3: botón refrescar
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnRefrescar, gbc);

        // Renderers con HTML para mostrar información enriquecida
        comboClientes.setRenderer(new ClienteResumenRenderer());
        comboMascotas.setRenderer(new MascotaResumenRenderer());

        // Cargar datos iniciales
        cargarClientesEnCombo();
        cargarMascotasEnCombo();

        // Refrescar
        btnRefrescar.addActionListener(e -> {
            cargarClientesEnCombo();
            cargarMascotasEnCombo();
        });
    }

    // ------------------------------------------------------------------------------------
    // CARGA DE DATOS
    // ------------------------------------------------------------------------------------
    private void cargarClientesEnCombo() {
        List<ClienteDTO> clientes = clienteDAO.listarCliente();
        DefaultComboBoxModel<ClienteResumen> model = new DefaultComboBoxModel<>();

        for (ClienteDTO c : clientes) {
            // Obtener mascotas del cliente por IDs
            List<MascotaDTO> mascotasDeCliente = new ArrayList<>();
            if (c.getMascotas() != null) {
                for (String idMascota : c.getMascotas()) {
                    MascotaDTO m = mascotaDAO.consultarMascota(idMascota);
                    if (m != null) mascotasDeCliente.add(m);
                }
            }
            model.addElement(new ClienteResumen(c, mascotasDeCliente));
        }
        comboClientes.setModel(model);
    }

    private void cargarMascotasEnCombo() {
        List<MascotaDTO> mascotas = mascotaDAO.listarMascota();
        DefaultComboBoxModel<MascotaResumen> model = new DefaultComboBoxModel<>();

        for (MascotaDTO m : mascotas) {
            // Dueño (cliente)
            ClienteDTO dueño = null;
            if (m.getIdDueño() != null && !m.getIdDueño().trim().isEmpty()) {
                dueño = clienteDAO.consultarCliente(m.getIdDueño());
            }
            // Consultas de la mascota
            List<ConsultaDTO> consultasDeMascota = new ArrayList<>();
            List<ConsultaDTO> todas = consultaDAO.listarConsultas();
            if (todas != null) {
                for (ConsultaDTO con : todas) {
                    if (m.getId().equals(con.getIdMascota())) {
                        consultasDeMascota.add(con);
                    }
                }
            }
            model.addElement(new MascotaResumen(m, dueño, consultasDeMascota));
        }
        comboMascotas.setModel(model);
    }

    // ------------------------------------------------------------------------------------
    // VIEW MODELS
    // ------------------------------------------------------------------------------------
    public static class ClienteResumen {
        public final ClienteDTO cliente;
        public final List<MascotaDTO> mascotas;

        public ClienteResumen(ClienteDTO cliente, List<MascotaDTO> mascotas) {
            this.cliente = cliente;
            this.mascotas = mascotas != null ? mascotas : new ArrayList<>();
        }
    }

    public static class MascotaResumen {
        public final MascotaDTO mascota;
        public final ClienteDTO dueño;
        public final List<ConsultaDTO> consultas;

        public MascotaResumen(MascotaDTO mascota, ClienteDTO dueño, List<ConsultaDTO> consultas) {
            this.mascota = mascota;
            this.dueño = dueño;
            this.consultas = consultas != null ? consultas : new ArrayList<>();
        }
    }

    // ------------------------------------------------------------------------------------
    // RENDERERS
    // ------------------------------------------------------------------------------------
    private static class ClienteResumenRenderer extends BasicComboBoxRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ClienteResumen) {
                ClienteResumen cr = (ClienteResumen) value;
                String id = safe(cr.cliente.getIdentificacion());
                String nombre = safe(cr.cliente.getNombres());
                StringBuilder sb = new StringBuilder("<html>");
                sb.append("<b>Cliente:</b> ").append(id).append(" - ").append(nombre);

                if (cr.mascotas.isEmpty()) {
                    sb.append("<br><i>Sin mascotas</i>");
                } else {
                    sb.append("<br><b>Mascotas:</b>");
                    int count = 0;
                    for (MascotaDTO m : cr.mascotas) {
                        if (count >= 4) { // limite para no hacer muy alto el item
                            sb.append("<br>... (").append(cr.mascotas.size() - count).append(" más)");
                            break;
                        }
                        sb.append("<br>- ").append(safe(m.getId()))
                                .append(" | ").append(safe(m.getNombre()))
                                .append(" (").append(safe(m.getTipo()))
                                .append("/").append(safe(m.getRaza())).append(")");
                        count++;
                    }
                }
                sb.append("</html>");
                lbl.setText(sb.toString());
            }
            return lbl;
        }
    }

    private static class MascotaResumenRenderer extends BasicComboBoxRenderer {
        private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof MascotaResumen) {
                MascotaResumen mr = (MascotaResumen) value;
                MascotaDTO m = mr.mascota;
                StringBuilder sb = new StringBuilder("<html>");
                sb.append("<b>Mascota:</b> ")
                        .append(safe(m.getId()))
                        .append(" - ").append(safe(m.getNombre()))
                        .append(" (").append(safe(m.getTipo()))
                        .append("/").append(safe(m.getRaza())).append(")");

                if (mr.dueño != null) {
                    sb.append("<br><b>Dueño:</b> ")
                            .append(safe(mr.dueño.getIdentificacion()))
                            .append(" - ").append(safe(mr.dueño.getNombres()));
                } else {
                    sb.append("<br><i>Sin dueño asignado</i>");
                }

                if (mr.consultas.isEmpty()) {
                    sb.append("<br><i>Sin consultas</i>");
                } else {
                    sb.append("<br><b>Consultas:</b>");
                    int count = 0;
                    for (ConsultaDTO c : mr.consultas) {
                        if (count >= 4) {
                            sb.append("<br>... (").append(mr.consultas.size() - count).append(" más)");
                            break;
                        }
                        String fecha = c.getFecha() != null ? SDF.format(c.getFecha()) : "-";
                        sb.append("<br>- ").append(fecha)
                                .append(" | ").append(ellipsis(safe(c.getSintomas()), 40));
                        count++;
                    }
                }
                sb.append("</html>");
                lbl.setText(sb.toString());
            }
            return lbl;
        }
    }
    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static String ellipsis(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }

    // Ejecutar para prueba aislada
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReportesFrame().setVisible(true));
    }
}