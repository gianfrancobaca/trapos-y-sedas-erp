package modulo3_ventas.models;

import java.time.LocalDateTime;

/**
 * Registro de un pago asociado a una venta.
 * RF: Procesamiento de pagos en ventas.
 */
public class Pago {

    public enum EstadoPago   { PENDIENTE, APROBADO, RECHAZADO, REEMBOLSADO }
    public enum MetodoPago   { EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA, YAPE, PLIN }

    private int            id;
    private int            ventaId;
    private double         monto;
    private MetodoPago     metodo;
    private EstadoPago     estado;
    private LocalDateTime  fechaPago;
    private String         referencia;

    public Pago() {}

    public Pago(int id, int ventaId, double monto, MetodoPago metodo,
                EstadoPago estado, LocalDateTime fechaPago, String referencia) {
        this.id         = id;
        this.ventaId    = ventaId;
        this.monto      = monto;
        this.metodo     = metodo;
        this.estado     = estado;
        this.fechaPago  = fechaPago;
        this.referencia = referencia;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int getId()                                { return id; }
    public void setId(int id)                         { this.id = id; }

    public int getVentaId()                           { return ventaId; }
    public void setVentaId(int v)                     { this.ventaId = v; }

    public double getMonto()                          { return monto; }
    public void setMonto(double m)                    { this.monto = m; }

    public MetodoPago getMetodo()                     { return metodo; }
    public void setMetodo(MetodoPago m)               { this.metodo = m; }

    public EstadoPago getEstado()                     { return estado; }
    public void setEstado(EstadoPago e)               { this.estado = e; }

    public LocalDateTime getFechaPago()               { return fechaPago; }
    public void setFechaPago(LocalDateTime f)         { this.fechaPago = f; }

    public String getReferencia()                     { return referencia; }
    public void setReferencia(String r)               { this.referencia = r; }

    @Override
    public String toString() {
        return String.format("Pago[ventaId=%d | S/ %.2f | %s | %s]",
                ventaId, monto, metodo, estado);
    }
}
