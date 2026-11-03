package modulo1_inventario.repositories;

import modulo1_inventario.models.Categoria;
import java.util.List;

public interface CategoriaRepository {
    Categoria       buscarPorId(int id);
    List<Categoria> buscarTodas();
    List<Categoria> buscarActivas();
    void            guardar(Categoria categoria);
    void            actualizar(Categoria categoria);
    void            eliminar(int id);
}