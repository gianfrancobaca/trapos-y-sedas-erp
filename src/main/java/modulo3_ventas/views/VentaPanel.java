package modulo3_ventas.views;

import core.Model;
import core.View;
import modulo3_ventas.controllers.ProductoController;
import modulo3_ventas.controllers.VentaController;
import modulo3_ventas.models.DetalleVenta;
import modulo3_ventas.models.Producto;
import modulo3_ventas.models.Venta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaPanel extends JPanel implements View {

    private final VentaController    ventaCtrl;
    private final ProductoController productoCtrl;
    private DefaultTableModel tableModel;
    private JTable tabla;

    public VentaPanel(VentaController ventaCtrl, ProductoController productoCtrl) {
        this.ventaCtrl    = ventaCtrl;
        this.productoCtrl = productoCtrl;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        // Titulo
        JLabel titulo = new JLabel("Registro de Ventas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Cliente ID", "Vendedor ID", "Fecha", "Estado", "Metodo Pago", "Total"};
        tableModel = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(tableModel);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel derecho: acciones
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setPreferredSize(new Dimension(200, 0));

        JPanel botones = new JPanel(new GridLayout(3, 1, 5, 5));
        botones.setBorder(BorderFactory.createTitledBorder("Acciones"));
        JButton btnNueva      = new JButton("Nueva Venta");
        JButton btnCancelar   = new JButton("Cancelar Venta");
        JButton btnRefrescar  = new JButton("Refrescar");
        botones.add(btnNueva);
        botones.add(btnCancelar);
        botones.add(btnRefrescar);

        btnNueva.addActionListener(e    -> abrirDialogoNuevaVenta());
        btnCancelar.addActionListener(e -> cancelarVenta());
        btnRefrescar.addActionListener(e -> cargarTabla());

        panelDerecho.add(botones, BorderLayout.NORTH);
        add(panelDerecho, BorderLayout.EAST);
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Venta> lista = ventaCtrl.obtenerVentas();
        for (Venta v : lista) {
            tableModel.addRow(new Object[]{
                    v.getId(), v.getClienteId(), v.getVendedorId(),
                    v.getFechaHora() != null ? v.getFechaHora().toLocalDate() : "-",
                    v.getEstado(), v.getMetodoPago(),
                    String.format("S/ %.2f", v.getTotal())
            });
        }
    }

    private void abrirDialogoNuevaVenta() {
        // Dialogo para registrar una venta rapida
        JTextField txtClienteId  = new JTextField("1");
        JTextField txtVendedorId = new JTextField("2");
        JComboBox<Venta.MetodoPago> cbMetodo = new JComboBox<>(Venta.MetodoPago.values());

        // Selector de producto
        List<Producto> productos = productoCtrl.obtenerProductosConStock();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos con stock disponible.");
            return;
        }
        JComboBox<String> cbProducto = new JComboBox<>();
        for (Producto p : productos) cbProducto.addItem(p.getId() + " - " + p.getNombre() + " (S/ " + p.getPrecioVenta() + ")");

        JTextField txtCantidad = new JTextField("1");

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Cliente ID:"));     panel.add(txtClienteId);
        panel.add(new JLabel("Vendedor ID:"));    panel.add(txtVendedorId);
        panel.add(new JLabel("Metodo de pago:")); panel.add(cbMetodo);
        panel.add(new JLabel("Producto:"));       panel.add(cbProducto);
        panel.add(new JLabel("Cantidad:"));       panel.add(txtCantidad);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nueva Venta", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            int clienteId  = Integer.parseInt(txtClienteId.getText().trim());
            int vendedorId = Integer.parseInt(txtVendedorId.getText().trim());
            int cantidad   = Integer.parseInt(txtCantidad.getText().trim());

            Producto productoSel = productos.get(cbProducto.getSelectedIndex());
            double subtotal = productoSel.getPrecioVenta() * cantidad;
            double igv      = subtotal * 0.18;
            double total    = subtotal + igv;

            Venta venta = new Venta();
            venta.setClienteId(clienteId);
            venta.setVendedorId(vendedorId);
            venta.setFechaHora(LocalDateTime.now());
            venta.setEstado(Venta.EstadoVenta.COMPLETADA);
            venta.setMetodoPago((Venta.MetodoPago) cbMetodo.getSelectedItem());
            venta.setSubtotal(subtotal);
            venta.setDescuento(0);
            venta.setIgv(igv);
            venta.setTotal(total);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProductoId(productoSel.getId());
            detalle.setProductoNombre(productoSel.getNombre());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(productoSel.getPrecioVenta());
            detalle.setDescuentoLinea(0);
            detalle.setSubtotal(subtotal);

            List<DetalleVenta> detalles = new ArrayList<>();
            detalles.add(detalle);

            ventaCtrl.registrarVenta(venta, detalles);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Venta registrada. Total: S/ " + String.format("%.2f", total));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID y cantidad deben ser numericos.");
        }
    }

    private void cancelarVenta() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una venta."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancelar venta #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ventaCtrl.cancelarVenta(id);
            cargarTabla();
        }
    }

    @Override
    public void update(Model model, Object data) { cargarTabla(); }
}
