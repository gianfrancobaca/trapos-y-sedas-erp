package modulo2_gestionClientes.interfaces;

import modulo2_gestionClientes.models.Pedido;
import java.util.List;

public interface IPedidoRepository {
    void agregar(Pedido pedido);
    void actualizar(Pedido pedido);
    void eliminar(int idPedido);
    Pedido buscarPorId(int idPedido);
    List<Pedido> listarTodos();
    List<Pedido> listarPorCliente(int idCliente);
}
