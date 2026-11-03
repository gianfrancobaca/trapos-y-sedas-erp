package modulo1_inventario.views;

import modulo1_inventario.controllers.InventarioController;
import modulo1_inventario.models.MovimientoInventario;
import modulo1_inventario.models.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReporteInventarioPanel extends JPanel {

    private final InventarioController controller;
    private final JTextField           campoFechaInicio;
    private final JTextField           campoFechaFin;
    private final JTextArea            areaResumen;
    private final DefaultTableModel    modeloTabla;
    private final JTable               tabla;

    public ReporteInventarioPanel(InventarioController controller) {
        this.controller      = controller;
        this.campoFechaInicio = new JTextField(LocalDate.now().minusMonths(1).toString(), 12);
        this.campoFechaFin    = new JTextField(LocalDate.now().toString(), 12);
        this.areaResumen      = new JTextArea(5, 50);
        this.areaResumen.setEditable(false);
        String[] cols = {"Producto","Entradas","Salidas","Ajustes","Devoluciones"};
        this.modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        this.tabla = new JTable(modeloTabla);

        setLayout(new BorderLayout(5, 5));
        construirUI();
    }

    private void construirUI() {
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Generar Reporte"));
        panelFiltros.add(new JLabel("Desde:")); panelFiltros.add(campoFechaInicio);
        panelFiltros.add(new JLabel("Hasta:")); panelFiltros.add(campoFechaFin);
        JButton btnGenerar = new JButton("Generar");
        btnGenerar.addActionListener(e -> generarReporte());
        panelFiltros.add(btnGenerar);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(areaResumen), new JScrollPane(tabla));
        split.setDividerLocation(120);

        add(panelFiltros, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private void generarReporte() {
        try {
            LocalDate inicio = LocalDate.parse(campoFechaInicio.getText().trim());
            LocalDate fin    = LocalDate.parse(campoFechaFin.getText().trim());

            List<MovimientoInventario> movimientos = controller.obtenerMovimientosPorPeriodo(inicio, fin);
            List<Producto>             stockBajo   = controller.obtenerProductosConStockBajo();

            long entradas    = movimientos.stream().filter(m -> m.getTipo() == MovimientoInventario.TipoMovimiento.ENTRADA).count();
            long salidas     = movimientos.stream().filter(m -> m.getTipo() == MovimientoInventario.TipoMovimiento.SALIDA).count();
            long ajustes     = movimientos.stream().filter(m -> m.getTipo() == MovimientoInventario.TipoMovimiento.AJUSTE).count();
            long devoluciones= movimientos.stream().filter(m -> m.getTipo() == MovimientoInventario.TipoMovimiento.DEVOLUCION).count();

            areaResumen.setText(
                "RESUMEN INVENTARIO  |  " + inicio + "  →  " + fin + "\n" +
                "─────────────────────────────────────────────\n" +
                "Total movimientos : " + movimientos.size() + "\n" +
                "  Entradas        : " + entradas + "\n" +
                "  Salidas         : " + salidas + "\n" +
                "  Ajustes         : " + ajustes + "\n" +
                "  Devoluciones    : " + devoluciones + "\n" +
                "Productos stock bajo: " + stockBajo.size()
            );

            modeloTabla.setRowCount(0);
            for (Producto p : controller.getProductoCtrl().obtenerActivos()) {
                final int pid = p.getId();
                long ent = movimientos.stream().filter(m -> m.getProductoId()==pid && m.getTipo()==MovimientoInventario.TipoMovimiento.ENTRADA).count();
                long sal = movimientos.stream().filter(m -> m.getProductoId()==pid && m.getTipo()==MovimientoInventario.TipoMovimiento.SALIDA).count();
                long aj  = movimientos.stream().filter(m -> m.getProductoId()==pid && m.getTipo()==MovimientoInventario.TipoMovimiento.AJUSTE).count();
                long dev = movimientos.stream().filter(m -> m.getProductoId()==pid && m.getTipo()==MovimientoInventario.TipoMovimiento.DEVOLUCION).count();
                if (ent+sal+aj+dev > 0)
                    modeloTabla.addRow(new Object[]{p.getNombre(), ent, sal, aj, dev});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}