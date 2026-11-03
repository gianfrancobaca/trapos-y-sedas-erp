package modulo3_ventas.controllers;

import core.Controller;
import modulo3_ventas.models.Pago;
import modulo3_ventas.repositories.*;
import java.util.List;

/**
 * Controlador de procesamiento de pagos en ventas.
 * RF: Procesamiento de pagos en ventas.
 */
public class PagoController extends Controller {

    private final PagoRepository pagoRepo;

    public PagoController() {
        this.pagoRepo = new PagoRepositorySQL();
    }

    @Override
    public void run() {
        // Vista se integrará en sprint siguiente
    }

    public List<Pago> obtenerPagosPorVenta(int ventaId) {
        return pagoRepo.buscarPorVenta(ventaId);
    }

    public List<Pago> obtenerPagosPendientes() {
        return pagoRepo.buscarPorEstado(Pago.EstadoPago.PENDIENTE);
    }

    public void registrarPago(Pago pago) {
        pagoRepo.guardar(pago);
    }

    public void aprobarPago(int pagoId) {
        Pago pago = pagoRepo.buscarPorId(pagoId);
        if (pago != null) {
            pago.setEstado(Pago.EstadoPago.APROBADO);
            pagoRepo.actualizar(pago);
        }
    }

    public void rechazarPago(int pagoId) {
        Pago pago = pagoRepo.buscarPorId(pagoId);
        if (pago != null) {
            pago.setEstado(Pago.EstadoPago.RECHAZADO);
            pagoRepo.actualizar(pago);
        }
    }

    public void reembolsarPago(int pagoId) {
        Pago pago = pagoRepo.buscarPorId(pagoId);
        if (pago != null) {
            pago.setEstado(Pago.EstadoPago.REEMBOLSADO);
            pagoRepo.actualizar(pago);
        }
    }
}
