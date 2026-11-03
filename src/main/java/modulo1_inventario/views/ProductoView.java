package modulo1_inventario.views;

import core.Model;
import core.View;
import modulo1_inventario.controllers.ProductoController;

import javax.swing.*;
import java.awt.*;

public class ProductoView extends JPanel implements View {

    private final ProductoController controller;

    private final ProductoListaPanel   listaPanel;
    private final ProductoFormPanel    formPanel;
    private final FiltroProductoPanel  filtroPanel;

    public ProductoView(ProductoController controller) {
        this.controller   = controller;
        this.listaPanel   = new ProductoListaPanel(controller);
        this.formPanel    = new ProductoFormPanel(controller, listaPanel);
        this.filtroPanel  = new FiltroProductoPanel(controller, listaPanel);

        setLayout(new BorderLayout(5, 5));
        construirUI();
    }

    private void construirUI() {
        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(filtroPanel, BorderLayout.NORTH);
        centro.add(new JScrollPane(listaPanel), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centro, formPanel);
        split.setDividerLocation(550);

        JTabbedPane tabsProducto = new JTabbedPane();
        tabsProducto.addTab("Lista y Formulario", split);

        add(tabsProducto, BorderLayout.CENTER);
    }

    @Override
    public void update(Model model, Object data) {
        listaPanel.recargar();
    }
}