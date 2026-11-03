package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.models.CategoriaCliente;
import modulo2_gestionClientes.Patrones.CategoriaClienteFactory;
import modulo2_gestionClientes.repositories.CategoriaClienteRepository;

import java.util.List;

public class CategoriaClienteController {

    private CategoriaClienteRepository repository;

    public CategoriaClienteController() {
        this.repository = new CategoriaClienteRepository();
    }

    // Mantiene el Factory
    public void agregar(String tipo) {
        CategoriaCliente categoria = CategoriaClienteFactory.crear(tipo);
        repository.agregar(categoria);
    }

    // Nuevo método para los paneles Swing
    public void agregar(CategoriaCliente categoria) {
        repository.agregar(categoria);
    }

    public void actualizar(CategoriaCliente categoria) {
        repository.actualizar(categoria);
    }

    public void eliminar(int idCategoria) {
        repository.eliminar(idCategoria);
    }

    public CategoriaCliente buscarPorId(int idCategoria) {
        return repository.buscarPorId(idCategoria);
    }

    public List<CategoriaCliente> listarTodos() {
        return repository.listarTodos();
    }
} 
