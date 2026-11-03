package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.FacturaController;
import modulo2_gestionClientes.models.Factura;
import core.Model;
import core.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacturaPanel extends JPanel implements View {

    private final FacturaController controller;
    private DefaultTableModel tableModel;

    public FacturaPanel(FacturaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("Facturas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Cliente ID", "Total", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabla = new JTable(tableModel);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarTabla());
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnRefrescar);
        add(sur, BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Factura> lista = controller.listarTodos();
        for (Factura f : lista) {
            tableModel.addRow(new Object[]{
                    f.getIdFactura(),
                    f.getCliente() != null ? f.getCliente().getIdCliente() : "-",
                    f.getTotal(),
                    f.getEstado()
            });
        }
    }

    @Override
    public void update(Model model, Object data) {
        cargarTabla();
    }
}