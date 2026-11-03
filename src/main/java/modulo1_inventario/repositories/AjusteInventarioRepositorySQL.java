package modulo1_inventario.repositories;

import database.DatabaseConnection;
import modulo1_inventario.models.AjusteInventario;
import modulo1_inventario.models.DetalleAjuste;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AjusteInventarioRepositorySQL implements AjusteInventarioRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private AjusteInventario mapearAjuste(ResultSet rs) throws SQLException {
        AjusteInventario a = new AjusteInventario();
        a.setId(rs.getInt("id"));
        a.setTipo(AjusteInventario.TipoAjuste.valueOf(rs.getString("tipo")));
        a.setMotivo(rs.getString("motivo"));
        a.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        a.setUsuarioId(rs.getInt("usuario_id"));
        return a;
    }

    private DetalleAjuste mapearDetalle(ResultSet rs) throws SQLException {
        DetalleAjuste d = new DetalleAjuste();
        d.setId(rs.getInt("id")); d.setAjusteId(rs.getInt("ajuste_id"));
        d.setProductoId(rs.getInt("producto_id")); d.setProductoNombre(rs.getString("producto_nombre"));
        d.setStockAnterior(rs.getInt("stock_anterior")); d.setStockNuevo(rs.getInt("stock_nuevo"));
        d.setDiferencia(rs.getInt("diferencia"));
        return d;
    }

    @Override public AjusteInventario buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_ajustes WHERE id=?")) {
            ps.setInt(1,id); ResultSet rs = ps.executeQuery(); if (rs.next()) return mapearAjuste(rs);
        } catch (SQLException e) { System.err.println("[AjusteRepo] " + e.getMessage()); }
        return null;
    }

    @Override public List<AjusteInventario> buscarTodos() {
        List<AjusteInventario> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_ajustes ORDER BY fecha_hora DESC")) {
            ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapearAjuste(rs));
        } catch (SQLException e) { System.err.println("[AjusteRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<DetalleAjuste> buscarDetalles(int ajusteId) {
        List<DetalleAjuste> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_detalle_ajuste WHERE ajuste_id=?")) {
            ps.setInt(1,ajusteId); ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapearDetalle(rs));
        } catch (SQLException e) { System.err.println("[AjusteRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public void guardar(AjusteInventario a) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO inv_ajustes (tipo, motivo, fecha_hora, usuario_id) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,a.getTipo().name()); ps.setString(2,a.getMotivo());
            ps.setTimestamp(3,Timestamp.valueOf(a.getFechaHora())); ps.setInt(4,a.getUsuarioId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); if (keys.next()) a.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[AjusteRepo] " + e.getMessage()); }
    }

    @Override public void guardarDetalle(DetalleAjuste d) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO inv_detalle_ajuste (ajuste_id, producto_id, producto_nombre, stock_anterior, stock_nuevo, diferencia) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,d.getAjusteId()); ps.setInt(2,d.getProductoId());
            ps.setString(3,d.getProductoNombre()); ps.setInt(4,d.getStockAnterior());
            ps.setInt(5,d.getStockNuevo()); ps.setInt(6,d.getDiferencia());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); if (keys.next()) d.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[AjusteRepo] " + e.getMessage()); }
    }
}