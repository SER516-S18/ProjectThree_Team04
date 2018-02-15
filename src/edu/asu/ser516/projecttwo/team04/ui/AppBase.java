package edu.asu.ser516.projecttwo.team04.ui;

import javax.swing.*;

/**
 * AppBase, the base UI panel (child of the App JFrame) for both the client and server
 */
public class AppBase extends JPanel {
    private App _parent;

    public AppBase(App parent) {
        _parent = parent;
    }
}
