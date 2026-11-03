package modulo3_ventas.controllers;

import core.Controller;
import modulo3_ventas.models.*;
import modulo3_ventas.repositories.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador principal del módulo de ventas.
 * RF: Registro y gestión de ventas.
 */
public class VentaController extends Controller {

    private final VentaRepository       ventaRepo;
    private final DetalleVentaRepository detalleRepo;

    public VentaController() {
        this.ventaRepo   = new VentaRepositorySQL();
        this.detalleRepo = new DetalleVentaRepositorySQL();
    }

    @Override
    public void run() {
        // Vista se integrará en sprint siguiente
    }

    public List<Venta> obtenerVentas() {
        return ventaRepo.buscarTodas();
    }

    public Venta obtenerVentaPorId(int id) {
        return ventaRepo.buscarPorId(id);
    }

    public List<Venta> obtenerVentasPorCliente(int clienteId) {
        return ventaRepo.buscarPorCliente(clienteId);
    }

    public List<Venta> obtenerVentasPorPeriodo(LocalDate inicio, LocalDate fin) {
        return ventaRepo.buscarPorPeriodo(inicio, fin);
    }

    public List<DetalleVenta> obtenerDetalles(int ventaId) {
        return detalleRepo.buscarPorVenta(ventaId);
    }

    public void registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        ventaRepo.guardar(venta);
        for (DetalleVenta detalle : detalles) {
            detalle.setVentaId(venta.getId());
            detalleRepo.guardar(detalle);
        }
    }

    public void actualizarEstado(int ventaId, Venta.EstadoVenta nuevoEstado) {
        Venta venta = ventaRepo.buscarPorId(ventaId);
        if (venta != null) {
            venta.setEstado(nuevoEstado);
            ventaRepo.actualizar(venta);
        }
    }

    public void cancelarVenta(int ventaId) {
        actualizarEstado(ventaId, Venta.EstadoVenta.CANCELADA);
    }

    public void eliminarVenta(int ventaId) {
        detalleRepo.eliminarPorVenta(ventaId);
        ventaRepo.eliminar(ventaId);
    }
}
