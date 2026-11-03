package modulo1_inventario.models;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class Proveedor implements Model {

    public enum TipoDocumento { DNI, RUC, NINGUNO }

    private int    id;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String razonSocial;
    private String contacto;
    private String telefono;
    private String correo;
    private String direccion;
    private boolean activo;

    private final List<View> observers = new ArrayList<>();

    public Proveedor() {}

    public Proveedor(int id, TipoDocumento tipoDocumento, String numeroDocumento,
                     String razonSocial, String contacto,
                     String telefono, String correo, String direccion) {
        this.id              = id;
        this.tipoDocumento   = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.razonSocial     = razonSocial;
        this.contacto        = contacto;
        this.telefono        = telefono;
        this.correo          = correo;
        this.direccion       = direccion;
        this.activo          = true;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int            getId()                              { return id; }
    public void           setId(int id)                        { this.id = id; }
    public TipoDocumento  getTipoDocumento()                   { return tipoDocumento; }
    public void           setTipoDocumento(TipoDocumento t)    { this.tipoDocumento = t; }
    public String         getNumeroDocumento()                 { return numeroDocumento; }
    public void           setNumeroDocumento(String nd)        { this.numeroDocumento = nd; }
    public String         getRazonSocial()                     { return razonSocial; }
    public void           setRazonSocial(String rs)            { this.razonSocial = rs; }
    public String         getContacto()                        { return contacto; }
    public void           setContacto(String contacto)         { this.contacto = contacto; }
    public String         getTelefono()                        { return telefono; }
    public void           setTelefono(String telefono)         { this.telefono = telefono; }
    public String         getCorreo()                          { return correo; }
    public void           setCorreo(String correo)             { this.correo = correo; }
    public String         getDireccion()                       { return direccion; }
    public void           setDireccion(String direccion)       { this.direccion = direccion; }
    public boolean        isActivo()                           { return activo; }
    public void           setActivo(boolean activo)            { this.activo = activo; }

    @Override
    public String toString() {
        String doc = (numeroDocumento == null || numeroDocumento.isEmpty())
                ? "Sin documento"
                : tipoDocumento + ": " + numeroDocumento;
        return String.format("Proveedor[%s | %s]", doc, razonSocial);
    }
}