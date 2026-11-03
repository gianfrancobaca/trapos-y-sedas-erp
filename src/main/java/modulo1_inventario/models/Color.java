package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class Color implements Model {

    private int    id;
    private String nombre;
    private String codigoHex;

    private final List<View> observers = new ArrayList<>();

    public Color() {}

    public Color(int id, String nombre, String codigoHex) {
        this.id        = id;
        this.nombre    = nombre;
        this.codigoHex = codigoHex;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int    getId()                        { return id; }
    public void   setId(int id)                  { this.id = id; }
    public String getNombre()                    { return nombre; }
    public void   setNombre(String nombre)       { this.nombre = nombre; }
    public String getCodigoHex()                 { return codigoHex; }
    public void   setCodigoHex(String hex)       { this.codigoHex = hex; }

    @Override
    public String toString() { return nombre; }
}