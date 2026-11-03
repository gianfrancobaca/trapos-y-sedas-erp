package modulo3_ventas.patrones;

/**
 * Implementacion Strategy: descuento de monto fijo en soles.
 * Ejemplo: S/ 20.00 de descuento directo. No supera el monto base.
 */
public class DescuentoMontoFijoStrategy implements DescuentoStrategy {

    private final double montoFijo;

    public DescuentoMontoFijoStrategy(double montoFijo) {
        this.montoFijo = montoFijo;
    }

    @Override
    public double aplicar(double montoBase) {
        // El descuento no puede superar el precio del producto
        return Math.min(montoFijo, montoBase);
    }

    @Override
    public String getDescripcion() {
        return "Descuento fijo de S/ " + String.format("%.2f", montoFijo);
    }
}
