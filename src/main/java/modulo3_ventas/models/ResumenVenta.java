package modulo3_ventas.models;

import java.time.LocalDate;

/**
 * DTO con los totales agregados de ventas para un periodo determinado.
 * Usado para mostrar resumenes en reportes y dashboards.
 */
public class ResumenVenta {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int       totalVentas;
    private int       ventasCompletadas;
    private int       ventasCanceladas;
    private double    montoTotal;
    private double    descuentoTotal;
    private double    igvTotal;

    public ResumenVenta() {}

    public ResumenVenta(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
    }

    /** Devuelve el monto neto: total - descuentos aplicados. */
    public double getMontoNeto() {
        return montoTotal - descuentoTotal;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public LocalDate getFechaInicio()                    { return fechaInicio; }
    public void      setFechaInicio(LocalDate f)         { this.fechaInicio = f; }

    public LocalDate getFechaFin()                       { return fechaFin; }
    public void      setFechaFin(LocalDate f)            { this.fechaFin = f; }

    public int    getTotalVentas()                       { return totalVentas; }
    public void   setTotalVentas(int t)                  { this.totalVentas = t; }

    public int    getVentasCompletadas()                 { return ventasCompletadas; }
    public void   setVentasCompletadas(int v)            { this.ventasCompletadas = v; }

    public int    getVentasCanceladas()                  { return ventasCanceladas; }
    public void   setVentasCanceladas(int v)             { this.ventasCanceladas = v; }

    public double getMontoTotal()                        { return montoTotal; }
    public void   setMontoTotal(double m)                { this.montoTotal = m; }

    public double getDescuentoTotal()                    { return descuentoTotal; }
    public void   setDescuentoTotal(double d)            { this.descuentoTotal = d; }

    public double getIgvTotal()                          { return igvTotal; }
    public void   setIgvTotal(double i)                  { this.igvTotal = i; }

    @Override
    public String toString() {
        return String.format("ResumenVenta[%s - %s | ventas=%d | total=S/ %.2f]",
                fechaInicio, fechaFin, totalVentas, montoTotal);
    }
}
