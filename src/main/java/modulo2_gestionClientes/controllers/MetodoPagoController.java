package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.models.MetodoPago;
import modulo2_gestionClientes.repositories.MetodoPagoRepository;

import java.util.List;

public class MetodoPagoController {

    private MetodoPagoRepository repository;

    public MetodoPagoController() {
        this.repository = new MetodoPagoRepository();
    }

    public void agregar(MetodoPago metodoPago) {
        repository.agregar(metodoPago);
    }

    public void actualizar(MetodoPago metodoPago) {
        repository.actualizar(metodoPago);
    }

    public void eliminar(int idMetodoPago) {
        repository.eliminar(idMetodoPago);
    }

    public MetodoPago buscarPorId(int idMetodoPago) {
        return repository.buscarPorId(idMetodoPago);
    }

    public List<MetodoPago> listarTodos() {
        return repository.listarTodos();
    }

    public List<MetodoPago> listarPorCliente(int idCliente) {
        return repository.listarPorCliente(idCliente);
    }
}