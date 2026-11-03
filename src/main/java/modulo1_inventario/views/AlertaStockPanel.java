package modulo1_inventario.views;

import core.Model;
import core.View;
import modulo1_inventario.controllers.InventarioController;
import modulo1_inventario.models.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AlertaStockPanel extends JPanel implements View {

    private final InventarioController controller;
    private final JLabel               etiqueta;
    private final JButton              btnVer;

    public AlertaStockPanel(InventarioController controller) {
        this.controller = controller;
        this.etiqueta   = new JLabel(" ");
        this.btnVer     = new JButton("Ver productos con stock bajo");
        etiqueta.setForeground(Color.RED);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD));

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(etiqueta);
        add(btnVer);
        btnVer.addActionListener(e -> mostrarAlertaDetalle());
        actualizar();
    }

    private void actualizar() {
        List<Producto> bajos = controller.obtenerProductosConStockBajo();
        if (bajos.isEmpty()) {
            etiqueta.setText(" ");
            btnVer.setVisible(false);
        } else {
            etiqueta.setText("⚠  " + bajos.size() + " producto(s) con stock en mínimo o inferior");
            btnVer.setVisible(true);
        }
    }

    private void mostrarAlertaDetalle() {
        List<Producto> bajos = controller.obtenerProductosConStockBajo();
        StringBuilder sb = new StringBuilder("Productos con stock bajo:\n\n");
        for (Producto p : bajos)
            sb.append(String.format("- %s | %s | Stock: %d | Mín: %d%n",
                    p.getCodigo(), p.getNombre(), p.getStockActual(), p.getStockMinimo()));
        JOptionPane.showMessageDialog(this, sb.toString(), "Alerta de Stock", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void update(Model model, Object data) {
        actualizar();
    }
}