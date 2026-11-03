package modulo3_ventas.repositories;

import modulo3_ventas.models.Producto;
import java.util.List;

/**
 * Contrato del repositorio de productos del catálogo de ventas.
 * RF: Gestión de catálogo de productos en ventas.
 */
public interface ProductoRepository {
    Producto       buscarPorId(int id);
    Producto       buscarPorCodigo(String codigo);
    List<Producto> buscarTodos();
    List<Producto> buscarActivos();
    List<Producto> buscarPorCategoria(Producto.CategoriaProducto categoria);
    List<Producto> buscarConStock();
    void           guardar(Producto producto);
    void           actualizar(Producto producto);
    void           eliminar(int id);
}
