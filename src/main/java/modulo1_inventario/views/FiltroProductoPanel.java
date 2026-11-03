package modulo1_inventario.views;

import modulo1_inventario.controllers.ProductoController;
import modulo1_inventario.models.Categoria;
import modulo1_inventario.models.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroProductoPanel extends JPanel {

    private final ProductoController controller;
    private final ProductoListaPanel listaPanel;

    private final JTextField        campoBusqueda;
    private final JComboBox<String> comboCategoria;

    public FiltroProductoPanel(ProductoController controller, ProductoListaPanel listaPanel) {
        this.controller    = controller;
        this.listaPanel    = listaPanel;
        this.campoBusqueda  = new JTextField(20);
        this.comboCategoria = new JComboBox<>();

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Filtros"));
        construirUI();
        cargarCategorias();
    }

    private void construirUI() {
        add(new JLabel("Buscar:")); add(campoBusqueda);
        add(new JLabel("Categoría:")); add(comboCategoria);

        JButton btnFiltrar    = new JButton("Filtrar");
        JButton btnTodos      = new JButton("Todos");
        JButton btnStockBajo  = new JButton("Stock Bajo");
        add(btnFiltrar); add(btnTodos); add(btnStockBajo);

        btnFiltrar.addActionListener(e   -> filtrar());
        btnTodos.addActionListener(e     -> listaPanel.recargar());
        btnStockBajo.addActionListener(e -> listaPanel.recargarCon(controller.obtenerConStockBajo()));
    }

    private void cargarCategorias() {
        comboCategoria.addItem("TODOS");
        for (Categoria c : controller.obtenerCategorias()) comboCategoria.addItem(c.getNombre());
    }

    private void filtrar() {
        String texto     = campoBusqueda.getText().trim().toLowerCase();
        String categoria = (String) comboCategoria.getSelectedItem();

        List<Producto> base = "TODOS".equals(categoria)
                ? controller.obtenerTodos()
                : controller.obtenerCategorias().stream()
                    .filter(c -> c.getNombre().equals(categoria))
                    .findFirst()
                    .map(c -> controller.obtenerPorCategoria(c.getId()))
                    .orElse(controller.obtenerTodos());

        List<Producto> filtrados = texto.isEmpty() ? base : base.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(texto)
                          || p.getCodigo().toLowerCase().contains(texto))
                .collect(Collectors.toList());

        listaPanel.recargarCon(filtrados);
    }
}