package modulo2_gestionClientes.models;

public class Credencial {

    private int idCredencial;
    private String username;
    private String password;
    private Usuario usuario;

    public Credencial() {
    }

    public Credencial(int idCredencial, String username, String password, Usuario usuario) {
        this.idCredencial = idCredencial;
        this.username = username;
        this.password = password;
        this.usuario = usuario;
    }

    public int getIdCredencial() {
        return idCredencial;
    }

    public void setIdCredencial(int idCredencial) {
        this.idCredencial = idCredencial;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Credencial{" +
                "idCredencial=" + idCredencial +
                ", username='" + username + '\'' +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "Sin usuario") +
                '}';
    }
}
