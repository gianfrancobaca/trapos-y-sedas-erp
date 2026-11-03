package modulo3_ventas.views;

import core.Model;
import core.View;
import modulo3_ventas.controllers.PagoController;
import modulo3_ventas.models.Pago;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PagoPanel extends JPanel implements View {

    private final PagoController controller;
    private DefaultTableModel tableModel;
    private JTable tabla;

    public PagoPanel(PagoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        // Titulo
        JLabel titulo = new JLabel("Gestion de Pagos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Venta ID", "Monto", "Metodo", "Estado", "Fecha"};
        tableModel = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(tableModel);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel derecho: acciones
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setPreferredSize(new Dimension(200, 0));

        JPanel botones = new JPanel(new GridLayout(4, 1, 5, 5));
        botones.setBorder(BorderFactory.createTitledBorder("Acciones"));
        JButton btnAprobar    = new JButton("Aprobar");
        JButton btnRechazar   = new JButton("Rechazar");
        JButton btnReembolsar = new JButton("Reembolsar");
        JButton btnRefrescar  = new JButton("Refrescar");
        botones.add(btnAprobar);
        botones.add(btnRechazar);
        botones.add(btnReembolsar);
        botones.add(btnRefrescar);

        btnAprobar.addActionListener(e    -> aprobarPago());
        btnRechazar.addActionListener(e   -> rechazarPago());
        btnReembolsar.addActionListener(e -> reembolsarPago());
        btnRefrescar.addActionListener(e  -> cargarTabla());

        panelDerecho.add(botones, BorderLayout.NORTH);
        add(panelDerecho, BorderLayout.EAST);
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Pago> pendientes = controller.obtenerPagosPendientes();
        for (Pago p : pendientes) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getVentaId(),
                    String.format("S/ %.2f", p.getMonto()),
                    p.getMetodo(), p.getEstado(),
                    p.getFechaPago() != null ? p.getFechaPago().toLocalDate() : "-"
            });
        }
    }

    private void aprobarPago() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un pago."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        controller.aprobarPago(id);
        cargarTabla();
    }

    private void rechazarPago() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un pago."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        controller.rechazarPago(id);
        cargarTabla();
    }

    private void reembolsarPago() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un pago."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Reembolsar pago #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) { controller.reembolsarPago(id); cargarTabla(); }
    }

    @Override
    public void update(Model model, Object data) { cargarTabla(); }
}
