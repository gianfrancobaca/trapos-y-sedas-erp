package modulo2_gestionClientes.interfaces;

import modulo2_gestionClientes.models.Notificacion;
import java.util.List;

public interface INotificacion {
    void enviar(Notificacion notificacion);
    void marcarComoLeida(int idNotificacion);
    List<Notificacion> listarPorCliente(int idCliente);
    List<Notificacion> listarNoLeidas(int idCliente);
}