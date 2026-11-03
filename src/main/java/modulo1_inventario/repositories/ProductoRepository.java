package modulo1_inventario.repositories;

import modulo1_inventario.models.Producto;
import java.util.List;

public interface ProductoRepository {
    Producto       buscarPorId(int id);
    Producto       buscarPorCodigo(String codigo);
    List<Producto> buscarTodos();
    List<Producto> buscarActivos();
    List<Producto> buscarPorCategoria(int categoriaId);
    List<Producto> buscarConStockBajo();
    void           guardar(Producto producto);
    void           actualizar(Producto producto);
    void           eliminar(int id);
}