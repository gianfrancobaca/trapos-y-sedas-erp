package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.models.Notificacion;
import modulo2_gestionClientes.repositories.NotificacionRepository;

import java.util.List;

public class NotificacionController {

    private NotificacionRepository repository;

    public NotificacionController() {
        this.repository = new NotificacionRepository();
    }

    public void enviar(Notificacion notificacion) {
        repository.enviar(notificacion);
    }

    public void eliminar(int idNotificacion) {
        repository.eliminar(idNotificacion);
    }

    public void marcarComoLeida(int idNotificacion) {
        repository.marcarComoLeida(idNotificacion);
    }

    public Notificacion buscarPorId(int idNotificacion) {
        return repository.buscarPorId(idNotificacion);
    }

    public List<Notificacion> listarTodos() {
        return repository.listarTodos();
    }

    public List<Notificacion> listarPorCliente(int idCliente) {
        return repository.listarPorCliente(idCliente);
    }

    public List<Notificacion> listarNoLeidas(int idCliente) {
        return repository.listarNoLeidas(idCliente);
    }
}