package team04.project3.ui;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.listeners.ClientListener;
import team04.project3.listeners.ServerListener;
import team04.project3.model.client.ClientModel;
import team04.project3.model.server.ServerModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * AppToolbarView, the toolbar with the start/stop button and label which denotes
 * whether the application is of type Client or Server
 * @author  David Henderson (dchende2@asu.edu)
 */
public class AppToolbarView extends JToolBar {

    private JButton buttonToggle;
    private JLabel labelType;

    /**
     * AppToolbarView - A toolbar with the type of app and the start/stop button
     */
    public AppToolbarView() {
        // When server is starting or stopping, update button
        ServerModel.get().addListener(new ServerListener() {
            @Override
            public void started() {
                if(AppView.get().isServer())
                    buttonToggle.setText("Stop");
            }

            @Override
            public void shutdown() {
                if(AppView.get().isServer())
                    buttonToggle.setText("Start");
            }
        });

        // When client is starting or stopping, update button
        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void changedValues() {}

            @Override
            public void changedChannelCount() {}

            @Override
            public void started() {
                if(AppView.get().isClient())
                    buttonToggle.setText("Stop");
            }

            @Override
            public void shutdown() {
                if(AppView.get().isClient())
                    buttonToggle.setText("Start");
            }
        });

        this.setBackground(ColorConstants.BACKGROUND_BLUE);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));
        this.setFloatable(false);

        labelType = new JLabel(AppView.get().isClient() ? "Client" : "Server");
        labelType.setFont(TextConstants.DEFAULT_FONT);
        this.add(labelType);
        this.add(Box.createHorizontalGlue());

        buttonToggle = new JButton("Start");
        buttonToggle.setFont(TextConstants.DEFAULT_FONT);
        buttonToggle.setBackground(ColorConstants.BACKGROUND_PINK);
        buttonToggle.setFocusPainted(false);
        buttonToggle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createLineBorder(ColorConstants.BACKGROUND_PINK, 4)
                )
        );
        buttonToggle.addActionListener(e -> {
            // Check if it is client or server, then stop or start respectively
            if(AppView.get().isServer()) {
                if (ServerModel.get().isRunning())
                    ServerModel.get().shutdown();
                else
                    ServerModel.get().start();
            } else {
                if(ClientModel.get().isRunning())
                    ClientModel.get().shutdown();
                else
                    ClientModel.get().start();
            }
        });

        this.add(buttonToggle);
    }

    /**
     * updateType - Called to update the Client/Server header text
     */
    public void updateType() {
        if(AppView.get().isClient()) {
            this.labelType.setText("Client");
            this.buttonToggle.setText(ClientModel.get().isRunning() ? "Stop" : "Start");
        } else if(AppView.get().isServer()) {
            this.labelType.setText("Server");
            this.buttonToggle.setText(ServerModel.get().isRunning() ? "Stop" : "Start");
        }
    }
}
