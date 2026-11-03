package modulo1_inventario.views;

import modulo1_inventario.controllers.AjusteDevolucionController;
import modulo1_inventario.controllers.ProductoController;
import modulo1_inventario.models.AjusteInventario;
import modulo1_inventario.models.DetalleAjuste;
import modulo1_inventario.models.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AjusteInventarioPanel extends JPanel {

    private final AjusteDevolucionController ajusteCtrl;
    private final ProductoController         productoCtrl;

    private final JComboBox<String> comboTipo;
    private final JTextField        campoMotivo;
    private final JComboBox<String> comboProducto;
    private final JTextField        campoStockNuevo;
    private final DefaultTableModel modeloLineas;
    private final JTable            tablaLineas;

    public AjusteInventarioPanel(AjusteDevolucionController ajusteCtrl,
                                 ProductoController productoCtrl) {
        this.ajusteCtrl   = ajusteCtrl;
        this.productoCtrl = productoCtrl;
        this.comboTipo      = new JComboBox<>(new String[]{"CONTEO_FISICO","CORRECCION_ERROR","MERMA","OTRO"});
        this.campoMotivo    = new JTextField(20);
        this.comboProducto  = new JComboBox<>();
        this.campoStockNuevo = new JTextField(8);

        String[] cols = {"Producto ID","Producto","Stock Ant.","Stock Nuevo","Diferencia"};
        this.modeloLineas = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        this.tablaLineas = new JTable(modeloLineas);
        tablaLineas.setRowHeight(22);

        setLayout(new BorderLayout(5, 5));
        construirUI();
        cargarProductos();
    }

    private void construirUI() {

        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder("Ajuste de Inventario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(5, 8, 5, 8);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelDatos.add(new JLabel("Tipo de ajuste:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        panelDatos.add(comboTipo, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelDatos.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.6;
        panelDatos.add(campoMotivo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelDatos.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        panelDatos.add(comboProducto, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelDatos.add(new JLabel("Stock Nuevo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.6;
        panelDatos.add(campoStockNuevo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnAgregar   = new JButton("➕ Agregar Línea");
        JButton btnRegistrar = new JButton("✔ Registrar Ajuste");
        JButton btnLimpiar   = new JButton("✖ Limpiar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.weightx = 1.0;
        panelDatos.add(panelBotones, gbc);

        btnAgregar.addActionListener(e   -> agregarLinea());
        btnRegistrar.addActionListener(e -> registrarAjuste());
        btnLimpiar.addActionListener(e   -> {
            modeloLineas.setRowCount(0);
            campoMotivo.setText("");
            campoStockNuevo.setText("");
        });

        JScrollPane scrollTabla = new JScrollPane(tablaLineas);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Líneas del ajuste"));

        add(panelDatos,   BorderLayout.NORTH);
        add(scrollTabla,  BorderLayout.CENTER);
    }

    private void cargarProductos() {
        comboProducto.removeAllItems();
        for (Producto p : productoCtrl.obtenerActivos())
            comboProducto.addItem(p.getId() + " – " + p.getNombre() + "  [stock actual: " + p.getStockActual() + "]");
    }

    private void agregarLinea() {
        String sel = (String) comboProducto.getSelectedItem();
        if (sel == null) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        String stockTxt = campoStockNuevo.getText().trim();
        if (stockTxt.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el Stock Nuevo."); return; }

        try {
            int productoId = Integer.parseInt(sel.split(" – ")[0]);
            Producto p = productoCtrl.buscarPorId(productoId);
            if (p == null) { JOptionPane.showMessageDialog(this, "Producto no encontrado."); return; }
            int stockNuevo = Integer.parseInt(stockTxt);
            if (stockNuevo < 0) throw new NumberFormatException();

            modeloLineas.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getStockActual(),
                    stockNuevo,
                    stockNuevo - p.getStockActual()
            });
            campoStockNuevo.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero no negativo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarAjuste() {
        if (modeloLineas.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue al menos una línea antes de registrar.");
            return;
        }
        String motivo = campoMotivo.getText().trim();
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un motivo para el ajuste.");
            return;
        }

        AjusteInventario ajuste = new AjusteInventario(
                0,
                AjusteInventario.TipoAjuste.valueOf((String) comboTipo.getSelectedItem()),
                motivo,
                LocalDateTime.now(),
                1   // usuario por defecto
        );

        List<DetalleAjuste> detalles = new ArrayList<>();
        for (int i = 0; i < modeloLineas.getRowCount(); i++) {
            detalles.add(new DetalleAjuste(
                    0, 0,
                    (int) modeloLineas.getValueAt(i, 0),
                    (String) modeloLineas.getValueAt(i, 1),
                    (int) modeloLineas.getValueAt(i, 2),
                    (int) modeloLineas.getValueAt(i, 3)
            ));
        }

        try {
            ajusteCtrl.registrarAjuste(ajuste, detalles, 1);
            JOptionPane.showMessageDialog(this, "Ajuste registrado correctamente. ID: " + ajuste.getId(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            modeloLineas.setRowCount(0);
            campoMotivo.setText("");
            campoStockNuevo.setText("");
            cargarProductos(); // refresca stocks en el combo
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar ajuste:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}