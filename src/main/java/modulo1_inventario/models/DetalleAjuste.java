package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class DetalleAjuste implements Model {

    private int    id;
    private int    ajusteId;
    private int    productoId;
    private String productoNombre;
    private int    stockAnterior;
    private int    stockNuevo;
    private int    diferencia;

    private final List<View> observers = new ArrayList<>();

    public DetalleAjuste() {}

    public DetalleAjuste(int id, int ajusteId, int productoId,
                         String productoNombre, int stockAnterior, int stockNuevo) {
        this.id             = id;
        this.ajusteId       = ajusteId;
        this.productoId     = productoId;
        this.productoNombre = productoNombre;
        this.stockAnterior  = stockAnterior;
        this.stockNuevo     = stockNuevo;
        this.diferencia     = stockNuevo - stockAnterior;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int    getId()                              { return id; }
    public void   setId(int id)                        { this.id = id; }
    public int    getAjusteId()                        { return ajusteId; }
    public void   setAjusteId(int ajusteId)            { this.ajusteId = ajusteId; }
    public int    getProductoId()                      { return productoId; }
    public void   setProductoId(int productoId)        { this.productoId = productoId; }
    public String getProductoNombre()                  { return productoNombre; }
    public void   setProductoNombre(String pn)         { this.productoNombre = pn; }
    public int    getStockAnterior()                   { return stockAnterior; }
    public void   setStockAnterior(int stockAnterior)  { this.stockAnterior = stockAnterior; }
    public int    getStockNuevo()                      { return stockNuevo; }
    public void   setStockNuevo(int stockNuevo)        { this.stockNuevo = stockNuevo; this.diferencia = stockNuevo - stockAnterior; }
    public int    getDiferencia()                      { return diferencia; }
    public void   setDiferencia(int diferencia)        { this.diferencia = diferencia; }

    @Override
    public String toString() {
        return String.format("DetalleAjuste[%s | ant=%d | nuevo=%d | dif=%d]",
                productoNombre, stockAnterior, stockNuevo, diferencia);
    }
}