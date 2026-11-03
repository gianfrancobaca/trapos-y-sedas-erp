package modulo1_inventario.repositories;

import database.DatabaseConnection;
import modulo1_inventario.models.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositorySQL implements CategoriaRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getInt("id"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setActivo(rs.getBoolean("activo"));
        return c;
    }

    @Override
    public Categoria buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_categorias WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[CategoriaRepo] " + e.getMessage()); }
        return null;
    }

    @Override
    public List<Categoria> buscarTodas() {
        List<Categoria> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_categorias ORDER BY nombre")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[CategoriaRepo] " + e.getMessage()); }
        return lista;
    }

    @Override
    public List<Categoria> buscarActivas() {
        List<Categoria> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_categorias WHERE activo=true ORDER BY nombre")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[CategoriaRepo] " + e.getMessage()); }
        return lista;
    }

    @Override
    public void guardar(Categoria c) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO inv_categorias (nombre, descripcion, activo) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setBoolean(3, c.isActivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) c.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[CategoriaRepo] " + e.getMessage()); }
    }

    @Override
    public void actualizar(Categoria c) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE inv_categorias SET nombre=?, descripcion=?, activo=? WHERE id=?")) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setBoolean(3, c.isActivo());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[CategoriaRepo] " + e.getMessage()); }
    }

    @Override
    public void eliminar(int id) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE inv_categorias SET activo=false WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[CategoriaRepo] " + e.getMessage()); }
    }
}