package modulo3_ventas.utils;

import modulo3_ventas.models.Venta;
import modulo3_ventas.models.DetalleVenta;
import java.util.List;

/**
 * Valida los datos de una venta antes de registrarla en el sistema.
 */
public class ValidadorVenta {

    private ValidadorVenta() {}

    /** Verifica que la venta tenga cliente, metodo de pago y al menos un detalle. */
    public static boolean esValida(Venta venta, List<DetalleVenta> detalles) {
        if (venta == null)                        return false;
        if (venta.getClienteId() <= 0)            return false;
        if (venta.getMetodoPago() == null)        return false;
        if (detalles == null || detalles.isEmpty()) return false;
        return true;
    }

    /** Verifica que un detalle tenga cantidad positiva y precio mayor a cero. */
    public static boolean esDetalleValido(DetalleVenta detalle) {
        if (detalle == null)                    return false;
        if (detalle.getCantidad() <= 0)         return false;
        if (detalle.getPrecioUnitario() <= 0)   return false;
        return true;
    }

    /** Verifica que el ID sea un numero positivo valido. */
    public static boolean esIdValido(int id) {
        return id > 0;
    }
}
