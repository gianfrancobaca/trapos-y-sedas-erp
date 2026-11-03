package modulo1_inventario.views;

import modulo1_inventario.controllers.ProveedorController;
import modulo1_inventario.models.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProveedorListaPanel extends JTable {

    private final ProveedorController controller;
    private final DefaultTableModel   modelo;
    private int proveedorSeleccionadoId = -1;

    public ProveedorListaPanel(ProveedorController controller) {
        this.controller = controller;
        String[] cols = {"ID", "Tipo Doc.", "N° Documento", "Nombre / Razón Social", "Contacto", "Teléfono", "Correo"};
        this.modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        setModel(modelo);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && getSelectedRow() >= 0)
                proveedorSeleccionadoId = (int) modelo.getValueAt(getSelectedRow(), 0);
        });
        recargar();
    }

    public void recargar() {
        modelo.setRowCount(0);
        for (Proveedor p : controller.obtenerActivos())
            modelo.addRow(new Object[]{p.getId(), p.getTipoDocumento(), p.getNumeroDocumento(),
                    p.getRazonSocial(), p.getContacto(), p.getTelefono(), p.getCorreo()});
    }

    public int       getProveedorSeleccionadoId()    { return proveedorSeleccionadoId; }
    public Proveedor getProveedorSeleccionado()      {
        if (proveedorSeleccionadoId < 0) return null;
        return controller.buscarPorId(proveedorSeleccionadoId);
    }
}