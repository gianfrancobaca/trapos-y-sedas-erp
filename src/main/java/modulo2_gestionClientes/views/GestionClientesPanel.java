package modulo2_gestionClientes.views;

import core.Model;
import core.View;
import modulo2_gestionClientes.controllers.*;

import javax.swing.*;
import java.awt.*;

public class GestionClientesPanel extends JPanel implements View {

    public GestionClientesPanel(ClienteController clienteCtrl, FacturaController facturaCtrl) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Gestión de Clientes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JTabbedPane pestanas = new JTabbedPane();

        pestanas.addTab("Clientes", new ClientePanel(clienteCtrl));
        pestanas.addTab("Categorías", new CategoriaClientePanel(new CategoriaClienteController()));
        pestanas.addTab("Reclamos", new ReclamoPanel(new ReclamoController()));
        pestanas.addTab("Fidelización", new FidelizacionPanel(new FidelizacionController()));
        pestanas.addTab("Métodos de Pago", new MetodoPagoPanel(new MetodoPagoController()));
        pestanas.addTab("Notificaciones", new NotificacionPanel(new NotificacionController()));
        pestanas.addTab("Pedidos", new PedidoPanel(new PedidoController()));
        pestanas.addTab("Facturas", new FacturaPanel(facturaCtrl));
        pestanas.addTab("Reportes", new ReporteClientePanel(new ReporteClienteController()));

        add(titulo, BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
    }

    @Override
    public void update(Model model, Object data) {
    }
}