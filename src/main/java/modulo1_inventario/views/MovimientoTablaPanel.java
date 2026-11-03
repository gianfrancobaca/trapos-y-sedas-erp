package modulo1_inventario.views;

import modulo1_inventario.controllers.InventarioController;
import modulo1_inventario.models.MovimientoInventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MovimientoTablaPanel extends JPanel {

    private final InventarioController controller;
    private final DefaultTableModel    modelo;
    private final JTable               tabla;
    private final JTextField           campoFechaInicio;
    private final JTextField           campoFechaFin;
    private final JTextField           campoProductoId;

    public MovimientoTablaPanel(InventarioController controller) {
        this.controller     = controller;
        this.campoFechaInicio = new JTextField(LocalDate.now().minusDays(30).toString(), 12);
        this.campoFechaFin    = new JTextField(LocalDate.now().toString(), 12);
        this.campoProductoId  = new JTextField(6);
        String[] cols = {"ID","Producto","Tipo","Cantidad","Stock Ant.","Stock Result.","Motivo","Fecha"};
        this.modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        this.tabla = new JTable(modelo);

        setLayout(new BorderLayout(5, 5));
        construirUI();
        recargar();
    }

    private void construirUI() {
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        panelFiltros.add(new JLabel("Desde:")); panelFiltros.add(campoFechaInicio);
        panelFiltros.add(new JLabel("Hasta:")); panelFiltros.add(campoFechaFin);
        panelFiltros.add(new JLabel("ID Producto:")); panelFiltros.add(campoProductoId);
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnTodos   = new JButton("Todos");
        panelFiltros.add(btnFiltrar); panelFiltros.add(btnTodos);
        btnFiltrar.addActionListener(e -> filtrar());
        btnTodos.addActionListener(e   -> recargar());

        add(panelFiltros, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    public void recargar() {
        modelo.setRowCount(0);
        List<MovimientoInventario> lista = controller.obtenerMovimientosPorPeriodo(
                LocalDate.now().minusYears(1), LocalDate.now());
        cargarEnTabla(lista);
    }

    private void filtrar() {
        try {
            String idTexto = campoProductoId.getText().trim();
            List<MovimientoInventario> lista;
            if (!idTexto.isEmpty()) {
                lista = controller.obtenerMovimientosPorProducto(Integer.parseInt(idTexto));
            } else {
                lista = controller.obtenerMovimientosPorPeriodo(
                        LocalDate.parse(campoFechaInicio.getText().trim()),
                        LocalDate.parse(campoFechaFin.getText().trim()));
            }
            modelo.setRowCount(0);
            cargarEnTabla(lista);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Filtro inválido: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEnTabla(List<MovimientoInventario> lista) {
        for (MovimientoInventario m : lista)
            modelo.addRow(new Object[]{
                m.getId(), m.getProductoNombre(), m.getTipo(), m.getCantidad(),
                m.getStockAnterior(), m.getStockResultante(), m.getMotivo(), m.getFechaHora()
            });
    }
}