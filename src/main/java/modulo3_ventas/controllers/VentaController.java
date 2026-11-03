package modulo3_ventas.controllers;

import core.Controller;
import database.DatabaseConnection;
import modulo3_ventas.exceptions.RegistroVentaException;
import modulo3_ventas.models.*;
import modulo3_ventas.repositories.*;
import java.sql.Connection;
import java.sql.SQLException;
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

    /**
     * Registra la venta y todos sus detalles en una sola transacción:
     * o se guardan todos, o no se guarda ninguno.
     *
     * @throws RegistroVentaException si algún insert falla (se hace rollback)
     */
    public void registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un detalle.");
        }

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            try {
                ventaRepo.guardar(venta, conn);
                if (venta.getId() == 0) {
                    throw new SQLException("La base de datos no devolvió el id de la venta.");
                }
                for (DetalleVenta detalle : detalles) {
                    detalle.setVentaId(venta.getId());
                    detalleRepo.guardar(detalle, conn);
                }
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                venta.setId(0); // el id generado quedó invalidado por el rollback
                rollbackSilencioso(conn);
                throw new RegistroVentaException("No se pudo registrar la venta; se revirtieron los cambios.", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RegistroVentaException("Error de conexión al registrar la venta.", e);
        }
    }

    private void rollbackSilencioso(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            System.err.println("[VentaController] rollback falló: " + e.getMessage());
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
