package modulo3_ventas.views;

import core.Model;
import core.View;
import modulo3_ventas.controllers.ProductoController;
import modulo3_ventas.controllers.ReservaController;
import modulo3_ventas.models.Producto;
import modulo3_ventas.models.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class ReservaPanel extends JPanel implements View {

    private final ReservaController  reservaCtrl;
    private final ProductoController productoCtrl;
    private DefaultTableModel tableModel;
    private JTable tabla;

    private JTextField txtClienteId, txtHorasExpiracion;
    private JComboBox<String> cbProducto;
    private List<Producto> productosCache;

    public ReservaPanel(ReservaController reservaCtrl, ProductoController productoCtrl) {
        this.reservaCtrl  = reservaCtrl;
        this.productoCtrl = productoCtrl;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        // Titulo
        JLabel titulo = new JLabel("Gestion de Reservas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Cliente ID", "Producto ID", "Fecha Reserva", "Expira", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(tableModel);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel derecho: formulario + botones
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setPreferredSize(new Dimension(260, 0));

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Nueva Reserva"));

        txtClienteId      = new JTextField();
        txtHorasExpiracion = new JTextField("24");
        productosCache    = productoCtrl.obtenerProductosConStock();
        cbProducto        = new JComboBox<>();
        for (Producto p : productosCache) cbProducto.addItem(p.getId() + " - " + p.getNombre());

        form.add(new JLabel("Cliente ID:"));       form.add(txtClienteId);
        form.add(new JLabel("Producto:"));         form.add(cbProducto);
        form.add(new JLabel("Horas para vencer:")); form.add(txtHorasExpiracion);

        JPanel botones = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton btnCrear     = new JButton("Crear Reserva");
        JButton btnCancelar  = new JButton("Cancelar Reserva");
        JButton btnConfirmar = new JButton("Confirmar Reserva");
        JButton btnVencidas  = new JButton("Proc. Vencidas");
        botones.add(btnCrear);
        botones.add(btnCancelar);
        botones.add(btnConfirmar);
        botones.add(btnVencidas);

        btnCrear.addActionListener(e     -> crearReserva());
        btnCancelar.addActionListener(e  -> cancelarReserva());
        btnConfirmar.addActionListener(e -> confirmarReserva());
        btnVencidas.addActionListener(e  -> procesarVencidas());

        panelDerecho.add(form,    BorderLayout.NORTH);
        panelDerecho.add(botones, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Reserva> lista = reservaCtrl.obtenerReservasActivas();
        for (Reserva r : lista) {
            tableModel.addRow(new Object[]{
                    r.getId(), r.getClienteId(), r.getProductoId(),
                    r.getFechaReserva() != null ? r.getFechaReserva().toLocalDate() : "-",
                    r.getFechaExpiracion() != null ? r.getFechaExpiracion().toLocalDate() : "-",
                    r.getEstado()
            });
        }
    }

    private void crearReserva() {
        if (txtClienteId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del cliente.");
            return;
        }
        if (productosCache.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles para reservar.");
            return;
        }
        try {
            int clienteId  = Integer.parseInt(txtClienteId.getText().trim());
            int horas      = Integer.parseInt(txtHorasExpiracion.getText().trim());
            Producto p     = productosCache.get(cbProducto.getSelectedIndex());
            LocalDateTime expiracion = LocalDateTime.now().plusHours(horas);
            reservaCtrl.crearReserva(clienteId, p.getId(), expiracion);
            cargarTabla();
            txtClienteId.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID y horas deben ser numericos.");
        }
    }

    private void cancelarReserva() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una reserva."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancelar reserva #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) { reservaCtrl.cancelarReserva(id); cargarTabla(); }
    }

    private void confirmarReserva() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una reserva."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        reservaCtrl.confirmarReserva(id);
        cargarTabla();
    }

    private void procesarVencidas() {
        reservaCtrl.procesarReservasVencidas();
        cargarTabla();
        JOptionPane.showMessageDialog(this, "Reservas vencidas procesadas.");
    }

    @Override
    public void update(Model model, Object data) { cargarTabla(); }
}
