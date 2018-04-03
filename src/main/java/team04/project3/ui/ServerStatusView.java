package team04.project3.ui;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.model.EmostatePacket;
import team04.project3.model.Emotion;
import team04.project3.model.Expression;
import team04.project3.model.server.ServerModel;
import team04.project3.model.websocket.EmostatePacketBuilder;
import team04.project3.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.Timer;
import java.util.TimerTask;

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
    private JComboBox<String> performanceMetricDropDown;

    private JPanel panelBuffer;
    private JTextField timeField;
    private JSpinner spinnerUpperFace;
    private JSpinner spinnerDownFace;
    private JSpinner spinnerPerformanceMetric;
    private JRadioButton activeRadioButton;
    private double timeCounter = 0;
    
    /**
     * The view that
     */
    public ServerStatusView() {

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(60, 8, 8, 8));
        this.setOpaque(false);

        panelBuffer = new JPanel(new GridLayout(5, 2, 50, 20));
        panelBuffer.setBackground(Color.lightGray);
        panelBuffer.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray), "Detection", TitledBorder.LEADING,
                TitledBorder.TOP, TextConstants.DEFAULT_FONT, Color.black));
        panelBuffer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        createTimePanel();
        createUpperFacePanel();
        createLowerFacePanel();
        createEyePanel();
        createPerformanceMetricPanel();

        trackAndUpdateTimeField();
        this.add(panelBuffer);
        
    }

    private void createUpperFacePanel() {

        JLabel labelPromptMaximum = new JLabel(TextConstants.UPPER_FACE);
        labelPromptMaximum.setFont(TextConstants.DEFAULT_FONT);
        labelPromptMaximum.setHorizontalAlignment(JLabel.CENTER);
        labelPromptMaximum.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptMaximum = new JPanel();
        panelPromptMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptMaximum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptMaximum.add(labelPromptMaximum);
        panelBuffer.add(panelPromptMaximum);

        upperFaceDropDown = new JComboBox<String>(upperFaceDropDownValues);
        upperFaceDropDown.setMaximumSize(upperFaceDropDown.getPreferredSize());
        upperFaceDropDown.setVisible(true);
        upperFaceDropDown.setBackground(Color.white);

        upperFaceDropDown.addActionListener(event -> {
            try {
                makeAndSetExpressionPacket();
            } catch (Exception e) {
                Log.w("Failed to parse upper face drop down value", ServerStatusView.class);
            }
        });

        spinnerUpperFace = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 65536d, 0.10d) );
        spinnerUpperFace.setVisible(true);
        spinnerUpperFace.setBorder(null);
        spinnerUpperFace.getEditor().getComponent(0).setBackground(Color.gray);
        spinnerUpperFace.setFont(TextConstants.LARGE_FONT);

        spinnerUpperFace.addChangeListener(event -> {
            try {
                spinnerUpperFace.commitEdit();
                makeAndSetExpressionPacket();
            } catch (ParseException e) {
                Log.w("Failed to parse upper face spinner input", ServerStatusView.class);
            }
        });

        JPanel panelUpperFace = new JPanel(new GridLayout(1,2,0,1));
        panelUpperFace.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelUpperFace.add(upperFaceDropDown, BorderLayout.LINE_START);
        panelUpperFace.add(spinnerUpperFace, BorderLayout.LINE_END);
        panelBuffer.add(panelUpperFace);

    }

    private void createLowerFacePanel() {

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

        downFaceDropDown.addActionListener(event -> {
            try {
                makeAndSetExpressionPacket();
            } catch (Exception e) {
                Log.w("Failed to parse down face drop down value", ServerStatusView.class);
            }
        });

        spinnerDownFace = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 65536d, 0.10d) );
        spinnerDownFace.setVisible(true);
        spinnerDownFace.setBorder(null);
        spinnerDownFace.getEditor().getComponent(0).setBackground(Color.gray);
        spinnerDownFace.setFont(TextConstants.LARGE_FONT);

        spinnerDownFace.addChangeListener(event -> {
            try {
                spinnerDownFace.commitEdit();
                makeAndSetExpressionPacket();
            } catch (ParseException e) {
                Log.w("Failed to parse down face spinner input", ServerStatusView.class);
            }
        });

        JPanel panelDown = new JPanel(new GridLayout(1,2,10,1));
        panelDown.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelDown.add(downFaceDropDown);
        panelDown.add(spinnerDownFace);
        panelBuffer.add(panelDown);

    }

    private void createEyePanel() {

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

        eyeDropDown.addActionListener(event -> {
            try {
                makeAndSetExpressionPacket();
            } catch (Exception e) {
                Log.w("Failed to parse eye face drop down value", ServerStatusView.class);
            }
        });

        activeRadioButton = new JRadioButton("Active");
        activeRadioButton.setBackground(Color.LIGHT_GRAY);
        activeRadioButton.addActionListener(e -> {
            makeAndSetExpressionPacket();
        });

        JPanel panelEye = new JPanel();
        panelEye.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelEye.add(eyeDropDown);
        panelEye.add(activeRadioButton);
        panelBuffer.add(panelEye);
    }

    private void createPerformanceMetricPanel() {

        JLabel labelPromptPerformance = new JLabel(TextConstants.PERFORMANCE_METRICS);
        labelPromptPerformance.setFont(TextConstants.DEFAULT_FONT);
        labelPromptPerformance.setHorizontalAlignment(JLabel.CENTER);
        labelPromptPerformance.setVerticalAlignment(JLabel.CENTER);

        JPanel panelPromptPerformance = new JPanel();
        panelPromptPerformance.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPromptPerformance.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelPromptPerformance.add(labelPromptPerformance);
        panelBuffer.add(panelPromptPerformance);

        performanceMetricDropDown = new JComboBox<>(EnumSet.allOf(Emotion.class).stream().map(s -> s.name).toArray(String[]::new));
        performanceMetricDropDown.setVisible(true);
        performanceMetricDropDown.setBackground(Color.white);

        performanceMetricDropDown.addActionListener(event -> {
            try {
                makeAndSetExpressionPacket();
            } catch (Exception e) {
                Log.w("Failed to parse performance metrics drop down value", ServerStatusView.class);
            }
        });

        spinnerPerformanceMetric = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 65536d, 0.10d) );
        spinnerPerformanceMetric.setVisible(true);
        spinnerPerformanceMetric.setBorder(null);
        spinnerPerformanceMetric.getEditor().getComponent(0).setBackground(Color.gray);
        spinnerPerformanceMetric.setFont(TextConstants.LARGE_FONT);

        spinnerPerformanceMetric.addChangeListener(event -> {
            try {
                spinnerPerformanceMetric.commitEdit();
                makeAndSetExpressionPacket();
            } catch (ParseException e) {
                Log.w("Failed to parse performance face spinner input", ServerStatusView.class);
            }
        });

        JPanel panelDown = new JPanel(new GridLayout(1,2,10,1));
        panelDown.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelDown.add(performanceMetricDropDown);
        panelDown.add(spinnerPerformanceMetric);
        panelBuffer.add(panelDown);

    }

    /**
     * method that creates the time area and add to the "emostate" panel.
     * 
     * @param constraints to set the positions of labels and textfields.
     */

    private void createTimePanel () {
    	
    	JPanel timePanel = new JPanel();
    	timePanel.setLayout(new GridBagLayout());
    	timePanel.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        
        JLabel timeLabel = new JLabel ("Time");
        timeLabel.setFont(TextConstants.DEFAULT_FONT);
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel panelTime = new JPanel();
        panelTime.setBorder(BorderFactory.createLineBorder(Color.black));
        panelTime.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelTime.add(timeLabel);
        panelBuffer.add(panelTime);
        
        timeField = new JTextField(5);
        timeField.setVisible(true);
        timeField.setFont(TextConstants.DEFAULT_FONT);
        timeField.setBackground(Color.gray);
        timeField.setText("0");
        timeField.setEditable(false);
        timePanel.add(timeField);
        
        JLabel secondsLabel = new JLabel(" Seconds");
        timePanel.add(secondsLabel);
        
        panelBuffer.add(timePanel);
    
    }

    /**
     * 	tracks time value and updates in time field when a dataset is sent
     * 	to client at specified interval.
     */
    public void trackAndUpdateTimeField() {
    	
    	Timer intervalChecker = new Timer();
    	intervalChecker.scheduleAtFixedRate(new TimerTask() {
    		
    		public void run() {
    			
    			if(timeCounter != ServerSettingsView.getTimeCounter()) {
    				timeCounter = ServerSettingsView.getTimeCounter();
    				timeField.setText((String.valueOf(ServerSettingsView.getTimeCounter())));
    			}
    		}
    	}, 0, 1);
    }
    
    private void makeAndSetExpressionPacket() {

        String upperFaceExpression = upperFaceDropDown.getSelectedItem().toString();
        float upperFaceEmotionValue =  (float) ((double) spinnerUpperFace.getValue());

        String downFaceExpression = downFaceDropDown.getSelectedItem().toString();
        float downFaceEmotionValue = (float) ((double) spinnerUpperFace.getValue());

        String eyeExpression = eyeDropDown.getSelectedItem().toString();

        String performanceMetric = performanceMetricDropDown.getSelectedItem().toString();
        float performanceMetricSpinnerValue = (float) ((double) spinnerPerformanceMetric.getValue());

        EmostatePacketBuilder emostatePacketBuilder = new EmostatePacketBuilder();
        emostatePacketBuilder.setExpression(Expression.expressionMap.get(upperFaceExpression), upperFaceEmotionValue);
        emostatePacketBuilder.setExpression(Expression.expressionMap.get(downFaceExpression), downFaceEmotionValue);
        emostatePacketBuilder.setExpression(Expression.expressionMap.get(eyeExpression), activeRadioButton.isSelected());

        ServerModel.get().setPacket(emostatePacketBuilder);
    }
}
