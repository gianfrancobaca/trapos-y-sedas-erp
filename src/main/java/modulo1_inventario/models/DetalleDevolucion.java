package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class DetalleDevolucion implements Model {

    private int    id;
    private int    devolucionId;
    private int    productoId;
    private String productoNombre;
    private int    cantidad;
    private String motivoLinea;

    private final List<View> observers = new ArrayList<>();

    public DetalleDevolucion() {}

    public DetalleDevolucion(int id, int devolucionId, int productoId,
                              String productoNombre, int cantidad, String motivoLinea) {
        this.id             = id;
        this.devolucionId   = devolucionId;
        this.productoId     = productoId;
        this.productoNombre = productoNombre;
        this.cantidad       = cantidad;
        this.motivoLinea    = motivoLinea;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int    getId()                              { return id; }
    public void   setId(int id)                        { this.id = id; }
    public int    getDevolucionId()                    { return devolucionId; }
    public void   setDevolucionId(int devolucionId)    { this.devolucionId = devolucionId; }
    public int    getProductoId()                      { return productoId; }
    public void   setProductoId(int productoId)        { this.productoId = productoId; }
    public String getProductoNombre()                  { return productoNombre; }
    public void   setProductoNombre(String pn)         { this.productoNombre = pn; }
    public int    getCantidad()                        { return cantidad; }
    public void   setCantidad(int cantidad)            { this.cantidad = cantidad; }
    public String getMotivoLinea()                     { return motivoLinea; }
    public void   setMotivoLinea(String motivoLinea)   { this.motivoLinea = motivoLinea; }

    @Override
    public String toString() {
        return String.format("DetalleDevolucion[dev=%d | %s | cant=%d]",
                devolucionId, productoNombre, cantidad);
    }
}