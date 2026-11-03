package modulo1_inventario.repositories;

import database.DatabaseConnection;
import modulo1_inventario.models.DetalleDevolucion;
import modulo1_inventario.models.Devolucion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DevolucionRepositorySQL implements DevolucionRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Devolucion mapear(ResultSet rs) throws SQLException {
        Devolucion d = new Devolucion();
        d.setId(rs.getInt("id"));
        d.setTipo(Devolucion.TipoDevolucion.valueOf(rs.getString("tipo")));
        d.setReferenciaId(rs.getInt("referencia_id")); d.setMotivo(rs.getString("motivo"));
        d.setEstado(Devolucion.EstadoDevolucion.valueOf(rs.getString("estado")));
        d.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        d.setUsuarioId(rs.getInt("usuario_id"));
        return d;
    }

    private DetalleDevolucion mapearDetalle(ResultSet rs) throws SQLException {
        DetalleDevolucion dd = new DetalleDevolucion();
        dd.setId(rs.getInt("id")); dd.setDevolucionId(rs.getInt("devolucion_id"));
        dd.setProductoId(rs.getInt("producto_id")); dd.setProductoNombre(rs.getString("producto_nombre"));
        dd.setCantidad(rs.getInt("cantidad")); dd.setMotivoLinea(rs.getString("motivo_linea"));
        return dd;
    }

    @Override public Devolucion buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_devoluciones WHERE id=?")) {
            ps.setInt(1,id); ResultSet rs = ps.executeQuery(); if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[DevolucionRepo] " + e.getMessage()); }
        return null;
    }

    @Override public List<Devolucion> buscarTodas() {
        List<Devolucion> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_devoluciones ORDER BY fecha_hora DESC")) {
            ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[DevolucionRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<Devolucion> buscarPorTipo(Devolucion.TipoDevolucion tipo) {
        List<Devolucion> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_devoluciones WHERE tipo=?")) {
            ps.setString(1,tipo.name()); ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[DevolucionRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<DetalleDevolucion> buscarDetalles(int devolucionId) {
        List<DetalleDevolucion> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_detalle_devolucion WHERE devolucion_id=?")) {
            ps.setInt(1,devolucionId); ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapearDetalle(rs));
        } catch (SQLException e) { System.err.println("[DevolucionRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public void guardar(Devolucion d) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO inv_devoluciones (tipo, referencia_id, motivo, estado, fecha_hora, usuario_id) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,d.getTipo().name()); ps.setInt(2,d.getReferenciaId());
            ps.setString(3,d.getMotivo()); ps.setString(4,d.getEstado().name());
            ps.setTimestamp(5,Timestamp.valueOf(d.getFechaHora())); ps.setInt(6,d.getUsuarioId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); if (keys.next()) d.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[DevolucionRepo] " + e.getMessage()); }
    }

    @Override public void guardarDetalle(DetalleDevolucion dd) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO inv_detalle_devolucion (devolucion_id, producto_id, producto_nombre, cantidad, motivo_linea) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,dd.getDevolucionId()); ps.setInt(2,dd.getProductoId());
            ps.setString(3,dd.getProductoNombre()); ps.setInt(4,dd.getCantidad());
            ps.setString(5,dd.getMotivoLinea()); ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); if (keys.next()) dd.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[DevolucionRepo] " + e.getMessage()); }
    }

    @Override public void actualizarEstado(int id, Devolucion.EstadoDevolucion estado) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE inv_devoluciones SET estado=? WHERE id=?")) {
            ps.setString(1,estado.name()); ps.setInt(2,id); ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[DevolucionRepo] " + e.getMessage()); }
    }
}
