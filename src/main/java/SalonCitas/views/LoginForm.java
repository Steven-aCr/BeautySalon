package SalonCitas.views;

import SalonCitas.dao.ClienteDAO;
import SalonCitas.model.Cliente;
import SalonCitas.model.Sesion;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm {

    private JPanel panel1;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JButton btnIrARegistro;

    public LoginForm() {
        panel1.setPreferredSize(new Dimension(420, 320));
        panel1.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        btnIniciarSesion.addActionListener(e -> iniciarSesion());
        btnIrARegistro.addActionListener(e -> irARegistro());
    }

    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Ingresa tu correo y contraseña.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ClienteDAO dao = new ClienteDAO();
            Cliente cliente = dao.login(email, password);

            if (cliente != null) {
                Sesion.setClienteActual(cliente);
                SwingUtilities.getWindowAncestor(panel1).dispose();
                abrirMenu();
            } else {
                JOptionPane.showMessageDialog(panel1, "Correo o contraseña incorrectos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Error al iniciar sesión: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void irARegistro() {
        SwingUtilities.getWindowAncestor(panel1).dispose();
        JFrame frame = new JFrame("Registro de Cliente");
        RegistroForm registroForm = new RegistroForm();
        frame.setContentPane(registroForm.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void abrirMenu() {
        JFrame frame = new JFrame("Salon de Citas - Menú");
        MenuForm menuForm = new MenuForm();
        frame.setContentPane(menuForm.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel getPanel() { return panel1; }
}