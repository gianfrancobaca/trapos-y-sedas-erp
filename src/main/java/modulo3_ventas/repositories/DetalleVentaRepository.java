package modulo3_ventas.repositories;

import modulo3_ventas.models.DetalleVenta;
import java.util.List;

/**
 * Contrato del repositorio de detalles de venta.
 * RF: Registro y gestión de ventas.
 */
public interface DetalleVentaRepository {
    DetalleVenta       buscarPorId(int id);
    List<DetalleVenta> buscarPorVenta(int ventaId);
    List<DetalleVenta> buscarPorProducto(int productoId);
    void               guardar(DetalleVenta detalle);
    void               actualizar(DetalleVenta detalle);
    void               eliminar(int id);
    void               eliminarPorVenta(int ventaId);
}
