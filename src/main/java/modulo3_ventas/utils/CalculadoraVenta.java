package modulo3_ventas.utils;

/**
 * Utilitario para calculos numericos de una venta: IGV, subtotal y total.
 */
public class CalculadoraVenta {

    private static final double TASA_IGV = 0.18;

    // Evita instanciacion: clase de metodos estaticos
    private CalculadoraVenta() {}

    /** Calcula el IGV (18%) sobre el subtotal. */
    public static double calcularIGV(double subtotal) {
        return subtotal * TASA_IGV;
    }

    /** Calcula el subtotal a partir de precio unitario y cantidad. */
    public static double calcularSubtotal(double precioUnitario, int cantidad) {
        return precioUnitario * cantidad;
    }

    /** Calcula el total final aplicando descuento e IGV. */
    public static double calcularTotal(double subtotal, double descuento) {
        double base = subtotal - descuento;
        return base + calcularIGV(base);
    }

    /** Redondea un valor a dos decimales. */
    public static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
