package modulo1_inventario.views;

import modulo1_inventario.controllers.ProveedorController;
import modulo1_inventario.models.Proveedor;

import javax.swing.*;
import java.awt.*;

public class ProveedorFormPanel extends JPanel {

    private final ProveedorController controller;
    private final ProveedorListaPanel  listaPanel;

    private final JComboBox<Proveedor.TipoDocumento> comboTipoDocumento;
    private final JTextField campoNumeroDocumento;
    private final JTextField campoRazonSocial;
    private final JTextField campoContacto;
    private final JTextField campoTelefono;
    private final JTextField campoCorreo;
    private final JTextField campoDireccion;
    private int idEnEdicion = -1;

    public ProveedorFormPanel(ProveedorController controller, ProveedorListaPanel listaPanel) {
        this.controller    = controller;
        this.listaPanel    = listaPanel;
        this.comboTipoDocumento  = new JComboBox<>(new Proveedor.TipoDocumento[] {
                Proveedor.TipoDocumento.DNI,
                Proveedor.TipoDocumento.RUC
        });
        this.campoNumeroDocumento = new JTextField(13);
        this.campoRazonSocial     = new JTextField(25);
        this.campoContacto        = new JTextField(20);
        this.campoTelefono        = new JTextField(12);
        this.campoCorreo          = new JTextField(20);
        this.campoDireccion       = new JTextField(30);

        setBorder(BorderFactory.createTitledBorder("Formulario Proveedor / Consignante"));
        setLayout(new GridBagLayout());
        construirUI();
        registrarSeleccion();
    }

    private void construirUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.anchor = GridBagConstraints.WEST;
        Object[][] campos = {
                {"Tipo Documento:", comboTipoDocumento},
                {"N° Documento:", campoNumeroDocumento},
                {"Nombre / Razón Social:", campoRazonSocial},
                {"Contacto:", campoContacto},
                {"Teléfono:", campoTelefono},
                {"Correo:", campoCorreo},
                {"Dirección:", campoDireccion}
        };
        for (int i = 0; i < campos.length; i++) {
            gbc.gridx=0; gbc.gridy=i; gbc.weightx=0; gbc.fill=GridBagConstraints.NONE;
            add(new JLabel((String)campos[i][0]), gbc);
            gbc.gridx=1; gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
            add((Component)campos[i][1], gbc);
        }
        JPanel btns = new JPanel(new FlowLayout());
        JButton btnGuardar    = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar   = new JButton("Desactivar");
        JButton btnLimpiar    = new JButton("Limpiar");
        btns.add(btnGuardar); btns.add(btnActualizar);
        btns.add(btnEliminar); btns.add(btnLimpiar);
        btnGuardar.addActionListener(e    -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e   -> desactivar());
        btnLimpiar.addActionListener(e    -> limpiar());
        gbc.gridx=0; gbc.gridy=campos.length; gbc.gridwidth=2; gbc.fill=GridBagConstraints.HORIZONTAL; add(btns, gbc);
    }

    private void registrarSeleccion() {
        listaPanel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Proveedor p = listaPanel.getProveedorSeleccionado();
                if (p != null) {
                    idEnEdicion = p.getId();
                    comboTipoDocumento.setSelectedItem(p.getTipoDocumento());
                    campoNumeroDocumento.setText(p.getNumeroDocumento());
                    campoRazonSocial.setText(p.getRazonSocial());
                    campoContacto.setText(p.getContacto());
                    campoTelefono.setText(p.getTelefono());
                    campoCorreo.setText(p.getCorreo());
                    campoDireccion.setText(p.getDireccion());
                }
            }
        });
    }

    private Proveedor construir() {
        Proveedor p = new Proveedor();
        p.setTipoDocumento((Proveedor.TipoDocumento) comboTipoDocumento.getSelectedItem());
        p.setNumeroDocumento(campoNumeroDocumento.getText().trim());
        p.setRazonSocial(campoRazonSocial.getText().trim());
        p.setContacto(campoContacto.getText().trim());
        p.setTelefono(campoTelefono.getText().trim());
        p.setCorreo(campoCorreo.getText().trim());
        p.setDireccion(campoDireccion.getText().trim());
        p.setActivo(true);
        return p;
    }

    private boolean validarDocumento() {
        Proveedor.TipoDocumento tipo = (Proveedor.TipoDocumento) comboTipoDocumento.getSelectedItem();
        String numero = campoNumeroDocumento.getText().trim();

        if (tipo == Proveedor.TipoDocumento.DNI && numero.length() != 8) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener 8 dígitos.", "Documento inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (tipo == Proveedor.TipoDocumento.RUC && numero.length() != 11) {
            JOptionPane.showMessageDialog(this, "El RUC debe tener 11 dígitos.", "Documento inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!numero.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El número de documento solo debe contener dígitos.", "Documento inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void guardar() {
        if (!validarDocumento()) return;
        try {
            controller.registrar(construir());
            JOptionPane.showMessageDialog(this,"Proveedor registrado.");
            limpiar(); listaPanel.recargar();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }
    }

    private void actualizar() {
        if (idEnEdicion < 0) { JOptionPane.showMessageDialog(this,"Seleccione un proveedor."); return; }
        if (!validarDocumento()) return;
        Proveedor p = construir(); p.setId(idEnEdicion);
        controller.actualizar(p); JOptionPane.showMessageDialog(this,"Proveedor actualizado."); listaPanel.recargar();
    }

    private void desactivar() {
        if (idEnEdicion < 0) { JOptionPane.showMessageDialog(this,"Seleccione un proveedor."); return; }
        if (JOptionPane.showConfirmDialog(this,"¿Desactivar?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            controller.desactivar(idEnEdicion); limpiar(); listaPanel.recargar();
        }
    }

    private void limpiar() {
        comboTipoDocumento.setSelectedIndex(0);
        campoNumeroDocumento.setText(""); campoRazonSocial.setText(""); campoContacto.setText("");
        campoTelefono.setText(""); campoCorreo.setText(""); campoDireccion.setText("");
        idEnEdicion = -1;
    }
}