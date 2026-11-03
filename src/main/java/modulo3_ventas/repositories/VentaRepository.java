package modulo3_ventas.repositories;

import modulo3_ventas.models.Venta;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrato del repositorio de ventas.
 * RF: Registro y gestión de ventas.
 */
public interface VentaRepository {
    Venta       buscarPorId(int id);
    List<Venta> buscarTodas();
    List<Venta> buscarPorCliente(int clienteId);
    List<Venta> buscarPorPeriodo(LocalDate inicio, LocalDate fin);
    List<Venta> buscarPorEstado(Venta.EstadoVenta estado);
    void        guardar(Venta venta);
    void        actualizar(Venta venta);
    void        eliminar(int id);
}
