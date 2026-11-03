package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.ClienteController;
import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.models.CategoriaCliente;
import core.Model;
import core.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientePanel extends JPanel implements View{

    private final ClienteController controller;
    private DefaultTableModel tableModel;
    private JTable tabla;

    private JTextField txtNombre, txtApellido, txtEmail, txtDireccion;

    public ClientePanel(ClienteController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        // Titulo
        JLabel titulo = new JLabel("Gestion de Clientes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Nombre", "Apellido", "Email", "Direccion", "Categoria"};
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
        form.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        txtNombre    = new JTextField(); txtApellido = new JTextField();
        txtEmail     = new JTextField(); txtDireccion = new JTextField();

        form.add(new JLabel("Nombre:"));    form.add(txtNombre);
        form.add(new JLabel("Apellido:"));  form.add(txtApellido);
        form.add(new JLabel("Email:"));     form.add(txtEmail);
        form.add(new JLabel("Direccion:")); form.add(txtDireccion);

        JPanel botones = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton btnAgregar   = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar  = new JButton("Eliminar");
        botones.add(btnAgregar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);

        btnAgregar.addActionListener(e -> agregarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());

        panelDerecho.add(form, BorderLayout.NORTH);
        panelDerecho.add(botones, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Cliente> lista = controller.listarTodos();
        for (Cliente c : lista) {
            tableModel.addRow(new Object[]{
                    c.getIdCliente(), c.getNombre(), c.getApellido(),
                    c.getEmail(), c.getDireccion(),
                    c.getCategoria() != null ? c.getCategoria().getNombre() : "-"
            });
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtNombre.setText((String) tableModel.getValueAt(fila, 1));
        txtApellido.setText((String) tableModel.getValueAt(fila, 2));
        txtEmail.setText((String) tableModel.getValueAt(fila, 3));
        txtDireccion.setText((String) tableModel.getValueAt(fila, 4));
    }

    private void agregarCliente() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }
        CategoriaCliente categoria = new CategoriaCliente(
                1,
                "Regular",
                "Cliente regular",
                0.00
        );

        Cliente c = new Cliente(0, txtNombre.getText().trim(),
                txtApellido.getText().trim(), txtEmail.getText().trim(),
                txtDireccion.getText().trim(), categoria);

        controller.agregar(c);
        cargarTabla();
        limpiarFormulario();
    }

    private void actualizarCliente() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un cliente."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        CategoriaCliente categoria = new CategoriaCliente(
                1,
                "Regular",
                "Cliente regular",
                0.00
        );

        Cliente c = new Cliente(id, txtNombre.getText().trim(),
                txtApellido.getText().trim(), txtEmail.getText().trim(),
                txtDireccion.getText().trim(), categoria);
        controller.actualizar(c);
        cargarTabla();
        limpiarFormulario();
    }

    private void eliminarCliente() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un cliente."); return; }
        int id = (int) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Eliminar cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.eliminar(id);
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtApellido.setText("");
        txtEmail.setText("");  txtDireccion.setText("");
        tabla.clearSelection();
    }

    @Override
    public void update(Model model, Object data) {
        cargarTabla();
    }

}