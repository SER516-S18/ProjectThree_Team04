package team04.project2.ui;

import team04.project2.constants.ColorConstants;
import team04.project2.constants.TextConstants;
import team04.project2.listeners.ServerListener;
import team04.project2.model.server.ServerModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The view that contains an indicator showing if the server is running
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerStatusView extends  JPanel {
    private final Color COLOR_OFF = Color.DARK_GRAY;
    private final Color COLOR_ON_DIM = new Color(197, 224, 179);
    private final Color COLOR_ON_BRIGHT = new Color(168,208,141);

    private boolean running;
    private JLabel labelIndicator;

    /**
     * The view that contains an indicator showing if the server is running
     */
    public ServerStatusView() {
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

        // Create the indicator (The circular bullet character is Unicode hex 0x2022)
        labelIndicator = new JLabel(Character.toString((char) 0x2022), SwingConstants.CENTER);
        labelIndicator.setFont(new Font("Monospaced", Font.PLAIN, TextConstants.DEFAULT_FONT.getSize() * 16));
        labelIndicator.setForeground(running ? COLOR_ON_BRIGHT : COLOR_OFF);
        panelBuffer.add(labelIndicator, BorderLayout.CENTER);

        this.add(panelBuffer, BorderLayout.CENTER);
    }
}
