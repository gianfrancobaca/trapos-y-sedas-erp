package modulo3_ventas.exceptions;

/**
 * Lanzada cuando no se encuentra una venta por el ID proporcionado.
 */
public class VentaNoEncontradaException extends RuntimeException {

    private final int ventaId;

    public VentaNoEncontradaException(int ventaId) {
        super("No se encontro ninguna venta con ID: " + ventaId);
        this.ventaId = ventaId;
    }

    public int getVentaId() { return ventaId; }
}
