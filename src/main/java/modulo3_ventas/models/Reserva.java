package modulo3_ventas.models;

import java.time.LocalDateTime;

/**
 * Reserva de un producto para un cliente por un período determinado.
 * RF-02: Reserva de productos.
 */
public class Reserva {

    public enum EstadoReserva { ACTIVA, CONFIRMADA, VENCIDA, CANCELADA }

    private int            id;
    private int            clienteId;
    private int            productoId;
    private LocalDateTime  fechaReserva;
    private LocalDateTime  fechaExpiracion;
    private EstadoReserva  estado;
    private String         observaciones;

    public Reserva() {}

    public Reserva(int id, int clienteId, int productoId,
                   LocalDateTime fechaReserva, LocalDateTime fechaExpiracion) {
        this.id              = id;
        this.clienteId       = clienteId;
        this.productoId      = productoId;
        this.fechaReserva    = fechaReserva;
        this.fechaExpiracion = fechaExpiracion;
        this.estado          = EstadoReserva.ACTIVA;
    }

    public boolean estaVencida(LocalDateTime ahora) {
        return estado == EstadoReserva.ACTIVA && ahora.isAfter(fechaExpiracion);
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int getId()                                  { return id; }
    public void setId(int id)                           { this.id = id; }

    public int getClienteId()                           { return clienteId; }
    public void setClienteId(int c)                     { this.clienteId = c; }

    public int getProductoId()                          { return productoId; }
    public void setProductoId(int p)                    { this.productoId = p; }

    public LocalDateTime getFechaReserva()              { return fechaReserva; }
    public void setFechaReserva(LocalDateTime f)        { this.fechaReserva = f; }

    public LocalDateTime getFechaExpiracion()           { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime f)     { this.fechaExpiracion = f; }

    public EstadoReserva getEstado()                    { return estado; }
    public void setEstado(EstadoReserva e)              { this.estado = e; }

    public String getObservaciones()                    { return observaciones; }
    public void setObservaciones(String o)              { this.observaciones = o; }

    @Override
    public String toString() {
        return String.format("Reserva[id=%d | cliente=%d | producto=%d | %s | vence=%s]",
                id, clienteId, productoId, estado, fechaExpiracion);
    }
}
