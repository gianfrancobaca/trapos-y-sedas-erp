package modulo2_gestionClientes.repositories;

import modulo2_gestionClientes.interfaces.ICategoriaClienteRepository;
import modulo2_gestionClientes.models.CategoriaCliente;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaClienteRepository implements ICategoriaClienteRepository {

    private Connection connection;

    public CategoriaClienteRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void agregar(CategoriaCliente categoria) {
        String sql = "INSERT INTO categoria_cliente (nombre, descripcion, descuento) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setDouble(3, categoria.getDescuento());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(CategoriaCliente categoria) {
        String sql = "UPDATE categoria_cliente SET nombre=?, descripcion=?, descuento=? WHERE id_categoria=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setDouble(3, categoria.getDescuento());
            ps.setInt(4, categoria.getIdCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idCategoria) {
        String sql = "DELETE FROM categoria_cliente WHERE id_categoria=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CategoriaCliente buscarPorId(int idCategoria) {
        String sql = "SELECT * FROM categoria_cliente WHERE id_categoria=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CategoriaCliente(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("descuento")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CategoriaCliente> listarTodos() {
        List<CategoriaCliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria_cliente";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new CategoriaCliente(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("descuento")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
