package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.Patrones.NotificacionFactory;
import modulo2_gestionClientes.Patrones.NotificadorCliente;
import modulo2_gestionClientes.models.Reclamo;
import modulo2_gestionClientes.repositories.NotificacionRepository;
import modulo2_gestionClientes.repositories.ReclamoRepository;

import java.util.List;

public class ReclamoController {

    private ReclamoRepository repository;
    private NotificacionRepository notificacionRepository;

    public ReclamoController() {
        this.repository = new ReclamoRepository();
        this.notificacionRepository = new NotificacionRepository();
    }

    public void agregar(Reclamo reclamo) {
        repository.agregar(reclamo);

        notificacionRepository.enviar(
                NotificacionFactory.crear("RECLAMO", reclamo.getCliente())
        );

        NotificadorCliente.getInstance().notificar("RECLAMO_REGISTRADO", reclamo);
    }

    public void actualizar(Reclamo reclamo) {
        repository.actualizar(reclamo);
    }

    public void eliminar(int idReclamo) {
        repository.eliminar(idReclamo);
    }

    public List<Reclamo> listarPorCliente(int idCliente) {
        return repository.listarPorCliente(idCliente);
    }

    public List<Reclamo> listarTodos() {
        return repository.listarTodos();
    }
}