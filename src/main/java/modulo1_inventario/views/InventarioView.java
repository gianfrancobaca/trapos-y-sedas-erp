package modulo1_inventario.views;

import core.Model;
import core.View;
import modulo1_inventario.controllers.InventarioController;

import javax.swing.*;
import java.awt.*;

public class InventarioView extends JPanel implements View {

    private final InventarioController controller;
    private final JTabbedPane tabs;

    private final ProductoView     productoView;
    private final ProveedorView    proveedorView;
    private final OrdenCompraView  ordenCompraView;
    private final MovimientoView   movimientoView;
    private final AlertaStockPanel alertaPanel;

    public InventarioView(InventarioController controller) {
        this.controller      = controller;
        this.productoView    = new ProductoView(controller.getProductoCtrl());
        this.proveedorView   = new ProveedorView(controller.getProveedorCtrl());
        this.ordenCompraView = new OrdenCompraView(controller.getOrdenCompraCtrl(),
                                                   controller.getProveedorCtrl());
        this.movimientoView  = new MovimientoView(controller);
        this.alertaPanel     = new AlertaStockPanel(controller);
        this.tabs            = new JTabbedPane();

        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        tabs.addTab("Productos",      productoView);
        tabs.addTab("Proveedores",    proveedorView);
        tabs.addTab("Órdenes Compra", ordenCompraView);
        tabs.addTab("Movimientos",    movimientoView);
        add(tabs, BorderLayout.CENTER);
        add(alertaPanel, BorderLayout.SOUTH);
    }

    @Override
    public void update(Model model, Object data) {
        productoView.update(model, data);
        alertaPanel.update(model, data);
    }
}