package modulo3_ventas.controllers;

import core.Controller;
import modulo3_ventas.models.ComprobanteVenta;
import modulo3_ventas.repositories.*;
import java.util.List;

/**
 * Controlador de emisión y gestión de comprobantes de venta.
 * RF: Emisión de comprobantes en ventas.
 */
public class ComprobanteController extends Controller {

    private final ComprobanteVentaRepository comprobanteRepo;

    public ComprobanteController() {
        this.comprobanteRepo = new ComprobanteVentaRepositorySQL();
    }

    @Override
    public void run() {
        // Vista se integrará en sprint siguiente
    }

    public ComprobanteVenta obtenerPorVenta(int ventaId) {
        return comprobanteRepo.buscarPorVenta(ventaId);
    }

    public List<ComprobanteVenta> obtenerTodos() {
        return comprobanteRepo.buscarTodos();
    }

    public List<ComprobanteVenta> obtenerPorTipo(ComprobanteVenta.TipoComprobante tipo) {
        return comprobanteRepo.buscarPorTipo(tipo);
    }

    public void emitirComprobante(ComprobanteVenta comprobante) {
        comprobanteRepo.guardar(comprobante);
    }

    public void anularComprobante(int id) {
        comprobanteRepo.anular(id);
    }
}
