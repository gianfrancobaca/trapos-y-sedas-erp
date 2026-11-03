package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class ProductoVariante implements Model {

    private int    id;
    private int    productoId;
    private int    tallaId;
    private String tallaCodigo;
    private int    colorId;
    private String colorNombre;
    private int    stock;
    private String codigoVariante;

    private final List<View> observers = new ArrayList<>();

    public ProductoVariante() {}

    public ProductoVariante(int id, int productoId, int tallaId, String tallaCodigo,
                            int colorId, String colorNombre, int stock, String codigoVariante) {
        this.id             = id;
        this.productoId     = productoId;
        this.tallaId        = tallaId;
        this.tallaCodigo    = tallaCodigo;
        this.colorId        = colorId;
        this.colorNombre    = colorNombre;
        this.stock          = stock;
        this.codigoVariante = codigoVariante;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int    getId()                              { return id; }
    public void   setId(int id)                        { this.id = id; }
    public int    getProductoId()                      { return productoId; }
    public void   setProductoId(int productoId)        { this.productoId = productoId; }
    public int    getTallaId()                         { return tallaId; }
    public void   setTallaId(int tallaId)              { this.tallaId = tallaId; }
    public String getTallaCodigo()                     { return tallaCodigo; }
    public void   setTallaCodigo(String tallaCodigo)   { this.tallaCodigo = tallaCodigo; }
    public int    getColorId()                         { return colorId; }
    public void   setColorId(int colorId)              { this.colorId = colorId; }
    public String getColorNombre()                     { return colorNombre; }
    public void   setColorNombre(String colorNombre)   { this.colorNombre = colorNombre; }
    public int    getStock()                           { return stock; }
    public void   setStock(int stock)                  { this.stock = stock; }
    public String getCodigoVariante()                  { return codigoVariante; }
    public void   setCodigoVariante(String cod)        { this.codigoVariante = cod; }

    @Override
    public String toString() {
        return String.format("Variante[%s | Talla: %s | Color: %s | Stock: %d]",
                codigoVariante, tallaCodigo, colorNombre, stock);
    }
}