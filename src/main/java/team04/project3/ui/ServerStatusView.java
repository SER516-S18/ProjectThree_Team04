package team04.project3.ui;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.model.Emotion;
import team04.project3.model.Expression;
import team04.project3.model.server.ServerModel;
import team04.project3.model.EmostatePacketBuilder;
import team04.project3.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The view that contains an
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerStatusView extends  JPanel {
    
	private EmostatePacketBuilder emostatePacketBuilder = EmostatePacketBuilder.getZeroedEmostatePacket();
    private Expression[] eyeDropDownValues = new Expression[] {Expression.BLINK, Expression.WINK_LEFT, Expression.WINK_RIGHT,
            Expression.LOOK_LEFT, Expression.LOOK_RIGHT};
    private Expression[] upperFaceDropDownValues = new Expression[] {Expression.BROW_RAISE, Expression.BROW_FURROW};
    private Expression[] downFaceDropDownValues = new Expression[] {Expression.SMILE, Expression.CLENCH, Expression.SMIRK_LEFT,
            Expression.SMIRK_RIGHT, Expression.LAUGH};

    private JComboBox<Expression> upperFaceDropDown;
    private JComboBox<Expression> downFaceDropDown;
    private JComboBox<Expression> eyeDropDown;
    private JComboBox<Emotion> performanceMetricDropDown;

    private JPanel panelBuffer;
    private JTextField timeField;
    private JSpinner spinnerUpperFace;
    private JSpinner spinnerDownFace;
    private JSpinner spinnerPerformanceMetric;
    private JCheckBox checkboxEye;
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

        upperFaceDropDown = new JComboBox<>();
        upperFaceDropDown.setModel(new DefaultComboBoxModel<>(upperFaceDropDownValues));
        upperFaceDropDown.setMaximumSize(upperFaceDropDown.getPreferredSize());
        upperFaceDropDown.setVisible(true);
        upperFaceDropDown.setBackground(Color.white);
        upperFaceDropDown.addActionListener(event -> {
            spinnerUpperFace.setValue(emostatePacketBuilder.getExpression((Expression) upperFaceDropDown.getSelectedItem()).doubleValue());
        });

        spinnerUpperFace = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 1d, 0.05d) );
        spinnerUpperFace.setVisible(true);
        spinnerUpperFace.setBorder(null);
        spinnerUpperFace.getEditor().getComponent(0).setBackground(Color.gray);
        spinnerUpperFace.setFont(TextConstants.LARGE_FONT);

        spinnerUpperFace.addChangeListener(event -> {
            try {
                spinnerUpperFace.commitEdit();
                updateExpressionPacket();
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

        downFaceDropDown = new JComboBox<>();
        downFaceDropDown.setModel(new DefaultComboBoxModel<>(downFaceDropDownValues));
        downFaceDropDown.setVisible(true);
        downFaceDropDown.setBackground(Color.white);

        downFaceDropDown.addActionListener(event -> {
            spinnerDownFace.setValue(emostatePacketBuilder.getExpression((Expression) downFaceDropDown.getSelectedItem()).doubleValue());
        });

        spinnerDownFace = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 1d, 0.05d) );
        spinnerDownFace.setVisible(true);
        spinnerDownFace.setBorder(null);
        spinnerDownFace.getEditor().getComponent(0).setBackground(Color.gray);
        spinnerDownFace.setFont(TextConstants.LARGE_FONT);

        spinnerDownFace.addChangeListener(event -> {
            try {
                spinnerDownFace.commitEdit();
                updateExpressionPacket();
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

        eyeDropDown = new JComboBox<>();
        eyeDropDown.setModel(new DefaultComboBoxModel<>(eyeDropDownValues));
        eyeDropDown.setVisible(true);
        eyeDropDown.setBackground(Color.white);

        eyeDropDown.addActionListener(event -> {
            checkboxEye.setSelected(emostatePacketBuilder.getExpressionBoolean((Expression) eyeDropDown.getSelectedItem()));
        });

        checkboxEye = new JCheckBox("Active");
        checkboxEye.setBackground(Color.LIGHT_GRAY);
        checkboxEye.addActionListener(e -> {
            updateExpressionPacket();
        });

        JPanel panelEye = new JPanel();
        panelEye.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
        panelEye.add(eyeDropDown);
        panelEye.add(checkboxEye);
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

        performanceMetricDropDown = new JComboBox<>();
        performanceMetricDropDown.setModel(new DefaultComboBoxModel<>(Emotion.values()));
        performanceMetricDropDown.setVisible(true);
        performanceMetricDropDown.setBackground(Color.white);

        performanceMetricDropDown.addActionListener(event -> {
            try {
                updateExpressionPacket();
            } catch (Exception e) {
                Log.w("Failed to parse performance metrics drop down value", ServerStatusView.class);
            }
        });

        spinnerPerformanceMetric = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 1d, 0.05d) );
        spinnerPerformanceMetric.setVisible(true);
        spinnerPerformanceMetric.setBorder(null);
        spinnerPerformanceMetric.getEditor().getComponent(0).setBackground(Color.gray);
        spinnerPerformanceMetric.setFont(TextConstants.LARGE_FONT);

        spinnerPerformanceMetric.addChangeListener(event -> {
            try {
                spinnerPerformanceMetric.commitEdit();
                updateExpressionPacket();
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
        
        JLabel secondsLabel = new JLabel(" seconds");
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
    				
    				//setting tick value on timeCounter change
    				emostatePacketBuilder.setTick((float)timeCounter);
    			}
    		}
    	}, 0, 1);
    }
    
    private void updateExpressionPacket() {
        Expression upperFaceExpression = (Expression) upperFaceDropDown.getSelectedItem();
        float upperFaceEmotionValue =  (float) ((double) spinnerUpperFace.getValue());

        Expression downFaceExpression = (Expression) downFaceDropDown.getSelectedItem();
        float downFaceEmotionValue = (float) ((double) spinnerDownFace.getValue());

        Expression eyeExpression = (Expression) eyeDropDown.getSelectedItem();

        Emotion performanceMetric = (Emotion) performanceMetricDropDown.getSelectedItem();
        float performanceMetricSpinnerValue = (float) ((double) spinnerPerformanceMetric.getValue());

        emostatePacketBuilder.setExpression(upperFaceExpression, upperFaceEmotionValue);
        emostatePacketBuilder.setExpression(downFaceExpression, downFaceEmotionValue);
        emostatePacketBuilder.setExpression(eyeExpression, checkboxEye.isSelected());
        emostatePacketBuilder.setEmotion(performanceMetric, performanceMetricSpinnerValue);

        ServerModel.get().setPacket(emostatePacketBuilder);
    }
}
