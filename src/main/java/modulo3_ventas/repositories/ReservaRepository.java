package modulo3_ventas.repositories;

import modulo3_ventas.models.Reserva;
import java.util.List;

/**
 * Contrato del repositorio de reservas.
 * RF-02: Reserva de productos.
 */
public interface ReservaRepository {
    Reserva       buscarPorId(int id);
    List<Reserva> buscarPorCliente(int clienteId);
    List<Reserva> buscarPorProducto(int productoId);
    List<Reserva> buscarActivas();
    List<Reserva> buscarVencidas();
    void          guardar(Reserva reserva);
    void          actualizar(Reserva reserva);
    void          eliminar(int id);
}
