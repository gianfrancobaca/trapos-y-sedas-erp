package modulo2_gestionClientes.interfaces;

import modulo2_gestionClientes.models.CategoriaCliente;
import java.util.List;

public interface ICategoriaClienteRepository {
    void agregar(CategoriaCliente categoria);
    void actualizar(CategoriaCliente categoria);
    void eliminar(int idCategoria);
    CategoriaCliente buscarPorId(int idCategoria);
    List<CategoriaCliente> listarTodos();
}
