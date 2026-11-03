package modulo3_ventas.patrones;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del patron Observer para eventos del modulo de ventas.
 * Notifica a los suscriptores cuando ocurre una venta, reserva o cancelacion.
 */
public class NotificadorVentas {

    /** Tipos de evento que pueden notificarse. */
    public enum EventoVenta { VENTA_REGISTRADA, VENTA_CANCELADA, RESERVA_CREADA, RESERVA_VENCIDA }

    /** Interfaz que deben implementar los observadores. */
    public interface ObservadorVenta {
        void onEvento(EventoVenta evento, String detalle);
    }

    private final List<ObservadorVenta> observadores = new ArrayList<>();

    public void suscribir(ObservadorVenta observador) {
        observadores.add(observador);
    }

    public void desuscribir(ObservadorVenta observador) {
        observadores.remove(observador);
    }

    /** Notifica a todos los observadores registrados con el evento ocurrido. */
    public void notificar(EventoVenta evento, String detalle) {
        for (ObservadorVenta obs : observadores) {
            obs.onEvento(evento, detalle);
        }
    }
}
