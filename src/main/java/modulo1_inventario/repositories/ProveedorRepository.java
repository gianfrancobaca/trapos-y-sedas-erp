package modulo1_inventario.repositories;

import modulo1_inventario.models.Proveedor;
import java.util.List;

public interface ProveedorRepository {
    Proveedor       buscarPorId(int id);
    Proveedor       buscarPorNumeroDocumento(String numeroDocumento);
    List<Proveedor> buscarTodos();
    List<Proveedor> buscarActivos();
    void            guardar(Proveedor proveedor);
    void            actualizar(Proveedor proveedor);
    void            eliminar(int id);
}