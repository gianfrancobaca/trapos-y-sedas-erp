package core;

import modulo1_inventario.controllers.InventarioController;
import modulo1_inventario.views.InventarioView;
import modulo2_gestionClientes.Patrones.SesionUsuario;
import modulo2_gestionClientes.controllers.ClienteController;
import modulo2_gestionClientes.controllers.FacturaController;
import modulo2_gestionClientes.views.GestionClientesPanel;
import modulo3_ventas.views.VentasView;

import javax.swing.*;
import java.awt.*;

public class MainController extends Controller {

    private InventarioController inventarioCtrl;
    private ClienteController clienteCtrl;
    private FacturaController facturaCtrl;

    @Override
    public void run() {
        inventarioCtrl = new InventarioController();
        clienteCtrl = new ClienteController();
        facturaCtrl = new FacturaController();

        InventarioView inventarioView = new InventarioView(inventarioCtrl);
        GestionClientesPanel gestionClientesPanel = new GestionClientesPanel(clienteCtrl, facturaCtrl);
        VentasView ventasPanel = new VentasView();

        addView("Inventario", inventarioView);
        addView("Clientes", gestionClientesPanel);
        addView("Ventas", ventasPanel);

        construirUI();

        loadView("Inventario");
        mainFrame.setVisible(true);
    }

    private void construirUI() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 52, 70));
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String nombreUsuario = SesionUsuario.getInstance().getUsuarioActivo() != null
                ? SesionUsuario.getInstance().getUsuarioActivo().getNombre()
                : "Usuario";

        JLabel lblUsuario = new JLabel("  " + nombreUsuario);
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblUsuario);
        sidebar.add(Box.createVerticalStrut(5));

        String rol = SesionUsuario.getInstance().getUsuarioActivo() != null
                ? SesionUsuario.getInstance().getUsuarioActivo().getRol()
                : "";

        JLabel lblRol = new JLabel("  " + rol);
        lblRol.setForeground(new Color(180, 180, 180));
        lblRol.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblRol);
        sidebar.add(Box.createVerticalStrut(25));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(80, 90, 110));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(15));

        sidebar.add(crearBotonNav("Inventario", "Inventario"));
        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(crearBotonNav("Clientes", "Clientes"));
        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(crearBotonNav("Ventas", "Ventas"));
        sidebar.add(Box.createVerticalGlue());

        JButton btnSalir = crearBotonNav("Cerrar Sesion", null);
        btnSalir.setBackground(new Color(180, 60, 60));
        btnSalir.addActionListener(e -> {
            SesionUsuario.getInstance().cerrarSesion();
            mainFrame.dispose();
            System.exit(0);
        });
        sidebar.add(btnSalir);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(sidebar, BorderLayout.WEST);
        mainFrame.add(getViewsViewer(), BorderLayout.CENTER);
    }

    private JButton crearBotonNav(String texto, String vistaKey) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setBackground(new Color(60, 70, 90));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (vistaKey != null) {
            btn.addActionListener(e -> loadView(vistaKey));
        }

        return btn;
    }
}