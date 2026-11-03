package modulo3_ventas.models;

import java.time.LocalDate;

/**
 * Descuento o promoción aplicable a productos o ventas.
 * RF: Gestión de descuentos y promociones en ventas.
 */
public class Descuento {

    public enum TipoDescuento { PORCENTAJE, MONTO_FIJO, OFERTA_2X1, DESCUENTO_TEMPORADA }

    private int           id;
    private String        nombre;
    private String        descripcion;
    private TipoDescuento tipo;
    private double        valor;
    private LocalDate     fechaInicio;
    private LocalDate     fechaFin;
    private boolean       activo;

    public Descuento() {}

    public Descuento(int id, String nombre, String descripcion, TipoDescuento tipo,
                     double valor, LocalDate fechaInicio, LocalDate fechaFin) {
        this.id          = id;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.tipo        = tipo;
        this.valor       = valor;
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.activo      = true;
    }

    public boolean estaVigente(LocalDate fecha) {
        return activo && !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    public double aplicar(double monto) {
        if (tipo == TipoDescuento.PORCENTAJE) return monto * (valor / 100.0);
        if (tipo == TipoDescuento.MONTO_FIJO) return Math.min(valor, monto);
        return 0;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int getId()                               { return id; }
    public void setId(int id)                        { this.id = id; }

    public String getNombre()                        { return nombre; }
    public void setNombre(String n)                  { this.nombre = n; }

    public String getDescripcion()                   { return descripcion; }
    public void setDescripcion(String d)             { this.descripcion = d; }

    public TipoDescuento getTipo()                   { return tipo; }
    public void setTipo(TipoDescuento t)             { this.tipo = t; }

    public double getValor()                         { return valor; }
    public void setValor(double v)                   { this.valor = v; }

    public LocalDate getFechaInicio()                { return fechaInicio; }
    public void setFechaInicio(LocalDate f)          { this.fechaInicio = f; }

    public LocalDate getFechaFin()                   { return fechaFin; }
    public void setFechaFin(LocalDate f)             { this.fechaFin = f; }

    public boolean isActivo()                        { return activo; }
    public void setActivo(boolean a)                 { this.activo = a; }

    @Override
    public String toString() {
        return String.format("Descuento[%s | %s %.2f | %s → %s]",
                nombre, tipo, valor, fechaInicio, fechaFin);
    }
}
