package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.PedidoController;
import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.models.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoPanel extends JPanel {

    private PedidoController controller;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtIdCliente;
    private JTextField txtEstado;

    public PedidoPanel(PedidoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Pedidos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelo = new DefaultTableModel(new Object[]{"ID", "ID Cliente", "Estado", "Fecha"}, 0);
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
        form.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));
        form.setPreferredSize(new Dimension(300, 0));

        txtIdCliente = new JTextField();
        txtEstado = new JTextField("Pendiente");

        JButton btnAgregar = new JButton("Registrar Pedido");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        form.add(new JLabel("ID Cliente:"));
        form.add(txtIdCliente);
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

        List<Pedido> pedidos = controller.listarTodos();

        for (Pedido p : pedidos) {
            modelo.addRow(new Object[]{
                    p.getIdPedido(),
                    p.getCliente() != null ? p.getCliente().getIdCliente() : "",
                    p.getEstado(),
                    p.getFecha()
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            txtIdCliente.setText(tabla.getValueAt(fila, 1).toString());
            txtEstado.setText(tabla.getValueAt(fila, 2).toString());
        }
    }

    private void agregar() {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));

            Pedido pedido = new Pedido(
                    0,
                    new Date(),
                    txtEstado.getText(),
                    cliente,
                    new ArrayList<>()
            );

            controller.agregar(pedido);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar pedido: " + e.getMessage());
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido.");
            return;
        }

        try {
            int idPedido = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));

            Pedido pedido = new Pedido(
                    idPedido,
                    new Date(),
                    txtEstado.getText(),
                    cliente,
                    new ArrayList<>()
            );

            controller.actualizar(pedido);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar pedido: " + e.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido.");
            return;
        }

        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        controller.eliminar(id);
        cargarTabla();
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtIdCliente.setText("");
        txtEstado.setText("Pendiente");
    }
}