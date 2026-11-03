package modulo3_ventas.models;

/**
 * Representa un producto agregado al carrito antes de confirmar la venta.
 * Permite acumular items y calcular subtotales antes de registrar la venta.
 */
public class ItemCarrito {

    private int    productoId;
    private String productoNombre;
    private int    cantidad;
    private double precioUnitario;
    private double descuentoLinea;

    public ItemCarrito() {}

    public ItemCarrito(int productoId, String productoNombre, int cantidad, double precioUnitario) {
        this.productoId      = productoId;
        this.productoNombre  = productoNombre;
        this.cantidad        = cantidad;
        this.precioUnitario  = precioUnitario;
        this.descuentoLinea  = 0;
    }

    /** Calcula el subtotal de esta linea: (precio - descuento) * cantidad. */
    public double calcularSubtotal() {
        return (precioUnitario - descuentoLinea) * cantidad;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int    getProductoId()                       { return productoId; }
    public void   setProductoId(int id)                 { this.productoId = id; }

    public String getProductoNombre()                   { return productoNombre; }
    public void   setProductoNombre(String n)           { this.productoNombre = n; }

    public int    getCantidad()                         { return cantidad; }
    public void   setCantidad(int c)                    { this.cantidad = c; }

    public double getPrecioUnitario()                   { return precioUnitario; }
    public void   setPrecioUnitario(double p)           { this.precioUnitario = p; }

    public double getDescuentoLinea()                   { return descuentoLinea; }
    public void   setDescuentoLinea(double d)           { this.descuentoLinea = d; }

    @Override
    public String toString() {
        return String.format("ItemCarrito[%s x%d | S/ %.2f]", productoNombre, cantidad, calcularSubtotal());
    }
}
