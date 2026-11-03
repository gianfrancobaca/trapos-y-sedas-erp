package modulo2_gestionClientes.repositories;

import modulo2_gestionClientes.interfaces.INotificacion;
import modulo2_gestionClientes.models.Notificacion;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionRepository implements INotificacion {

    private Connection connection;

    public NotificacionRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void enviar(Notificacion notificacion) {
        String sql = "INSERT INTO notificacion (mensaje, tipo, fecha, leida, id_cliente) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, notificacion.getMensaje());
            ps.setString(2, notificacion.getTipo());
            ps.setDate(3, new java.sql.Date(notificacion.getFecha().getTime()));
            ps.setBoolean(4, notificacion.isLeida());
            ps.setInt(5, notificacion.getCliente().getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idNotificacion) {
        String sql = "DELETE FROM notificacion WHERE id_notificacion=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idNotificacion);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void marcarComoLeida(int idNotificacion) {
        String sql = "UPDATE notificacion SET leida=true WHERE id_notificacion=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idNotificacion);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Notificacion buscarPorId(int idNotificacion) {
        String sql = "SELECT * FROM notificacion WHERE id_notificacion=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idNotificacion);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ClienteRepository clienteRepo = new ClienteRepository();

                return new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("mensaje"),
                        rs.getString("tipo"),
                        rs.getDate("fecha"),
                        rs.getBoolean("leida"),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Notificacion> listarTodos() {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacion";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ClienteRepository clienteRepo = new ClienteRepository();

            while (rs.next()) {
                lista.add(new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("mensaje"),
                        rs.getString("tipo"),
                        rs.getDate("fecha"),
                        rs.getBoolean("leida"),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Notificacion> listarPorCliente(int idCliente) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacion WHERE id_cliente=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            ClienteRepository clienteRepo = new ClienteRepository();

            while (rs.next()) {
                lista.add(new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("mensaje"),
                        rs.getString("tipo"),
                        rs.getDate("fecha"),
                        rs.getBoolean("leida"),
                        clienteRepo.buscarPorId(idCliente)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Notificacion> listarNoLeidas(int idCliente) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacion WHERE id_cliente=? AND leida=false";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            ClienteRepository clienteRepo = new ClienteRepository();

            while (rs.next()) {
                lista.add(new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("mensaje"),
                        rs.getString("tipo"),
                        rs.getDate("fecha"),
                        rs.getBoolean("leida"),
                        clienteRepo.buscarPorId(idCliente)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}