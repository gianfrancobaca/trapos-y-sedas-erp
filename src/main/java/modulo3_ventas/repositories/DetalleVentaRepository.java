package modulo3_ventas.repositories;

import modulo3_ventas.models.DetalleVenta;
import java.sql.Connection;
import java.sql.SQLException;
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
    /** Inserta usando la conexión dada y propaga el error, para participar en una transacción. */
    void               guardar(DetalleVenta detalle, Connection conexion) throws SQLException;
    void               actualizar(DetalleVenta detalle);
    void               eliminar(int id);
    void               eliminarPorVenta(int ventaId);
}
