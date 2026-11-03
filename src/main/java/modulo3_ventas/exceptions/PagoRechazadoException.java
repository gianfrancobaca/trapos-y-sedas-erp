package modulo3_ventas.exceptions;

/**
 * Lanzada cuando un pago es rechazado durante el procesamiento.
 */
public class PagoRechazadoException extends RuntimeException {

    private final int    pagoId;
    private final String motivo;

    public PagoRechazadoException(int pagoId, String motivo) {
        super("El pago " + pagoId + " fue rechazado. Motivo: " + motivo);
        this.pagoId = pagoId;
        this.motivo = motivo;
    }

    public int    getPagoId() { return pagoId; }
    public String getMotivo() { return motivo; }
}
