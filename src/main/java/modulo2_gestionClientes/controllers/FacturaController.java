package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.models.Factura;
import modulo2_gestionClientes.repositories.FacturaRepository;

import java.util.List;

public class FacturaController {

    private FacturaRepository repository;

    public FacturaController() {
        this.repository = new FacturaRepository();
    }

    public void agregar(Factura factura) {
        repository.agregar(factura);
    }

    public void actualizar(Factura factura) {
        repository.actualizar(factura);
    }

    public void eliminar(int idFactura) {
        repository.eliminar(idFactura);
    }

    public Factura buscarPorId(int idFactura) {
        return repository.buscarPorId(idFactura);
    }

    public List<Factura> listarTodos() {
        return repository.listarTodos();
    }
}
