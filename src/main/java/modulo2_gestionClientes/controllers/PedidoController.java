package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.models.Pedido;
import modulo2_gestionClientes.repositories.PedidoRepository;

import java.util.List;

public class PedidoController {

    private PedidoRepository repository;

    public PedidoController() {
        this.repository = new PedidoRepository();
    }

    public void agregar(Pedido pedido) {
        repository.agregar(pedido);
    }

    public void actualizar(Pedido pedido) {
        repository.actualizar(pedido);
    }

    public void eliminar(int idPedido) {
        repository.eliminar(idPedido);
    }

    public Pedido buscarPorId(int idPedido) {
        return repository.buscarPorId(idPedido);
    }

    public List<Pedido> listarTodos() {
        return repository.listarTodos();
    }

    public List<Pedido> listarPorCliente(int idCliente) {
        return repository.listarPorCliente(idCliente);
    }
}
