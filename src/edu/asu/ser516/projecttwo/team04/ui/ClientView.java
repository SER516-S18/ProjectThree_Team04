package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ClientModel;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;

import javax.swing.*;

/**
 * ClientView, the main UI for the client application
 * @author  David Henderson (dchende2@asu.edu)
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
            public void changedValues() {
                labelTemp.setText("Min " + ClientModel.get().getMinimum() + " | Max " + ClientModel.get().getMaximum() + " | Avg " + ClientModel.get().getAverage() + " | Channels " + ClientModel.get().getChannelCount());
            }

            @Override
            public void changedChannelCount(int count) {
                labelTemp.setText("Min " + ClientModel.get().getMinimum() + " | Max " + ClientModel.get().getMaximum() + " | Avg " + ClientModel.get().getAverage() + " | Channels " + count);
            }

            @Override
            public void started() {}

            @Override
            public void shutdown() {}
        });
    }
}