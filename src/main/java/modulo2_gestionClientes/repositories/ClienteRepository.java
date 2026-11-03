package modulo2_gestionClientes.repositories;

import modulo2_gestionClientes.models.CategoriaCliente;
import modulo2_gestionClientes.models.Cliente;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    private Connection connection;

    public ClienteRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void agregar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombre, apellido, email, direccion, id_categoria) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getDireccion());
            ps.setInt(5, cliente.getCategoria().getIdCategoria());
            int filas = ps.executeUpdate();
            System.out.println("Clientes insertados: " + filas);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre=?, apellido=?, email=?, direccion=?, id_categoria=? WHERE id_cliente=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getDireccion());
            ps.setInt(5, cliente.getCategoria().getIdCategoria());
            ps.setInt(6, cliente.getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idCliente) {
        String sql = "DELETE FROM cliente WHERE id_cliente=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente buscarPorId(int idCliente) {
        String sql = "SELECT c.*, cat.nombre as cat_nombre, cat.descripcion as cat_desc, cat.descuento FROM cliente c JOIN categoria_cliente cat ON c.id_categoria = cat.id_categoria WHERE c.id_cliente=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CategoriaCliente cat = new CategoriaCliente(
                        rs.getInt("id_categoria"),
                        rs.getString("cat_nombre"),
                        rs.getString("cat_desc"),
                        rs.getDouble("descuento")
                );
                return new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        cat
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.*, cat.nombre as cat_nombre, cat.descripcion as cat_desc, cat.descuento FROM cliente c JOIN categoria_cliente cat ON c.id_categoria = cat.id_categoria";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                CategoriaCliente cat = new CategoriaCliente(
                        rs.getInt("id_categoria"),
                        rs.getString("cat_nombre"),
                        rs.getString("cat_desc"),
                        rs.getDouble("descuento")
                );
                lista.add(new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        cat
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
