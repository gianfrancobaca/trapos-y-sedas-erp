package modulo2_gestionClientes.repositories;

import modulo2_gestionClientes.models.ProgramaFidelizacion;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialRepository {

    private Connection connection;

    public HistorialRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void agregar(ProgramaFidelizacion programa) {
        String sql = "INSERT INTO programa_fidelizacion (nombre, puntos_acumulados, nivel, id_cliente) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, programa.getNombre());
            ps.setInt(2, programa.getPuntosAcumulados());
            ps.setString(3, programa.getNivel());
            ps.setInt(4, programa.getCliente().getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(ProgramaFidelizacion programa) {
        String sql = "UPDATE programa_fidelizacion SET nombre=?, puntos_acumulados=?, nivel=?, id_cliente=? WHERE id_programa=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, programa.getNombre());
            ps.setInt(2, programa.getPuntosAcumulados());
            ps.setString(3, programa.getNivel());
            ps.setInt(4, programa.getCliente().getIdCliente());
            ps.setInt(5, programa.getIdPrograma());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idPrograma) {
        String sql = "DELETE FROM programa_fidelizacion WHERE id_programa=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPrograma);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProgramaFidelizacion> listarPorCliente(int idCliente) {
        List<ProgramaFidelizacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM programa_fidelizacion WHERE id_cliente=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            ClienteRepository clienteRepo = new ClienteRepository();
            while (rs.next()) {
                lista.add(new ProgramaFidelizacion(
                        rs.getInt("id_programa"),
                        rs.getString("nombre"),
                        rs.getInt("puntos_acumulados"),
                        rs.getString("nivel"),
                        clienteRepo.buscarPorId(idCliente)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ProgramaFidelizacion> listarTodos() {
        List<ProgramaFidelizacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM programa_fidelizacion";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ClienteRepository clienteRepo = new ClienteRepository();
            while (rs.next()) {
                lista.add(new ProgramaFidelizacion(
                        rs.getInt("id_programa"),
                        rs.getString("nombre"),
                        rs.getInt("puntos_acumulados"),
                        rs.getString("nivel"),
                        clienteRepo.buscarPorId(rs.getInt("id_cliente"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}

