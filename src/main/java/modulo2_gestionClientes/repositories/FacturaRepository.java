package modulo2_gestionClientes.repositories;

import modulo2_gestionClientes.models.Factura;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaRepository {

    private Connection connection;

    public FacturaRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void agregar(Factura factura) {
        String sql = "INSERT INTO factura (fecha, total, estado, id_pedido, id_cliente) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(factura.getFecha().getTime()));
            ps.setDouble(2, factura.getTotal());
            ps.setString(3, factura.getEstado());
            ps.setInt(4, factura.getPedido().getIdPedido());
            ps.setInt(5, factura.getCliente().getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Factura factura) {
        String sql = "UPDATE factura SET fecha=?, total=?, estado=?, id_pedido=?, id_cliente=? WHERE id_factura=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(factura.getFecha().getTime()));
            ps.setDouble(2, factura.getTotal());
            ps.setString(3, factura.getEstado());
            ps.setInt(4, factura.getPedido().getIdPedido());
            ps.setInt(5, factura.getCliente().getIdCliente());
            ps.setInt(6, factura.getIdFactura());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idFactura) {
        String sql = "DELETE FROM factura WHERE id_factura=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Factura buscarPorId(int idFactura) {
        String sql = "SELECT * FROM factura WHERE id_factura=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PedidoRepository pedidoRepo = new PedidoRepository();
                ClienteRepository clienteRepo = new ClienteRepository();
                return new Factura(
                        rs.getInt("id_factura"),
                        rs.getDate("fecha"),
                        rs.getDouble("total"),
                        rs.getString("estado"),
                        pedidoRepo.buscarPorId(rs.getInt("id_pedido")),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Factura> listarTodos() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM factura";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            PedidoRepository pedidoRepo = new PedidoRepository();
            ClienteRepository clienteRepo = new ClienteRepository();
            while (rs.next()) {
                lista.add(new Factura(
                        rs.getInt("id_factura"),
                        rs.getDate("fecha"),
                        rs.getDouble("total"),
                        rs.getString("estado"),
                        pedidoRepo.buscarPorId(rs.getInt("id_pedido")),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}