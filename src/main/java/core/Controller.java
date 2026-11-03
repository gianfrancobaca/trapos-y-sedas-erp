package core;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Controller {

    protected static final JFrame mainFrame = new JFrame("Trapos y Sedas – Admin");
    private static final JPanel viewsViewer = new JPanel(new CardLayout());
    private static final Map<String, Component> mainFrameComponents = new HashMap<>();

    static {
        mainFrame.add(viewsViewer);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setBounds(100, 100, 1100, 700);
    }

    public abstract void run();

    public static void addView(String viewName, View view) {
        viewsViewer.add((Component) view, viewName);
    }

    public static void loadView(String viewName) {
        CardLayout cl = (CardLayout) viewsViewer.getLayout();
        cl.show(viewsViewer, viewName);
    }

    public static void addComponent(String name, Component component) {
        mainFrameComponents.put(name, component);
    }

    public static void removeComponent(String name) {
        mainFrameComponents.remove(name);
    }

    public static Component getComponent(String name) {
        return mainFrameComponents.get(name);
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }

    public static JPanel getViewsViewer() {
        return viewsViewer;
    }
}