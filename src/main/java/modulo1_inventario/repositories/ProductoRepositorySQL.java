package modulo1_inventario.repositories;

import database.DatabaseConnection;
import modulo1_inventario.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorySQL implements ProductoRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setCategoriaNombre(rs.getString("categoria_nombre"));
        p.setPrecioCompra(rs.getDouble("precio_compra"));
        p.setPrecioVenta(rs.getDouble("precio_venta"));
        p.setStockActual(rs.getInt("stock_actual"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setEstado(Producto.EstadoProducto.valueOf(rs.getString("estado")));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }

    @Override
    public Producto buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_productos WHERE id=?")) {
            ps.setInt(1, id); ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
        return null;
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_productos WHERE codigo=?")) {
            ps.setString(1, codigo); ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
        return null;
    }

    @Override
    public List<Producto> buscarTodos() {
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_productos ORDER BY nombre")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
        return lista;
    }

    @Override
    public List<Producto> buscarActivos() {
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_productos WHERE activo=true ORDER BY nombre")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
        return lista;
    }

    @Override
    public List<Producto> buscarPorCategoria(int categoriaId) {
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_productos WHERE categoria_id=? AND activo=true")) {
            ps.setInt(1, categoriaId); ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
        return lista;
    }

    @Override
    public List<Producto> buscarConStockBajo() {
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_productos WHERE stock_actual <= stock_minimo AND activo=true")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
        return lista;
    }

    @Override
    public void guardar(Producto p) {
        String sql = "INSERT INTO inv_productos (codigo, nombre, descripcion, categoria_id, categoria_nombre, precio_compra, precio_venta, stock_actual, stock_minimo, estado, activo) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCodigo()); ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion()); ps.setInt(4, p.getCategoriaId());
            ps.setString(5, p.getCategoriaNombre()); ps.setDouble(6, p.getPrecioCompra());
            ps.setDouble(7, p.getPrecioVenta()); ps.setInt(8, p.getStockActual());
            ps.setInt(9, p.getStockMinimo()); ps.setString(10, p.getEstado().name());
            ps.setBoolean(11, p.isActivo()); ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
    }

    @Override
    public void actualizar(Producto p) {
        String sql = "UPDATE inv_productos SET codigo=?, nombre=?, descripcion=?, categoria_id=?, categoria_nombre=?, precio_compra=?, precio_venta=?, stock_actual=?, stock_minimo=?, estado=?, activo=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo()); ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion()); ps.setInt(4, p.getCategoriaId());
            ps.setString(5, p.getCategoriaNombre()); ps.setDouble(6, p.getPrecioCompra());
            ps.setDouble(7, p.getPrecioVenta()); ps.setInt(8, p.getStockActual());
            ps.setInt(9, p.getStockMinimo()); ps.setString(10, p.getEstado().name());
            ps.setBoolean(11, p.isActivo()); ps.setInt(12, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
    }

    @Override
    public void eliminar(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE inv_productos SET activo=false WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[ProductoRepo] " + e.getMessage()); }
    }
}