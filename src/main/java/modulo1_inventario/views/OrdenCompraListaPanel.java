package modulo1_inventario.views;

import modulo1_inventario.controllers.OrdenCompraController;
import modulo1_inventario.models.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OrdenCompraListaPanel extends JTable {

    private final OrdenCompraController controller;
    private final DefaultTableModel     modelo;
    private int ordenSeleccionadaId = -1;

    public OrdenCompraListaPanel(OrdenCompraController controller) {
        this.controller = controller;
        String[] cols = {"ID", "Proveedor", "Emisión", "Entrega Est.", "Estado", "Total"};
        this.modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        setModel(modelo);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && getSelectedRow() >= 0)
                ordenSeleccionadaId = (int) modelo.getValueAt(getSelectedRow(), 0);
        });
        recargar();
    }

    public void recargar() {
        modelo.setRowCount(0);
        for (OrdenCompra o : controller.obtenerTodas())
            modelo.addRow(new Object[]{o.getId(), o.getProveedorNombre(),
                o.getFechaEmision(), o.getFechaEntregaEstimada(), o.getEstado(), o.getTotal()});
    }

    public int getOrdenSeleccionadaId() { return ordenSeleccionadaId; }
}