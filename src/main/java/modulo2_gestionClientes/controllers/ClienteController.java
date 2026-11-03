package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.Patrones.NotificadorCliente;
import modulo2_gestionClientes.repositories.ClienteRepository;

import java.util.List;

public class ClienteController {

    private ClienteRepository repository;
    private NotificadorCliente notificador;

    public ClienteController() {
        this.repository = new ClienteRepository();
        this.notificador = NotificadorCliente.getInstance();
    }

    public void agregar(Cliente cliente) {
        repository.agregar(cliente);
        notificador.notificar("CLIENTE_REGISTRADO", cliente);
    }

    public void actualizar(Cliente cliente) {
        repository.actualizar(cliente);
        notificador.notificar("CLIENTE_ACTUALIZADO", cliente);
    }

    public void eliminar(int idCliente) {
        repository.eliminar(idCliente);
        notificador.notificar("CLIENTE_ELIMINADO", idCliente);
    }

    public Cliente buscarPorId(int idCliente) {
        return repository.buscarPorId(idCliente);
    }

    public List<Cliente> listarTodos() {
        return repository.listarTodos();
    }
}

