package modulo1_inventario.views;

import core.Model;
import core.View;
import modulo1_inventario.controllers.OrdenCompraController;
import modulo1_inventario.controllers.ProveedorController;

import javax.swing.*;
import java.awt.*;

public class OrdenCompraView extends JPanel implements View {

    private final OrdenCompraListaPanel  listaPanel;
    private final OrdenCompraFormPanel   formPanel;
    private final OrdenCompraDetallePanel detallePanel;

    public OrdenCompraView(OrdenCompraController ordenCtrl, ProveedorController provCtrl) {
        this.listaPanel   = new OrdenCompraListaPanel(ordenCtrl);
        this.formPanel    = new OrdenCompraFormPanel(ordenCtrl, provCtrl, listaPanel);
        this.detallePanel = new OrdenCompraDetallePanel(ordenCtrl);

        setLayout(new BorderLayout(5, 5));
        construirUI();
    }

    private void construirUI() {
        JTabbedPane tabs = new JTabbedPane();
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaPanel), formPanel);
        split.setDividerLocation(480);
        tabs.addTab("Órdenes", split);
        tabs.addTab("Detalle de Orden", detallePanel);
        add(tabs, BorderLayout.CENTER);
    }

    @Override
    public void update(Model model, Object data) {
        listaPanel.recargar();
    }
}