package modulo1_inventario.repositories;

import modulo1_inventario.models.DetalleOrdenCompra;
import modulo1_inventario.models.OrdenCompra;
import java.util.List;

public interface OrdenCompraRepository {
    OrdenCompra            buscarPorId(int id);
    List<OrdenCompra>      buscarTodas();
    List<OrdenCompra>      buscarPorProveedor(int proveedorId);
    List<OrdenCompra>      buscarPorEstado(OrdenCompra.EstadoOrden estado);
    List<DetalleOrdenCompra> buscarDetalles(int ordenId);
    void                   guardar(OrdenCompra orden);
    void                   guardarDetalle(DetalleOrdenCompra detalle);
    void                   actualizar(OrdenCompra orden);
    void                   eliminar(int id);
}