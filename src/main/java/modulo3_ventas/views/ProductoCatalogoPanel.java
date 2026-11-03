package modulo3_ventas.views;

import core.Model;
import core.View;
import modulo3_ventas.controllers.ProductoController;
import modulo3_ventas.models.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoCatalogoPanel extends JPanel implements View {

    private final ProductoController controller;
    private DefaultTableModel tableModel;
    private JTable tabla;

    private JTextField txtCodigo, txtNombre, txtDescripcion, txtPrecio, txtStock;
    private JComboBox<Producto.CategoriaProducto> cbCategoria;

    public ProductoCatalogoPanel(ProductoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        // Titulo
        JLabel titulo = new JLabel("Catalogo de Productos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Codigo", "Nombre", "Categoria", "Precio", "Stock", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(tableModel);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionEnFormulario();
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel derecho: formulario + botones
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setPreferredSize(new Dimension(260, 0));

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));

        txtCodigo      = new JTextField();
        txtNombre      = new JTextField();
        txtDescripcion = new JTextField();
        txtPrecio      = new JTextField();
        txtStock       = new JTextField();
        cbCategoria    = new JComboBox<>(Producto.CategoriaProducto.values());

        form.add(new JLabel("Codigo:"));      form.add(txtCodigo);
        form.add(new JLabel("Nombre:"));      form.add(txtNombre);
        form.add(new JLabel("Descripcion:")); form.add(txtDescripcion);
        form.add(new JLabel("Categoria:"));   form.add(cbCategoria);
        form.add(new JLabel("Precio:"));      form.add(txtPrecio);
        form.add(new JLabel("Stock:"));       form.add(txtStock);

        JPanel botones = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton btnAgregar    = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnDesactivar = new JButton("Desactivar");
        botones.add(btnAgregar);
        botones.add(btnActualizar);
        botones.add(btnDesactivar);

        btnAgregar.addActionListener(e    -> agregarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnDesactivar.addActionListener(e -> desactivarProducto());

        panelDerecho.add(form,    BorderLayout.NORTH);
        panelDerecho.add(botones, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Producto> lista = controller.obtenerProductos();
        for (Producto p : lista) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getNombre(),
                    p.getCategoria(), p.getPrecioVenta(),
                    p.getStockDisponible(), p.getEstado()
            });
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtCodigo.setText(String.valueOf(tableModel.getValueAt(fila, 1)));
        txtNombre.setText(String.valueOf(tableModel.getValueAt(fila, 2)));
        cbCategoria.setSelectedItem(tableModel.getValueAt(fila, 3));
        txtPrecio.setText(String.valueOf(tableModel.getValueAt(fila, 4)));
        txtStock.setText(String.valueOf(tableModel.getValueAt(fila, 5)));
    }

    private void agregarProducto() {
        if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Codigo y nombre son obligatorios.");
            return;
        }
        try {
            Producto p = new Producto();
            p.setCodigo(txtCodigo.getText().trim());
            p.setNombre(txtNombre.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setCategoria((Producto.CategoriaProducto) cbCategoria.getSelectedItem());
            p.setPrecioVenta(Double.parseDouble(txtPrecio.getText().trim()));
            p.setStockDisponible(Integer.parseInt(txtStock.getText().trim()));
            p.setEstado(Producto.EstadoProducto.DISPONIBLE);
            p.setActivo(true);
            controller.registrarProducto(p);
            cargarTabla();
            limpiarFormulario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y stock deben ser numericos.");
        }
    }

    private void actualizarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        try {
            Producto p = new Producto();
            p.setId((int) tableModel.getValueAt(fila, 0));
            p.setCodigo(txtCodigo.getText().trim());
            p.setNombre(txtNombre.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setCategoria((Producto.CategoriaProducto) cbCategoria.getSelectedItem());
            p.setPrecioVenta(Double.parseDouble(txtPrecio.getText().trim()));
            p.setStockDisponible(Integer.parseInt(txtStock.getText().trim()));
            p.setEstado(Producto.EstadoProducto.DISPONIBLE);
            p.setActivo(true);
            controller.actualizarProducto(p);
            cargarTabla();
            limpiarFormulario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y stock deben ser numericos.");
        }
    }

    private void desactivarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Desactivar producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.desactivarProducto(id);
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText(""); txtNombre.setText("");
        txtDescripcion.setText(""); txtPrecio.setText(""); txtStock.setText("");
        tabla.clearSelection();
    }

    @Override
    public void update(Model model, Object data) { cargarTabla(); }
}
