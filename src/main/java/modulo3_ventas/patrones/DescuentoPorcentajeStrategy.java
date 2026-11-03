package modulo3_ventas.patrones;

/**
 * Implementacion Strategy: descuento por porcentaje sobre el monto base.
 * Ejemplo: 15% de descuento sobre S/ 100.00 → descuenta S/ 15.00.
 */
public class DescuentoPorcentajeStrategy implements DescuentoStrategy {

    private final double porcentaje;

    public DescuentoPorcentajeStrategy(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public double aplicar(double montoBase) {
        return montoBase * (porcentaje / 100.0);
    }

    @Override
    public String getDescripcion() {
        return "Descuento del " + porcentaje + "%";
    }
}
