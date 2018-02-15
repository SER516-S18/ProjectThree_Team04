package edu.asu.ser516.projecttwo.team04.ui;

import javax.swing.*;

/**
 * ClientView, the main UI for the client application
 */
public class ClientView extends JPanel {
    App _parent;

    public ClientView(App parent) {
        _parent = parent;
        this.add(new JLabel("Temp - This is a client"));
    }
}