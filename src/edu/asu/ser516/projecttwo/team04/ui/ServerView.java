package edu.asu.ser516.projecttwo.team04.ui;

import javax.swing.*;

/**
 * ServerView, the main UI for the server application
 */
public class ServerView extends JPanel {
    App _parent;

    public ServerView(App parent) {
        _parent = parent;
        this.add(new JLabel("Temp - This is a server"));
    }
}
