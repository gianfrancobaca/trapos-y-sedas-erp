package modulo1_inventario.views;

import modulo1_inventario.controllers.AjusteDevolucionController;
import modulo1_inventario.controllers.ProductoController;
import modulo1_inventario.models.DetalleDevolucion;
import modulo1_inventario.models.Devolucion;
import modulo1_inventario.models.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DevolucionPanel extends JPanel {

    private final AjusteDevolucionController ajusteCtrl;
    private final ProductoController         productoCtrl;

    private final JComboBox<String> comboTipo;
    private final JTextField        campoMotivo;
    private final JComboBox<String> comboProducto;
    private final JTextField        campoCantidad;
    private final JTextField        campoMotivoLinea;
    private final DefaultTableModel modeloLineas;
    private final JTable            tablaLineas;

    public DevolucionPanel(AjusteDevolucionController ajusteCtrl,
                           ProductoController productoCtrl) {
        this.ajusteCtrl   = ajusteCtrl;
        this.productoCtrl = productoCtrl;
        this.comboTipo        = new JComboBox<>(new String[]{"CLIENTE","PROVEEDOR"});
        this.campoMotivo      = new JTextField(20);
        this.comboProducto    = new JComboBox<>();
        this.campoCantidad    = new JTextField(6);
        this.campoMotivoLinea = new JTextField(15);

        String[] cols = {"Producto ID","Producto","Cantidad","Motivo línea"};
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
        panelDatos.setBorder(BorderFactory.createTitledBorder("Registrar Devolución"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelDatos.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        panelDatos.add(comboTipo, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelDatos.add(new JLabel("Motivo general:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7;
        panelDatos.add(campoMotivo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelDatos.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        panelDatos.add(comboProducto, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelDatos.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7;
        panelDatos.add(campoCantidad, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelDatos.add(new JLabel("Motivo línea:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        panelDatos.add(campoMotivoLinea, gbc);
        gbc.gridwidth = 1;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnAgregar   = new JButton("➕ Agregar Línea");
        JButton btnRegistrar = new JButton("✔ Registrar Devolución");
        JButton btnLimpiar   = new JButton("✖ Limpiar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4; gbc.weightx = 1.0;
        panelDatos.add(panelBotones, gbc);

        btnAgregar.addActionListener(e   -> agregarLinea());
        btnRegistrar.addActionListener(e -> registrarDevolucion());
        btnLimpiar.addActionListener(e   -> {
            modeloLineas.setRowCount(0);
            campoMotivo.setText("");
            campoCantidad.setText("");
            campoMotivoLinea.setText("");
        });


        JScrollPane scrollTabla = new JScrollPane(tablaLineas);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Líneas de la devolución"));

        add(panelDatos,  BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void cargarProductos() {
        comboProducto.removeAllItems();
        for (Producto p : productoCtrl.obtenerActivos())
            comboProducto.addItem(p.getId() + " – " + p.getNombre() + "  [stock: " + p.getStockActual() + "]");
    }

    private void agregarLinea() {
        String sel = (String) comboProducto.getSelectedItem();
        if (sel == null) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        String cantidadTxt = campoCantidad.getText().trim();
        if (cantidadTxt.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese la cantidad."); return; }

        try {
            int productoId = Integer.parseInt(sel.split(" – ")[0]);
            Producto p = productoCtrl.buscarPorId(productoId);
            if (p == null) { JOptionPane.showMessageDialog(this, "Producto no encontrado."); return; }
            int cantidad = Integer.parseInt(cantidadTxt);
            if (cantidad <= 0) throw new NumberFormatException();

            modeloLineas.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    cantidad,
                    campoMotivoLinea.getText().trim()
            });
            campoCantidad.setText("");
            campoMotivoLinea.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero positivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarDevolucion() {
        if (modeloLineas.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue al menos una línea antes de registrar.");
            return;
        }
        String motivo = campoMotivo.getText().trim();
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el motivo general de la devolución.");
            return;
        }

        try {
            Devolucion devolucion = new Devolucion(
                    0,
                    Devolucion.TipoDevolucion.valueOf((String) comboTipo.getSelectedItem()),
                    0, motivo, LocalDateTime.now(), 1
            );

            List<DetalleDevolucion> detalles = new ArrayList<>();
            for (int i = 0; i < modeloLineas.getRowCount(); i++) {
                detalles.add(new DetalleDevolucion(
                        0, 0,
                        (int) modeloLineas.getValueAt(i, 0),
                        (String) modeloLineas.getValueAt(i, 1),
                        (int) modeloLineas.getValueAt(i, 2),
                        (String) modeloLineas.getValueAt(i, 3)
                ));
            }

            String tipo = (String) comboTipo.getSelectedItem();
            if ("CLIENTE".equals(tipo))
                ajusteCtrl.registrarDevolucionCliente(devolucion, detalles, 1);
            else
                ajusteCtrl.registrarDevolucionProveedor(devolucion, detalles, 1);

            JOptionPane.showMessageDialog(this,
                    "Devolución registrada correctamente. ID: " + devolucion.getId(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            modeloLineas.setRowCount(0);
            campoMotivo.setText("");
            campoCantidad.setText("");
            campoMotivoLinea.setText("");
            cargarProductos(); // refresca stocks en el combo

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar devolución:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}