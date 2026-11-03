package modulo3_ventas.models;

/**
 * Producto disponible para la venta en el catálogo.
 * RF: Gestión de catálogo de productos en ventas.
 */
public class Producto {

    public enum CategoriaProducto { VESTIDO, BLUSA, PANTALON, FALDA, ACCESORIO, CALZADO, OTRO }
    public enum EstadoProducto    { DISPONIBLE, RESERVADO, VENDIDO }

    private int               id;
    private String            codigo;
    private String            nombre;
    private String            descripcion;
    private CategoriaProducto categoria;
    private double            precioVenta;
    private int               stockDisponible;
    private EstadoProducto    estado;
    private boolean           activo;

    public Producto() {}

    public Producto(int id, String codigo, String nombre, String descripcion,
                    CategoriaProducto categoria, double precioVenta, int stockDisponible) {
        this.id              = id;
        this.codigo          = codigo;
        this.nombre          = nombre;
        this.descripcion     = descripcion;
        this.categoria       = categoria;
        this.precioVenta     = precioVenta;
        this.stockDisponible = stockDisponible;
        this.estado          = EstadoProducto.DISPONIBLE;
        this.activo          = true;
    }

    public boolean tieneStock(int cantidad) {
        return stockDisponible >= cantidad && estado == EstadoProducto.DISPONIBLE;
    }

    public void reducirStock(int cantidad) {
        if (tieneStock(cantidad)) this.stockDisponible -= cantidad;
    }

    public boolean estaDisponible() {
        return estado == EstadoProducto.DISPONIBLE && activo;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int getId()                                    { return id; }
    public void setId(int id)                             { this.id = id; }

    public String getCodigo()                             { return codigo; }
    public void setCodigo(String c)                       { this.codigo = c; }

    public String getNombre()                             { return nombre; }
    public void setNombre(String n)                       { this.nombre = n; }

    public String getDescripcion()                        { return descripcion; }
    public void setDescripcion(String d)                  { this.descripcion = d; }

    public CategoriaProducto getCategoria()               { return categoria; }
    public void setCategoria(CategoriaProducto c)         { this.categoria = c; }

    public double getPrecioVenta()                        { return precioVenta; }
    public void setPrecioVenta(double p)                  { this.precioVenta = p; }

    public int getStockDisponible()                       { return stockDisponible; }
    public void setStockDisponible(int s)                 { this.stockDisponible = s; }

    public EstadoProducto getEstado()                     { return estado; }
    public void setEstado(EstadoProducto e)               { this.estado = e; }

    public boolean isActivo()                             { return activo; }
    public void setActivo(boolean a)                      { this.activo = a; }

    @Override
    public String toString() {
        return String.format("Producto[%s | %s | S/ %.2f | stock=%d | %s]",
                codigo, nombre, precioVenta, stockDisponible, estado);
    }
}
