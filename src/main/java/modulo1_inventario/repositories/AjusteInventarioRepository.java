package modulo1_inventario.repositories;

import modulo1_inventario.models.AjusteInventario;
import modulo1_inventario.models.DetalleAjuste;
import java.util.List;

public interface AjusteInventarioRepository {
    AjusteInventario    buscarPorId(int id);
    List<AjusteInventario> buscarTodos();
    List<DetalleAjuste> buscarDetalles(int ajusteId);
    void                guardar(AjusteInventario ajuste);
    void                guardarDetalle(DetalleAjuste detalle);
}