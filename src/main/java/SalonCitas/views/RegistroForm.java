package SalonCitas.views;

import SalonCitas.dao.ClienteDAO;
import SalonCitas.model.Cliente;
import SalonCitas.model.Persona;

import java.awt.Dimension;

import javax.swing.*;

public class RegistroForm {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtDui;
    private JTextField txtDireccion;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmar;
    private JButton btnRegistrar;
    private JPanel panel1;
    private JButton iniciarSesiónButton;


    public RegistroForm() {
        btnRegistrar.addActionListener(e -> registrar());

        // Tamaño y márgenes igual que CitaForm
        panel1.setPreferredSize(new Dimension(500, 480));
        panel1.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        iniciarSesiónButton.addActionListener(e -> abrirLogin());
    }


    private void registrar() {
        String nombre    = txtNombre.getText().trim();
        String apellido  = new String(txtApellido.getText()).trim();
        String telefono  = txtTelefono.getText().trim();
        String dui       = txtDui.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String email     = txtEmail.getText().trim();
        String pass      = new String(txtPassword.getPassword());
        String confirmar = new String(txtConfirmar.getPassword());



        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Completa todos los campos obligatorios.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!pass.equals(confirmar)) {
            JOptionPane.showMessageDialog(panel1, "Las contraseñas no coinciden.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTelefono(telefono);
        persona.setDireccion(direccion);
        persona.setDui(dui);

        Cliente cliente = new Cliente();
        cliente.setIdEstado(1);
        cliente.setEmail(email);
        cliente.setPassword(pass);

        try {
            ClienteDAO dao = new ClienteDAO();
            boolean ok = dao.register(persona, cliente);
            if (ok) {
                JOptionPane.showMessageDialog(panel1, "¡Registro exitoso! Ahora inicia sesión.");
                limpiar();
                SwingUtilities.getWindowAncestor(panel1).dispose();
                abrirLogin(); // ← antes decía abrirCitas()
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel1, "Error al registrar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);


        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtDui.setText("");
        txtDireccion.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfirmar.setText("");
    }

    private void abrirLogin() {
        JFrame frame = new JFrame("Login");
        LoginForm loginForm = new LoginForm();
        frame.setContentPane(loginForm.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel getPanel() { return panel1; }
}