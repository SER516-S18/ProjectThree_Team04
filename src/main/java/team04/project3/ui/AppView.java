package team04.project3.ui;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.model.server.ServerModel;
import team04.project3.util.Log;

import javax.swing.*;
import java.awt.*;

/**
 * Main app singleton frame that contains either the client or server UI
 * @author  David Henderson (dchende2@asu.edu)
 */
public class AppView extends JFrame {
    public static final int TYPE_CLIENT = 0;
    public static final int TYPE_SERVER = 1;

    public static final int TYPE_CLIENT_FACE_EXPRESSION = 0;
    public static final int TYPE_CLIENT_PERFORMANCE_METRIC = 1;

    private static AppView _instance = null;

    /**
     * Singleton instance getter for AppView
     * @return The AppView instance
     */
    public static AppView get() {
        if(_instance == null)
            _instance = new AppView();

        return _instance;
    }

    private static int _type = TYPE_CLIENT;
    private static int _client_type = TYPE_CLIENT_FACE_EXPRESSION;
    private AppToolbarView viewToolbar;
    private JPanel viewMenu;
    private ConsoleView viewConsole;

    public static AppView get(int type) {
        if(_instance == null)
            _instance = new AppView();

        else {
            if (type != _type) {
                return new AppView();
            }
        }

        return _instance;
    }

    /**
     * Private constructor for Singleton pattern
     */
    private AppView() {}

    /**
     * Set whether the app is a client or server
     * @param type Either TYPE_CLIENT or TYPE_SERVER
     */
    public void setType(int type) {
        if(type != TYPE_CLIENT && type != TYPE_SERVER)
            throw new IllegalArgumentException("Type must be TYPE_CLIENT or TYPE_SERVER");
        else if(type != _type) {
            _type = type;
            this.updateType();
        }
    }

    /**
     * Enables/disables the Console having timestamps prefixing messages
     * @param show True/false to show timestamps in Console
     */
    public void showConsoleTimestamps(boolean show) {
        this.viewConsole.timestamp = show;
    }

    /**
     * Initialize the AppView frame UI
     */
    public void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (_type == TYPE_SERVER) {
            // Add top toolbar component
            viewToolbar = new AppToolbarView();
            this.add(viewToolbar, BorderLayout.PAGE_START);
            this.setTitle(TextConstants.SERVER_TITLE_VALUE);
            this.setMinimumSize(new Dimension(600, 800));
            ServerModel.get().setServerConsolePresent(true);

            // Add bottom footer console component
            viewConsole = new ConsoleView();
            this.add(viewConsole, BorderLayout.PAGE_END);
        } else {
            this.setTitle(TextConstants.CLIENT_TITLE_VALUE);
            this.setMinimumSize(new Dimension(1000, 800));
        }

        this.setLayout(new BorderLayout(8, 8));
        this.setBackground(ColorConstants.BACKGROUND_BLUE);

        // Add respective Client or Server panel to the center
        this.updateType();

        // Package, set visible, move to center of screen
        this.getContentPane().setBackground(ColorConstants.BACKGROUND_BLUE);
        this.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2+(this.getSize().width/2 * (isClient() ? 1 : -1)), 
                         dim.height/2-this.getSize().height/2);
    }

    /**
     * Update the AppView with the correct UI (either client or server)
     */
    private void updateType() {
        // If it's already created, remove the old one
        if(viewMenu != null) {
            viewMenu.removeAll();
            viewMenu.setVisible(false);
            this.remove(viewMenu);
            viewMenu = null;
        }
        // Update the AppView with the Client UI for @param type TYPE_CLIENT
        if(_type == AppView.TYPE_CLIENT) {
            Log.i("Application initializing as a Client", AppView.class);
            viewMenu = new ClientView(_client_type);
            this.setTitle(TextConstants.CLIENT_TITLE_VALUE);

            if(this.viewToolbar != null)
                this.viewToolbar.updateType();
        }
        // Update the AppView with the Server UI for @param type TYPE_SERVER
        else if(_type == AppView.TYPE_SERVER) {
            Log.i("Application initializing as a Server", AppView.class);
            viewMenu = new ServerView();
            this.setTitle(TextConstants.SERVER_TITLE_VALUE);

            if(this.viewToolbar != null)
                this.viewToolbar.updateType();
        } else {
            throw new IllegalStateException("Type is not client or server");
        }

        this.add(viewMenu, BorderLayout.CENTER);
        this.repaint();
        this.revalidate();
    }

    /**
     * If the Application is a Client
     * @return boolean whether App is a client
     */
    public boolean isClient() {
        return _type == TYPE_CLIENT;
    }

    /**
     * If the Application is a Server
     * @return boolean whether App is a server
     */
    public boolean isServer() {
        return  _type == TYPE_SERVER;
    }

    public int getClientType() {
        return _client_type;
    }

    public void setClientType(int _client_type) {
        AppView._client_type = _client_type;
    }
}
