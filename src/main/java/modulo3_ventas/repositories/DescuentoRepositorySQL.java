package modulo3_ventas.repositories;

import database.DatabaseConnection;
import modulo3_ventas.models.Descuento;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQL del repositorio de descuentos.
 */
public class DescuentoRepositorySQL implements DescuentoRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Descuento mapear(ResultSet rs) throws SQLException {
        Descuento d = new Descuento();
        d.setId(rs.getInt("id"));
        d.setNombre(rs.getString("nombre"));
        d.setDescripcion(rs.getString("descripcion"));
        d.setTipo(Descuento.TipoDescuento.valueOf(rs.getString("tipo")));
        d.setValor(rs.getDouble("valor"));
        d.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        d.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        d.setActivo(rs.getBoolean("activo"));
        return d;
    }

    @Override
    public Descuento buscarPorId(int id) {
        String sql = "SELECT * FROM descuentos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[DescuentoRepo] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Descuento> buscarTodos() {
        List<Descuento> lista = new ArrayList<>();
        String sql = "SELECT * FROM descuentos ORDER BY fecha_inicio DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DescuentoRepo] buscarTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Descuento> buscarActivos() {
        List<Descuento> lista = new ArrayList<>();
        String sql = "SELECT * FROM descuentos WHERE activo = true";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DescuentoRepo] buscarActivos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Descuento> buscarVigentes(LocalDate fecha) {
        List<Descuento> lista = new ArrayList<>();
        String sql = "SELECT * FROM descuentos WHERE activo = true AND fecha_inicio <= ? AND fecha_fin >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            ps.setDate(2, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DescuentoRepo] buscarVigentes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(Descuento descuento) {
        String sql = "INSERT INTO descuentos (nombre, descripcion, tipo, valor, fecha_inicio, fecha_fin, activo) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, descuento.getNombre());
            ps.setString(2, descuento.getDescripcion());
            ps.setString(3, descuento.getTipo().name());
            ps.setDouble(4, descuento.getValor());
            ps.setDate(5, Date.valueOf(descuento.getFechaInicio()));
            ps.setDate(6, Date.valueOf(descuento.getFechaFin()));
            ps.setBoolean(7, descuento.isActivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) descuento.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.err.println("[DescuentoRepo] guardar: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Descuento descuento) {
        String sql = "UPDATE descuentos SET nombre=?, descripcion=?, tipo=?, valor=?, fecha_inicio=?, fecha_fin=?, activo=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descuento.getNombre());
            ps.setString(2, descuento.getDescripcion());
            ps.setString(3, descuento.getTipo().name());
            ps.setDouble(4, descuento.getValor());
            ps.setDate(5, Date.valueOf(descuento.getFechaInicio()));
            ps.setDate(6, Date.valueOf(descuento.getFechaFin()));
            ps.setBoolean(7, descuento.isActivo());
            ps.setInt(8, descuento.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DescuentoRepo] actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM descuentos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DescuentoRepo] eliminar: " + e.getMessage());
        }
    }
}
