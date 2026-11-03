package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.CategoriaClienteController;
import modulo2_gestionClientes.models.CategoriaCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaClientePanel extends JPanel {

    private CategoriaClienteController controller;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtDescuento;

    public CategoriaClientePanel(CategoriaClienteController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Categorías de Cliente");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Descuento"}, 0);
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
        form.setBorder(BorderFactory.createTitledBorder("Datos de Categoría"));
        form.setPreferredSize(new Dimension(300, 0));

        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtDescuento = new JTextField();

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Descripción:"));
        form.add(txtDescripcion);
        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Descuento:"));
        form.add(txtDescuento);
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

        List<CategoriaCliente> categorias = controller.listarTodos();

        for (CategoriaCliente c : categorias) {
            modelo.addRow(new Object[]{
                    c.getIdCategoria(),
                    c.getNombre(),
                    c.getDescripcion(),
                    c.getDescuento()
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            txtNombre.setText(tabla.getValueAt(fila, 1).toString());
            txtDescripcion.setText(tabla.getValueAt(fila, 2).toString());
            txtDescuento.setText(tabla.getValueAt(fila, 3).toString());
        }
    }

    private void agregar() {
        try {
            CategoriaCliente categoria = new CategoriaCliente(
                    0,
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    Double.parseDouble(txtDescuento.getText())
            );

            controller.agregar(categoria);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar categoría: " + e.getMessage());
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría.");
            return;
        }

        try {
            int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            CategoriaCliente categoria = new CategoriaCliente(
                    id,
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    Double.parseDouble(txtDescuento.getText())
            );

            controller.actualizar(categoria);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar categoría: " + e.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría.");
            return;
        }

        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        controller.eliminar(id);
        cargarTabla();
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtDescuento.setText("");
    }
}