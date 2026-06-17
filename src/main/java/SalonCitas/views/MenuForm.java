package SalonCitas.views;

import SalonCitas.model.Sesion;

import javax.swing.*;
import java.awt.*;

public class MenuForm {

    private JPanel panel1;
    private JLabel lblBienvenida;
    private JButton btnCrearCita;
    private JButton btnVerCitas;
    private JButton cerrarSesiónButton;
    private JButton btnCerrarSesion;

    public MenuForm() {
        panel1.setPreferredSize(new Dimension(420,320));
        panel1.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        if (Sesion.getClienteActual() != null) {
            lblBienvenida.setText("Bienvenida," + Sesion.getClienteActual().getNombreCompleto());
        }

        btnCrearCita.addActionListener(e -> abrirCrearCita());
        btnVerCitas.addActionListener(e -> JOptionPane.showMessageDialog(panel1, "Pantalla de Ver Citas pendiente."));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void abrirCrearCita() {
        SwingUtilities.getWindowAncestor(panel1).dispose();
        JFrame frame = new JFrame("Salon de citas - Agendar Cita");
        CitaForm citaForm = new CitaForm();
        frame.setContentPane(citaForm.getPanel1());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void cerrarSesion() {
        Sesion.setClienteActual(null);
        SwingUtilities.getWindowAncestor(panel1).dispose();
        JFrame frame = new JFrame("Login");
        LoginForm loginForm = new LoginForm();
        frame.setContentPane(loginForm.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel getPanel() { return panel1;}
}
