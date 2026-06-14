package org.example;

import SalonCitas.views.CitaForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Salon de Citas - Agendar Cita");

            CitaForm citaForm = new CitaForm();
            frame.setContentPane(citaForm.getPanel());

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // centra la ventana
            frame.setVisible(true);
        });
    }
}