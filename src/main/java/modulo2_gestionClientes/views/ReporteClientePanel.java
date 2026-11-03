package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.ReporteClienteController;

import javax.swing.*;
import java.awt.*;

public class ReporteClientePanel extends JPanel {

    private ReporteClienteController controller;
    private JTextArea txtReporte;

    public ReporteClientePanel(ReporteClienteController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Reportes de Clientes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtReporte = new JTextArea();
        txtReporte.setEditable(false);
        txtReporte.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JButton btnGenerar = new JButton("Generar Reporte de Clientes");
        btnGenerar.addActionListener(e -> generarReporte());

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(txtReporte), BorderLayout.CENTER);
        add(btnGenerar, BorderLayout.SOUTH);
    }

    private void generarReporte() {
        try {
            txtReporte.setText(controller.generarReporteTexto());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + e.getMessage());
        }
    }
} 
