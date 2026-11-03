package modulo1_inventario.models;

import core.Model;
import core.View;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventario implements Model {

    public enum TipoMovimiento { ENTRADA, SALIDA, AJUSTE, DEVOLUCION }

    private int             id;
    private int             productoId;
    private String          productoNombre;
    private TipoMovimiento  tipo;
    private int             cantidad;
    private int             stockAnterior;
    private int             stockResultante;
    private String          motivo;
    private String          referenciaDocumento;
    private LocalDateTime   fechaHora;
    private int             usuarioId;

    private final List<View> observers = new ArrayList<>();

    public MovimientoInventario() {}

    public MovimientoInventario(int id, int productoId, String productoNombre,
                                TipoMovimiento tipo, int cantidad, int stockAnterior,
                                String motivo, String referenciaDocumento,
                                LocalDateTime fechaHora, int usuarioId) {
        this.id                  = id;
        this.productoId          = productoId;
        this.productoNombre      = productoNombre;
        this.tipo                = tipo;
        this.cantidad            = cantidad;
        this.stockAnterior       = stockAnterior;
        this.stockResultante     = calcularResultante(tipo, stockAnterior, cantidad);
        this.motivo              = motivo;
        this.referenciaDocumento = referenciaDocumento;
        this.fechaHora           = fechaHora;
        this.usuarioId           = usuarioId;
    }

    private int calcularResultante(TipoMovimiento tipo, int anterior, int cantidad) {
        switch (tipo) {
            case ENTRADA:
            case DEVOLUCION: return anterior + cantidad;
            case SALIDA:     return anterior - cantidad;
            case AJUSTE:     return cantidad;
            default:         return anterior;
        }
    }

    @Override public void attach(View v)  { observers.add(v); }
    @Override public void detach(View v)  { observers.remove(v); }
    @Override public void notifyViews()   { for (View v : observers) v.update(this, null); }

    public int            getId()                                    { return id; }
    public void           setId(int id)                              { this.id = id; }
    public int            getProductoId()                            { return productoId; }
    public void           setProductoId(int productoId)              { this.productoId = productoId; }
    public String         getProductoNombre()                        { return productoNombre; }
    public void           setProductoNombre(String pn)               { this.productoNombre = pn; }
    public TipoMovimiento getTipo()                                  { return tipo; }
    public void           setTipo(TipoMovimiento tipo)               { this.tipo = tipo; }
    public int            getCantidad()                              { return cantidad; }
    public void           setCantidad(int cantidad)                  { this.cantidad = cantidad; }
    public int            getStockAnterior()                         { return stockAnterior; }
    public void           setStockAnterior(int stockAnterior)        { this.stockAnterior = stockAnterior; }
    public int            getStockResultante()                       { return stockResultante; }
    public void           setStockResultante(int stockResultante)    { this.stockResultante = stockResultante; }
    public String         getMotivo()                                { return motivo; }
    public void           setMotivo(String motivo)                   { this.motivo = motivo; }
    public String         getReferenciaDocumento()                   { return referenciaDocumento; }
    public void           setReferenciaDocumento(String ref)         { this.referenciaDocumento = ref; }
    public LocalDateTime  getFechaHora()                             { return fechaHora; }
    public void           setFechaHora(LocalDateTime fechaHora)      { this.fechaHora = fechaHora; }
    public int            getUsuarioId()                             { return usuarioId; }
    public void           setUsuarioId(int usuarioId)                { this.usuarioId = usuarioId; }

    @Override
    public String toString() {
        return String.format("Movimiento[%s | %s | cant=%d | %d→%d]",
                tipo, productoNombre, cantidad, stockAnterior, stockResultante);
    }
}