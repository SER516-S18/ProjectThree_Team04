package team04.project3.ui;

import team04.project3.constants.ColorConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main UI for the client application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientView extends JPanel {
    private ClientGraphView graphView;
    private ClientSettingsView settingsView;

    /**
     * Representing the UI for the client application
     */
    public ClientView() {
        // Create a transparent border around this view
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Content goes in here (inside the invisible border)
        JPanel panelBuffer = new JPanel(new GridLayout(1, 2, 8, 8));
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        // Add left (graphical) view
        graphView = new ClientGraphView();
        panelBuffer.add(graphView, BorderLayout.LINE_START);

        // Add right (input/output) view
        settingsView = new ClientSettingsView();
        panelBuffer.add(settingsView, BorderLayout.LINE_END);

        this.add(panelBuffer, BorderLayout.CENTER);
    }
}