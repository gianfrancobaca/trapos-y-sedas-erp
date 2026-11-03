package modulo3_ventas.views;

import core.Model;
import core.View;
import modulo3_ventas.controllers.*;

import javax.swing.*;
import java.awt.*;

public class VentasView extends JPanel implements View {

    private final VentaController       ventaCtrl;
    private final ProductoController    productoCtrl;
    private final ReservaController     reservaCtrl;
    private final DescuentoController   descuentoCtrl;
    private final PagoController        pagoCtrl;
    private final ComprobanteController comprobanteCtrl;

    private final JTabbedPane tabs;

    public VentasView() {
        this.ventaCtrl       = new VentaController();
        this.productoCtrl    = new ProductoController();
        this.reservaCtrl     = new ReservaController();
        this.descuentoCtrl   = new DescuentoController();
        this.pagoCtrl        = new PagoController();
        this.comprobanteCtrl = new ComprobanteController();
        this.tabs            = new JTabbedPane();

        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        tabs.addTab("Productos",     new ProductoCatalogoPanel(productoCtrl));
        tabs.addTab("Ventas",        new VentaPanel(ventaCtrl, productoCtrl));
        tabs.addTab("Reservas",      new ReservaPanel(reservaCtrl, productoCtrl));
        tabs.addTab("Descuentos",    new DescuentoPanel(descuentoCtrl));
        tabs.addTab("Pagos",         new PagoPanel(pagoCtrl));
        tabs.addTab("Comprobantes",  new ComprobantePanel(comprobanteCtrl));
        add(tabs, BorderLayout.CENTER);
    }

    @Override
    public void update(Model model, Object data) {
        // Delegado a las pestañas individuales si se requiere
    }
}
