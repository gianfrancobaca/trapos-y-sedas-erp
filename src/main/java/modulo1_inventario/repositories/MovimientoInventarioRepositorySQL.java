package modulo1_inventario.repositories;

import database.DatabaseConnection;
import modulo1_inventario.models.MovimientoInventario;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioRepositorySQL implements MovimientoInventarioRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private MovimientoInventario mapear(ResultSet rs) throws SQLException {
        MovimientoInventario m = new MovimientoInventario();
        m.setId(rs.getInt("id")); m.setProductoId(rs.getInt("producto_id"));
        m.setProductoNombre(rs.getString("producto_nombre"));
        m.setTipo(MovimientoInventario.TipoMovimiento.valueOf(rs.getString("tipo")));
        m.setCantidad(rs.getInt("cantidad")); m.setStockAnterior(rs.getInt("stock_anterior"));
        m.setStockResultante(rs.getInt("stock_resultante")); m.setMotivo(rs.getString("motivo"));
        m.setReferenciaDocumento(rs.getString("referencia_documento"));
        m.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        m.setUsuarioId(rs.getInt("usuario_id"));
        return m;
    }

    @Override public MovimientoInventario buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_movimientos WHERE id=?")) {
            ps.setInt(1,id); ResultSet rs = ps.executeQuery(); if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[MovimientoRepo] " + e.getMessage()); }
        return null;
    }

    @Override public List<MovimientoInventario> buscarPorProducto(int productoId) {
        List<MovimientoInventario> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_movimientos WHERE producto_id=? ORDER BY fecha_hora DESC")) {
            ps.setInt(1,productoId); ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[MovimientoRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<MovimientoInventario> buscarPorTipo(MovimientoInventario.TipoMovimiento tipo) {
        List<MovimientoInventario> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_movimientos WHERE tipo=? ORDER BY fecha_hora DESC")) {
            ps.setString(1,tipo.name()); ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[MovimientoRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<MovimientoInventario> buscarPorPeriodo(LocalDate inicio, LocalDate fin) {
        List<MovimientoInventario> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_movimientos WHERE DATE(fecha_hora) BETWEEN ? AND ? ORDER BY fecha_hora DESC")) {
            ps.setDate(1,Date.valueOf(inicio)); ps.setDate(2,Date.valueOf(fin));
            ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[MovimientoRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<MovimientoInventario> buscarTodos() {
        List<MovimientoInventario> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_movimientos ORDER BY fecha_hora DESC")) {
            ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[MovimientoRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public void guardar(MovimientoInventario m) {
        String sql = "INSERT INTO inv_movimientos (producto_id, producto_nombre, tipo, cantidad, stock_anterior, stock_resultante, motivo, referencia_documento, fecha_hora, usuario_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,m.getProductoId()); ps.setString(2,m.getProductoNombre());
            ps.setString(3,m.getTipo().name()); ps.setInt(4,m.getCantidad());
            ps.setInt(5,m.getStockAnterior()); ps.setInt(6,m.getStockResultante());
            ps.setString(7,m.getMotivo()); ps.setString(8,m.getReferenciaDocumento());
            ps.setTimestamp(9,Timestamp.valueOf(m.getFechaHora())); ps.setInt(10,m.getUsuarioId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); if (keys.next()) m.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[MovimientoRepo] " + e.getMessage()); }
    }
}