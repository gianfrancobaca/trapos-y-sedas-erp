package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.models.ProgramaFidelizacion;
import modulo2_gestionClientes.repositories.HistorialRepository;

import java.util.List;

public class FidelizacionController {

    private HistorialRepository repository;

    public FidelizacionController() {
        this.repository = new HistorialRepository();
    }

    public void agregar(ProgramaFidelizacion programa) {
        repository.agregar(programa);
    }

    public void actualizar(ProgramaFidelizacion programa) {
        repository.actualizar(programa);
    }

    public void eliminar(int idPrograma) {
        repository.eliminar(idPrograma);
    }

    public List<ProgramaFidelizacion> listarPorCliente(int idCliente) {
        return repository.listarPorCliente(idCliente);
    }

    public List<ProgramaFidelizacion> listarTodos() {
        return repository.listarTodos();
    }
}