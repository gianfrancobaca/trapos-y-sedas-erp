package modulo1_inventario.controllers;

import core.Controller;
import modulo1_inventario.models.MovimientoInventario;
import modulo1_inventario.models.Producto;
import modulo1_inventario.repositories.*;
import modulo1_inventario.views.InventarioView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InventarioController extends Controller {

    private final ProductoRepository             productoRepo;
    private final MovimientoInventarioRepository movimientoRepo;

    private final ProductoController         productoCtrl;
    private final ProveedorController        proveedorCtrl;
    private final OrdenCompraController      ordenCompraCtrl;
    private final AjusteDevolucionController ajusteDevCtrl;

    private InventarioView vista;

    public InventarioController() {
        this.productoRepo    = new ProductoRepositorySQL();
        this.movimientoRepo  = new MovimientoInventarioRepositorySQL();
        this.productoCtrl    = new ProductoController();
        this.proveedorCtrl   = new ProveedorController();
        this.ordenCompraCtrl = new OrdenCompraController();
        this.ajusteDevCtrl   = new AjusteDevolucionController();
    }

    @Override
    public void run() {
        vista = new InventarioView(this);
        addView("InventarioView", vista);
        loadView("InventarioView");
        mainFrame.setVisible(true);
    }

    public void registrarEntrada(int productoId, int cantidad, String motivo,
                                 String referencia, int usuarioId) {
        Producto p = productoRepo.buscarPorId(productoId);
        if (p == null) return;
        int anterior = p.getStockActual();
        p.aumentarStock(cantidad);
        productoRepo.actualizar(p);
        movimientoRepo.guardar(new MovimientoInventario(
                0, productoId, p.getNombre(),
                MovimientoInventario.TipoMovimiento.ENTRADA,
                cantidad, anterior, motivo, referencia,
                LocalDateTime.now(), usuarioId));
        if (vista != null) vista.update(p, "ENTRADA");
    }

    public void registrarSalida(int productoId, int cantidad, String motivo,
                                String referencia, int usuarioId) {
        Producto p = productoRepo.buscarPorId(productoId);
        if (p == null || !p.tieneStockSuficiente(cantidad)) return;
        int anterior = p.getStockActual();
        p.reducirStock(cantidad);
        productoRepo.actualizar(p);
        movimientoRepo.guardar(new MovimientoInventario(
                0, productoId, p.getNombre(),
                MovimientoInventario.TipoMovimiento.SALIDA,
                cantidad, anterior, motivo, referencia,
                LocalDateTime.now(), usuarioId));
        if (vista != null) vista.update(p, "SALIDA");
    }

    public List<MovimientoInventario> obtenerMovimientosPorPeriodo(LocalDate inicio, LocalDate fin) {
        return movimientoRepo.buscarPorPeriodo(inicio, fin);
    }

    public List<MovimientoInventario> obtenerMovimientosPorProducto(int productoId) {
        return movimientoRepo.buscarPorProducto(productoId);
    }

    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepo.buscarConStockBajo();
    }

    public ProductoController         getProductoCtrl()    { return productoCtrl; }
    public ProveedorController        getProveedorCtrl()   { return proveedorCtrl; }
    public OrdenCompraController      getOrdenCompraCtrl() { return ordenCompraCtrl; }
    public AjusteDevolucionController getAjusteDevCtrl()   { return ajusteDevCtrl; }
}
