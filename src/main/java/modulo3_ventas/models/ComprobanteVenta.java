package modulo3_ventas.models;

import java.time.LocalDateTime;

/**
 * Comprobante de pago emitido al finalizar una venta (boleta o factura).
 * RF: Emisión de comprobantes en ventas.
 */
public class ComprobanteVenta {

    public enum TipoComprobante { BOLETA, FACTURA, NOTA_CREDITO }

    private int              id;
    private int              ventaId;
    private TipoComprobante  tipo;
    private String           serie;
    private int              numero;
    private LocalDateTime    fechaEmision;
    private double           montoTotal;
    private boolean          anulado;

    public ComprobanteVenta() {}

    public ComprobanteVenta(int id, int ventaId, TipoComprobante tipo,
                            String serie, int numero, LocalDateTime fechaEmision,
                            double montoTotal) {
        this.id           = id;
        this.ventaId      = ventaId;
        this.tipo         = tipo;
        this.serie        = serie;
        this.numero       = numero;
        this.fechaEmision = fechaEmision;
        this.montoTotal   = montoTotal;
        this.anulado      = false;
    }

    public String getNumeroCompleto() {
        return String.format("%s-%08d", serie, numero);
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int getId()                                     { return id; }
    public void setId(int id)                              { this.id = id; }

    public int getVentaId()                                { return ventaId; }
    public void setVentaId(int v)                          { this.ventaId = v; }

    public TipoComprobante getTipo()                       { return tipo; }
    public void setTipo(TipoComprobante t)                 { this.tipo = t; }

    public String getSerie()                               { return serie; }
    public void setSerie(String s)                         { this.serie = s; }

    public int getNumero()                                 { return numero; }
    public void setNumero(int n)                           { this.numero = n; }

    public LocalDateTime getFechaEmision()                 { return fechaEmision; }
    public void setFechaEmision(LocalDateTime f)           { this.fechaEmision = f; }

    public double getMontoTotal()                          { return montoTotal; }
    public void setMontoTotal(double m)                    { this.montoTotal = m; }

    public boolean isAnulado()                             { return anulado; }
    public void setAnulado(boolean a)                      { this.anulado = a; }

    @Override
    public String toString() {
        return String.format("Comprobante[%s %s | S/ %.2f | %s]",
                tipo, getNumeroCompleto(), montoTotal, fechaEmision);
    }
}
