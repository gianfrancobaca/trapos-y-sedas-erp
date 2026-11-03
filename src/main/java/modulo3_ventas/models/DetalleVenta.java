package modulo3_ventas.models;

/**
 * Línea de detalle de una venta: producto, cantidad y precio.
 * RF: Registro y gestión de ventas.
 */
public class DetalleVenta {

    private int    id;
    private int    ventaId;
    private int    productoId;
    private String productoNombre;
    private int    cantidad;
    private double precioUnitario;
    private double descuentoLinea;
    private double subtotal;

    public DetalleVenta() {}

    public DetalleVenta(int id, int ventaId, int productoId, String productoNombre,
                        int cantidad, double precioUnitario, double descuentoLinea) {
        this.id              = id;
        this.ventaId         = ventaId;
        this.productoId      = productoId;
        this.productoNombre  = productoNombre;
        this.cantidad        = cantidad;
        this.precioUnitario  = precioUnitario;
        this.descuentoLinea  = descuentoLinea;
        this.subtotal        = (precioUnitario * cantidad) - descuentoLinea;
    }

    public void calcularSubtotal() {
        this.subtotal = (precioUnitario * cantidad) - descuentoLinea;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int getId()                                 { return id; }
    public void setId(int id)                          { this.id = id; }

    public int getVentaId()                            { return ventaId; }
    public void setVentaId(int v)                      { this.ventaId = v; }

    public int getProductoId()                         { return productoId; }
    public void setProductoId(int p)                   { this.productoId = p; }

    public String getProductoNombre()                  { return productoNombre; }
    public void setProductoNombre(String n)            { this.productoNombre = n; }

    public int getCantidad()                           { return cantidad; }
    public void setCantidad(int c)                     { this.cantidad = c; }

    public double getPrecioUnitario()                  { return precioUnitario; }
    public void setPrecioUnitario(double p)            { this.precioUnitario = p; }

    public double getDescuentoLinea()                  { return descuentoLinea; }
    public void setDescuentoLinea(double d)            { this.descuentoLinea = d; }

    public double getSubtotal()                        { return subtotal; }
    public void setSubtotal(double s)                  { this.subtotal = s; }

    @Override
    public String toString() {
        return String.format("DetalleVenta[ventaId=%d | producto=%s | cant=%d | subtotal=S/ %.2f]",
                ventaId, productoNombre, cantidad, subtotal);
    }
}
