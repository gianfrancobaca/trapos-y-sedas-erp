package modulo2_gestionClientes.repositories;

import modulo2_gestionClientes.models.Reclamo;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamoRepository {

    private Connection connection;

    public ReclamoRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void agregar(Reclamo reclamo) {
        String sql = "INSERT INTO reclamo (motivo, estado, fecha, id_cliente) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reclamo.getMotivo());
            ps.setString(2, reclamo.getEstado());
            ps.setDate(3, new java.sql.Date(reclamo.getFecha().getTime()));
            ps.setInt(4, reclamo.getCliente().getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Reclamo reclamo) {
        String sql = "UPDATE reclamo SET motivo=?, estado=?, fecha=?, id_cliente=? WHERE id_reclamo=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reclamo.getMotivo());
            ps.setString(2, reclamo.getEstado());
            ps.setDate(3, new java.sql.Date(reclamo.getFecha().getTime()));
            ps.setInt(4, reclamo.getCliente().getIdCliente());
            ps.setInt(5, reclamo.getIdReclamo());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idReclamo) {
        String sql = "DELETE FROM reclamo WHERE id_reclamo=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReclamo);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Reclamo buscarPorId(int idReclamo) {
        String sql = "SELECT * FROM reclamo WHERE id_reclamo=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReclamo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ClienteRepository clienteRepo = new ClienteRepository();

                return new Reclamo(
                        rs.getInt("id_reclamo"),
                        rs.getString("motivo"),
                        rs.getString("estado"),
                        rs.getDate("fecha"),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Reclamo> listarTodos() {
        List<Reclamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM reclamo";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ClienteRepository clienteRepo = new ClienteRepository();

            while (rs.next()) {
                lista.add(new Reclamo(
                        rs.getInt("id_reclamo"),
                        rs.getString("motivo"),
                        rs.getString("estado"),
                        rs.getDate("fecha"),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Reclamo> listarPorCliente(int idCliente) {
        List<Reclamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM reclamo WHERE id_cliente=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            ClienteRepository clienteRepo = new ClienteRepository();

            while (rs.next()) {
                lista.add(new Reclamo(
                        rs.getInt("id_reclamo"),
                        rs.getString("motivo"),
                        rs.getString("estado"),
                        rs.getDate("fecha"),
                        clienteRepo.buscarPorId(idCliente)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
