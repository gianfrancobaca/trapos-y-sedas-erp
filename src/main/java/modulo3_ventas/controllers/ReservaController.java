package modulo3_ventas.controllers;

import core.Controller;
import modulo3_ventas.models.Producto;
import modulo3_ventas.models.Reserva;
import modulo3_ventas.repositories.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador de reservas de productos.
 * RF-02: Reserva de productos.
 */
public class ReservaController extends Controller {

    private final ReservaRepository  reservaRepo;
    private final ProductoRepository productoRepo;

    public ReservaController() {
        this.reservaRepo  = new ReservaRepositorySQL();
        this.productoRepo = new ProductoRepositorySQL();
    }

    @Override
    public void run() {
        // Vista se integrará en sprint siguiente
    }

    public List<Reserva> obtenerReservasActivas() {
        return reservaRepo.buscarActivas();
    }

    public List<Reserva> obtenerReservasPorCliente(int clienteId) {
        return reservaRepo.buscarPorCliente(clienteId);
    }

    public void crearReserva(int clienteId, int productoId, LocalDateTime expiracion) {
        Producto producto = productoRepo.buscarPorId(productoId);
        if (producto == null || !producto.estaDisponible()) return;

        Reserva reserva = new Reserva(0, clienteId, productoId, LocalDateTime.now(), expiracion);
        reservaRepo.guardar(reserva);

        producto.setEstado(Producto.EstadoProducto.RESERVADO);
        productoRepo.actualizar(producto);
    }

    public void confirmarReserva(int reservaId) {
        Reserva reserva = reservaRepo.buscarPorId(reservaId);
        if (reserva == null) return;
        reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
        reservaRepo.actualizar(reserva);
    }

    public void cancelarReserva(int reservaId) {
        Reserva reserva = reservaRepo.buscarPorId(reservaId);
        if (reserva == null) return;

        reserva.setEstado(Reserva.EstadoReserva.CANCELADA);
        reservaRepo.actualizar(reserva);

        Producto producto = productoRepo.buscarPorId(reserva.getProductoId());
        if (producto != null) {
            producto.setEstado(Producto.EstadoProducto.DISPONIBLE);
            productoRepo.actualizar(producto);
        }
    }

    /** Revierte a DISPONIBLE todas las reservas cuyo plazo venció (RF-02). */
    public void procesarReservasVencidas() {
        List<Reserva> vencidas = reservaRepo.buscarVencidas();
        for (Reserva reserva : vencidas) {
            reserva.setEstado(Reserva.EstadoReserva.VENCIDA);
            reservaRepo.actualizar(reserva);

            Producto producto = productoRepo.buscarPorId(reserva.getProductoId());
            if (producto != null) {
                producto.setEstado(Producto.EstadoProducto.DISPONIBLE);
                productoRepo.actualizar(producto);
            }
        }
    }
}
