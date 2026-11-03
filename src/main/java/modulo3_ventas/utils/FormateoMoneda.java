package modulo3_ventas.utils;

/**
 * Utilitario para formatear valores monetarios en soles peruanos.
 */
public class FormateoMoneda {

    private FormateoMoneda() {}

    /** Devuelve el valor con prefijo S/ y dos decimales. Ejemplo: S/ 1234.50 */
    public static String formatear(double monto) {
        return String.format("S/ %.2f", monto);
    }

    /** Devuelve solo los dos decimales sin prefijo. Ejemplo: 1234.50 */
    public static String formatearSinPrefijo(double monto) {
        return String.format("%.2f", monto);
    }

    /** Intenta parsear un String a double; devuelve 0.0 si falla. */
    public static double parsear(String texto) {
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
