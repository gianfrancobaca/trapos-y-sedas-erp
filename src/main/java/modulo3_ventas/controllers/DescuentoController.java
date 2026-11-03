package modulo3_ventas.controllers;

import core.Controller;
import modulo3_ventas.models.Descuento;
import modulo3_ventas.repositories.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador de descuentos y promociones del módulo de ventas.
 * RF: Gestión de descuentos y promociones en ventas.
 */
public class DescuentoController extends Controller {

    private final DescuentoRepository descuentoRepo;

    public DescuentoController() {
        this.descuentoRepo = new DescuentoRepositorySQL();
    }

    @Override
    public void run() {
        // Vista se integrará en sprint siguiente
    }

    public List<Descuento> obtenerDescuentos() {
        return descuentoRepo.buscarTodos();
    }

    public List<Descuento> obtenerDescuentosVigentes() {
        return descuentoRepo.buscarVigentes(LocalDate.now());
    }

    public void crearDescuento(Descuento descuento) {
        descuentoRepo.guardar(descuento);
    }

    public void actualizarDescuento(Descuento descuento) {
        descuentoRepo.actualizar(descuento);
    }

    public void eliminarDescuento(int id) {
        descuentoRepo.eliminar(id);
    }

    public double calcularDescuentoAplicable(int descuentoId, double montoBase) {
        Descuento descuento = descuentoRepo.buscarPorId(descuentoId);
        if (descuento != null && descuento.estaVigente(LocalDate.now())) {
            return descuento.aplicar(montoBase);
        }
        return 0;
    }
}
