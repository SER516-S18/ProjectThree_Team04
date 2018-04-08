package team04.project3.ui.server;

import team04.project3.constants.TextConstants;
import team04.project3.listeners.ServerListener;
import team04.project3.model.server.ServerModel;
import team04.project3.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class ServerTransmitView extends JPanel {
    private JButton buttonSend;
    private JCheckBox checkboxRepeat;
    private JSpinner spinnerInterval;

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
            public void packetRepeatingToggled(boolean repeating) {
                updateSendButtonText();
            }
        });
    }

    private void init() {
        System.out.println("init");
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

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
            updatedDisabledState();
        });
        this.add(checkboxRepeat);

        this.add(Box.createHorizontalStrut(16));

        spinnerInterval = new JSpinner( new SpinnerNumberModel(0.25d, 0.01d, 1440d, 0.25d) );
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

        updatedDisabledState();
        updateSendButtonText();
    }

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

    private void updatedDisabledState() {
        if(checkboxRepeat == null || spinnerInterval == null)
            return;

        if(checkboxRepeat.isSelected()) {
            spinnerInterval.setEnabled(true);
        } else {
            spinnerInterval.setEnabled(false);
        }
    }
}
