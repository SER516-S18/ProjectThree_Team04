package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ClientModel;
import edu.asu.ser516.projecttwo.team04.Constants.AppConstants;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
import edu.asu.ser516.projecttwo.team04.util.Log;
import edu.asu.ser516.projecttwo.team04.util.UIStandards;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * ClientView, the main UI for the client application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientView extends JPanel {
    private AppView parent;
    private JLabel labelTemp;

    public ClientView(AppView appView) {
        parent = appView;

        JPanel clientPanel = new JPanel();
        clientPanel.setBackground(Color.lightGray);
        clientPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        clientPanel.setBounds(5, 50, 780, 400);
        clientPanel.setLayout(null);

        JPanel graphView = new JPanel();
        graphView.setBackground(UIStandards.BACKGROUND_PINK);
        graphView.setBounds(15, 15, 520, 370);
        graphView.setLayout(null);
        graphView.setBorder(BorderFactory.createLineBorder(Color.black));
        clientPanel.add(graphView);

        JLabel maxValLabel = new JLabel(AppConstants.HIGHEST_VALUE_STRING, JLabel.CENTER);
        maxValLabel.setHorizontalAlignment(JLabel.CENTER);
        maxValLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel maxValLabelPanel = new JPanel();
        maxValLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        maxValLabelPanel.setBounds(555, 16, 86, 59);
        maxValLabelPanel.setBackground(UIStandards.BACKGROUND_BLUE);
        maxValLabelPanel.add(maxValLabel);
        clientPanel.add(maxValLabelPanel);

        JTextPane maxValTextPanel = new JTextPane();
        maxValTextPanel.setEditable(false);
        maxValTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        maxValTextPanel.setBounds(650, 16, 86, 59);
        maxValTextPanel.setBackground(UIStandards.BACKGROUND_PINK);
        clientPanel.add(maxValTextPanel);

        JLabel minValLabel = new JLabel(AppConstants.LOWEST_VALUE_STRING, JLabel.CENTER);
        minValLabel.setHorizontalAlignment(JLabel.CENTER);
        minValLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel minValLabelPanel = new JPanel();
        minValLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        minValLabelPanel.setBounds(555, 91, 86, 59);
        minValLabelPanel.setBackground(UIStandards.BACKGROUND_PINK);
        minValLabelPanel.add(minValLabel);
        clientPanel.add(minValLabelPanel);

        JTextPane minValTextPanel = new JTextPane();
        minValTextPanel.setEditable(false);
        minValTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        minValTextPanel.setBounds(650, 91, 86, 59);
        minValTextPanel.setBackground(UIStandards.BACKGROUND_BLUE);
        clientPanel.add(minValTextPanel);

        JLabel average = new JLabel(AppConstants.AVERAGE_VALUE_STRING, JLabel.CENTER);
        average.setHorizontalAlignment(JLabel.CENTER);
        average.setVerticalAlignment(JLabel.CENTER);

        JPanel avgValLabelPanel = new JPanel();
        avgValLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        avgValLabelPanel.setBounds(555, 166, 88, 59);
        avgValLabelPanel.setBackground(UIStandards.BACKGROUND_BLUE);
        avgValLabelPanel.add(average);
        clientPanel.add(avgValLabelPanel);

        JTextPane avgValTextPanel = new JTextPane();
        avgValTextPanel.setEditable(false);
        avgValTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        avgValTextPanel.setBounds(650, 166, 86, 59);
        avgValTextPanel.setBackground(UIStandards.BACKGROUND_PINK);
        clientPanel.add(avgValTextPanel);

        JLabel channelLabel = new JLabel(AppConstants.CHANNELS_VALUE_STRING, JLabel.CENTER);
        channelLabel.setHorizontalAlignment(JLabel.CENTER);
        channelLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel channelLabelPanel = new JPanel();
        channelLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        channelLabelPanel.setBounds(555, 246, 86, 59);
        channelLabelPanel.setBackground(UIStandards.BACKGROUND_PINK);
        channelLabelPanel.add(channelLabel);
        clientPanel.add(channelLabelPanel);

        JComboBox<String> channelValue = new JComboBox<>(UIStandards.NUMBER_OF_CHANNELS);
        channelValue.setVisible(true);
        channelValue.setBorder(BorderFactory.createLineBorder(Color.black));
        channelValue.setBounds(650, 246, 86, 59);
        channelValue.setBackground(UIStandards.BACKGROUND_BLUE);
        channelValue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) channelValue.getSelectedItem();
                Log.e("Channel selected - " + selectedValue, ClientView.class);
            }
        });
        clientPanel.add(channelValue);

        JLabel frequencyLabel = new JLabel(AppConstants.FREQUENCY_VALUE_STRING, JLabel.CENTER);
        frequencyLabel.setHorizontalAlignment(JLabel.CENTER);
        frequencyLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel frequencyLabelPanel = new JPanel();
        frequencyLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        frequencyLabelPanel.setBounds(555, 326, 86, 59);
        frequencyLabelPanel.setBackground(UIStandards.BACKGROUND_BLUE);
        frequencyLabelPanel.add(frequencyLabel);
        clientPanel.add(frequencyLabelPanel);

        JTextPane frequencyTextPanel = new JTextPane();
        frequencyTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        frequencyTextPanel.setBounds(650, 326, 86, 59);
        frequencyTextPanel.setBackground(UIStandards.BACKGROUND_PINK);
        clientPanel.add(frequencyTextPanel);

        parent.add(clientPanel);
        clientPanel.repaint();

        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void changedValues() {
                labelTemp.setText("Min " + ClientModel.get().getMinimum() + " | Max " + ClientModel.get().getMaximum() + " | Avg " + ClientModel.get().getAverage() + " | Channels " + ClientModel.get().getChannelCount());
            }

            @Override
            public void changedChannelCount(int count) {
                labelTemp.setText("Min " + ClientModel.get().getMinimum() + " | Max " + ClientModel.get().getMaximum() + " | Avg " + ClientModel.get().getAverage() + " | Channels " + count);
            }

            @Override
            public void started() {}

            @Override
            public void shutdown() {}
        });
    }
}