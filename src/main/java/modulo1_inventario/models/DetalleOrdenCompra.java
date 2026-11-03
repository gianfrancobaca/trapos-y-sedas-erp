package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class DetalleOrdenCompra implements Model {

    private int    id;
    private int    ordenId;
    private int    productoId;
    private String productoNombre;
    private int    cantidad;
    private double precioUnitario;
    private double subtotal;

    private final List<View> observers = new ArrayList<>();

    public DetalleOrdenCompra() {}

    public DetalleOrdenCompra(int id, int ordenId, int productoId,
                               String productoNombre, int cantidad, double precioUnitario) {
        this.id             = id;
        this.ordenId        = ordenId;
        this.productoId     = productoId;
        this.productoNombre = productoNombre;
        this.cantidad       = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal       = cantidad * precioUnitario;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public void calcularSubtotal() { this.subtotal = cantidad * precioUnitario; }

    public int    getId()                              { return id; }
    public void   setId(int id)                        { this.id = id; }
    public int    getOrdenId()                         { return ordenId; }
    public void   setOrdenId(int ordenId)              { this.ordenId = ordenId; }
    public int    getProductoId()                      { return productoId; }
    public void   setProductoId(int productoId)        { this.productoId = productoId; }
    public String getProductoNombre()                  { return productoNombre; }
    public void   setProductoNombre(String pn)         { this.productoNombre = pn; }
    public int    getCantidad()                        { return cantidad; }
    public void   setCantidad(int cantidad)            { this.cantidad = cantidad; calcularSubtotal(); }
    public double getPrecioUnitario()                  { return precioUnitario; }
    public void   setPrecioUnitario(double p)          { this.precioUnitario = p; calcularSubtotal(); }
    public double getSubtotal()                        { return subtotal; }
    public void   setSubtotal(double subtotal)         { this.subtotal = subtotal; }

    @Override
    public String toString() {
        return String.format("DetalleOC[orden=%d | %s | cant=%d | S/ %.2f]",
                ordenId, productoNombre, cantidad, subtotal);
    }
}