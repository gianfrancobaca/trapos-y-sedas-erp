package modulo2_gestionClientes.Patrones;

import modulo2_gestionClientes.models.Usuario;

public class SesionUsuario {

    private static SesionUsuario instance;
    private Usuario usuarioActivo;

    private SesionUsuario() {}

    public static SesionUsuario getInstance() {
        if (instance == null) {
            instance = new SesionUsuario();
        }
        return instance;
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(Usuario usuario) {
        this.usuarioActivo = usuario;
    }

    public void cerrarSesion() {
        this.usuarioActivo = null;
    }

    public boolean haySesionActiva() {
        return usuarioActivo != null;
    }
}

