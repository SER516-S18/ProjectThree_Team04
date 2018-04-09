package team04.project3.ui.server;

import team04.project3.model.server.ServerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main UI for the server application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerView extends JFrame {
    public static final Dimension WINDOW_SIZE = new Dimension(600, 800);
    private static ServerView instance;

    /**
     * Singleton getter
     * @return ServerView instance
     */
    public static ServerView getInstance() {
        ServerView result = instance;
        if(result == null) {
            synchronized (ServerView.class) {
                result = instance;
                if (result == null)
                    instance = result = new ServerView();
            }
        }
        return result;
    }

    private ServerToolbarView panelToolbar;
    private ServerTransmitView panelTransmit;
    private ServerValuesView panelValues;
    private ServerConsoleView panelConsole;

    /**
     * Private constructor for singleton
     */
    private ServerView() {
        // Start the server model if it isn't running
        if(!ServerModel.get().isRunning())
            ServerModel.get().start();

        // Shut down the server if the window is closed
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                if(ServerModel.get().isRunning())
                    ServerModel.get().shutdown();
            }
        });
    }

    /**
     * Initializes the UI
     */
    public void init() {
        this.setTitle("Emotiv Composer");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setMinimumSize(WINDOW_SIZE);

        this.setLayout(new BorderLayout());

        panelToolbar = new ServerToolbarView();
        this.add(panelToolbar, BorderLayout.PAGE_START);

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());
        this.add(panelCenter, BorderLayout.CENTER);

        panelTransmit = new ServerTransmitView();
        panelCenter.add(panelTransmit, BorderLayout.PAGE_START);

        panelValues = new ServerValuesView();
        panelCenter.add(panelValues, BorderLayout.CENTER);

        panelConsole = new ServerConsoleView();
        this.add(panelConsole, BorderLayout.PAGE_END);

        // Show frame
        this.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
}
