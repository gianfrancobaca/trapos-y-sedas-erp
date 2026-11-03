package modulo3_ventas.repositories;

import database.DatabaseConnection;
import modulo3_ventas.models.Venta;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQL del repositorio de ventas.
 */
public class VentaRepositorySQL implements VentaRepository {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    private Venta mapear(ResultSet rs) throws SQLException {
        Venta v = new Venta();
        v.setId(rs.getInt("id"));
        v.setClienteId(rs.getInt("cliente_id"));
        v.setVendedorId(rs.getInt("vendedor_id"));
        v.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        v.setEstado(Venta.EstadoVenta.valueOf(rs.getString("estado")));
        v.setMetodoPago(Venta.MetodoPago.valueOf(rs.getString("metodo_pago")));
        v.setSubtotal(rs.getDouble("subtotal"));
        v.setDescuento(rs.getDouble("descuento"));
        v.setIgv(rs.getDouble("igv"));
        v.setTotal(rs.getDouble("total"));
        v.setObservaciones(rs.getString("observaciones"));
        return v;
    }

    @Override
    public Venta buscarPorId(int id) {
        String sql = "SELECT * FROM ventas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[VentaRepo] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Venta> buscarTodas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY fecha_hora DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[VentaRepo] buscarTodas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Venta> buscarPorCliente(int clienteId) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE cliente_id = ? ORDER BY fecha_hora DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[VentaRepo] buscarPorCliente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Venta> buscarPorPeriodo(LocalDate inicio, LocalDate fin) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE DATE(fecha_hora) BETWEEN ? AND ? ORDER BY fecha_hora DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[VentaRepo] buscarPorPeriodo: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Venta> buscarPorEstado(Venta.EstadoVenta estado) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE estado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[VentaRepo] buscarPorEstado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(Venta venta) {
        String sql = "INSERT INTO ventas (cliente_id, vendedor_id, fecha_hora, estado, metodo_pago, subtotal, descuento, igv, total, observaciones) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, venta.getClienteId());
            ps.setInt(2, venta.getVendedorId());
            ps.setTimestamp(3, Timestamp.valueOf(venta.getFechaHora()));
            ps.setString(4, venta.getEstado().name());
            ps.setString(5, venta.getMetodoPago().name());
            ps.setDouble(6, venta.getSubtotal());
            ps.setDouble(7, venta.getDescuento());
            ps.setDouble(8, venta.getIgv());
            ps.setDouble(9, venta.getTotal());
            ps.setString(10, venta.getObservaciones());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) venta.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.err.println("[VentaRepo] guardar: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Venta venta) {
        String sql = "UPDATE ventas SET estado=?, metodo_pago=?, subtotal=?, descuento=?, igv=?, total=?, observaciones=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, venta.getEstado().name());
            ps.setString(2, venta.getMetodoPago().name());
            ps.setDouble(3, venta.getSubtotal());
            ps.setDouble(4, venta.getDescuento());
            ps.setDouble(5, venta.getIgv());
            ps.setDouble(6, venta.getTotal());
            ps.setString(7, venta.getObservaciones());
            ps.setInt(8, venta.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[VentaRepo] actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM ventas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[VentaRepo] eliminar: " + e.getMessage());
        }
    }
}
