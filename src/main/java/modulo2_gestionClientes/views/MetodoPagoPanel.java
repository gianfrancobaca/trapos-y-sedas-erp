package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.MetodoPagoController;
import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.models.MetodoPago;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MetodoPagoPanel extends JPanel {

    private MetodoPagoController controller;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtIdCliente;
    private JTextField txtTipo;
    private JTextField txtDescripcion;

    public MetodoPagoPanel(MetodoPagoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Métodos de Pago");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelo = new DefaultTableModel(new Object[]{"ID", "ID Cliente", "Tipo", "Descripción"}, 0);
        tabla = new JTable(modelo);

        JPanel form = crearFormulario();

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(form, BorderLayout.EAST);

        cargarTabla();

        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
    }

    private JPanel crearFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createTitledBorder("Datos del Método de Pago"));
        form.setPreferredSize(new Dimension(300, 0));

        txtIdCliente = new JTextField();
        txtTipo = new JTextField();
        txtDescripcion = new JTextField();

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        form.add(new JLabel("ID Cliente:"));
        form.add(txtIdCliente);
        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Tipo:"));
        form.add(txtTipo);
        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Descripción:"));
        form.add(txtDescripcion);
        form.add(Box.createVerticalStrut(20));

        form.add(btnAgregar);
        form.add(Box.createVerticalStrut(8));
        form.add(btnActualizar);
        form.add(Box.createVerticalStrut(8));
        form.add(btnEliminar);

        btnAgregar.addActionListener(e -> agregar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());

        return form;
    }

    private void cargarTabla() {
        modelo.setRowCount(0);

        List<MetodoPago> metodos = controller.listarTodos();

        for (MetodoPago m : metodos) {
            modelo.addRow(new Object[]{
                    m.getIdMetodoPago(),
                    m.getCliente() != null ? m.getCliente().getIdCliente() : "",
                    m.getTipo(),
                    m.getDescripcion()
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            txtIdCliente.setText(tabla.getValueAt(fila, 1).toString());
            txtTipo.setText(tabla.getValueAt(fila, 2).toString());
            txtDescripcion.setText(tabla.getValueAt(fila, 3).toString());
        }
    }

    private void agregar() {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));

            MetodoPago metodo = new MetodoPago(
                    0,
                    txtTipo.getText(),
                    txtDescripcion.getText(),
                    cliente
            );

            controller.agregar(metodo);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar método de pago: " + e.getMessage());
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un método de pago.");
            return;
        }

        try {
            int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));

            MetodoPago metodo = new MetodoPago(
                    id,
                    txtTipo.getText(),
                    txtDescripcion.getText(),
                    cliente
            );

            controller.actualizar(metodo);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar método de pago: " + e.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un método de pago.");
            return;
        }

        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        controller.eliminar(id);
        cargarTabla();
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtIdCliente.setText("");
        txtTipo.setText("");
        txtDescripcion.setText("");
    }
}