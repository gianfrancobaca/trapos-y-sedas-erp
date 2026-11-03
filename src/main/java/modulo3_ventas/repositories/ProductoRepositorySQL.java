package modulo3_ventas.repositories;

import database.DatabaseConnection;
import modulo3_ventas.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQL del repositorio de productos.
 */
public class ProductoRepositorySQL implements ProductoRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setCategoria(Producto.CategoriaProducto.valueOf(rs.getString("categoria")));
        p.setPrecioVenta(rs.getDouble("precio_venta"));
        p.setStockDisponible(rs.getInt("stock_disponible"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }

    @Override
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] buscarPorCodigo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Producto> buscarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] buscarTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Producto> buscarActivos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE activo = true ORDER BY nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] buscarActivos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Producto> buscarPorCategoria(Producto.CategoriaProducto categoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria = ? AND activo = true";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] buscarPorCategoria: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Producto> buscarConStock() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock_disponible > 0 AND activo = true";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] buscarConStock: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(Producto producto) {
        String sql = "INSERT INTO productos (codigo, nombre, descripcion, categoria, precio_venta, stock_disponible, activo) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getDescripcion());
            ps.setString(4, producto.getCategoria().name());
            ps.setDouble(5, producto.getPrecioVenta());
            ps.setInt(6, producto.getStockDisponible());
            ps.setBoolean(7, producto.isActivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) producto.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] guardar: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Producto producto) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, descripcion=?, categoria=?, precio_venta=?, stock_disponible=?, activo=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getDescripcion());
            ps.setString(4, producto.getCategoria().name());
            ps.setDouble(5, producto.getPrecioVenta());
            ps.setInt(6, producto.getStockDisponible());
            ps.setBoolean(7, producto.isActivo());
            ps.setInt(8, producto.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "UPDATE productos SET activo = false WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ProductoRepo] eliminar: " + e.getMessage());
        }
    }
}
