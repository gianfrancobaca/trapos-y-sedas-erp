package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.NotificacionController;
import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.models.Notificacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class NotificacionPanel extends JPanel {

    private NotificacionController controller;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtIdCliente;
    private JTextField txtTipo;
    private JTextArea txtMensaje;

    public NotificacionPanel(NotificacionController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Notificaciones");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelo = new DefaultTableModel(new Object[]{"ID", "ID Cliente", "Tipo", "Mensaje", "Fecha", "Leída"}, 0);
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
        form.setBorder(BorderFactory.createTitledBorder("Datos de Notificación"));
        form.setPreferredSize(new Dimension(320, 0));

        txtIdCliente = new JTextField();
        txtTipo = new JTextField();
        txtMensaje = new JTextArea(6, 20);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);

        JButton btnEnviar = new JButton("Enviar Notificación");
        JButton btnLeida = new JButton("Marcar como Leída");
        JButton btnEliminar = new JButton("Eliminar");

        form.add(new JLabel("ID Cliente:"));
        form.add(txtIdCliente);
        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Tipo:"));
        form.add(txtTipo);
        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Mensaje:"));
        form.add(new JScrollPane(txtMensaje));
        form.add(Box.createVerticalStrut(20));

        form.add(btnEnviar);
        form.add(Box.createVerticalStrut(8));
        form.add(btnLeida);
        form.add(Box.createVerticalStrut(8));
        form.add(btnEliminar);

        btnEnviar.addActionListener(e -> enviar());
        btnLeida.addActionListener(e -> marcarComoLeida());
        btnEliminar.addActionListener(e -> eliminar());

        return form;
    }

    private void cargarTabla() {
        modelo.setRowCount(0);

        List<Notificacion> notificaciones = controller.listarTodos();

        for (Notificacion n : notificaciones) {
            modelo.addRow(new Object[]{
                    n.getIdNotificacion(),
                    n.getCliente() != null ? n.getCliente().getIdCliente() : "",
                    n.getTipo(),
                    n.getMensaje(),
                    n.getFecha(),
                    n.isLeida()
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            txtIdCliente.setText(tabla.getValueAt(fila, 1).toString());
            txtTipo.setText(tabla.getValueAt(fila, 2).toString());
            txtMensaje.setText(tabla.getValueAt(fila, 3).toString());
        }
    }

    private void enviar() {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));

            Notificacion notificacion = new Notificacion(
                    0,
                    txtMensaje.getText(),
                    txtTipo.getText(),
                    new Date(),
                    false,
                    cliente
            );

            controller.enviar(notificacion);
            cargarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al enviar notificación: " + e.getMessage());
        }
    }

    private void marcarComoLeida() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una notificación.");
            return;
        }

        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        controller.marcarComoLeida(id);
        cargarTabla();
        limpiarCampos();
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una notificación.");
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
        txtMensaje.setText("");
    }
}