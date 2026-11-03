package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class Categoria implements Model {

    private int    id;
    private String nombre;
    private String descripcion;
    private boolean activo;

    private final List<View> observers = new ArrayList<>();

    public Categoria() {}

    public Categoria(int id, String nombre, String descripcion) {
        this.id          = id;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.activo      = true;
    }

    @Override public void attach(View v)   { observers.add(v); }
    @Override public void detach(View v)   { observers.remove(v); }
    @Override public void notifyViews()    { for (View v : observers) v.update(this, null); }

    public int     getId()                        { return id; }
    public void    setId(int id)                  { this.id = id; }
    public String  getNombre()                    { return nombre; }
    public void    setNombre(String nombre)       { this.nombre = nombre; }
    public String  getDescripcion()               { return descripcion; }
    public void    setDescripcion(String d)       { this.descripcion = d; }
    public boolean isActivo()                     { return activo; }
    public void    setActivo(boolean activo)      { this.activo = activo; }

    @Override
    public String toString() { return nombre; }
}