package modulo2_gestionClientes.repositories;

import modulo2_gestionClientes.interfaces.IPedidoRepository;
import modulo2_gestionClientes.models.Cliente;
import modulo2_gestionClientes.models.Pedido;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository implements IPedidoRepository {

    private Connection connection;

    public PedidoRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void agregar(Pedido pedido) {
        String sql = "INSERT INTO pedido (fecha, estado, id_cliente) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            ps.setString(2, pedido.getEstado());
            ps.setInt(3, pedido.getCliente().getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET fecha=?, estado=?, id_cliente=? WHERE id_pedido=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            ps.setString(2, pedido.getEstado());
            ps.setInt(3, pedido.getCliente().getIdCliente());
            ps.setInt(4, pedido.getIdPedido());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idPedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pedido buscarPorId(int idPedido) {
        String sql = "SELECT * FROM pedido WHERE id_pedido=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ClienteRepository clienteRepo = new ClienteRepository();
                Cliente cliente = clienteRepo.buscarPorId(rs.getInt("id_cliente"));
                return new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha"),
                        rs.getString("estado"),
                        cliente,
                        new ArrayList<>()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ClienteRepository clienteRepo = new ClienteRepository();
            while (rs.next()) {
                Cliente cliente = clienteRepo.buscarPorId(rs.getInt("id_cliente"));
                lista.add(new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha"),
                        rs.getString("estado"),
                        cliente,
                        new ArrayList<>()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Pedido> listarPorCliente(int idCliente) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_cliente=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            ClienteRepository clienteRepo = new ClienteRepository();
            while (rs.next()) {
                Cliente cliente = clienteRepo.buscarPorId(rs.getInt("id_cliente"));
                lista.add(new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha"),
                        rs.getString("estado"),
                        cliente,
                        new ArrayList<>()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
