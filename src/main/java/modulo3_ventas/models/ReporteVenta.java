package modulo3_ventas.models;

import java.time.LocalDate;

/**
 * DTO para el reporte mensual de ventas por producto y categoria.
 * Agrupa las metricas mas importantes del mes para analisis de negocio.
 */
public class ReporteVenta {

    private int       mes;
    private int       anio;
    private LocalDate fechaGeneracion;
    private String    productoMasVendido;
    private String    categoriaMasVendida;
    private int       unidadesVendidas;
    private double    ingresosBrutos;
    private double    ingresosNetos;
    private int       totalClientes;

    public ReporteVenta() {}

    public ReporteVenta(int mes, int anio) {
        this.mes             = mes;
        this.anio            = anio;
        this.fechaGeneracion = LocalDate.now();
    }

    /** Calcula el margen neto como porcentaje de los ingresos brutos. */
    public double calcularMargenPorcentual() {
        if (ingresosBrutos == 0) return 0;
        return (ingresosNetos / ingresosBrutos) * 100.0;
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public int    getMes()                              { return mes; }
    public void   setMes(int m)                         { this.mes = m; }

    public int    getAnio()                             { return anio; }
    public void   setAnio(int a)                        { this.anio = a; }

    public LocalDate getFechaGeneracion()               { return fechaGeneracion; }
    public void      setFechaGeneracion(LocalDate f)    { this.fechaGeneracion = f; }

    public String getProductoMasVendido()               { return productoMasVendido; }
    public void   setProductoMasVendido(String p)       { this.productoMasVendido = p; }

    public String getCategoriaMasVendida()              { return categoriaMasVendida; }
    public void   setCategoriaMasVendida(String c)      { this.categoriaMasVendida = c; }

    public int    getUnidadesVendidas()                 { return unidadesVendidas; }
    public void   setUnidadesVendidas(int u)            { this.unidadesVendidas = u; }

    public double getIngresosBrutos()                   { return ingresosBrutos; }
    public void   setIngresosBrutos(double i)           { this.ingresosBrutos = i; }

    public double getIngresosNetos()                    { return ingresosNetos; }
    public void   setIngresosNetos(double i)            { this.ingresosNetos = i; }

    public int    getTotalClientes()                    { return totalClientes; }
    public void   setTotalClientes(int t)               { this.totalClientes = t; }

    @Override
    public String toString() {
        return String.format("ReporteVenta[%02d/%d | unidades=%d | bruto=S/ %.2f]",
                mes, anio, unidadesVendidas, ingresosBrutos);
    }
}
