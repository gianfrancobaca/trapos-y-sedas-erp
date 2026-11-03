package modulo3_ventas.patrones;

/**
 * Interfaz Strategy para aplicar distintos tipos de descuento sobre un monto base.
 * Permite intercambiar la logica de descuento sin modificar el codigo del controlador.
 */
public interface DescuentoStrategy {

    /**
     * Aplica el descuento sobre el monto base y devuelve el monto a descontar.
     * @param montoBase precio original antes del descuento
     * @return valor a restar del precio original
     */
    double aplicar(double montoBase);

    /** Descripcion legible del tipo de descuento. */
    String getDescripcion();
}
