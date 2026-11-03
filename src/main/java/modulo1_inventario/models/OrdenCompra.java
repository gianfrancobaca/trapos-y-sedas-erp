package modulo1_inventario.models;

import core.Model;
import core.View;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdenCompra implements Model {

    public enum EstadoOrden { PENDIENTE, APROBADA, RECIBIDA, CANCELADA }

    private int         id;
    private int         proveedorId;
    private String      proveedorNombre;
    private LocalDate   fechaEmision;
    private LocalDate   fechaEntregaEstimada;
    private EstadoOrden estado;
    private double      total;
    private String      observaciones;

    private final List<View> observers = new ArrayList<>();

    public OrdenCompra() {}

    public OrdenCompra(int id, int proveedorId, String proveedorNombre,
                       LocalDate fechaEmision, LocalDate fechaEntregaEstimada) {
        this.id                   = id;
        this.proveedorId          = proveedorId;
        this.proveedorNombre      = proveedorNombre;
        this.fechaEmision         = fechaEmision;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estado               = EstadoOrden.PENDIENTE;
        this.total                = 0;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int         getId()                                   { return id; }
    public void        setId(int id)                             { this.id = id; }
    public int         getProveedorId()                          { return proveedorId; }
    public void        setProveedorId(int proveedorId)           { this.proveedorId = proveedorId; }
    public String      getProveedorNombre()                      { return proveedorNombre; }
    public void        setProveedorNombre(String pn)             { this.proveedorNombre = pn; }
    public LocalDate   getFechaEmision()                         { return fechaEmision; }
    public void        setFechaEmision(LocalDate f)              { this.fechaEmision = f; }
    public LocalDate   getFechaEntregaEstimada()                 { return fechaEntregaEstimada; }
    public void        setFechaEntregaEstimada(LocalDate f)      { this.fechaEntregaEstimada = f; }
    public EstadoOrden getEstado()                               { return estado; }
    public void        setEstado(EstadoOrden estado)             { this.estado = estado; }
    public double      getTotal()                                { return total; }
    public void        setTotal(double total)                    { this.total = total; }
    public String      getObservaciones()                        { return observaciones; }
    public void        setObservaciones(String obs)              { this.observaciones = obs; }

    @Override
    public String toString() {
        return String.format("OrdenCompra[id=%d | %s | %s | S/ %.2f]",
                id, proveedorNombre, estado, total);
    }
}