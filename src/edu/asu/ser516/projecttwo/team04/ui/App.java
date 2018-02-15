package edu.asu.ser516.projecttwo.team04.ui;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
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

    public void setType(int type) {
        if(type != TYPE_CLIENT && type != TYPE_SERVER)
            throw new IllegalArgumentException("Type must be TYPE_CLIENT or TYPE_SERVER");
        else
            _type = type;
    }

    public void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if(_type == App.TYPE_CLIENT)
            menu = new AppClient(this);
        else if(_type == App.TYPE_SERVER)
            menu = new AppServer(this);

        this.add(menu);

        this.setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
}
