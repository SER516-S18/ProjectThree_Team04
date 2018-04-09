package team04.project3.ui.client;

import team04.project3.constants.TextConstants;
import team04.project3.model.client.ClientModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Main UI for the client application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientView extends JFrame {
    public static final Dimension WINDOW_SIZE = new Dimension(1024, 768);
    private static ClientView instance;

    /**
     * Getter for singleton ClientView
     * @return ClientView instance
     */
    public static ClientView getInstance() {
        ClientView result = instance;
        if(result == null){
            synchronized (ClientView.class) {
                result = instance;
                if (result == null)
                    instance = result = new ClientView();
            }
        }
        return result;
    }

    private ClientToolbarView panelToolbar;
    private JTabbedPane tabbedPane;

    /**
     * Representing the UI for the client application
     */
    private ClientView() {
        // Start the client model if it isn't running
        if(!ClientModel.get().isRunning())
            ClientModel.get().start();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            // If CONTROL + SHIFT + Q, open server
            if (e.isShiftDown() && e.getKeyCode() == 81 && e.getID() == KeyEvent.KEY_RELEASED) {
                panelToolbar.openServerPanel();
                return true;
            }
            return false;
        });

    }

    /**
     * UI initializer
     */
    public void init() {
        this.setTitle("Emotiv Control Panel");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(WINDOW_SIZE);

        this.setLayout(new BorderLayout());

        // Add toolbar
        panelToolbar = new ClientToolbarView();
        this.add(panelToolbar, BorderLayout.PAGE_START);

        // Add tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(TextConstants.LARGE_FONT);
        this.add(tabbedPane, BorderLayout.CENTER);

        ClientExpressionView expressionView = new ClientExpressionView();
        tabbedPane.addTab("Face Expressions", expressionView);

        ClientPerformanceMetricsView metricsView = new ClientPerformanceMetricsView();
        tabbedPane.addTab("Performance Metrics", metricsView);

        // Show frame
        this.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
}