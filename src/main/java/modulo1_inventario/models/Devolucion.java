package modulo1_inventario.models;

import core.Model;
import core.View;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Devolucion implements Model {

    public enum TipoDevolucion { CLIENTE, PROVEEDOR }
    public enum EstadoDevolucion { PENDIENTE, APROBADA, RECHAZADA, COMPLETADA }

    private int              id;
    private TipoDevolucion   tipo;
    private int              referenciaId;
    private String           motivo;
    private EstadoDevolucion estado;
    private LocalDateTime    fechaHora;
    private int              usuarioId;

    private final List<View> observers = new ArrayList<>();

    public Devolucion() {}

    public Devolucion(int id, TipoDevolucion tipo, int referenciaId,
                      String motivo, LocalDateTime fechaHora, int usuarioId) {
        this.id           = id;
        this.tipo         = tipo;
        this.referenciaId = referenciaId;
        this.motivo       = motivo;
        this.estado       = EstadoDevolucion.PENDIENTE;
        this.fechaHora    = fechaHora;
        this.usuarioId    = usuarioId;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int              getId()                                  { return id; }
    public void             setId(int id)                            { this.id = id; }
    public TipoDevolucion   getTipo()                                { return tipo; }
    public void             setTipo(TipoDevolucion tipo)             { this.tipo = tipo; }
    public int              getReferenciaId()                        { return referenciaId; }
    public void             setReferenciaId(int referenciaId)        { this.referenciaId = referenciaId; }
    public String           getMotivo()                              { return motivo; }
    public void             setMotivo(String motivo)                 { this.motivo = motivo; }
    public EstadoDevolucion getEstado()                              { return estado; }
    public void             setEstado(EstadoDevolucion estado)       { this.estado = estado; }
    public LocalDateTime    getFechaHora()                           { return fechaHora; }
    public void             setFechaHora(LocalDateTime f)            { this.fechaHora = f; }
    public int              getUsuarioId()                           { return usuarioId; }
    public void             setUsuarioId(int usuarioId)              { this.usuarioId = usuarioId; }

    @Override
    public String toString() {
        return String.format("Devolucion[id=%d | %s | %s | %s]",
                id, tipo, estado, fechaHora);
    }
}