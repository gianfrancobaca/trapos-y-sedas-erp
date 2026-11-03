package modulo3_ventas.repositories;

import modulo3_ventas.models.Venta;
import java.sql.Connection;
import java.sql.SQLException;
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
    /** Inserta usando la conexión dada y propaga el error, para participar en una transacción. */
    void        guardar(Venta venta, Connection conexion) throws SQLException;
    void        actualizar(Venta venta);
    void        eliminar(int id);
}
