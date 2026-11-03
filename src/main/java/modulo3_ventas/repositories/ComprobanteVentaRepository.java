package modulo3_ventas.repositories;

import modulo3_ventas.models.ComprobanteVenta;
import java.util.List;

/**
 * Contrato del repositorio de comprobantes de venta.
 * RF: Emisión de comprobantes en ventas.
 */
public interface ComprobanteVentaRepository {
    ComprobanteVenta       buscarPorId(int id);
    ComprobanteVenta       buscarPorVenta(int ventaId);
    List<ComprobanteVenta> buscarTodos();
    List<ComprobanteVenta> buscarPorTipo(ComprobanteVenta.TipoComprobante tipo);
    void                   guardar(ComprobanteVenta comprobante);
    void                   actualizar(ComprobanteVenta comprobante);
    void                   anular(int id);
}
