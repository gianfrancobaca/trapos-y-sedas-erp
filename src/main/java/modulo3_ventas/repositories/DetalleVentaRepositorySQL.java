package modulo3_ventas.repositories;

import database.DatabaseConnection;
import modulo3_ventas.models.DetalleVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQL del repositorio de detalles de venta.
 */
public class DetalleVentaRepositorySQL implements DetalleVentaRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private DetalleVenta mapear(ResultSet rs) throws SQLException {
        DetalleVenta d = new DetalleVenta();
        d.setId(rs.getInt("id"));
        d.setVentaId(rs.getInt("venta_id"));
        d.setProductoId(rs.getInt("producto_id"));
        d.setProductoNombre(rs.getString("producto_nombre"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setPrecioUnitario(rs.getDouble("precio_unitario"));
        d.setDescuentoLinea(rs.getDouble("descuento_linea"));
        d.setSubtotal(rs.getDouble("subtotal"));
        return d;
    }

    @Override
    public DetalleVenta buscarPorId(int id) {
        String sql = "SELECT * FROM detalle_ventas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[DetalleVentaRepo] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<DetalleVenta> buscarPorVenta(int ventaId) {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_ventas WHERE venta_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ventaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleVentaRepo] buscarPorVenta: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<DetalleVenta> buscarPorProducto(int productoId) {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_ventas WHERE producto_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleVentaRepo] buscarPorProducto: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(DetalleVenta detalle) {
        String sql = "INSERT INTO detalle_ventas (venta_id, producto_id, producto_nombre, cantidad, precio_unitario, descuento_linea, subtotal) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getVentaId());
            ps.setInt(2, detalle.getProductoId());
            ps.setString(3, detalle.getProductoNombre());
            ps.setInt(4, detalle.getCantidad());
            ps.setDouble(5, detalle.getPrecioUnitario());
            ps.setDouble(6, detalle.getDescuentoLinea());
            ps.setDouble(7, detalle.getSubtotal());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) detalle.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.err.println("[DetalleVentaRepo] guardar: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(DetalleVenta detalle) {
        String sql = "UPDATE detalle_ventas SET cantidad=?, precio_unitario=?, descuento_linea=?, subtotal=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getPrecioUnitario());
            ps.setDouble(3, detalle.getDescuentoLinea());
            ps.setDouble(4, detalle.getSubtotal());
            ps.setInt(5, detalle.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DetalleVentaRepo] actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM detalle_ventas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DetalleVentaRepo] eliminar: " + e.getMessage());
        }
    }

    @Override
    public void eliminarPorVenta(int ventaId) {
        String sql = "DELETE FROM detalle_ventas WHERE venta_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ventaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DetalleVentaRepo] eliminarPorVenta: " + e.getMessage());
        }
    }
}
