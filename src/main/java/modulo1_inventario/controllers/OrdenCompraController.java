package modulo1_inventario.controllers;

import core.Controller;
import modulo1_inventario.models.DetalleOrdenCompra;
import modulo1_inventario.models.MovimientoInventario;
import modulo1_inventario.models.OrdenCompra;
import modulo1_inventario.models.Producto;
import modulo1_inventario.repositories.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenCompraController extends Controller {

    private final OrdenCompraRepository          ordenRepo;
    private final ProductoRepository             productoRepo;
    private final MovimientoInventarioRepository movimientoRepo;

    public OrdenCompraController() {
        this.ordenRepo      = new OrdenCompraRepositorySQL();
        this.productoRepo   = new ProductoRepositorySQL();
        this.movimientoRepo = new MovimientoInventarioRepositorySQL();
    }

    @Override public void run() {}

    public List<OrdenCompra>        obtenerTodas()                             { return ordenRepo.buscarTodas(); }
    public List<OrdenCompra>        obtenerPorProveedor(int proveedorId)       { return ordenRepo.buscarPorProveedor(proveedorId); }
    public List<OrdenCompra>        obtenerPorEstado(OrdenCompra.EstadoOrden e){ return ordenRepo.buscarPorEstado(e); }
    public List<DetalleOrdenCompra> obtenerDetalles(int ordenId)               { return ordenRepo.buscarDetalles(ordenId); }
    public OrdenCompra              buscarPorId(int id)                        { return ordenRepo.buscarPorId(id); }

    public void crearOrden(OrdenCompra orden, List<DetalleOrdenCompra> detalles) {
        ordenRepo.guardar(orden);
        double total = 0;
        for (DetalleOrdenCompra d : detalles) {
            d.setOrdenId(orden.getId());
            ordenRepo.guardarDetalle(d);
            total += d.getSubtotal();
        }
        orden.setTotal(total);
        ordenRepo.actualizar(orden);
    }

    public void aprobarOrden(int ordenId) {
        OrdenCompra orden = ordenRepo.buscarPorId(ordenId);
        if (orden != null) { orden.setEstado(OrdenCompra.EstadoOrden.APROBADA); ordenRepo.actualizar(orden); }
    }

    public void recibirOrden(int ordenId, int usuarioId) {
        OrdenCompra orden = ordenRepo.buscarPorId(ordenId);
        if (orden == null) return;
        List<DetalleOrdenCompra> detalles = ordenRepo.buscarDetalles(ordenId);
        for (DetalleOrdenCompra d : detalles) {
            Producto producto = productoRepo.buscarPorId(d.getProductoId());
            if (producto == null) continue;
            int stockAnterior = producto.getStockActual();
            producto.aumentarStock(d.getCantidad());
            productoRepo.actualizar(producto);
            movimientoRepo.guardar(new MovimientoInventario(
                    0, producto.getId(), producto.getNombre(),
                    MovimientoInventario.TipoMovimiento.ENTRADA,
                    d.getCantidad(), stockAnterior,
                    "Recepción OC #" + ordenId, "OC-" + ordenId,
                    LocalDateTime.now(), usuarioId));
        }
        orden.setEstado(OrdenCompra.EstadoOrden.RECIBIDA);
        ordenRepo.actualizar(orden);
    }

    public void cancelarOrden(int ordenId) {
        OrdenCompra orden = ordenRepo.buscarPorId(ordenId);
        if (orden != null) { orden.setEstado(OrdenCompra.EstadoOrden.CANCELADA); ordenRepo.actualizar(orden); }
    }
}