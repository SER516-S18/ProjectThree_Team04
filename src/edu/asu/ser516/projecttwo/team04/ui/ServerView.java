package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ServerModel;
import edu.asu.ser516.projecttwo.team04.listeners.ServerListener;
import edu.asu.ser516.projecttwo.team04.constants.UIStandards;
import edu.asu.ser516.projecttwo.team04.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

/**
 * ServerView, the main UI for the server application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerView extends JPanel {
    private AppView parent;
    private ServerStatusView statusView;
    private ServerSettingsView settingsView;

    public ServerView(AppView appView) {
        parent = appView;

        this.setLayout(new BorderLayout());
        this.setBackground(UIStandards.BACKGROUND_GRAY);

        statusView = new ServerStatusView();
        this.add(statusView, BorderLayout.LINE_START);

        settingsView = new ServerSettingsView();
        this.add(settingsView, BorderLayout.LINE_END);
    }

    private class ServerStatusView extends JPanel {
        private Color COLOR_OFF = Color.RED;
        private Color COLOR_ON_DIM = new Color(197, 224, 179);
        private Color COLOR_ON_BRIGHT = new Color(168,208,141);

        private boolean running;
        private JLabel labelIndicator;

        private ServerStatusView() {
            running = ServerModel.get().isRunning();
            ServerModel.get().addListener(new ServerListener() {
                @Override
                public void started() {
                    running = true;
                    labelIndicator.setForeground(COLOR_ON_BRIGHT);
                }

                @Override
                public void shutdown() {
                    running = false;
                    labelIndicator.setForeground(COLOR_OFF);
                }
            });

            Timer timer = new Timer(1000, e2 -> {
                if(running) {
                    if(labelIndicator.getForeground() == COLOR_ON_DIM) {
                        labelIndicator.setForeground(COLOR_ON_BRIGHT);
                    } else {
                        labelIndicator.setForeground(COLOR_ON_DIM);
                    }
                } else {
                    labelIndicator.setForeground(COLOR_OFF);
                }
            });
            timer.start();

            this.setLayout(new BorderLayout());
            this.setBackground(UIStandards.BACKGROUND_PINK);

            labelIndicator = new JLabel("•");
            labelIndicator.setFont(new Font("Monospaced", Font.PLAIN, UIStandards.DEFAULT_FONT.getSize() * 16));
            labelIndicator.setForeground(running ? COLOR_ON_BRIGHT : COLOR_OFF);
            this.add(labelIndicator, BorderLayout.CENTER);
        }
    }

    private class ServerSettingsView extends JPanel {
        private ServerSettingsView() {
            this.setLayout(new GridLayout());
            this.setOpaque(false);
        }
    }
}
