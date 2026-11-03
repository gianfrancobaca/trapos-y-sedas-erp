package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class Talla implements Model {

    private int    id;
    private String codigo;
    private String descripcion;
    private int    orden;

    private final List<View> observers = new ArrayList<>();

    public Talla() {}

    public Talla(int id, String codigo, String descripcion, int orden) {
        this.id          = id;
        this.codigo      = codigo;
        this.descripcion = descripcion;
        this.orden       = orden;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int    getId()                       { return id; }
    public void   setId(int id)                 { this.id = id; }
    public String getCodigo()                   { return codigo; }
    public void   setCodigo(String codigo)      { this.codigo = codigo; }
    public String getDescripcion()              { return descripcion; }
    public void   setDescripcion(String d)      { this.descripcion = d; }
    public int    getOrden()                    { return orden; }
    public void   setOrden(int orden)           { this.orden = orden; }

    @Override
    public String toString() { return codigo; }
}