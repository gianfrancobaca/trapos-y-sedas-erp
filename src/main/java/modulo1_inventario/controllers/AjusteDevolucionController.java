package modulo1_inventario.controllers;

import core.Controller;
import modulo1_inventario.models.*;
import modulo1_inventario.repositories.*;
import java.time.LocalDateTime;
import java.util.List;

public class AjusteDevolucionController extends Controller {

    private final AjusteInventarioRepository     ajusteRepo;
    private final DevolucionRepository           devolucionRepo;
    private final ProductoRepository             productoRepo;
    private final MovimientoInventarioRepository movimientoRepo;

    public AjusteDevolucionController() {
        this.ajusteRepo     = new AjusteInventarioRepositorySQL();
        this.devolucionRepo = new DevolucionRepositorySQL();
        this.productoRepo   = new ProductoRepositorySQL();
        this.movimientoRepo = new MovimientoInventarioRepositorySQL();
    }

    @Override public void run() {}

    public void registrarAjuste(AjusteInventario ajuste, List<DetalleAjuste> detalles, int usuarioId) {
        ajusteRepo.guardar(ajuste);
        for (DetalleAjuste d : detalles) {
            d.setAjusteId(ajuste.getId());
            ajusteRepo.guardarDetalle(d);
            Producto producto = productoRepo.buscarPorId(d.getProductoId());
            if (producto == null) continue;
            int stockAnterior = producto.getStockActual();
            producto.setStockActual(d.getStockNuevo());
            productoRepo.actualizar(producto);
            movimientoRepo.guardar(new MovimientoInventario(
                    0, producto.getId(), producto.getNombre(),
                    MovimientoInventario.TipoMovimiento.AJUSTE,
                    d.getStockNuevo(), stockAnterior,
                    "Ajuste #" + ajuste.getId(), null,
                    LocalDateTime.now(), usuarioId));
        }
    }

    public void registrarDevolucionCliente(Devolucion devolucion,
                                           List<DetalleDevolucion> detalles, int usuarioId) {
        devolucionRepo.guardar(devolucion);
        for (DetalleDevolucion dd : detalles) {
            dd.setDevolucionId(devolucion.getId());
            devolucionRepo.guardarDetalle(dd);
            Producto producto = productoRepo.buscarPorId(dd.getProductoId());
            if (producto == null) continue;
            int stockAnterior = producto.getStockActual();
            producto.aumentarStock(dd.getCantidad());
            productoRepo.actualizar(producto);
            movimientoRepo.guardar(new MovimientoInventario(
                    0, producto.getId(), producto.getNombre(),
                    MovimientoInventario.TipoMovimiento.DEVOLUCION,
                    dd.getCantidad(), stockAnterior,
                    "Devolución cliente #" + devolucion.getId(), null,
                    LocalDateTime.now(), usuarioId));
        }
        devolucionRepo.actualizarEstado(devolucion.getId(), Devolucion.EstadoDevolucion.COMPLETADA);
    }

    public void registrarDevolucionProveedor(Devolucion devolucion,
                                             List<DetalleDevolucion> detalles, int usuarioId) {
        devolucionRepo.guardar(devolucion);
        for (DetalleDevolucion dd : detalles) {
            dd.setDevolucionId(devolucion.getId());
            devolucionRepo.guardarDetalle(dd);
            Producto producto = productoRepo.buscarPorId(dd.getProductoId());
            if (producto == null) continue;
            if (!producto.tieneStockSuficiente(dd.getCantidad()))
                throw new IllegalArgumentException("Stock insuficiente. Disponible: "
                        + producto.getStockActual() + ", Solicitado: " + dd.getCantidad());
            int stockAnterior = producto.getStockActual();
            producto.reducirStock(dd.getCantidad());
            productoRepo.actualizar(producto);
            movimientoRepo.guardar(new MovimientoInventario(
                    0, producto.getId(), producto.getNombre(),
                    MovimientoInventario.TipoMovimiento.SALIDA,
                    dd.getCantidad(), stockAnterior,
                    "Devolución proveedor #" + devolucion.getId(), null,
                    LocalDateTime.now(), usuarioId));
        }
        devolucionRepo.actualizarEstado(devolucion.getId(), Devolucion.EstadoDevolucion.COMPLETADA);
    }

    public List<AjusteInventario>  obtenerAjustes()                     { return ajusteRepo.buscarTodos(); }
    public List<DetalleAjuste>     obtenerDetallesAjuste(int ajusteId)  { return ajusteRepo.buscarDetalles(ajusteId); }
    public List<Devolucion>        obtenerDevoluciones()                 { return devolucionRepo.buscarTodas(); }
    public List<DetalleDevolucion> obtenerDetallesDevolucion(int id)     { return devolucionRepo.buscarDetalles(id); }
}