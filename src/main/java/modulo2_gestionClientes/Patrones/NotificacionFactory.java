package modulo2_gestionClientes.Patrones;

import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.models.Notificacion;
import java.util.Date;

public class NotificacionFactory {

    public static Notificacion crear(String tipo, Cliente cliente) {
        switch (tipo.toUpperCase()) {
            case "BIENVENIDA":
                return new Notificacion(0, "Bienvenido a nuestro sistema, " + cliente.getNombre(), "BIENVENIDA", new Date(), false, cliente);
            case "PEDIDO":
                return new Notificacion(0, "Tu pedido ha sido registrado exitosamente", "PEDIDO", new Date(), false, cliente);
            case "PAGO":
                return new Notificacion(0, "Tu pago ha sido procesado correctamente", "PAGO", new Date(), false, cliente);
            case "RECLAMO":
                return new Notificacion(0, "Tu reclamo ha sido recibido y sera atendido pronto", "RECLAMO", new Date(), false, cliente);
            default:
                return new Notificacion(0, "Tienes una nueva notificacion", "GENERAL", new Date(), false, cliente);
        }
    }
}
