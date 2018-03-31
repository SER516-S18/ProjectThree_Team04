package team04.project3.ui;

import team04.project3.constants.ColorConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The main UI for the server application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerView extends JPanel {
    private ServerStatusView statusView;
    private ServerSettingsView settingsView;

    /**
     * The container for the left (indicator) and right (input) views
     */
    public ServerView() {
        // Create transparent border around view
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Buffer for an opaque border surrounding ServerView, the buffer is the actual visible panel
        JPanel panelBuffer = new JPanel(new BorderLayout(8, 8));
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        // Add the settings view (top)
        settingsView = new ServerSettingsView();
        panelBuffer.add(settingsView, BorderLayout.PAGE_START);

        // Add the status view (bottom)
        statusView = new ServerStatusView();
        panelBuffer.add(statusView, BorderLayout.CENTER);



        this.add(panelBuffer, BorderLayout.CENTER);
    }
}
