package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.util.Log;

import javax.swing.*;
import java.awt.*;

/**
 * App, the main app singleton frame that contains either the client or server UI
 */
public class App extends JFrame {
    public static final int TYPE_CLIENT = 0;
    public static final int TYPE_SERVER = 1;

    private static App _instance = null;

    private AppBase menu;
    private int _type = TYPE_CLIENT;

    private App() {}

    public static App getInstance() {
        if(_instance == null)
            _instance = new App();

        return _instance;
    }

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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("SER516 Project Two: Team 4");
        this.updateType();
        this.setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    /**
     * Update the App with the correct UI (either client or server)
     */
    private void updateType() {
        menu = null;

        if(_type == App.TYPE_CLIENT) {
            menu = new AppClient(this);
            this.setTitle("SER516 P2T4: Client");
            Log.i("Application initializing as a Client", App.class);
        } else if(_type == App.TYPE_SERVER) {
            menu = new AppServer(this);
            this.setTitle("SER516 P2T4: Server");
            Log.i("Application initializing as a Server", App.class);
        }

        this.add(menu);
    }
}
