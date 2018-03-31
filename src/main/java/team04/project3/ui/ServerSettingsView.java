package team04.project3.ui;

import com.sun.tools.javadoc.Start;
import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.model.client.ClientModel;
import team04.project3.model.server.ServerModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * The right hand side to change the output min/max/frequency
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerSettingsView extends JPanel {

    private static String buttonState = "Send";
    /**
     * The right hand side to change the output min/max/frequency
     */
    public ServerSettingsView() {
        // Create the settings view with a transparent border encompassing
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(45, 8, 8, 8));
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
        JSpinner spinnerInputMaximum = new JSpinner( new SpinnerNumberModel(0.25, -65536, 65536, 0.25) );
        spinnerInputMaximum.setVisible(true);
        spinnerInputMaximum.setBorder(null);
        spinnerInputMaximum.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_PINK);
        spinnerInputMaximum.setFont(TextConstants.LARGE_FONT);

        JPanel panelInputMaximum = new JPanel();
        panelInputMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelInputMaximum.setBackground(ColorConstants.BACKGROUND_PINK);
        panelInputMaximum.add(spinnerInputMaximum);
        panelBuffer.add(panelInputMaximum);

        JCheckBox autoResetCheckBox = new JCheckBox("Auto Reset");
        panelBuffer.add(autoResetCheckBox);

        JButton sendButton = new JButton(buttonState);
        sendButton.setFont(TextConstants.DEFAULT_FONT);
        sendButton.setBackground(ColorConstants.BACKGROUND_GRAY);
        sendButton.setForeground(Color.BLACK);
        sendButton.setFocusPainted(false);
        panelBuffer.add(sendButton);

        autoResetCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()==ItemEvent.SELECTED) {
                    buttonState = "Start";
                    ServerModel.get().setAutoRepeat(true);
                } else {
                    buttonState = "Send";
                    ServerModel.get().setAutoRepeat(false);
                }

                sendButton.setText(buttonState);
            }
        });

        sendButton.addActionListener(e -> {
            if (ServerModel.get().isRunning()) {
                sendButton.setText(buttonState);
                ServerModel.get().shutdown();
            } else {
                ServerModel.get().setInterval(Long.valueOf(spinnerInputMaximum.getValue().toString()) * 1000);
                ServerModel.get().start();
                sendButton.setText("Stop");
            }
        });

        this.add(panelBuffer);
    }
}
