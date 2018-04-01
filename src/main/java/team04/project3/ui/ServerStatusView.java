package team04.project3.ui;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.model.Expression;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The view that contains an
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerStatusView extends  JPanel {

    private String[] eyeDropDownValues = new String[] {Expression.BLINK.name, Expression.WINK_LEFT.name, Expression.WINK_RIGHT.name,
                                                        Expression.LOOK_LEFT.name, Expression.LOOK_RIGHT.name};
    private String[] upperFaceDropDownValues = new String[] {Expression.BROW_RAISE.name, Expression.BROW_FURROW.name};
    private String[] downFaceDropDownValues = new String[] {Expression.SMILE.name, Expression.CLENCH.name, Expression.SMIRK_LEFT.name,
                                                      Expression.SMIRK_RIGHT.name, Expression.LAUGH.name};
    private JComboBox<String> upperFaceDropDown;
    private JComboBox<String> downFaceDropDown;
    private JComboBox<String> eyeDropDown;

    private JPanel panelBuffer;
    
    
    /**
     * The view that
     */
    public ServerStatusView() {

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(60, 8, 8, 8));
        this.setOpaque(false);

        panelBuffer = new JPanel(new GridLayout(4, 2, 50, 25));
        panelBuffer.setBackground(Color.lightGray);
        panelBuffer.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelBuffer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        panelBuffer.add(createTimePanel());
        panelBuffer.add(new JLabel());
        
        // Maximum - Prompt
        JLabel labelPromptMaximum = new JLabel(TextConstants.UPPER_FACE);
        labelPromptMaximum.setFont(TextConstants.DEFAULT_FONT);
        labelPromptMaximum.setHorizontalAlignment(JLabel.CENTER);
        labelPromptMaximum.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptMaximum = new JPanel();
        panelPromptMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptMaximum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptMaximum.add(labelPromptMaximum);
        panelBuffer.add(panelPromptMaximum);

        // Maximum - Input
        upperFaceDropDown = new JComboBox<String>(upperFaceDropDownValues);
        upperFaceDropDown.setMaximumSize(upperFaceDropDown.getPreferredSize());
        upperFaceDropDown.setVisible(true);
        upperFaceDropDown.setBackground(Color.white);

        JSpinner spinnerUpperFace = new JSpinner( new SpinnerNumberModel(0.0, -65536, 65536, 0.10) );
        spinnerUpperFace.setVisible(true);
        spinnerUpperFace.setBorder(null);
        spinnerUpperFace.getEditor().getComponent(0).setBackground(Color.gray);
        spinnerUpperFace.setFont(TextConstants.LARGE_FONT);

        JPanel panelUpperFace = new JPanel(new GridLayout(1,2,0,1));
        panelUpperFace.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelUpperFace.add(upperFaceDropDown, BorderLayout.LINE_START);
        panelUpperFace.add(spinnerUpperFace, BorderLayout.LINE_END);
        panelBuffer.add(panelUpperFace);


        JLabel labelPromptDownFace = new JLabel(TextConstants.DOWN_FACE);
        labelPromptDownFace.setFont(TextConstants.DEFAULT_FONT);
        labelPromptDownFace.setHorizontalAlignment(JLabel.CENTER);
        labelPromptDownFace.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptDownFace = new JPanel();
        panelPromptDownFace.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptDownFace.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptDownFace.add(labelPromptDownFace);
        panelBuffer.add(panelPromptDownFace);

        downFaceDropDown = new JComboBox<String>(downFaceDropDownValues);
        downFaceDropDown.setVisible(true);
        downFaceDropDown.setBackground(Color.white);

        JSpinner spinnerDownFace = new JSpinner( new SpinnerNumberModel(0.0, -65536, 65536, 0.10) );
        spinnerDownFace.setVisible(true);
        spinnerDownFace.setBorder(null);
        spinnerDownFace.getEditor().getComponent(0).setBackground(Color.gray);
        spinnerDownFace.setFont(TextConstants.LARGE_FONT);

        JPanel panelDown = new JPanel(new GridLayout(1,2,10,1));
        panelDown.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelDown.add(downFaceDropDown);
        panelDown.add(spinnerDownFace);
        panelBuffer.add(panelDown);


        JLabel labelPromptEye = new JLabel(TextConstants.EYE);
        labelPromptEye.setFont(TextConstants.DEFAULT_FONT);
        labelPromptEye.setHorizontalAlignment(JLabel.CENTER);
        labelPromptEye.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptEye = new JPanel();
        panelPromptEye.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptEye.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptEye.add(labelPromptEye);
        panelBuffer.add(panelPromptEye);

        eyeDropDown = new JComboBox<String>(eyeDropDownValues);
        eyeDropDown.setVisible(true);
        eyeDropDown.setBackground(Color.white);

        JPanel panelEye = new JPanel();
        panelEye.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelEye.add(eyeDropDown);
        panelBuffer.add(panelEye);


        this.add(panelBuffer);

        
        // Put the main view in a "buffer", this creates a transparent border around the buffer's content
    }
    
    /**
     * method that creates the time area and add to the "emostate" panel.
     * 
     * @param constraints to set the positions of labels and textfields.*/

    public JPanel createTimePanel () {
    	
    	JPanel timePanel = new JPanel();
    	timePanel.setLayout(new GridBagLayout());
    	timePanel.setBackground(Color.lightGray);
        
    	
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        
        JLabel timeLabel = new JLabel ("Time:",JLabel.LEFT);
        timeLabel.setBorder(new EmptyBorder(10,10,10,10));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        timePanel.add(timeLabel);
        
        JTextField timeField = new JTextField(11);
        timeField.setBorder(BorderFactory.createLineBorder(Color.black));
        timeField.setVisible(true);
        timeField.setMinimumSize(timeField.getPreferredSize());
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 1;
        timePanel.add(timeField);
        
        JLabel secondsLabel = new JLabel(" Seconds");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 1;
        timePanel.add(secondsLabel);
        
        return timePanel;
    }
}
