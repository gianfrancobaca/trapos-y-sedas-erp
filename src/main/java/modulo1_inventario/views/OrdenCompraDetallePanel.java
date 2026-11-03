package modulo1_inventario.views;

import modulo1_inventario.controllers.OrdenCompraController;
import modulo1_inventario.models.DetalleOrdenCompra;
import modulo1_inventario.models.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdenCompraDetallePanel extends JPanel {

    private final OrdenCompraController controller;
    private final JTextField            campoOrdenId;
    private final DefaultTableModel     modeloTabla;
    private final JTable                tabla;
    private final JLabel                labelTotal;
    private final JLabel                labelInfoOrden;

    public OrdenCompraDetallePanel(OrdenCompraController controller) {
        this.controller   = controller;
        this.campoOrdenId = new JTextField(8);
        String[] cols = {"ID Línea", "Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        this.modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        this.tabla         = new JTable(modeloTabla);
        this.labelTotal    = new JLabel("Total: S/ 0.00");
        this.labelInfoOrden = new JLabel(" ");

        labelTotal.setFont(labelTotal.getFont().deriveFont(Font.BOLD, 13f));
        labelInfoOrden.setFont(labelInfoOrden.getFont().deriveFont(Font.ITALIC, 12f));
        labelInfoOrden.setForeground(new Color(60, 100, 160));

        setLayout(new BorderLayout(5, 5));
        construirUI();
    }

    private void construirUI() {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar detalle de orden"));
        panelBusqueda.add(new JLabel("ID Orden:"));
        panelBusqueda.add(campoOrdenId);

        JButton btnBuscar = new JButton("Cargar Detalle");
        JButton btnLimpiar = new JButton("Limpiar");
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnLimpiar);
        panelBusqueda.add(labelInfoOrden);

        btnBuscar.addActionListener(e  -> cargarDetalle());
        btnLimpiar.addActionListener(e -> limpiar());

        campoOrdenId.addActionListener(e -> cargarDetalle());

        tabla.setRowHeight(22);
        tabla.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelPie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        panelPie.add(labelTotal);

        add(panelBusqueda, BorderLayout.NORTH);
        add(scroll,        BorderLayout.CENTER);
        add(panelPie,      BorderLayout.SOUTH);
    }

    private void cargarDetalle() {
        String texto = campoOrdenId.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID de la orden.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int ordenId = Integer.parseInt(texto);

            OrdenCompra orden = controller.buscarPorId(ordenId);
            if (orden == null) {
                labelInfoOrden.setText("⚠  Orden #" + ordenId + " no encontrada.");
                labelInfoOrden.setForeground(Color.RED);
                modeloTabla.setRowCount(0);
                labelTotal.setText("Total: S/ 0.00");
                return;
            }

            List<DetalleOrdenCompra> detalles = controller.obtenerDetalles(ordenId);

            modeloTabla.setRowCount(0);
            double total = 0;

            if (detalles.isEmpty()) {
                labelInfoOrden.setText("Orden #" + ordenId + " — " + orden.getProveedorNombre() + " | Sin líneas de detalle.");
            } else {
                for (DetalleOrdenCompra d : detalles) {
                    modeloTabla.addRow(new Object[]{
                            d.getId(),
                            d.getProductoNombre(),
                            d.getCantidad(),
                            String.format("S/ %.2f", d.getPrecioUnitario()),
                            String.format("S/ %.2f", d.getSubtotal())
                    });
                    total += d.getSubtotal();
                }
                labelInfoOrden.setText("Orden #" + ordenId + " — " + orden.getProveedorNombre()
                        + " | Estado: " + orden.getEstado()
                        + " | Líneas: " + detalles.size());
            }

            labelInfoOrden.setForeground(new Color(60, 100, 160));
            labelTotal.setText(String.format("Total: S/ %.2f", total));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID de orden debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        campoOrdenId.setText("");
        modeloTabla.setRowCount(0);
        labelTotal.setText("Total: S/ 0.00");
        labelInfoOrden.setText(" ");
    }
}