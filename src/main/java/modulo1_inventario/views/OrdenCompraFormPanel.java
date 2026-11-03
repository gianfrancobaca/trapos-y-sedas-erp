package modulo1_inventario.views;

import modulo1_inventario.controllers.OrdenCompraController;
import modulo1_inventario.controllers.ProveedorController;
import modulo1_inventario.models.OrdenCompra;
import modulo1_inventario.models.Proveedor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class OrdenCompraFormPanel extends JPanel {

    private final OrdenCompraController  ordenCtrl;
    private final ProveedorController    provCtrl;
    private final OrdenCompraListaPanel  listaPanel;

    private final JComboBox<String> comboProveedor;
    private final JTextField        campoFechaEmision;
    private final JTextField        campoFechaEntrega;

    public OrdenCompraFormPanel(OrdenCompraController ordenCtrl,
                                ProveedorController provCtrl,
                                OrdenCompraListaPanel listaPanel) {
        this.ordenCtrl   = ordenCtrl;
        this.provCtrl    = provCtrl;
        this.listaPanel  = listaPanel;
        this.comboProveedor   = new JComboBox<>();
        this.campoFechaEmision = new JTextField(LocalDate.now().toString(), 12);
        this.campoFechaEntrega = new JTextField(LocalDate.now().plusDays(7).toString(), 12);

        setBorder(BorderFactory.createTitledBorder("Nueva Orden de Compra"));
        setLayout(new GridBagLayout());
        construirUI();
        cargarProveedores();
    }

    private void construirUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.anchor = GridBagConstraints.WEST;
        Object[][] campos = {
            {"Proveedor:",      comboProveedor},
            {"Fecha Emisión:",  campoFechaEmision},
            {"Fecha Entrega:",  campoFechaEntrega}
        };
        for (int i = 0; i < campos.length; i++) {
            gbc.gridx=0; gbc.gridy=i; add(new JLabel((String)campos[i][0]), gbc);
            gbc.gridx=1; add((Component)campos[i][1], gbc);
        }
        JPanel btns = new JPanel(new FlowLayout());
        JButton btnCrear   = new JButton("Crear Orden");
        JButton btnAprobar = new JButton("Aprobar");
        JButton btnRecibir = new JButton("Recibir");
        JButton btnCancelar= new JButton("Cancelar Orden");
        btns.add(btnCrear); btns.add(btnAprobar); btns.add(btnRecibir); btns.add(btnCancelar);

        btnCrear.addActionListener(e -> crearOrden());
        btnAprobar.addActionListener(e -> {
            if (listaPanel.getOrdenSeleccionadaId() > 0) {
                ordenCtrl.aprobarOrden(listaPanel.getOrdenSeleccionadaId());
                listaPanel.recargar();
            }
        });
        btnRecibir.addActionListener(e -> {
            if (listaPanel.getOrdenSeleccionadaId() > 0) {
                ordenCtrl.recibirOrden(listaPanel.getOrdenSeleccionadaId(), 1);
                listaPanel.recargar();
            }
        });
        btnCancelar.addActionListener(e -> {
            if (listaPanel.getOrdenSeleccionadaId() > 0) {
                ordenCtrl.cancelarOrden(listaPanel.getOrdenSeleccionadaId());
                listaPanel.recargar();
            }
        });
        gbc.gridx=0; gbc.gridy=campos.length; gbc.gridwidth=2; add(btns, gbc);
    }

    private void cargarProveedores() {
        comboProveedor.removeAllItems();
        for (Proveedor p : provCtrl.obtenerActivos())
            comboProveedor.addItem(p.getId() + " - " + p.getRazonSocial());
    }

    private void crearOrden() {
        try {
            String seleccion = (String) comboProveedor.getSelectedItem();
            if (seleccion == null) return;
            int proveedorId = Integer.parseInt(seleccion.split(" - ")[0]);
            Proveedor proveedor = provCtrl.buscarPorId(proveedorId);
            OrdenCompra orden = new OrdenCompra(0, proveedorId, proveedor.getRazonSocial(),
                    LocalDate.parse(campoFechaEmision.getText().trim()),
                    LocalDate.parse(campoFechaEntrega.getText().trim()));
            ordenCtrl.crearOrden(orden, new java.util.ArrayList<>());
            JOptionPane.showMessageDialog(this, "Orden creada con ID: " + orden.getId());
            listaPanel.recargar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}