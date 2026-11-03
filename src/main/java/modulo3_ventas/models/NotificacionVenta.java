package modulo3_ventas.models;

import java.time.LocalDateTime;

/**
 * Notificación generada al completarse una venta o reserva.
 * RF-09: Notificación de confirmación al cliente.
 */
public class NotificacionVenta {

    public enum TipoNotificacion { CONFIRMACION_VENTA, CONFIRMACION_RESERVA, CANCELACION, VENCIMIENTO_RESERVA }
    public enum EstadoNotificacion { PENDIENTE, ENVIADA, FALLIDA }

    private int                id;
    private int                clienteId;
    private int                referenciaId;
    private TipoNotificacion   tipo;
    private String             mensaje;
    private double             montoTotal;
    private LocalDateTime      fechaGeneracion;
    private EstadoNotificacion estado;

    public NotificacionVenta() {}

    public NotificacionVenta(int id, int clienteId, int referenciaId,
                             TipoNotificacion tipo, String mensaje,
                             double montoTotal, LocalDateTime fechaGeneracion) {
        this.id              = id;
        this.clienteId       = clienteId;
        this.referenciaId    = referenciaId;
        this.tipo            = tipo;
        this.mensaje         = mensaje;
        this.montoTotal      = montoTotal;
        this.fechaGeneracion = fechaGeneracion;
        this.estado          = EstadoNotificacion.PENDIENTE;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int getId()                                        { return id; }
    public void setId(int id)                                 { this.id = id; }

    public int getClienteId()                                 { return clienteId; }
    public void setClienteId(int c)                           { this.clienteId = c; }

    public int getReferenciaId()                              { return referenciaId; }
    public void setReferenciaId(int r)                        { this.referenciaId = r; }

    public TipoNotificacion getTipo()                         { return tipo; }
    public void setTipo(TipoNotificacion t)                   { this.tipo = t; }

    public String getMensaje()                                { return mensaje; }
    public void setMensaje(String m)                          { this.mensaje = m; }

    public double getMontoTotal()                             { return montoTotal; }
    public void setMontoTotal(double m)                       { this.montoTotal = m; }

    public LocalDateTime getFechaGeneracion()                 { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime f)           { this.fechaGeneracion = f; }

    public EstadoNotificacion getEstado()                     { return estado; }
    public void setEstado(EstadoNotificacion e)               { this.estado = e; }

    @Override
    public String toString() {
        return String.format("Notificacion[cliente=%d | %s | S/ %.2f | %s]",
                clienteId, tipo, montoTotal, estado);
    }
}
