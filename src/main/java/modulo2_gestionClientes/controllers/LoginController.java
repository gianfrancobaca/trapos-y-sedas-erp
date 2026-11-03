package modulo2_gestionClientes.controllers;

import core.PasswordHasher;
import modulo2_gestionClientes.Patrones.SesionUsuario;
import modulo2_gestionClientes.models.Usuario;
import modulo2_gestionClientes.repositories.UsuarioRepository;

public class LoginController {

    private UsuarioRepository repository;

    public LoginController() {
        this.repository = new UsuarioRepository();
    }

    public boolean iniciarSesion(String username, String password) {
        String hashGuardado = repository.obtenerHashPorUsername(username);

        if (!PasswordHasher.verify(password, hashGuardado)) {
            return false;
        }

        Usuario usuario = repository.buscarPorUsername(username);
        if (usuario == null) {
            return false;
        }

        SesionUsuario.getInstance().setUsuarioActivo(usuario);
        return true;
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
