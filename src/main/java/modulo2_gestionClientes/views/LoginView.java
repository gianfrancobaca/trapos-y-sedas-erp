package modulo2_gestionClientes.views;

import modulo2_gestionClientes.controllers.LoginController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JDialog {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private boolean loginCorrecto;
    private LoginController controller;

    public LoginView() {
        this.controller = new LoginController();
        this.loginCorrecto = false;

        setTitle("Inicio de Sesión");
        setModal(true);
        setSize(360, 220);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("TRAPOS Y SEDAS", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();

        panelCampos.add(new JLabel("Usuario:"));
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtPassword);

        JPanel panelBotones = new JPanel();
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnIngresar);
        panelBotones.add(btnCancelar);

        add(titulo, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnIngresar.addActionListener(e -> iniciarSesion());
        btnCancelar.addActionListener(e -> {
            loginCorrecto = false;
            dispose();
        });
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.");
            return;
        }

        boolean ok = controller.iniciarSesion(usuario, password);

        if (ok) {
            loginCorrecto = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
    }

    public boolean mostrarLogin() {
        setVisible(true);
        return loginCorrecto;
    }
}
