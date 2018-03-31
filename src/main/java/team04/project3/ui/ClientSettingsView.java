package team04.project3.ui;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The right hand side of the Client app, containing settings and the min/max/avg
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientSettingsView extends JPanel {
    private JLabel labelValueMaximum;
    private JLabel labelValueMinimum;
    private JLabel labelValueAverage;
    private JSpinner spinnerInputChannels;
    private JSpinner spinnerInputFrequency;

    /**
     * The right hand side of the Client app, containing settings and the min/max/avg
     */
    public ClientSettingsView() {
        this.setLayout(new GridLayout(5, 2, 8, 8));
        this.setBorder(new EmptyBorder(8, 8, 8, 8));
        this.setOpaque(false);

        // Maximum - Prompt
        JLabel labelPromptMaximum = new JLabel(TextConstants.HIGHEST_VALUE_STRING);
        labelPromptMaximum.setFont(TextConstants.DEFAULT_FONT);
        labelPromptMaximum.setHorizontalAlignment(JLabel.CENTER);
        labelPromptMaximum.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptMaximum = new JPanel();
        panelPromptMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptMaximum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptMaximum.add(labelPromptMaximum);
        this.add(panelPromptMaximum);

        // Maximum - Value
        labelValueMaximum = new JLabel("DEPRECATED");
        labelValueMaximum.setFont(TextConstants.LARGE_FONT);

        JPanel panelValueMaximum = new JPanel();
        panelValueMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelValueMaximum.setBackground(ColorConstants.BACKGROUND_PINK);
        panelValueMaximum.add(labelValueMaximum);
        this.add(panelValueMaximum);

        // Minimum - Prompt
        JLabel labelPromptMinimum = new JLabel(TextConstants.LOWEST_VALUE_STRING);
        labelPromptMinimum.setFont(TextConstants.DEFAULT_FONT);
        labelPromptMinimum.setHorizontalAlignment(JLabel.CENTER);
        labelPromptMinimum.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptMinimum = new JPanel();
        panelPromptMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptMinimum.setBackground(ColorConstants.BACKGROUND_PINK);
        panelPromptMinimum.add(labelPromptMinimum);
        this.add(panelPromptMinimum);

        // Minimum - Value
        labelValueMinimum = new JLabel("DEPRECATED");
        labelValueMinimum.setFont(TextConstants.LARGE_FONT);

        JPanel panelValueMinimum = new JPanel();
        panelValueMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelValueMinimum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelValueMinimum.add(labelValueMinimum);
        this.add(panelValueMinimum);

        // Average - Prompt
        JLabel labelPromptAverage = new JLabel(TextConstants.AVERAGE_VALUE_STRING);
        labelPromptAverage.setFont(TextConstants.DEFAULT_FONT);
        labelPromptAverage.setHorizontalAlignment(JLabel.CENTER);
        labelPromptAverage.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptAverage = new JPanel();
        panelPromptAverage.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptAverage.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptAverage.add(labelPromptAverage);
        this.add(panelPromptAverage);

        // Average - Value
        labelValueAverage = new JLabel("DEPRECATED");
        labelValueAverage.setFont(TextConstants.LARGE_FONT);

        JPanel panelValueAverage = new JPanel();
        panelValueAverage.setBorder(BorderFactory.createLineBorder(Color.black));
        panelValueAverage.setBackground(ColorConstants.BACKGROUND_PINK);
        panelValueAverage.add(labelValueAverage);
        this.add(panelValueAverage);

        // Channels - Prompt
        JLabel labelPromptChannels = new JLabel(TextConstants.CHANNELS_VALUE_STRING);
        labelPromptChannels.setFont(TextConstants.DEFAULT_FONT);
        labelPromptChannels.setHorizontalAlignment(JLabel.CENTER);
        labelPromptChannels.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptChannels = new JPanel();
        panelPromptChannels.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptChannels.setBackground(ColorConstants.BACKGROUND_PINK);
        panelPromptChannels.add(labelPromptChannels);
        this.add(panelPromptChannels);

        // Channels - Input
        spinnerInputChannels = new JSpinner( new SpinnerNumberModel(1, 1, 65536, 1) );
        spinnerInputChannels.setVisible(true);
        spinnerInputChannels.setBorder(null);
        spinnerInputChannels.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        spinnerInputChannels.setFont(TextConstants.LARGE_FONT);

        JPanel panelInputChannels = new JPanel();
        panelInputChannels.setBorder(BorderFactory.createLineBorder(Color.black));
        panelInputChannels.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelInputChannels.add(spinnerInputChannels);
        this.add(panelInputChannels);

        // Frequency - Prompt
        JLabel labelPromptFrequency = new JLabel(TextConstants.FREQUENCY_VALUE_STRING);
        labelPromptFrequency.setFont(TextConstants.DEFAULT_FONT);
        labelPromptFrequency.setHorizontalAlignment(JLabel.CENTER);
        labelPromptFrequency.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptFrequency = new JPanel();
        panelPromptFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptFrequency.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptFrequency.add(labelPromptFrequency);
        this.add(panelPromptFrequency);

        // Frequency - Input
        spinnerInputFrequency = new JSpinner( new SpinnerNumberModel(1, 1, 65536, 1) );
        spinnerInputFrequency.setVisible(true);
        spinnerInputFrequency.setBorder(null);
        spinnerInputFrequency.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_PINK);
        spinnerInputFrequency.setFont(TextConstants.LARGE_FONT);

        JPanel panelInputFrequency = new JPanel();
        panelInputFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
        panelInputFrequency.setBackground(ColorConstants.BACKGROUND_PINK);
        panelInputFrequency.add(spinnerInputFrequency);
        this.add(panelInputFrequency);
    }
}
