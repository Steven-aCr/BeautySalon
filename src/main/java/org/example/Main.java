package org.example;
import java.awt.Font;
import java.awt.Color;
import com.formdev.flatlaf.FlatDarkLaf;
import SalonCitas.views.CitaForm;
import SalonCitas.views.RegistroForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Panel.background", new Color(30, 31, 40));
            UIManager.put("ComboBox.background", new Color(42, 44, 56));
            UIManager.put("ComboBox.foreground", new Color(220, 220, 230));
            UIManager.put("TextArea.background", new Color(42, 44, 56));
            UIManager.put("TextArea.foreground", new Color(220, 220, 230));
            UIManager.put("Spinner.background", new Color(42, 44, 56));
            UIManager.put("Spinner.foreground", new Color(220, 220, 230));
            UIManager.put("Button.background", new Color(56, 161, 105));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.hoverBackground", new Color(72, 187, 120));
            UIManager.put("Button.pressedBackground", new Color(47, 133, 90));
            UIManager.put("Label.foreground", new Color(200, 200, 215));
            FlatDarkLaf.setup();

            // Primero abre el Registro
            JFrame frame = new JFrame("Registro de Cliente");
            RegistroForm registroForm = new RegistroForm();
            frame.setContentPane(registroForm.getPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}