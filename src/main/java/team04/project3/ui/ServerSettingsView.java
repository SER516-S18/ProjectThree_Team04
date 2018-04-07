package team04.project3.ui;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.model.server.ServerModel;
import team04.project3.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.ParseException;

/**
 * The right hand side to change the output min/max/frequency
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerSettingsView extends JPanel {

    private static String buttonState = "Send";
    private static double timeCounter= 0.0;
    private Runnable updateTimeWhenAutoReset;
    private Thread addIntervaltoTimeCounter;
    /**
     * The right hand side to change the output min/max/frequency
     */
    public ServerSettingsView() {
        
    	// Create the settings view with a transparent border encompassing
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray), "Interactive", TitledBorder.LEADING,
                TitledBorder.TOP, TextConstants.DEFAULT_FONT, Color.black));
        this.setOpaque(false);

        JPanel panelBuffer = new JPanel(new GridLayout(2, 1, 50, 4));
        panelBuffer.setBackground(Color.lightGray);
        panelBuffer.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Maximum - Prompt
        JLabel labelPromptMaximum = new JLabel(TextConstants.EMO_STATE_INTERVAL_STRING);
        labelPromptMaximum.setFont(TextConstants.DEFAULT_FONT);
        labelPromptMaximum.setHorizontalAlignment(JLabel.CENTER);
        labelPromptMaximum.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptMaximum = new JPanel();
        panelPromptMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptMaximum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptMaximum.add(labelPromptMaximum);
        panelBuffer.add(panelPromptMaximum);

        // Maximum - Input
        JSpinner spinnerInputInterval = new JSpinner( new SpinnerNumberModel(0.25d, 0.01d, 65536d, 0.25d) );
        spinnerInputInterval.setVisible(true);
        spinnerInputInterval.setBorder(null);
        spinnerInputInterval.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_PINK);
        spinnerInputInterval.addChangeListener(event -> {
            try {
                spinnerInputInterval.commitEdit();
                long interval = (long) ((double) spinnerInputInterval.getValue() * 1000d);
                ServerModel.get().setAutoRepeatInterval(interval);
            } catch (ParseException e) {
                Log.w("Failed to parse interval spinner input", ServerSettingsView.class);
            }
        });
        spinnerInputInterval.setFont(TextConstants.LARGE_FONT);

        JPanel panelInputInterval = new JPanel();
        panelInputInterval.setBorder(BorderFactory.createLineBorder(Color.black));
        panelInputInterval.setBackground(ColorConstants.BACKGROUND_PINK);
        panelInputInterval.add(spinnerInputInterval);
        panelBuffer.add(panelInputInterval);

        JCheckBox autoResetCheckBox = new JCheckBox("Auto Reset");
        autoResetCheckBox.setBackground(Color.LIGHT_GRAY);
        autoResetCheckBox.addActionListener(e -> {
            ServerModel.get().setPacketRepeatMode(autoResetCheckBox.isSelected());
        });
        panelBuffer.add(autoResetCheckBox);

        JButton sendButton = new JButton(buttonState);
        sendButton.setFont(TextConstants.DEFAULT_FONT);
        sendButton.setBackground(ColorConstants.BACKGROUND_GRAY);
        sendButton.setForeground(Color.BLACK);
        sendButton.setFocusPainted(false);
        panelBuffer.add(sendButton);

        updateTimeWhenAutoReset = () -> {
    		while(autoResetCheckBox.isSelected()) {

                if(ServerModel.get().isRepeatingPackets()) {
                    
                	long INTERVAL = (long) ((double) spinnerInputInterval.getValue() * 1000);
                    timeCounter = timeCounter + (double) spinnerInputInterval.getValue();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        Thread.currentThread().interrupt();
                        e1.printStackTrace();
                    }
                }
    		}
    	};
        
    	autoResetCheckBox.addItemListener(e -> {
            if (e.getStateChange()==ItemEvent.SELECTED) {
                buttonState = "Start";
                ServerModel.get().setPacketRepeatMode(true);
            } else {
                buttonState = "Send";
                ServerModel.get().setPacketRepeatMode(false);
            }

            sendButton.setText(buttonState);
        });

        sendButton.addActionListener(e -> {
            if (ServerModel.get().isRunning()) {
                if(ServerModel.get().isPacketRepeatMode()) {
                    // Repeatedly send packets
                    ServerModel.get().sendPacketsToggle();
                    
                    if(ServerModel.get().isRepeatingPackets()) {

                        addIntervaltoTimeCounter = new Thread(updateTimeWhenAutoReset);
                        addIntervaltoTimeCounter.start();
                    	sendButton.setText("Stop");
                        autoResetCheckBox.setEnabled(false);
                    } else {
                    	addIntervaltoTimeCounter.interrupt();
                    	sendButton.setText("Start");
                        autoResetCheckBox.setEnabled(true);
                    }
                } else {
                    // Send individual packets
                    ServerModel.get().sendPacketIndividual();
                    sendButton.setText(buttonState);
                    timeCounter=timeCounter+(double)spinnerInputInterval.getValue();
                }
            } else {
            	Log.e("Failed to send because the server model is not running", ServerSettingsView.class);
            }
        });

        this.add(panelBuffer);
    }
    
    
    
    /**
     * setter method for time counter. 
     * @param timeCounter
     */
    public static void setTimeCounter(double timeCounter) {
		ServerSettingsView.timeCounter = timeCounter;
	}

	/**
     * getter method for time counter. 
     * @returns timeCounter
     */
    public static double getTimeCounter() {
		return timeCounter;
	}
}
