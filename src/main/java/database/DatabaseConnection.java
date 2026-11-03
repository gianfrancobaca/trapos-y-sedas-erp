
package database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    /** Archivo con las credenciales, en el directorio desde donde se ejecuta la app. */
    private static final Path CONFIG_FILE = Path.of("config.properties");

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Properties config = cargarConfiguracion();
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    config.getProperty("db.url"),
                    config.getProperty("db.user"),
                    config.getProperty("db.password", ""));
            System.out.println("[DB] Conexión establecida con Trapos y Sedas.");
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.err.println("[DB] Error al conectar: " + e.getMessage());
        }
    }

    private static Properties cargarConfiguracion() throws IOException {
        if (!Files.exists(CONFIG_FILE)) {
            throw new IOException("No se encontró " + CONFIG_FILE.toAbsolutePath()
                    + ". Copia config.example.properties a config.properties y completa tus credenciales.");
        }
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(CONFIG_FILE)) {
            props.load(in);
        }
        for (String clave : new String[]{"db.url", "db.user"}) {
            if (props.getProperty(clave) == null) {
                throw new IOException("Falta la propiedad '" + clave + "' en " + CONFIG_FILE);
            }
        }
        return props;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null || isConnectionClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private static boolean isConnectionClosed() {
        try {
            return instance.connection == null || instance.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
