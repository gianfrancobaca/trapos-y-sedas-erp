package modulo1_inventario.repositories;

import modulo1_inventario.models.MovimientoInventario;
import java.time.LocalDate;
import java.util.List;

public interface MovimientoInventarioRepository {
    MovimientoInventario       buscarPorId(int id);
    List<MovimientoInventario> buscarPorProducto(int productoId);
    List<MovimientoInventario> buscarPorTipo(MovimientoInventario.TipoMovimiento tipo);
    List<MovimientoInventario> buscarPorPeriodo(LocalDate inicio, LocalDate fin);
    List<MovimientoInventario> buscarTodos();
    void                       guardar(MovimientoInventario movimiento);
}