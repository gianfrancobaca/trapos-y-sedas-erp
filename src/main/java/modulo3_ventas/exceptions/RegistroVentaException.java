package modulo3_ventas.exceptions;

/**
 * Lanzada cuando no se pudo registrar una venta y se revirtió la transacción.
 */
public class RegistroVentaException extends RuntimeException {

    public RegistroVentaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
