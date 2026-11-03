package modulo1_inventario.repositories;

import database.DatabaseConnection;
import modulo1_inventario.models.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorRepositorySQL implements ProveedorRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Proveedor mapear(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setId(rs.getInt("id"));
        String tipoDoc = rs.getString("tipo_documento");
        p.setTipoDocumento(tipoDoc != null ? Proveedor.TipoDocumento.valueOf(tipoDoc) : Proveedor.TipoDocumento.NINGUNO);
        p.setNumeroDocumento(rs.getString("numero_documento"));
        p.setRazonSocial(rs.getString("razon_social"));
        p.setContacto(rs.getString("contacto"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setDireccion(rs.getString("direccion"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }

    @Override public Proveedor buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_proveedores WHERE id=?")) {
            ps.setInt(1, id); ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[ProveedorRepo] " + e.getMessage()); }
        return null;
    }

    @Override public Proveedor buscarPorNumeroDocumento(String numeroDocumento) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_proveedores WHERE numero_documento=?")) {
            ps.setString(1, numeroDocumento); ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[ProveedorRepo] " + e.getMessage()); }
        return null;
    }

    @Override public List<Proveedor> buscarTodos() {
        List<Proveedor> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_proveedores ORDER BY razon_social")) {
            ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[ProveedorRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public List<Proveedor> buscarActivos() {
        List<Proveedor> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inv_proveedores WHERE activo=true ORDER BY razon_social")) {
            ResultSet rs = ps.executeQuery(); while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[ProveedorRepo] " + e.getMessage()); }
        return lista;
    }

    @Override public void guardar(Proveedor p) {
        String sql = "INSERT INTO inv_proveedores (tipo_documento, numero_documento, razon_social, contacto, telefono, correo, direccion, activo) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getTipoDocumento().name());
            ps.setString(2, p.getNumeroDocumento());
            ps.setString(3, p.getRazonSocial());
            ps.setString(4, p.getContacto());
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getCorreo());
            ps.setString(7, p.getDireccion());
            ps.setBoolean(8, p.isActivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); if (keys.next()) p.setId(keys.getInt(1));
        } catch (SQLException e) { System.err.println("[ProveedorRepo] " + e.getMessage()); }
    }

    @Override public void actualizar(Proveedor p) {
        String sql = "UPDATE inv_proveedores SET tipo_documento=?, numero_documento=?, razon_social=?, contacto=?, telefono=?, correo=?, direccion=?, activo=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getTipoDocumento().name());
            ps.setString(2, p.getNumeroDocumento());
            ps.setString(3, p.getRazonSocial());
            ps.setString(4, p.getContacto());
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getCorreo());
            ps.setString(7, p.getDireccion());
            ps.setBoolean(8, p.isActivo());
            ps.setInt(9, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[ProveedorRepo] " + e.getMessage()); }
    }

    @Override public void eliminar(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE inv_proveedores SET activo=false WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[ProveedorRepo] " + e.getMessage()); }
    }
}