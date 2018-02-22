package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ServerModel;
import edu.asu.ser516.projecttwo.team04.constants.StringConstants;
import edu.asu.ser516.projecttwo.team04.constants.UIConstants;
import edu.asu.ser516.projecttwo.team04.listeners.ServerListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * ServerView, the main UI for the server application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerView extends JPanel {
    private static final SpinnerNumberModel SPINNER_MODEL = new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
    private AppView parent;
    private ServerStatusView statusView;
    private ServerSettingsView settingsView;

    public ServerView(AppView appView) {
        parent = appView;

        this.setLayout(new BorderLayout());
        this.setBackground(UIConstants.BACKGROUND_GRAY);

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
            this.setBackground(UIConstants.BACKGROUND_PINK);

            labelIndicator = new JLabel("•");
            labelIndicator.setFont(new Font("Monospaced", Font.PLAIN, UIConstants.DEFAULT_FONT.getSize() * 16));
            labelIndicator.setForeground(running ? COLOR_ON_BRIGHT : COLOR_OFF);
            this.add(labelIndicator, BorderLayout.CENTER);
        }
    }

    private class ServerSettingsView extends JPanel {
        private ServerSettingsView() {
            this.setLayout(new GridLayout(5, 2, 8, 8));
            this.setOpaque(false);

            // Maximum - Prompt
            JLabel labelPromptMaximum = new JLabel(StringConstants.HIGHEST_VALUE_STRING);
            labelPromptMaximum.setFont(UIConstants.DEFAULT_FONT);
            labelPromptMaximum.setHorizontalAlignment(JLabel.CENTER);
            labelPromptMaximum.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptMaximum = new JPanel();
            panelPromptMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptMaximum.setBackground(UIConstants.BACKGROUND_BLUEGRAY);
            panelPromptMaximum.add(labelPromptMaximum);
            this.add(panelPromptMaximum);

            // Maximum - Input
            JSpinner spinnerInputMaximum = new JSpinner( new SpinnerNumberModel(ServerModel.get().getValueMax(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1) );
            spinnerInputMaximum.setVisible(true);
            spinnerInputMaximum.setBorder(null);
            spinnerInputMaximum.getEditor().getComponent(0).setBackground(UIConstants.BACKGROUND_PINK);
            spinnerInputMaximum.setFont(UIConstants.DEFAULT_FONT);
            spinnerInputMaximum.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    ServerModel.get().setValueMax((Integer) spinnerInputMaximum.getValue());
                }
            });

            JPanel panelInputMaximum = new JPanel();
            panelInputMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelInputMaximum.setBackground(UIConstants.BACKGROUND_PINK);
            panelInputMaximum.add(spinnerInputMaximum);
            this.add(panelInputMaximum);

            // Minimum - Prompt
            JLabel labelPromptMinimum = new JLabel(StringConstants.LOWEST_VALUE_STRING);
            labelPromptMinimum.setFont(UIConstants.DEFAULT_FONT);
            labelPromptMinimum.setHorizontalAlignment(JLabel.CENTER);
            labelPromptMinimum.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptMinimum = new JPanel();
            panelPromptMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptMinimum.setBackground(UIConstants.BACKGROUND_PINK);
            panelPromptMinimum.add(labelPromptMinimum);
            this.add(panelPromptMinimum);

            // Minimum - Input
            JSpinner spinnerInputMinimum = new JSpinner( new SpinnerNumberModel(ServerModel.get().getValueMin(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1) );
            spinnerInputMinimum.setVisible(true);
            spinnerInputMinimum.setBorder(null);
            spinnerInputMinimum.getEditor().getComponent(0).setBackground(UIConstants.BACKGROUND_BLUEGRAY);
            spinnerInputMinimum.setFont(UIConstants.DEFAULT_FONT);
            spinnerInputMinimum.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    ServerModel.get().setValueMin((Integer) spinnerInputMinimum.getValue());
                }
            });

            JPanel panelInputMinimum = new JPanel();
            panelInputMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelInputMinimum.setBackground(UIConstants.BACKGROUND_BLUEGRAY);
            panelInputMinimum.add(spinnerInputMinimum);
            this.add(panelInputMinimum);

            // Frequency - Prompt
            JLabel labelPromptFrequency = new JLabel(StringConstants.FREQUENCY_VALUE_STRING);
            labelPromptFrequency.setFont(UIConstants.DEFAULT_FONT);
            labelPromptFrequency.setHorizontalAlignment(JLabel.CENTER);
            labelPromptFrequency.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptFrequency = new JPanel();
            panelPromptFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptFrequency.setBackground(UIConstants.BACKGROUND_BLUEGRAY);
            panelPromptFrequency.add(labelPromptFrequency);
            this.add(panelPromptFrequency);

            // Frequency - Input
            JSpinner spinnerInputFrequency = new JSpinner( new SpinnerNumberModel(ServerModel.get().getFrequency(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1) );
            spinnerInputFrequency.setVisible(true);
            spinnerInputFrequency.setBorder(null);
            spinnerInputFrequency.getEditor().getComponent(0).setBackground(UIConstants.BACKGROUND_PINK);
            spinnerInputFrequency.setFont(UIConstants.DEFAULT_FONT);
            spinnerInputFrequency.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    ServerModel.get().setFrequency((Integer) spinnerInputFrequency.getValue());
                }
            });

            JPanel panelInputFrequency = new JPanel();
            panelInputFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
            panelInputFrequency.setBackground(UIConstants.BACKGROUND_PINK);
            panelInputFrequency.add(spinnerInputFrequency);
            this.add(panelInputFrequency);

            this.add(new JLabel());
            this.add(new JLabel());
            this.add(new JLabel());
            this.add(new JLabel());
        }
    }
}
