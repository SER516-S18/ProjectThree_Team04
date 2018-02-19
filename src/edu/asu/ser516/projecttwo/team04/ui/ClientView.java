package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ClientModel;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
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
    private JComboBox<String> channelDropDown;

    public ClientView(AppView appView) {
        parent = appView;

        JPanel clientPanel = new JPanel();
        clientPanel.setBackground(Color.lightGray);
        clientPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        clientPanel.setBounds(5, 70, 780, 400);
        clientPanel.setLayout(null);

        JPanel graphView = new JPanel();
        graphView.setBackground(UIStandards.BACKGROUND_PINK);
        graphView.setBounds(15, 15, 520, 370);
        graphView.setLayout(null);
        graphView.setBorder(BorderFactory.createLineBorder(Color.black));
        clientPanel.add(graphView);

        JLabel highVal = new JLabel("<html>Highest<br>value:</html>");
        highVal.setBorder(BorderFactory.createLineBorder(Color.black));
        highVal.setBackground(UIStandards.BACKGROUND_BLUE);
        highVal.setBounds(560, 15, 85, 60);
        highVal.setHorizontalAlignment(SwingConstants.CENTER);
        highVal.setOpaque(true);
        clientPanel.add(highVal);

        JTextPane maxValTextPanel = new JTextPane();
        maxValTextPanel.setEditable(false);
        maxValTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        maxValTextPanel.setBounds(655, 15, 85, 60);
        maxValTextPanel.setBackground(UIStandards.BACKGROUND_PINK);
        clientPanel.add(maxValTextPanel);

        JLabel lowVal = new JLabel("<html>Lowest<br>value:</html>");
        lowVal.setBorder(BorderFactory.createLineBorder(Color.black));
        lowVal.setBackground(UIStandards.BACKGROUND_PINK);
        lowVal.setBounds(560, 90, 85, 60);
        lowVal.setHorizontalAlignment(SwingConstants.CENTER);
        lowVal.setOpaque(true);
        clientPanel.add(lowVal);

        JTextPane lowTxt = new JTextPane();
        lowTxt.setBorder(BorderFactory.createLineBorder(Color.black));
        lowTxt.setBackground(UIStandards.BACKGROUND_BLUE);
        lowTxt.setBounds(655, 90, 85, 60);
        clientPanel.add(lowTxt);

        JLabel average = new JLabel("<html>Average</html>");
        average.setBorder(BorderFactory.createLineBorder(Color.black));
        average.setBackground(UIStandards.BACKGROUND_BLUE);
        average.setBounds(560, 165, 85, 60);
        average.setHorizontalAlignment(SwingConstants.CENTER);
        average.setOpaque(true);
        clientPanel.add(average);

        JTextPane avgTxt = new JTextPane();
        avgTxt.setBorder(BorderFactory.createLineBorder(Color.black));
        avgTxt.setBackground(UIStandards.BACKGROUND_PINK);
        avgTxt.setBounds(655, 165, 85, 60);
        clientPanel.add(avgTxt);

        JLabel channels = new JLabel("<html>Channels:</html>");
        channels.setBorder(BorderFactory.createLineBorder(Color.black));
        channels.setBackground(UIStandards.BACKGROUND_PINK);
        channels.setBounds(560, 245, 85, 60);
        channels.setHorizontalAlignment(SwingConstants.CENTER);
        channels.setOpaque(true);
        clientPanel.add(channels);

        channelDropDown = new JComboBox<>(UIStandards.NUMBER_OF_CHANNELS);
        channelDropDown.setVisible(true);
        channelDropDown.setBorder(BorderFactory.createLineBorder(Color.black));
        channelDropDown.setBackground(UIStandards.BACKGROUND_BLUE);
        channelDropDown.setBounds(655, 245, 85, 60);
        channelDropDown.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) channelDropDown.getSelectedItem();

            }
        });
        clientPanel.add(channelDropDown);

        JLabel frequency = new JLabel("<html>Frequency<br>(Hz):</html>");
        frequency.setBorder(BorderFactory.createLineBorder(Color.black));
        frequency.setBackground(UIStandards.BACKGROUND_BLUE);
        frequency.setBounds(560,325,85,60);
        frequency.setHorizontalAlignment(SwingConstants.CENTER);
        frequency.setOpaque(true);
        clientPanel.add(frequency);

        NumberFormat longFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        JFormattedTextField freqTxt = new JFormattedTextField(numberFormatter);
        freqTxt.setBorder(BorderFactory.createLineBorder(Color.black));
        freqTxt.setBackground(UIStandards.BACKGROUND_PINK);
        freqTxt.setBounds(655, 325, 85, 60);
        clientPanel.add(freqTxt);

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