package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.ReclamoController;
import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.models.Reclamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class ReclamoPanel extends JPanel {

    private ReclamoController controller;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtIdCliente;
    private JTextArea txtMotivo;
    private JTextField txtEstado;

    public ReclamoPanel(ReclamoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Reclamos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelo = new DefaultTableModel(new Object[]{"ID", "ID Cliente", "Motivo", "Estado", "Fecha"}, 0);
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
        form.setBorder(BorderFactory.createTitledBorder("Datos del Reclamo"));
        form.setPreferredSize(new Dimension(320, 0));

        txtIdCliente = new JTextField();
        txtMotivo = new JTextArea(6, 20);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        txtEstado = new JTextField("Pendiente");

        JButton btnAgregar = new JButton("Registrar Reclamo");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        form.add(new JLabel("ID Cliente:"));
        form.add(txtIdCliente);
        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Reclamo / Motivo:"));
        form.add(new JScrollPane(txtMotivo));
        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Estado:"));
        form.add(txtEstado);
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

        List<Reclamo> reclamos = controller.listarTodos();

        for (Reclamo r : reclamos) {
            modelo.addRow(new Object[]{
                    r.getIdReclamo(),
                    r.getCliente() != null ? r.getCliente().getIdCliente() : "",
                    r.getMotivo(),
                    r.getEstado(),
                    r.getFecha()
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            txtIdCliente.setText(tabla.getValueAt(fila, 1).toString());
            txtMotivo.setText(tabla.getValueAt(fila, 2).toString());
            txtEstado.setText(tabla.getValueAt(fila, 3).toString());
        }
    }

    private void agregar() {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));

            Reclamo reclamo = new Reclamo(
                    0,
                    txtMotivo.getText(),
                    txtEstado.getText(),
                    new Date(),
                    cliente
            );

            controller.agregar(reclamo);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar reclamo: " + e.getMessage());
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un reclamo.");
            return;
        }

        try {
            int idReclamo = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));

            Reclamo reclamo = new Reclamo(
                    idReclamo,
                    txtMotivo.getText(),
                    txtEstado.getText(),
                    new Date(),
                    cliente
            );

            controller.actualizar(reclamo);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar reclamo: " + e.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un reclamo.");
            return;
        }

        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        controller.eliminar(id);
        cargarTabla();
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtIdCliente.setText("");
        txtMotivo.setText("");
        txtEstado.setText("Pendiente");
    }
}