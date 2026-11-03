package modulo1_inventario.views;

import modulo1_inventario.controllers.ProductoController;
import modulo1_inventario.models.Categoria;
import modulo1_inventario.models.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductoFormPanel extends JPanel {

    private final ProductoController controller;
    private final ProductoListaPanel  listaPanel;

    private final JTextField  campoCodigo;
    private final JTextField  campoNombre;
    private final JTextField  campoDescripcion;
    private final JComboBox<String> comboCategoria;
    private final JTextField  campoPrecioCompra;
    private final JTextField  campoPrecioVenta;
    private final JTextField  campoStockActual;
    private final JTextField  campoStockMinimo;

    private int idEnEdicion = -1;

    public ProductoFormPanel(ProductoController controller, ProductoListaPanel listaPanel) {
        this.controller  = controller;
        this.listaPanel  = listaPanel;
        this.campoCodigo       = new JTextField(15);
        this.campoNombre       = new JTextField(20);
        this.campoDescripcion  = new JTextField(25);
        this.comboCategoria    = new JComboBox<>();
        this.campoPrecioCompra = new JTextField(10);
        this.campoPrecioVenta  = new JTextField(10);
        this.campoStockActual  = new JTextField(8);
        this.campoStockMinimo  = new JTextField(8);

        setBorder(BorderFactory.createTitledBorder("Formulario Producto"));
        setLayout(new GridBagLayout());
        construirUI();
        cargarCategorias();
        registrarSeleccion();
    }

    private void construirUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Object[][] campos = {
                {"Código:", campoCodigo},
                {"Nombre:", campoNombre},
                {"Descripción:", campoDescripcion},
                {"Categoría:", comboCategoria},
                {"P. Compra:", campoPrecioCompra},
                {"P. Venta:", campoPrecioVenta},
                {"Stock Actual:", campoStockActual},
                {"Stock Mínimo:", campoStockMinimo}
        };

        for (int i = 0; i < campos.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            add(new JLabel((String) campos[i][0]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add((Component) campos[i][1], gbc);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Desactivar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnPanel.add(btnGuardar);
        btnPanel.add(btnActualizar);
        btnPanel.add(btnEliminar);
        btnPanel.add(btnLimpiar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> desactivar());
        btnLimpiar.addActionListener(e -> limpiar());

        gbc.gridx = 0;
        gbc.gridy = campos.length;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(btnPanel, gbc);
    }

    private void cargarCategorias() {
        comboCategoria.removeAllItems();
        for (Categoria c : controller.obtenerCategorias()) comboCategoria.addItem(c.getNombre());
    }

    private void registrarSeleccion() {
        listaPanel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Producto p = listaPanel.getProductoSeleccionado();
                if (p != null) cargarEnFormulario(p);
            }
        });
    }

    private void cargarEnFormulario(Producto p) {
        idEnEdicion = p.getId();
        campoCodigo.setText(p.getCodigo());
        campoNombre.setText(p.getNombre());
        campoDescripcion.setText(p.getDescripcion());
        comboCategoria.setSelectedItem(p.getCategoriaNombre());
        campoPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
        campoPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
        campoStockActual.setText(String.valueOf(p.getStockActual()));
        campoStockMinimo.setText(String.valueOf(p.getStockMinimo()));
    }

    private void guardar() {
        try {
            controller.registrar(construirProducto(0));
            JOptionPane.showMessageDialog(this, "Producto registrado.");
            limpiar(); listaPanel.recargar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idEnEdicion < 0) { JOptionPane.showMessageDialog(this,"Seleccione un producto."); return; }
        try {
            controller.actualizar(construirProducto(idEnEdicion));
            JOptionPane.showMessageDialog(this, "Producto actualizado.");
            listaPanel.recargar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desactivar() {
        if (idEnEdicion < 0) { JOptionPane.showMessageDialog(this,"Seleccione un producto."); return; }
        if (JOptionPane.showConfirmDialog(this,"¿Desactivar producto?","Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.desactivar(idEnEdicion);
            limpiar(); listaPanel.recargar();
        }
    }

    private Producto construirProducto(int id) {
        Producto p = new Producto();
        p.setId(id);
        p.setCodigo(campoCodigo.getText().trim());
        p.setNombre(campoNombre.getText().trim());
        p.setDescripcion(campoDescripcion.getText().trim());
        p.setCategoriaNombre((String) comboCategoria.getSelectedItem());
        p.setCategoriaId(comboCategoria.getSelectedIndex() + 1);
        p.setPrecioCompra(Double.parseDouble(campoPrecioCompra.getText().trim()));
        p.setPrecioVenta(Double.parseDouble(campoPrecioVenta.getText().trim()));
        p.setStockActual(Integer.parseInt(campoStockActual.getText().trim()));
        p.setStockMinimo(Integer.parseInt(campoStockMinimo.getText().trim()));
        p.setEstado(Producto.EstadoProducto.DISPONIBLE);
        p.setActivo(true);
        return p;
    }

    private void limpiar() {
        campoCodigo.setText(""); campoNombre.setText(""); campoDescripcion.setText("");
        campoPrecioCompra.setText(""); campoPrecioVenta.setText("");
        campoStockActual.setText(""); campoStockMinimo.setText("");
        comboCategoria.setSelectedIndex(0); idEnEdicion = -1;
    }
}