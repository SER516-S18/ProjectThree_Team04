package team04.project2.ui;

import team04.project2.constants.ColorConstants;

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

    /**
     * ServerView - The container for the left (indicator) and right (input) views
     */
    public ServerView() {
        // Create transparent border around view
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
}
