package modulo3_ventas.utils;

/**
 * Genera series y numeros correlativos para comprobantes de venta.
 */
public class GeneradorSerie {

    private GeneradorSerie() {}

    /**
     * Genera la serie segun el tipo de comprobante.
     * Boleta → B001, Factura → F001, Nota credito → NC01
     */
    public static String generarSerie(String tipoComprobante) {
        switch (tipoComprobante.toUpperCase()) {
            case "BOLETA":       return "B001";
            case "FACTURA":      return "F001";
            case "NOTA_CREDITO": return "NC01";
            default:             return "X001";
        }
    }

    /** Formatea el numero como correlativo de 8 digitos. Ejemplo: 00000023 */
    public static String formatearNumero(int numero) {
        return String.format("%08d", numero);
    }

    /** Devuelve el numero completo: serie-correlativo. Ejemplo: B001-00000023 */
    public static String getNumeroCompleto(String serie, int numero) {
        return serie + "-" + formatearNumero(numero);
    }
}
