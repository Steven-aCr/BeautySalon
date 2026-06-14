package SalonCitas.views;

import SalonCitas.dao.CitaDAO;
import SalonCitas.dao.ComboDAO;
import SalonCitas.dao.EstadoDAO;
import SalonCitas.model.Cita;
import SalonCitas.model.Estado;
import SalonCitas.model.Persona;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class CitaForm {

    private JPanel panel1; // panel raiz generado por el GUI Designer - verificar nombre real
    private JComboBox<Persona> comboBox1; // Cliente
    private JComboBox<Persona> comboBox2; // Empleado
    private JSpinner spinner1;            // Fecha
    private JSpinner spinner2;            // Hora
    private JComboBox<Estado> comboBox3;  // Estado
    private JTextArea textArea1;          // Observaciones
    private JButton LIMPIARButton;
    private JButton GUARDARButton;

    public CitaForm() {
        configurarSpinners();
        cargarCombos();

        LIMPIARButton.addActionListener(e -> limpiarFormulario());
        GUARDARButton.addActionListener(e -> guardarCita());
    }

    /** Configura los spinners de Fecha y Hora con sus respectivos formatos. */
    private void configurarSpinners() {
        spinner1.setModel(new SpinnerDateModel());
        spinner1.setEditor(new JSpinner.DateEditor(spinner1, "dd/MM/yyyy"));

        spinner2.setModel(new SpinnerDateModel());
        spinner2.setEditor(new JSpinner.DateEditor(spinner2, "HH:mm"));
    }

    /** Llena los combos de Cliente, Empleado y Estado con datos de la BD. */
    private void cargarCombos() {
        try {
            ComboDAO comboDAO = new ComboDAO();
            EstadoDAO estadoDAO = new EstadoDAO();

            comboBox1.removeAllItems();
            for (Persona p : comboDAO.listarClientes()) comboBox1.addItem(p);

            comboBox2.removeAllItems();
            for (Persona p : comboDAO.listarEmpleados()) comboBox2.addItem(p);

            comboBox3.removeAllItems();
            for (Estado e : estadoDAO.listarPorTipo("Cita")) comboBox3.addItem(e);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1,
                    "Error al cargar datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Crea una nueva Cita con los datos del formulario y la guarda en la BD. */
    private void guardarCita() {
        Persona cliente  = (Persona) comboBox1.getSelectedItem();
        Persona empleado = (Persona) comboBox2.getSelectedItem();
        Estado estado    = (Estado) comboBox3.getSelectedItem();

        if (cliente == null || empleado == null || estado == null) {
            JOptionPane.showMessageDialog(panel1,
                    "Debe seleccionar cliente, empleado y estado.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.util.Date fechaSeleccionada = (java.util.Date) spinner1.getValue();
        java.util.Date horaSeleccionada  = (java.util.Date) spinner2.getValue();

        Cita cita = new Cita();
        cita.setIdCliente(cliente.getIdPersona());   // IdPersona reutilizado como IdCliente
        cita.setIdEmpleado(empleado.getIdPersona()); // IdPersona reutilizado como IdEmpleado
        cita.setFecha(new Date(fechaSeleccionada.getTime()));
        cita.setHora(new Time(horaSeleccionada.getTime()));
        cita.setIdEstado(estado.getIdEstado());
        cita.setObservaciones(textArea1.getText());

        try {
            CitaDAO citaDAO = new CitaDAO();
            Cita creada = citaDAO.Create(cita);

            if (creada != null) {
                JOptionPane.showMessageDialog(panel1,
                        "Cita guardada correctamente (ID: " + creada.getIdCita() + ")");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(panel1,
                        "No se pudo guardar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1,
                    "Error al guardar la cita: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Restaura el formulario a sus valores iniciales. */
    private void limpiarFormulario() {
        if (comboBox1.getItemCount() > 0) comboBox1.setSelectedIndex(0);
        if (comboBox2.getItemCount() > 0) comboBox2.setSelectedIndex(0);
        if (comboBox3.getItemCount() > 0) comboBox3.setSelectedIndex(0);
        spinner1.setValue(new java.util.Date());
        spinner2.setValue(new java.util.Date());
        textArea1.setText("");
    }

    /** Retorna el panel raiz para insertarlo en la ventana principal. */
    public JPanel getPanel() {
        return panel1;
    }
}