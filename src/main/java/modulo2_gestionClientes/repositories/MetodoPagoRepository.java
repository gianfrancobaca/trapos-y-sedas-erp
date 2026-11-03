package modulo2_gestionClientes.repositories;

import modulo2_gestionClientes.models.MetodoPago;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoRepository {

    private Connection connection;

    public MetodoPagoRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void agregar(MetodoPago metodoPago) {
        String sql = "INSERT INTO metodo_pago (tipo, descripcion, id_cliente) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, metodoPago.getTipo());
            ps.setString(2, metodoPago.getDescripcion());
            ps.setInt(3, metodoPago.getCliente().getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(MetodoPago metodoPago) {
        String sql = "UPDATE metodo_pago SET tipo=?, descripcion=?, id_cliente=? WHERE id_metodo_pago=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, metodoPago.getTipo());
            ps.setString(2, metodoPago.getDescripcion());
            ps.setInt(3, metodoPago.getCliente().getIdCliente());
            ps.setInt(4, metodoPago.getIdMetodoPago());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idMetodoPago) {
        String sql = "DELETE FROM metodo_pago WHERE id_metodo_pago=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMetodoPago);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MetodoPago buscarPorId(int idMetodoPago) {
        String sql = "SELECT * FROM metodo_pago WHERE id_metodo_pago=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMetodoPago);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ClienteRepository clienteRepo = new ClienteRepository();

                return new MetodoPago(
                        rs.getInt("id_metodo_pago"),
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<MetodoPago> listarTodos() {
        List<MetodoPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM metodo_pago";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ClienteRepository clienteRepo = new ClienteRepository();

            while (rs.next()) {
                lista.add(new MetodoPago(
                        rs.getInt("id_metodo_pago"),
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<MetodoPago> listarPorCliente(int idCliente) {
        List<MetodoPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM metodo_pago WHERE id_cliente=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            ClienteRepository clienteRepo = new ClienteRepository();

            while (rs.next()) {
                lista.add(new MetodoPago(
                        rs.getInt("id_metodo_pago"),
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        clienteRepo.buscarPorId(idCliente)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}