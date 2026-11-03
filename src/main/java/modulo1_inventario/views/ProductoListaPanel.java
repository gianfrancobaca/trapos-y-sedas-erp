package modulo1_inventario.views;

import modulo1_inventario.controllers.ProductoController;
import modulo1_inventario.models.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoListaPanel extends JTable {

    private final ProductoController controller;
    private final DefaultTableModel  modelo;
    private int productoSeleccionadoId = -1;

    public ProductoListaPanel(ProductoController controller) {
        this.controller = controller;
        String[] cols = {"ID", "Código", "Nombre", "Categoría",
                         "P.Compra", "P.Venta", "Stock", "Mín.", "Estado"};
        this.modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        setModel(modelo);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        registrarSeleccion();
        recargar();
    }

    private void registrarSeleccion() {
        getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && getSelectedRow() >= 0)
                productoSeleccionadoId = (int) modelo.getValueAt(getSelectedRow(), 0);
        });
    }

    public void recargar() {
        recargarCon(controller.obtenerActivos());
    }

    public void recargarCon(List<Producto> lista) {
        modelo.setRowCount(0);
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getNombre(), p.getCategoriaNombre(),
                p.getPrecioCompra(), p.getPrecioVenta(),
                p.getStockActual(), p.getStockMinimo(), p.getEstado()
            });
        }
    }

    public int getProductoSeleccionadoId() { return productoSeleccionadoId; }

    public Producto getProductoSeleccionado() {
        if (productoSeleccionadoId < 0) return null;
        return controller.buscarPorId(productoSeleccionadoId);
    }
}