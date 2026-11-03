package modulo1_inventario.views;

import core.Model;
import core.View;
import modulo1_inventario.controllers.InventarioController;

import javax.swing.*;
import java.awt.*;

public class MovimientoView extends JPanel implements View {

    private final InventarioController controller;
    private final MovimientoTablaPanel  tablaPanel;
    private final AjusteInventarioPanel ajustePanel;
    private final DevolucionPanel       devolucionPanel;
    private final ReporteInventarioPanel reportePanel;

    public MovimientoView(InventarioController controller) {
        this.controller      = controller;
        this.tablaPanel      = new MovimientoTablaPanel(controller);
        this.ajustePanel     = new AjusteInventarioPanel(controller.getAjusteDevCtrl(),
                                                          controller.getProductoCtrl());
        this.devolucionPanel = new DevolucionPanel(controller.getAjusteDevCtrl(),
                                                   controller.getProductoCtrl());
        this.reportePanel    = new ReporteInventarioPanel(controller);

        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Historial",    tablaPanel);
        tabs.addTab("Ajustes",      ajustePanel);
        tabs.addTab("Devoluciones", devolucionPanel);
        tabs.addTab("Reporte",      reportePanel);
        add(tabs, BorderLayout.CENTER);
    }

    @Override
    public void update(Model model, Object data) {
        tablaPanel.recargar();
    }
}
