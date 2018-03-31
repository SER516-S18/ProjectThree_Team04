package main.java.team04.project3.ui;

import main.java.team04.project3.constants.ColorConstants;
import main.java.team04.project3.constants.TextConstants;
import main.java.team04.project3.listeners.ServerListener;
import main.java.team04.project3.model.server.ServerModel;

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
    private JLabel timeLabel;
    private JTextField timeField;
    private JPanel panelBuffer;
    
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
        panelBuffer = new JPanel(new GridBagLayout());
        panelBuffer.setBackground(Color.LIGHT_GRAY);
        panelBuffer.setBorder(new EmptyBorder (8,8,8,8));
        panelBuffer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        //setting grid bag constraints for emostate label at the top of panel.
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        
        panelBuffer.add(new JLabel("EMOSTATE",JLabel.LEFT),constraints);
        createTimeArea(constraints);    
        
        this.add(panelBuffer, BorderLayout.CENTER);
    }
    
    /**
     * method that creates the time area and add to the "emostate" panel.
     * 
     * @param constraints to set the positions of labels and textfields.
     */
    public void createTimeArea (GridBagConstraints constraints) {
    	
    	timeLabel = new JLabel ("Time:",JLabel.LEFT);
        timeLabel.setBorder(new EmptyBorder(10,10,10,10));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        panelBuffer.add(timeLabel,constraints);
        
        timeField = new JTextField(6);
        timeField.setBorder(BorderFactory.createLineBorder(Color.black));
        timeField.setVisible(true);
        constraints.weightx = 0.1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        panelBuffer.add(timeField,constraints);
        
        JLabel secondsLabel = new JLabel(" Seconds");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 1;
        panelBuffer.add(secondsLabel,constraints);
    }
}
