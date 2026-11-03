package modulo1_inventario.repositories;

import database.DatabaseConnection;
import modulo1_inventario.models.DetalleOrdenCompra;
import modulo1_inventario.models.OrdenCompra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenCompraRepositorySQL implements OrdenCompraRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private OrdenCompra mapearOrden(ResultSet rs) throws SQLException {
        OrdenCompra o = new OrdenCompra();
        o.setId(rs.getInt("id")); o.setProveedorId(rs.getInt("proveedor_id"));
        o.setProveedorNombre(rs.getString("proveedor_nombre"));
        o.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
        o.setFechaEntregaEstimada(rs.getDate("fecha_entrega_estimada").toLocalDate());
        o.setEstado(OrdenCompra.EstadoOrden.valueOf(rs.getString("estado")));
        o.setTotal(rs.getDouble("total")); o.setObservaciones(rs.getString("observaciones"));
        return o;
    }

    private DetalleOrdenCompra mapearDetalle(ResultSet rs) throws SQLException {
        DetalleOrdenCompra d = new DetalleOrdenCompra();
        d.setId(rs.getInt("id")); d.setOrdenId(rs.getInt("orden_id"));
        d.setProductoId(rs.getInt("producto_id")); d.setProductoNombre(rs.getString("producto_nombre"));
        d.setCantidad(rs.getInt("cantidad")); d.setPrecioUnitario(rs.getDouble("precio_unitario"));
        d.setSubtotal(rs.getDouble("subtotal"));
        return d;
    }

    @Override public OrdenCompra buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_ordenes_compra WHERE id=?")) {
            ps.setInt(1,id); ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearOrden(rs);
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
        return null;
    }

    @Override public List<OrdenCompra> buscarTodas() {
        List<OrdenCompra> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_ordenes_compra ORDER BY fecha_emision DESC")) {
            ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapearOrden(rs));
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<OrdenCompra> buscarPorProveedor(int proveedorId) {
        List<OrdenCompra> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_ordenes_compra WHERE proveedor_id=?")) {
            ps.setInt(1,proveedorId); ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearOrden(rs));
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<OrdenCompra> buscarPorEstado(OrdenCompra.EstadoOrden estado) {
        List<OrdenCompra> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_ordenes_compra WHERE estado=?")) {
            ps.setString(1, estado.name()); ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearOrden(rs));
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<DetalleOrdenCompra> buscarDetalles(int ordenId) {
        List<DetalleOrdenCompra> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_detalle_orden_compra WHERE orden_id=?")) {
            ps.setInt(1,ordenId); ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearDetalle(rs));
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public void guardar(OrdenCompra o) {
        String sql = "INSERT INTO inv_ordenes_compra (proveedor_id, proveedor_nombre, fecha_emision, fecha_entrega_estimada, estado, total, observaciones) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,o.getProveedorId()); ps.setString(2,o.getProveedorNombre());
            ps.setDate(3, Date.valueOf(o.getFechaEmision()));
            ps.setDate(4, Date.valueOf(o.getFechaEntregaEstimada()));
            ps.setString(5,o.getEstado().name()); ps.setDouble(6,o.getTotal());
            ps.setString(7,o.getObservaciones()); ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); if (keys.next()) o.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
    }

    @Override public void guardarDetalle(DetalleOrdenCompra d) {
        String sql = "INSERT INTO inv_detalle_orden_compra (orden_id, producto_id, producto_nombre, cantidad, precio_unitario, subtotal) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,d.getOrdenId()); ps.setInt(2,d.getProductoId());
            ps.setString(3,d.getProductoNombre()); ps.setInt(4,d.getCantidad());
            ps.setDouble(5,d.getPrecioUnitario()); ps.setDouble(6,d.getSubtotal());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); if (keys.next()) d.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
    }

    @Override public void actualizar(OrdenCompra o) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE inv_ordenes_compra SET estado=?, total=?, observaciones=? WHERE id=?")) {
            ps.setString(1,o.getEstado().name()); ps.setDouble(2,o.getTotal());
            ps.setString(3,o.getObservaciones()); ps.setInt(4,o.getId()); ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
    }

    @Override public void eliminar(int id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM inv_ordenes_compra WHERE id=?")) {
            ps.setInt(1,id); ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[OrdenCompraRepo] " + e.getMessage()); }
    }
}