package modulo1_inventario.views;

import core.Model;
import core.View;
import modulo1_inventario.controllers.ProveedorController;

import javax.swing.*;
import java.awt.*;

public class ProveedorView extends JPanel implements View {

    private final ProveedorController controller;
    private final ProveedorListaPanel listaPanel;
    private final ProveedorFormPanel  formPanel;

    public ProveedorView(ProveedorController controller) {
        this.controller = controller;
        this.listaPanel = new ProveedorListaPanel(controller);
        this.formPanel  = new ProveedorFormPanel(controller, listaPanel);

        setLayout(new BorderLayout(5, 5));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaPanel), formPanel);
        split.setDividerLocation(500);
        add(split, BorderLayout.CENTER);
    }

    @Override
    public void update(Model model, Object data) {
        listaPanel.recargar();
    }
}