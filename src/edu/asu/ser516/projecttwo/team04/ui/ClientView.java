package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ClientModel;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;

import javax.swing.*;

/**
 * ClientView, the main UI for the client application
 */
public class ClientView extends JPanel {
    private AppView parent;
    private JLabel labelTemp;

    public ClientView(AppView appView) {
        parent = appView;

        labelTemp = new JLabel("Temp - This is a client");
        this.add(labelTemp);

        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void inputChanged(Integer newest, Integer min, Integer max, Integer avg) {
                labelTemp.setText("Newest " + newest + " | Min " + min + " | Max " + max + " | Avg " + avg);
            }

            @Override
            public void started() {

            }

            @Override
            public void shutdown() {

            }
        });
    }
}