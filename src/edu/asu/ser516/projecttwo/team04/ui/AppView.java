package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.constants.ColorConstants;
import edu.asu.ser516.projecttwo.team04.util.Log;

import javax.swing.*;
import java.awt.*;

/**
 * AppView, the main app singleton frame that contains either the client or server UI
 * @author  David Henderson (dchende2@asu.edu)
 */
public class AppView extends JFrame {
    public static final int TYPE_CLIENT = 0;
    public static final int TYPE_SERVER = 1;

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

    private int _type = TYPE_CLIENT;
    private AppToolbarView viewToolbar;
    private JPanel viewMenu;
    private ConsoleView viewConsole;

    /**
     * AppView - Private constructor for Singleton pattern
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
     * Initialize the AppView frame UI
     */
    public void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("SER516 Project Two: Team 4");

        this.setMinimumSize(new Dimension(800, 600));
        this.setLayout(new BorderLayout(8, 8));
        this.setBackground(ColorConstants.BACKGROUND_BLUE);

        // Added a component to display Client or Server Label and the button to start or stop the connection        
        viewToolbar = new AppToolbarView();
        this.add(viewToolbar, BorderLayout.PAGE_START);

        // Added a component to update the Client/Server Header
        this.updateType();

        // Add footer (console)
        viewConsole = new ConsoleView();
        this.add(viewConsole, BorderLayout.PAGE_END);

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
            viewMenu = new ClientView();
            this.setTitle("Client - SER516 Project Two: Team 4");

            if(this.viewToolbar != null)
                this.viewToolbar.updateType();
        }
        // Update the AppView with the Server UI for @param type TYPE_SERVER
        else if(_type == AppView.TYPE_SERVER) {
            Log.i("Application initializing as a Server", AppView.class);
            viewMenu = new ServerView();
            this.setTitle("Server - SER516 Project Two: Team 4");

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
     * isClient
     * @return boolean whether App is a client
     */
    public boolean isClient() {
        return _type == TYPE_CLIENT;
    }

    /**
     * isServer
     * @return boolean whether App is a server
     */
    public boolean isServer() {
        return  _type == TYPE_SERVER;
    }
}
