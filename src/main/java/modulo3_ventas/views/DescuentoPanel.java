package modulo3_ventas.views;

import core.Model;
import core.View;
import modulo3_ventas.controllers.DescuentoController;
import modulo3_ventas.models.Descuento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class DescuentoPanel extends JPanel implements View {

    private final DescuentoController controller;
    private DefaultTableModel tableModel;
    private JTable tabla;

    private JTextField txtNombre, txtDescripcion, txtValor, txtFechaInicio, txtFechaFin;
    private JComboBox<Descuento.TipoDescuento> cbTipo;

    public DescuentoPanel(DescuentoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        // Titulo
        JLabel titulo = new JLabel("Gestion de Descuentos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Nombre", "Tipo", "Valor", "F.Inicio", "F.Fin", "Activo"};
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
        form.setBorder(BorderFactory.createTitledBorder("Datos del Descuento"));

        txtNombre      = new JTextField();
        txtDescripcion = new JTextField();
        cbTipo         = new JComboBox<>(Descuento.TipoDescuento.values());
        txtValor       = new JTextField();
        txtFechaInicio = new JTextField("2026-01-01");
        txtFechaFin    = new JTextField("2026-12-31");

        form.add(new JLabel("Nombre:"));       form.add(txtNombre);
        form.add(new JLabel("Descripcion:"));  form.add(txtDescripcion);
        form.add(new JLabel("Tipo:"));         form.add(cbTipo);
        form.add(new JLabel("Valor:"));        form.add(txtValor);
        form.add(new JLabel("F.Inicio (yyyy-MM-dd):")); form.add(txtFechaInicio);
        form.add(new JLabel("F.Fin (yyyy-MM-dd):"));    form.add(txtFechaFin);

        JPanel botones = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton btnAgregar    = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar   = new JButton("Eliminar");
        botones.add(btnAgregar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);

        btnAgregar.addActionListener(e    -> agregarDescuento());
        btnActualizar.addActionListener(e -> actualizarDescuento());
        btnEliminar.addActionListener(e   -> eliminarDescuento());

        panelDerecho.add(form,    BorderLayout.NORTH);
        panelDerecho.add(botones, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Descuento> lista = controller.obtenerDescuentos();
        for (Descuento d : lista) {
            tableModel.addRow(new Object[]{
                    d.getId(), d.getNombre(), d.getTipo(),
                    d.getValor(), d.getFechaInicio(), d.getFechaFin(), d.isActivo()
            });
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtNombre.setText(String.valueOf(tableModel.getValueAt(fila, 1)));
        cbTipo.setSelectedItem(tableModel.getValueAt(fila, 2));
        txtValor.setText(String.valueOf(tableModel.getValueAt(fila, 3)));
        txtFechaInicio.setText(String.valueOf(tableModel.getValueAt(fila, 4)));
        txtFechaFin.setText(String.valueOf(tableModel.getValueAt(fila, 5)));
    }

    private void agregarDescuento() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }
        try {
            Descuento d = new Descuento();
            d.setNombre(txtNombre.getText().trim());
            d.setDescripcion(txtDescripcion.getText().trim());
            d.setTipo((Descuento.TipoDescuento) cbTipo.getSelectedItem());
            d.setValor(Double.parseDouble(txtValor.getText().trim()));
            d.setFechaInicio(LocalDate.parse(txtFechaInicio.getText().trim()));
            d.setFechaFin(LocalDate.parse(txtFechaFin.getText().trim()));
            d.setActivo(true);
            controller.crearDescuento(d);
            cargarTabla();
            limpiarFormulario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Valor numerico y fechas en formato yyyy-MM-dd.");
        }
    }

    private void actualizarDescuento() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un descuento."); return; }
        try {
            Descuento d = new Descuento();
            d.setId((int) tableModel.getValueAt(fila, 0));
            d.setNombre(txtNombre.getText().trim());
            d.setDescripcion(txtDescripcion.getText().trim());
            d.setTipo((Descuento.TipoDescuento) cbTipo.getSelectedItem());
            d.setValor(Double.parseDouble(txtValor.getText().trim()));
            d.setFechaInicio(LocalDate.parse(txtFechaInicio.getText().trim()));
            d.setFechaFin(LocalDate.parse(txtFechaFin.getText().trim()));
            d.setActivo(true);
            controller.actualizarDescuento(d);
            cargarTabla();
            limpiarFormulario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Valor numerico y fechas en formato yyyy-MM-dd.");
        }
    }

    private void eliminarDescuento() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un descuento."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Eliminar descuento?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) { controller.eliminarDescuento(id); cargarTabla(); limpiarFormulario(); }
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtDescripcion.setText(""); txtValor.setText("");
        tabla.clearSelection();
    }

    @Override
    public void update(Model model, Object data) { cargarTabla(); }
}
