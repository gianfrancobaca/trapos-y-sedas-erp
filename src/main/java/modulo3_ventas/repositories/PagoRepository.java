package modulo3_ventas.repositories;

import modulo3_ventas.models.Pago;
import java.util.List;

/**
 * Contrato del repositorio de pagos.
 * RF: Procesamiento de pagos en ventas.
 */
public interface PagoRepository {
    Pago       buscarPorId(int id);
    List<Pago> buscarPorVenta(int ventaId);
    List<Pago> buscarPorEstado(Pago.EstadoPago estado);
    void       guardar(Pago pago);
    void       actualizar(Pago pago);
    void       eliminar(int id);
}
