package modulo3_ventas.exceptions;

import java.time.LocalDateTime;

/**
 * Lanzada cuando se intenta confirmar una reserva que ya vencio.
 */
public class ReservaVencidaException extends RuntimeException {

    private final int           reservaId;
    private final LocalDateTime fechaExpiracion;

    public ReservaVencidaException(int reservaId, LocalDateTime fechaExpiracion) {
        super("La reserva " + reservaId + " vencio el " + fechaExpiracion);
        this.reservaId       = reservaId;
        this.fechaExpiracion = fechaExpiracion;
    }

    public int           getReservaId()       { return reservaId; }
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
}
