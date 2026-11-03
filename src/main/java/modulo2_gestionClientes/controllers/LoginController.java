package modulo2_gestionClientes.controllers;

import modulo2_gestionClientes.Patrones.SesionUsuario;
import modulo2_gestionClientes.models.Usuario;
import modulo2_gestionClientes.repositories.UsuarioRepository;

public class LoginController {

    private UsuarioRepository repository;

    public LoginController() {
        this.repository = new UsuarioRepository();
    }

    public boolean iniciarSesion(String username, String password) {
        Usuario usuario = repository.buscarPorUsername(username);

        if (usuario != null) {
            SesionUsuario.getInstance().setUsuarioActivo(usuario);
            return true;
        }

        return false;
    }

    public void cerrarSesion() {
        SesionUsuario.getInstance().cerrarSesion();
    }

    public boolean haySesionActiva() {
        return SesionUsuario.getInstance().haySesionActiva();
    }

    public Usuario getUsuarioActivo() {
        return SesionUsuario.getInstance().getUsuarioActivo();
    }
}