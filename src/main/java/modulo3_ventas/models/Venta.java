package modulo3_ventas.models;

import java.time.LocalDateTime;

/**
 * Representa una transacción de venta realizada en el sistema.
 * RF: Registro y gestión de ventas.
 */
public class Venta {

    public enum EstadoVenta  { PENDIENTE, COMPLETADA, CANCELADA, DEVUELTA }
    public enum MetodoPago   { EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA, YAPE, PLIN }

    private int            id;
    private int            clienteId;
    private int            vendedorId;
    private LocalDateTime  fechaHora;
    private EstadoVenta    estado;
    private MetodoPago     metodoPago;
    private double         subtotal;
    private double         descuento;
    private double         igv;
    private double         total;
    private String         observaciones;
    private String         motivoCancelacion;

    public Venta() {}

    public Venta(int id, int clienteId, int vendedorId, LocalDateTime fechaHora,
                 EstadoVenta estado, MetodoPago metodoPago,
                 double subtotal, double descuento, double igv, double total) {
        this.id          = id;
        this.clienteId   = clienteId;
        this.vendedorId  = vendedorId;
        this.fechaHora   = fechaHora;
        this.estado      = estado;
        this.metodoPago  = metodoPago;
        this.subtotal    = subtotal;
        this.descuento   = descuento;
        this.igv         = igv;
        this.total       = total;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public int getClienteId()                       { return clienteId; }
    public void setClienteId(int c)                 { this.clienteId = c; }

    public int getVendedorId()                      { return vendedorId; }
    public void setVendedorId(int v)                { this.vendedorId = v; }

    public LocalDateTime getFechaHora()             { return fechaHora; }
    public void setFechaHora(LocalDateTime f)       { this.fechaHora = f; }

    public EstadoVenta getEstado()                  { return estado; }
    public void setEstado(EstadoVenta e)             { this.estado = e; }

    public MetodoPago getMetodoPago()               { return metodoPago; }
    public void setMetodoPago(MetodoPago m)          { this.metodoPago = m; }

    public double getSubtotal()                     { return subtotal; }
    public void setSubtotal(double s)               { this.subtotal = s; }

    public double getDescuento()                    { return descuento; }
    public void setDescuento(double d)              { this.descuento = d; }

    public double getIgv()                          { return igv; }
    public void setIgv(double i)                    { this.igv = i; }

    public double getTotal()                        { return total; }
    public void setTotal(double t)                  { this.total = t; }

    public String getObservaciones()                { return observaciones; }
    public void setObservaciones(String o)          { this.observaciones = o; }

    public String getMotivoCancelacion()            { return motivoCancelacion; }
    public void setMotivoCancelacion(String m)      { this.motivoCancelacion = m; }

    @Override
    public String toString() {
        return String.format("Venta[id=%d | cliente=%d | total=S/ %.2f | estado=%s]",
                id, clienteId, total, estado);
    }
}
