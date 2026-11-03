package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class Producto implements Model {

    public enum EstadoProducto { DISPONIBLE, RESERVADO, VENDIDO, DESCONTINUADO }

    private int            id;
    private String         codigo;
    private String         nombre;
    private String         descripcion;
    private int            categoriaId;
    private String         categoriaNombre;
    private double         precioCompra;
    private double         precioVenta;
    private int            stockActual;
    private int            stockMinimo;
    private EstadoProducto estado;
    private boolean        activo;

    private final List<View> observers = new ArrayList<>();

    public Producto() {}

    public Producto(int id, String codigo, String nombre, String descripcion,
                    int categoriaId, String categoriaNombre,
                    double precioCompra, double precioVenta,
                    int stockActual, int stockMinimo) {
        this.id              = id;
        this.codigo          = codigo;
        this.nombre          = nombre;
        this.descripcion     = descripcion;
        this.categoriaId     = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.precioCompra    = precioCompra;
        this.precioVenta     = precioVenta;
        this.stockActual     = stockActual;
        this.stockMinimo     = stockMinimo;
        this.estado          = EstadoProducto.DISPONIBLE;
        this.activo          = true;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public boolean tieneStockSuficiente(int cantidad) { return stockActual >= cantidad; }
    public boolean estaEnStockMinimo()                { return stockActual <= stockMinimo; }
    public boolean estaDisponible()                   { return estado == EstadoProducto.DISPONIBLE && activo; }

    public void reducirStock(int cantidad) {
        if (tieneStockSuficiente(cantidad)) { this.stockActual -= cantidad; notifyViews(); }
    }
    public void aumentarStock(int cantidad) { this.stockActual += cantidad; notifyViews(); }

    public int            getId()                                   { return id; }
    public void           setId(int id)                             { this.id = id; }
    public String         getCodigo()                               { return codigo; }
    public void           setCodigo(String codigo)                  { this.codigo = codigo; }
    public String         getNombre()                               { return nombre; }
    public void           setNombre(String nombre)                  { this.nombre = nombre; }
    public String         getDescripcion()                          { return descripcion; }
    public void           setDescripcion(String descripcion)        { this.descripcion = descripcion; }
    public int            getCategoriaId()                          { return categoriaId; }
    public void           setCategoriaId(int categoriaId)           { this.categoriaId = categoriaId; }
    public String         getCategoriaNombre()                      { return categoriaNombre; }
    public void           setCategoriaNombre(String cn)             { this.categoriaNombre = cn; }
    public double         getPrecioCompra()                         { return precioCompra; }
    public void           setPrecioCompra(double precioCompra)      { this.precioCompra = precioCompra; }
    public double         getPrecioVenta()                          { return precioVenta; }
    public void           setPrecioVenta(double precioVenta)        { this.precioVenta = precioVenta; }
    public int            getStockActual()                          { return stockActual; }
    public void           setStockActual(int stockActual)           { this.stockActual = stockActual; }
    public int            getStockMinimo()                          { return stockMinimo; }
    public void           setStockMinimo(int stockMinimo)           { this.stockMinimo = stockMinimo; }
    public EstadoProducto getEstado()                               { return estado; }
    public void           setEstado(EstadoProducto estado)          { this.estado = estado; }
    public boolean        isActivo()                                { return activo; }
    public void           setActivo(boolean activo)                 { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("Producto[%s | %s | S/ %.2f | stock=%d | %s]",
                codigo, nombre, precioVenta, stockActual, estado);
    }
}