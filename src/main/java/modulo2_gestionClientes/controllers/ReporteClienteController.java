package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.repositories.ClienteRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ReporteClienteController {

    private ClienteRepository repository;

    public ReporteClienteController() {
        this.repository = new ClienteRepository();
    }

    public List<Cliente> reportePorCategoria(int idCategoria) {
        return repository.listarTodos().stream()
                .filter(c -> c.getCategoria() != null && c.getCategoria().getIdCategoria() == idCategoria)
                .collect(Collectors.toList());
    }

    public List<Cliente> reporteTodos() {
        return repository.listarTodos();
    }

    public String generarReporteTexto() {
        List<Cliente> clientes = repository.listarTodos();

        StringBuilder sb = new StringBuilder();
        sb.append("===== REPORTE DE CLIENTES =====\n\n");

        for (Cliente c : clientes) {
            sb.append("ID: ").append(c.getIdCliente()).append("\n");
            sb.append("Nombre: ").append(c.getNombre()).append(" ").append(c.getApellido()).append("\n");
            sb.append("Email: ").append(c.getEmail()).append("\n");
            sb.append("Categoría: ");

            if (c.getCategoria() != null) {
                sb.append(c.getCategoria().getNombre());
            } else {
                sb.append("Sin categoría");
            }

            sb.append("\n-----------------------------\n");
        }

        sb.append("\nTotal de clientes: ").append(clientes.size());

        return sb.toString();
    }
}