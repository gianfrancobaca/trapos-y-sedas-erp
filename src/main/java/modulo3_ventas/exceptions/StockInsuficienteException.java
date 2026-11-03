package modulo3_ventas.exceptions;

/**
 * Lanzada cuando el stock disponible no alcanza para completar la venta.
 */
public class StockInsuficienteException extends RuntimeException {

    private final int productoId;
    private final int stockDisponible;
    private final int cantidadSolicitada;

    public StockInsuficienteException(int productoId, int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente para el producto " + productoId +
              ". Disponible: " + stockDisponible + ", solicitado: " + cantidadSolicitada);
        this.productoId          = productoId;
        this.stockDisponible     = stockDisponible;
        this.cantidadSolicitada  = cantidadSolicitada;
    }

    public int getProductoId()         { return productoId; }
    public int getStockDisponible()    { return stockDisponible; }
    public int getCantidadSolicitada() { return cantidadSolicitada; }
}
