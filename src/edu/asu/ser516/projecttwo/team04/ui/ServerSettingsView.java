package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.constants.ColorConstants;
import edu.asu.ser516.projecttwo.team04.constants.StringConstants;
import edu.asu.ser516.projecttwo.team04.model.server.ServerModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ServerSettingsView - The right hand side to change the output min/max/frequency
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerSettingsView extends JPanel {
    public ServerSettingsView() {
        // Create the settings view with a transparent border encompassing
        this.setLayout(new GridLayout(5, 2, 8, 8));
        this.setBorder(new EmptyBorder(8, 8, 8, 8));
        this.setOpaque(false);

        // Maximum - Prompt
        JLabel labelPromptMaximum = new JLabel(StringConstants.HIGHEST_VALUE_STRING);
        labelPromptMaximum.setFont(ColorConstants.DEFAULT_FONT);
        labelPromptMaximum.setHorizontalAlignment(JLabel.CENTER);
        labelPromptMaximum.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptMaximum = new JPanel();
        panelPromptMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptMaximum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptMaximum.add(labelPromptMaximum);
        this.add(panelPromptMaximum);

        // Maximum - Input
        JSpinner spinnerInputMaximum = new JSpinner( new SpinnerNumberModel(ServerModel.get().getValueMax(), -65536, 65536, 1) );
        spinnerInputMaximum.setVisible(true);
        spinnerInputMaximum.setBorder(null);
        spinnerInputMaximum.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_PINK);
        spinnerInputMaximum.setFont(ColorConstants.LARGE_FONT);
        spinnerInputMaximum.addChangeListener(e -> {
            ServerModel.get().setValueMax((Integer) spinnerInputMaximum.getValue());
        });

        JPanel panelInputMaximum = new JPanel();
        panelInputMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelInputMaximum.setBackground(ColorConstants.BACKGROUND_PINK);
        panelInputMaximum.add(spinnerInputMaximum);
        this.add(panelInputMaximum);

        // Minimum - Prompt
        JLabel labelPromptMinimum = new JLabel(StringConstants.LOWEST_VALUE_STRING);
        labelPromptMinimum.setFont(ColorConstants.DEFAULT_FONT);
        labelPromptMinimum.setHorizontalAlignment(JLabel.CENTER);
        labelPromptMinimum.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptMinimum = new JPanel();
        panelPromptMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptMinimum.setBackground(ColorConstants.BACKGROUND_PINK);
        panelPromptMinimum.add(labelPromptMinimum);
        this.add(panelPromptMinimum);

        // Minimum - Input
        JSpinner spinnerInputMinimum = new JSpinner( new SpinnerNumberModel(ServerModel.get().getValueMin(), -65536, 65536, 1) );
        spinnerInputMinimum.setVisible(true);
        spinnerInputMinimum.setBorder(null);
        spinnerInputMinimum.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        spinnerInputMinimum.setFont(ColorConstants.LARGE_FONT);
        spinnerInputMinimum.addChangeListener(e -> {
            ServerModel.get().setValueMin((Integer) spinnerInputMinimum.getValue());
        });

        JPanel panelInputMinimum = new JPanel();
        panelInputMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelInputMinimum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelInputMinimum.add(spinnerInputMinimum);
        this.add(panelInputMinimum);

        // Frequency - Prompt
        JLabel labelPromptFrequency = new JLabel(StringConstants.FREQUENCY_VALUE_STRING);
        labelPromptFrequency.setFont(ColorConstants.DEFAULT_FONT);
        labelPromptFrequency.setHorizontalAlignment(JLabel.CENTER);
        labelPromptFrequency.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptFrequency = new JPanel();
        panelPromptFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptFrequency.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptFrequency.add(labelPromptFrequency);
        this.add(panelPromptFrequency);

        // Frequency - Input
        JSpinner spinnerInputFrequency = new JSpinner( new SpinnerNumberModel(ServerModel.get().getFrequency(), -65536, 65536, 1) );
        spinnerInputFrequency.setVisible(true);
        spinnerInputFrequency.setBorder(null);
        spinnerInputFrequency.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_PINK);
        spinnerInputFrequency.setFont(ColorConstants.LARGE_FONT);
        spinnerInputFrequency.addChangeListener(e -> {
            ServerModel.get().setFrequency((Integer) spinnerInputFrequency.getValue());
        });

        JPanel panelInputFrequency = new JPanel();
        panelInputFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
        panelInputFrequency.setBackground(ColorConstants.BACKGROUND_PINK);
        panelInputFrequency.add(spinnerInputFrequency);
        this.add(panelInputFrequency);

        // Add four empty panels (to scale like the specification and the client's 5 rows)
        // (we have 3 rows, so add two rows, each with two empty columns)
        this.add(new JLabel());
        this.add(new JLabel());
        this.add(new JLabel());
        this.add(new JLabel());
    }
}
