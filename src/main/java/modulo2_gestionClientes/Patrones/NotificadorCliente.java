package modulo2_gestionClientes.Patrones;

import java.util.ArrayList;
import java.util.List;

public class NotificadorCliente {

    private static NotificadorCliente instance;
    private List<ClienteObserver> observadores;

    private NotificadorCliente() {
        this.observadores = new ArrayList<>();
    }

    public static NotificadorCliente getInstance() {
        if (instance == null) {
            instance = new NotificadorCliente();
        }
        return instance;
    }

    public void suscribir(ClienteObserver observer) {
        observadores.add(observer);
    }

    public void desuscribir(ClienteObserver observer) {
        observadores.remove(observer);
    }

    public void notificar(String evento, Object dato) {
        for (ClienteObserver observer : observadores) {
            observer.actualizar(evento, dato);
        }
    }
}

