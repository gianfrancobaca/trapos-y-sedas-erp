package modulo3_ventas.views;

import core.Model;
import core.View;
import modulo3_ventas.controllers.ComprobanteController;
import modulo3_ventas.models.ComprobanteVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class ComprobantePanel extends JPanel implements View {

    private final ComprobanteController controller;
    private DefaultTableModel tableModel;
    private JTable tabla;

    public ComprobantePanel(ComprobanteController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        // Titulo
        JLabel titulo = new JLabel("Comprobantes de Venta");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Venta ID", "Tipo", "Serie", "Numero", "Fecha", "Monto", "Anulado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(tableModel);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel derecho: acciones
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setPreferredSize(new Dimension(200, 0));

        JPanel botones = new JPanel(new GridLayout(3, 1, 5, 5));
        botones.setBorder(BorderFactory.createTitledBorder("Acciones"));
        JButton btnEmitir    = new JButton("Emitir");
        JButton btnAnular    = new JButton("Anular");
        JButton btnRefrescar = new JButton("Refrescar");
        botones.add(btnEmitir);
        botones.add(btnAnular);
        botones.add(btnRefrescar);

        btnEmitir.addActionListener(e    -> emitirComprobante());
        btnAnular.addActionListener(e    -> anularComprobante());
        btnRefrescar.addActionListener(e -> cargarTabla());

        panelDerecho.add(botones, BorderLayout.NORTH);
        add(panelDerecho, BorderLayout.EAST);
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<ComprobanteVenta> lista = controller.obtenerTodos();
        for (ComprobanteVenta c : lista) {
            tableModel.addRow(new Object[]{
                    c.getId(), c.getVentaId(), c.getTipo(),
                    c.getSerie(), c.getNumero(),
                    c.getFechaEmision() != null ? c.getFechaEmision().toLocalDate() : "-",
                    String.format("S/ %.2f", c.getMontoTotal()),
                    c.isAnulado() ? "Si" : "No"
            });
        }
    }

    private void emitirComprobante() {
        JTextField txtVentaId = new JTextField();
        JTextField txtSerie   = new JTextField("B001");
        JTextField txtNumero  = new JTextField();
        JTextField txtMonto   = new JTextField();
        JComboBox<ComprobanteVenta.TipoComprobante> cbTipo =
                new JComboBox<>(ComprobanteVenta.TipoComprobante.values());

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Venta ID:"));  panel.add(txtVentaId);
        panel.add(new JLabel("Tipo:"));      panel.add(cbTipo);
        panel.add(new JLabel("Serie:"));     panel.add(txtSerie);
        panel.add(new JLabel("Numero:"));    panel.add(txtNumero);
        panel.add(new JLabel("Monto:"));     panel.add(txtMonto);

        int result = JOptionPane.showConfirmDialog(this, panel, "Emitir Comprobante", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            ComprobanteVenta c = new ComprobanteVenta();
            c.setVentaId(Integer.parseInt(txtVentaId.getText().trim()));
            c.setTipo((ComprobanteVenta.TipoComprobante) cbTipo.getSelectedItem());
            c.setSerie(txtSerie.getText().trim());
            c.setNumero(Integer.parseInt(txtNumero.getText().trim()));
            c.setFechaEmision(LocalDateTime.now());
            c.setMontoTotal(Double.parseDouble(txtMonto.getText().trim()));
            c.setAnulado(false);
            controller.emitirComprobante(c);
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Venta ID, numero y monto deben ser numericos.");
        }
    }

    private void anularComprobante() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un comprobante."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Anular comprobante #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) { controller.anularComprobante(id); cargarTabla(); }
    }

    @Override
    public void update(Model model, Object data) { cargarTabla(); }
}
