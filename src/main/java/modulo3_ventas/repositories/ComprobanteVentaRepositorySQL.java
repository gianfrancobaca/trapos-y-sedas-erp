package modulo3_ventas.repositories;

import database.DatabaseConnection;
import modulo3_ventas.models.ComprobanteVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQL del repositorio de comprobantes de venta.
 */
public class ComprobanteVentaRepositorySQL implements ComprobanteVentaRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private ComprobanteVenta mapear(ResultSet rs) throws SQLException {
        ComprobanteVenta c = new ComprobanteVenta();
        c.setId(rs.getInt("id"));
        c.setVentaId(rs.getInt("venta_id"));
        c.setTipo(ComprobanteVenta.TipoComprobante.valueOf(rs.getString("tipo")));
        c.setSerie(rs.getString("serie"));
        c.setNumero(rs.getInt("numero"));
        c.setFechaEmision(rs.getTimestamp("fecha_emision").toLocalDateTime());
        c.setMontoTotal(rs.getDouble("monto_total"));
        c.setAnulado(rs.getBoolean("anulado"));
        return c;
    }

    @Override
    public ComprobanteVenta buscarPorId(int id) {
        String sql = "SELECT * FROM comprobantes_venta WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ComprobanteRepo] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public ComprobanteVenta buscarPorVenta(int ventaId) {
        String sql = "SELECT * FROM comprobantes_venta WHERE venta_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ventaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ComprobanteRepo] buscarPorVenta: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ComprobanteVenta> buscarTodos() {
        List<ComprobanteVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM comprobantes_venta ORDER BY fecha_emision DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ComprobanteRepo] buscarTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<ComprobanteVenta> buscarPorTipo(ComprobanteVenta.TipoComprobante tipo) {
        List<ComprobanteVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM comprobantes_venta WHERE tipo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ComprobanteRepo] buscarPorTipo: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(ComprobanteVenta comprobante) {
        String sql = "INSERT INTO comprobantes_venta (venta_id, tipo, serie, numero, fecha_emision, monto_total, anulado) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, comprobante.getVentaId());
            ps.setString(2, comprobante.getTipo().name());
            ps.setString(3, comprobante.getSerie());
            ps.setInt(4, comprobante.getNumero());
            ps.setTimestamp(5, Timestamp.valueOf(comprobante.getFechaEmision()));
            ps.setDouble(6, comprobante.getMontoTotal());
            ps.setBoolean(7, comprobante.isAnulado());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) comprobante.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.err.println("[ComprobanteRepo] guardar: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(ComprobanteVenta comprobante) {
        String sql = "UPDATE comprobantes_venta SET tipo=?, serie=?, numero=?, monto_total=?, anulado=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, comprobante.getTipo().name());
            ps.setString(2, comprobante.getSerie());
            ps.setInt(3, comprobante.getNumero());
            ps.setDouble(4, comprobante.getMontoTotal());
            ps.setBoolean(5, comprobante.isAnulado());
            ps.setInt(6, comprobante.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ComprobanteRepo] actualizar: " + e.getMessage());
        }
    }

    @Override
    public void anular(int id) {
        String sql = "UPDATE comprobantes_venta SET anulado = true WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ComprobanteRepo] anular: " + e.getMessage());
        }
    }
}
