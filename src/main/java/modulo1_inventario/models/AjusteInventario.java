package modulo1_inventario.models;

import core.Model;
import core.View;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AjusteInventario implements Model {

    public enum TipoAjuste { CONTEO_FISICO, CORRECCION_ERROR, MERMA, OTRO }

    private int          id;
    private TipoAjuste   tipo;
    private String       motivo;
    private LocalDateTime fechaHora;
    private int          usuarioId;

    private final List<View> observers = new ArrayList<>();

    public AjusteInventario() {}

    public AjusteInventario(int id, TipoAjuste tipo, String motivo,
                             LocalDateTime fechaHora, int usuarioId) {
        this.id        = id;
        this.tipo      = tipo;
        this.motivo    = motivo;
        this.fechaHora = fechaHora;
        this.usuarioId = usuarioId;
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int           getId()                            { return id; }
    public void          setId(int id)                      { this.id = id; }
    public TipoAjuste    getTipo()                          { return tipo; }
    public void          setTipo(TipoAjuste tipo)           { this.tipo = tipo; }
    public String        getMotivo()                        { return motivo; }
    public void          setMotivo(String motivo)           { this.motivo = motivo; }
    public LocalDateTime getFechaHora()                     { return fechaHora; }
    public void          setFechaHora(LocalDateTime f)      { this.fechaHora = f; }
    public int           getUsuarioId()                     { return usuarioId; }
    public void          setUsuarioId(int usuarioId)        { this.usuarioId = usuarioId; }

    @Override
    public String toString() {
        return String.format("Ajuste[id=%d | %s | %s]", id, tipo, fechaHora);
    }
}