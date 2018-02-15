package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.util.UIStandards;
import edu.asu.ser516.projecttwo.team04.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * App, the main app singleton frame that contains either the client or server UI
 */
public class App extends JFrame {
    public static final int TYPE_CLIENT = 0;
    public static final int TYPE_SERVER = 1;

    private static App _instance = null;

    public static App getInstance() {
        if(_instance == null)
            _instance = new App();

        return _instance;
    }

    public int port = 1516;
    private int _type = TYPE_CLIENT;
    private AppToolbar viewToolbar;
    private JPanel viewMenu;
    private ConsoleView viewConsole;

    private App() {}

    /**
     * Set whether the app is a client or server
     * @param type
     */
    public void setType(int type) {
        if(type != TYPE_CLIENT && type != TYPE_SERVER)
            throw new IllegalArgumentException("Type must be TYPE_CLIENT or TYPE_SERVER");
        else {
            _type = type;
            this.updateType();
        }
    }

    /**
     * Initialize the App frame UI
     */
    public void init() {
        this.setMinimumSize(new Dimension(800, 600));
        this.setBackground(UIStandards.BACKGROUND_BLUE);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("SER516 Project Two: Team 4");
        this.setLayout(new BorderLayout());

        // Add top
        viewToolbar = new AppToolbar();
        this.add(viewToolbar, BorderLayout.PAGE_START);

        // Add center
        this.updateType();

        // Add footer (console)
        viewConsole = new ConsoleView();
        this.add(viewConsole, BorderLayout.PAGE_END);

        // Package, set visible, move to center of screen
        this.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    /**
     * Update the App with the correct UI (either client or server)
     */
    private void updateType() {
        if(viewMenu != null) {
            this.remove(viewMenu);
            viewMenu = null;
        }

        if(_type == App.TYPE_CLIENT) {
            viewMenu = new ClientView(this);
            this.setTitle("SER516 P2T4: Client");
            Log.i("Application initializing as a Client", App.class);
        } else if(_type == App.TYPE_SERVER) {
            viewMenu = new ServerView(this);
            this.setTitle("SER516 P2T4: Server");
            Log.i("Application initializing as a Server", App.class);
        }

        this.add(viewMenu, BorderLayout.CENTER);
        this.repaint();
        this.revalidate();
    }

    /**
     * AppToolbar, the toolbar with the start/stop button
     */
    private static class AppToolbar extends JToolBar {
        private JButton buttonToggle;

        public AppToolbar() {
            this.setBackground(UIStandards.BACKGROUND_BLUE);
            this.setBorder(new EmptyBorder(8, 8, 8, 8));
            this.setFloatable(false);
            this.add(Box.createHorizontalGlue());

            buttonToggle = new JButton("Start / Stop");
            buttonToggle.setFont(UIStandards.DEFAULT_FONT);
            buttonToggle.setBackground(UIStandards.BACKGROUND_PINK);
            buttonToggle.setFocusPainted(false);
            this.add(buttonToggle);
        }
    }
}
