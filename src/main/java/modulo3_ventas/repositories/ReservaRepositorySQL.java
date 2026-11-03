package modulo3_ventas.repositories;

import database.DatabaseConnection;
import modulo3_ventas.models.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQL del repositorio de reservas.
 */
public class ReservaRepositorySQL implements ReservaRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Reserva mapear(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setId(rs.getInt("id"));
        r.setClienteId(rs.getInt("cliente_id"));
        r.setProductoId(rs.getInt("producto_id"));
        r.setFechaReserva(rs.getTimestamp("fecha_reserva").toLocalDateTime());
        r.setFechaExpiracion(rs.getTimestamp("fecha_expiracion").toLocalDateTime());
        r.setEstado(Reserva.EstadoReserva.valueOf(rs.getString("estado")));
        r.setObservaciones(rs.getString("observaciones"));
        return r;
    }

    @Override
    public Reserva buscarPorId(int id) {
        String sql = "SELECT * FROM reservas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ReservaRepo] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reserva> buscarPorCliente(int clienteId) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE cliente_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ReservaRepo] buscarPorCliente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> buscarPorProducto(int productoId) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE producto_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ReservaRepo] buscarPorProducto: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> buscarActivas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE estado = 'ACTIVA'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ReservaRepo] buscarActivas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> buscarVencidas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE estado = 'ACTIVA' AND fecha_expiracion < NOW()";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ReservaRepo] buscarVencidas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(Reserva reserva) {
        String sql = "INSERT INTO reservas (cliente_id, producto_id, fecha_reserva, fecha_expiracion, estado, observaciones) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reserva.getClienteId());
            ps.setInt(2, reserva.getProductoId());
            ps.setTimestamp(3, Timestamp.valueOf(reserva.getFechaReserva()));
            ps.setTimestamp(4, Timestamp.valueOf(reserva.getFechaExpiracion()));
            ps.setString(5, reserva.getEstado().name());
            ps.setString(6, reserva.getObservaciones());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) reserva.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.err.println("[ReservaRepo] guardar: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Reserva reserva) {
        String sql = "UPDATE reservas SET estado=?, fecha_expiracion=?, observaciones=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reserva.getEstado().name());
            ps.setTimestamp(2, Timestamp.valueOf(reserva.getFechaExpiracion()));
            ps.setString(3, reserva.getObservaciones());
            ps.setInt(4, reserva.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ReservaRepo] actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ReservaRepo] eliminar: " + e.getMessage());
        }
    }
}
