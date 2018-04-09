package team04.project3.ui.server;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.listeners.ServerListener;
import team04.project3.model.server.ServerModel;
import team04.project3.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.ParseException;

/**
 * UI for setting the packet transmit settings
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerTransmitView extends JPanel {
    private JButton buttonSend;
    private JCheckBox checkboxRepeat;
    private JSpinner spinnerInterval;

    /**
     * Constructor for ServerTransmitView, the input for how to send packets
     */
    public ServerTransmitView() {
        this.init();

        ServerModel.get().addListener(new ServerListener() {
            @Override
            public void started() { }

            @Override
            public void shutdown() { }

            @Override
            public void clientConnected() { }

            @Override
            public void clientDisconnected() { }

            @Override
            public void packetSent() { }

            @Override
            public void packetRepeatingToggled() {
                updateSendButtonText();
            }

            @Override
            public void packetRepeatingModeChanged() { }
        });
    }

    /**
     * Initializer for UI
     */
    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(new EmptyBorder(8, 8, 8, 8));
        this.setBackground(ColorConstants.BACKGROUND_BLUE);

        buttonSend = new JButton("Invalid");
        buttonSend.setFont(TextConstants.LARGE_FONT);
        buttonSend.addActionListener(e -> {
            if(ServerModel.get().isPacketRepeatMode()) {
                ServerModel.get().sendPacketsToggle();
            } else {
                ServerModel.get().sendPacketIndividual();
            }

            this.updateSendButtonText();
        });
        this.add(buttonSend);

        this.add(Box.createHorizontalStrut(16));

        checkboxRepeat = new JCheckBox("Repeat");
        checkboxRepeat.setFont(TextConstants.LARGE_FONT);
        checkboxRepeat.setOpaque(false);
        checkboxRepeat.addActionListener(e -> {
            if(!ServerModel.get().isRepeatingPackets()) {
                // If not already repeating, just change the mode
                ServerModel.get().setPacketRepeatMode(checkboxRepeat.isSelected());
            } else {
                // If already repeating, turn off, then change the mode
                ServerModel.get().sendPacketsToggle();
                ServerModel.get().setPacketRepeatMode(false);
            }
            updateSendButtonText();
        });
        this.add(checkboxRepeat);

        this.add(Box.createHorizontalStrut(16));

        double start = ServerModel.get().getAutoRepeatInterval() / 1000d;
        spinnerInterval = new JSpinner( new SpinnerNumberModel(start, 0.01d, 1440d, 0.25d) );
        spinnerInterval.setVisible(true);
        spinnerInterval.setBorder(null);
        spinnerInterval.setMaximumSize(new Dimension(128, 128));
        spinnerInterval.addChangeListener(event -> {
            try {
                spinnerInterval.commitEdit();
                long interval = (long) ((double) spinnerInterval.getValue() * 1000d);
                ServerModel.get().setAutoRepeatInterval(interval);
            } catch (ParseException e) {
                Log.w("Failed to parse interval spinner input", ServerTransmitView.class);
            }
        });
        spinnerInterval.setFont(TextConstants.LARGE_FONT);
        this.add(spinnerInterval);

        this.add(Box.createHorizontalStrut(8));

        JLabel promptIntervalSeconds = new JLabel("sec");
        promptIntervalSeconds.setFont(TextConstants.LARGE_FONT);
        this.add(promptIntervalSeconds);

        updateSendButtonText();
    }

    /**
     * Updates the send text depending on the repeat packet mode
     */
    private void updateSendButtonText() {
        if(buttonSend == null)
            return;

        if(ServerModel.get().isPacketRepeatMode()) {
            if(ServerModel.get().isRepeatingPackets()) {
                buttonSend.setText("Stop");
            } else {
                buttonSend.setText("Start");
            }
        } else {
            buttonSend.setText("Send");
        }

        this.validate();
        this.repaint();
    }
}
