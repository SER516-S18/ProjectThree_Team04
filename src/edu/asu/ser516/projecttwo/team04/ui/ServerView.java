package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ServerModel;
import edu.asu.ser516.projecttwo.team04.constants.ColorConstants;
import edu.asu.ser516.projecttwo.team04.constants.StringConstants;
import edu.asu.ser516.projecttwo.team04.listeners.ServerListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ServerView, the main UI for the server application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerView extends JPanel {
    private ServerStatusView statusView;
    private ServerSettingsView settingsView;

    public ServerView() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Buffer for an opaque border surrounding ServerView, the buffer is the actual visible panel
        JPanel panelBuffer = new JPanel(new GridLayout(1, 2, 8, 8));
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        // Add the status view (left)
        statusView = new ServerStatusView();
        panelBuffer.add(statusView, BorderLayout.LINE_START);

        // Add the settings view (right)
        settingsView = new ServerSettingsView();
        panelBuffer.add(settingsView, BorderLayout.LINE_END);

        this.add(panelBuffer, BorderLayout.CENTER);
    }

    /**
     * ServerStatusView - The view that contains an indicator showing if the server is running
     */
    private class ServerStatusView extends JPanel {
        private Color COLOR_OFF = Color.DARK_GRAY;
        private Color COLOR_ON_DIM = new Color(197, 224, 179);
        private Color COLOR_ON_BRIGHT = new Color(168,208,141);

        private boolean running;
        private JLabel labelIndicator;

        private ServerStatusView() {
            running = ServerModel.get().isRunning();
            ServerModel.get().addListener(new ServerListener() {
                @Override
                public void started() {
                    // When the server is started, set the label to on
                    running = true;
                    labelIndicator.setForeground(COLOR_ON_BRIGHT);
                }

                @Override
                public void shutdown() {
                    // When the server is stopped, set the label to off
                    running = false;
                    labelIndicator.setForeground(COLOR_OFF);
                }
            });

            // Create a timer to blink if on or off
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
            this.setOpaque(false);
            this.setBorder(new EmptyBorder(8, 8, 8, 8));

            // Put the main view in a "buffer", this creates a transparent border around the buffer's content
            JPanel panelBuffer = new JPanel(new BorderLayout());
            panelBuffer.setBackground(ColorConstants.BACKGROUND_PINK);
            panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

            // Create the indicator
            labelIndicator = new JLabel("•", SwingConstants.CENTER);
            labelIndicator.setFont(new Font("Monospaced", Font.PLAIN, ColorConstants.DEFAULT_FONT.getSize() * 16));
            labelIndicator.setForeground(running ? COLOR_ON_BRIGHT : COLOR_OFF);
            panelBuffer.add(labelIndicator, BorderLayout.CENTER);

            this.add(panelBuffer, BorderLayout.CENTER);
        }
    }

    /**
     * ServerSettingsView - The right hand side to change the output min/max/frequency
     */
    private class ServerSettingsView extends JPanel {
        private ServerSettingsView() {
            this.setLayout(new GridLayout(5, 2, 8, 8));
            this.setBorder(new EmptyBorder(8, 8, 8, 8));
            this.setOpaque(false);

            // Maximum - Prompt
            JLabel labelPromptMaximum = new JLabel(StringConstants.HIGHEST_VALUE_STRING);
            labelPromptMaximum.setFont(ColorConstants.DEFAULT_FONT);
            labelPromptMaximum.setHorizontalAlignment(JLabel.CENTER);
            labelPromptMaximum.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptMaximum = new JPanel();
            panelPromptMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptMaximum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            panelPromptMaximum.add(labelPromptMaximum);
            this.add(panelPromptMaximum);

            // Maximum - Input
            JSpinner spinnerInputMaximum = new JSpinner( new SpinnerNumberModel(ServerModel.get().getValueMax(), -65536, 65536, 1) );
            spinnerInputMaximum.setVisible(true);
            spinnerInputMaximum.setBorder(null);
            spinnerInputMaximum.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_PINK);
            spinnerInputMaximum.setFont(ColorConstants.LARGE_FONT);
            spinnerInputMaximum.addChangeListener(e -> {
                ServerModel.get().setValueMax((Integer) spinnerInputMaximum.getValue());
            });

            JPanel panelInputMaximum = new JPanel();
            panelInputMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelInputMaximum.setBackground(ColorConstants.BACKGROUND_PINK);
            panelInputMaximum.add(spinnerInputMaximum);
            this.add(panelInputMaximum);

            // Minimum - Prompt
            JLabel labelPromptMinimum = new JLabel(StringConstants.LOWEST_VALUE_STRING);
            labelPromptMinimum.setFont(ColorConstants.DEFAULT_FONT);
            labelPromptMinimum.setHorizontalAlignment(JLabel.CENTER);
            labelPromptMinimum.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptMinimum = new JPanel();
            panelPromptMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptMinimum.setBackground(ColorConstants.BACKGROUND_PINK);
            panelPromptMinimum.add(labelPromptMinimum);
            this.add(panelPromptMinimum);

            // Minimum - Input
            JSpinner spinnerInputMinimum = new JSpinner( new SpinnerNumberModel(ServerModel.get().getValueMin(), -65536, 65536, 1) );
            spinnerInputMinimum.setVisible(true);
            spinnerInputMinimum.setBorder(null);
            spinnerInputMinimum.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            spinnerInputMinimum.setFont(ColorConstants.LARGE_FONT);
            spinnerInputMinimum.addChangeListener(e -> {
                ServerModel.get().setValueMin((Integer) spinnerInputMinimum.getValue());
            });

            JPanel panelInputMinimum = new JPanel();
            panelInputMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelInputMinimum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            panelInputMinimum.add(spinnerInputMinimum);
            this.add(panelInputMinimum);

            // Frequency - Prompt
            JLabel labelPromptFrequency = new JLabel(StringConstants.FREQUENCY_VALUE_STRING);
            labelPromptFrequency.setFont(ColorConstants.DEFAULT_FONT);
            labelPromptFrequency.setHorizontalAlignment(JLabel.CENTER);
            labelPromptFrequency.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptFrequency = new JPanel();
            panelPromptFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptFrequency.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            panelPromptFrequency.add(labelPromptFrequency);
            this.add(panelPromptFrequency);

            // Frequency - Input
            JSpinner spinnerInputFrequency = new JSpinner( new SpinnerNumberModel(ServerModel.get().getFrequency(), -65536, 65536, 1) );
            spinnerInputFrequency.setVisible(true);
            spinnerInputFrequency.setBorder(null);
            spinnerInputFrequency.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_PINK);
            spinnerInputFrequency.setFont(ColorConstants.LARGE_FONT);
            spinnerInputFrequency.addChangeListener(e -> {
                ServerModel.get().setFrequency((Integer) spinnerInputFrequency.getValue());
            });

            JPanel panelInputFrequency = new JPanel();
            panelInputFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
            panelInputFrequency.setBackground(ColorConstants.BACKGROUND_PINK);
            panelInputFrequency.add(spinnerInputFrequency);
            this.add(panelInputFrequency);

            // Add four empty panels (to scale like the specification and the client's 5 rows)
            this.add(new JLabel());
            this.add(new JLabel());
            this.add(new JLabel());
            this.add(new JLabel());
        }
    }
}
