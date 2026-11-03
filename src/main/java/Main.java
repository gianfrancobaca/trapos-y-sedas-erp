import core.MainController;
import modulo2_gestionClientes.views.LoginView;

public class Main {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            boolean ok = loginView.mostrarLogin();

            if (ok) {
                MainController mainCtrl = new MainController();
                mainCtrl.run();
            } else {
                System.exit(0);
            }
        });
    }
}