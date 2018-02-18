package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ServerModel;
import edu.asu.ser516.projecttwo.team04.listeners.ServerListener;

import javax.swing.*;

/**
 * ServerView, the main UI for the server application
 */
public class ServerView extends JPanel {
    private AppView parent;
    private JLabel labelTemp;

    public ServerView(AppView appView) {
        parent = appView;

        labelTemp = new JLabel("Temp - This is a server");
        this.add(labelTemp);

        ServerModel.get().addListener(new ServerListener() {
            @Override
            public void started() {

            }

            @Override
            public void shutdown() {

            }
        });
    }
}
