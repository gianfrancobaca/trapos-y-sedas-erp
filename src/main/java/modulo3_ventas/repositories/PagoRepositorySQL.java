package modulo3_ventas.repositories;

import database.DatabaseConnection;
import modulo3_ventas.models.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQL del repositorio de pagos.
 */
public class PagoRepositorySQL implements PagoRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Pago mapear(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setId(rs.getInt("id"));
        p.setVentaId(rs.getInt("venta_id"));
        p.setMonto(rs.getDouble("monto"));
        p.setMetodo(Pago.MetodoPago.valueOf(rs.getString("metodo")));
        p.setEstado(Pago.EstadoPago.valueOf(rs.getString("estado")));
        p.setFechaPago(rs.getTimestamp("fecha_pago").toLocalDateTime());
        p.setReferencia(rs.getString("referencia"));
        return p;
    }

    @Override
    public Pago buscarPorId(int id) {
        String sql = "SELECT * FROM pagos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[PagoRepo] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Pago> buscarPorVenta(int ventaId) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE venta_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ventaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[PagoRepo] buscarPorVenta: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Pago> buscarPorEstado(Pago.EstadoPago estado) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE estado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[PagoRepo] buscarPorEstado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(Pago pago) {
        String sql = "INSERT INTO pagos (venta_id, monto, metodo, estado, fecha_pago, referencia) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pago.getVentaId());
            ps.setDouble(2, pago.getMonto());
            ps.setString(3, pago.getMetodo().name());
            ps.setString(4, pago.getEstado().name());
            ps.setTimestamp(5, Timestamp.valueOf(pago.getFechaPago()));
            ps.setString(6, pago.getReferencia());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) pago.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.err.println("[PagoRepo] guardar: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Pago pago) {
        String sql = "UPDATE pagos SET monto=?, metodo=?, estado=?, referencia=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, pago.getMonto());
            ps.setString(2, pago.getMetodo().name());
            ps.setString(3, pago.getEstado().name());
            ps.setString(4, pago.getReferencia());
            ps.setInt(5, pago.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[PagoRepo] actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM pagos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[PagoRepo] eliminar: " + e.getMessage());
        }
    }
}
