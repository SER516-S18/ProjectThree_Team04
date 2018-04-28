package team04.project3.ui.server;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.listeners.ServerListener;
import team04.project3.model.EmostatePacketBuilder;
import team04.project3.model.Emotion;
import team04.project3.model.Expression;
import team04.project3.model.server.ServerModel;
import team04.project3.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

/**
 * UI for setting/inputting the emostate values
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerValuesView extends JPanel {

    private JTextField textfieldTime;
    private Float textfieldSelected = null;
    private EmostatePacketBuilder emostatePacketBuilder;
    private final Expression[] expressionEyeValues = new Expression[] {
            Expression.BLINK, Expression.WINK_LEFT, Expression.WINK_RIGHT, Expression.LOOK_LEFT, Expression.LOOK_RIGHT
    };
    private final Expression[] expressionFaceUpperValues = new Expression[] {
            Expression.BROW_RAISE, Expression.BROW_FURROW
    };
    private final Expression[] expressionFaceLowerValues = new Expression[] {
            Expression.SMILE, Expression.CLENCH, Expression.SMIRK_LEFT, Expression.SMIRK_RIGHT, Expression.LAUGH
    };
    
    //added for body movement
    private final Expression[] bodyMovement = new Expression[] {
    	Expression.HAND_UP, Expression.HAND_DOWN, Expression.LEGS_MOMENT	
    };
    //till here
    
    private final Emotion[] emotionValues = Emotion.values();
    private JComboBox<Expression> comboExpressionFaceEyes;
    private JComboBox<Expression> comboExpressionFaceUpper;
    private JComboBox<Expression> comboExpressionFaceLower;
    private JComboBox<Expression> comboExpressionBodyExpression;
    private JComboBox<Emotion> comboEmotion;
    private JCheckBox checkboxEye;
    private JButton buttonEye;
    private JSpinner spinnerUpperFace;
    private JSpinner spinnerBodyMovement;
    private JSpinner spinnerLowerFace;
    private JSpinner spinnerEmotion;

    private Expression previousExpressionEyes;
    private Expression previousExpressionFaceLower;
    private Expression previousExpressionFaceUpper;
    private Expression previousBodyMovement;

    /**
     * Constructor for ServerValuesView, the input for the packet settings to send
     */
    public ServerValuesView() {
        emostatePacketBuilder = EmostatePacketBuilder.getZeroedEmostatePacket();
        ServerModel.get().setPacket(emostatePacketBuilder);
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
            public void packetSent() {
                if(textfieldSelected == null)
                    textfieldTime.setText(Float.toString(ServerModel.get().getTick()));

                // Reset eye values to false
                for(Expression expression : Expression.values()) {
                    // Reset only if it's a binary expression
                    if(expression.isBinary()) {
                        // Except if we're repeating packets and we're holding down the "activate" button
                        if(ServerModel.get().isPacketRepeatMode() && buttonEye != null && buttonEye.getModel().isPressed())
                            continue;

                        emostatePacketBuilder.setExpression(expression, false);
                    }
                }

                // Reset if the checkbox is checked
                if(checkboxEye != null)
                    checkboxEye.setSelected(false);
            }

            @Override
            public void packetRepeatingToggled() { }

            @Override
            public void packetRepeatingModeChanged() {
                if(ServerModel.get().isPacketRepeatMode()) {
                    buttonEye.setVisible(true);
                    checkboxEye.setVisible(false);
                    emostatePacketBuilder.setExpression((Expression) comboExpressionFaceEyes.getSelectedItem(), false);
                } else {
                    buttonEye.setVisible(false);
                    checkboxEye.setVisible(true);
                    emostatePacketBuilder.setExpression((Expression) comboExpressionFaceEyes.getSelectedItem(), false);
                }
            }
        });
        this.init();
    }

    /**
     * Initializes the UI
     */
    private void init() {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Top - Time input
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.X_AXIS));
        panelTop.setBorder(BorderFactory.createEmptyBorder(4,4,16,4));
        this.add(panelTop, BorderLayout.PAGE_START);

        JLabel labelTime = new JLabel("Time:");
        labelTime.setFont(TextConstants.LARGE_FONT);
        panelTop.add(labelTime);

        panelTop.add(Box.createHorizontalStrut(8));

        textfieldTime = new JTextField(Float.toString(ServerModel.get().getTick()));
        textfieldTime.setFont(TextConstants.LARGE_FONT);
        textfieldTime.setMaximumSize(new Dimension(128, 128));
        textfieldTime.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent event) {
                textfieldSelected = ServerModel.get().getTick();
            }

            @Override
            public void focusLost(FocusEvent event) {
                try {
                    Float tick = Float.parseFloat(textfieldTime.getText());
                    if(!textfieldSelected.equals(tick))
                        ServerModel.get().setTick(tick);
                    else
                        textfieldTime.setText(Float.toString(ServerModel.get().getTick()));
                } catch(NumberFormatException e) {
                    Log.w("Failed to parse time (" + e.getMessage() + ")", ServerValuesView.class);
                    textfieldTime.setText(Float.toString(ServerModel.get().getTick()));
                }

                textfieldSelected = null;
            }
        });
        panelTop.add(textfieldTime);

        panelTop.add(Box.createHorizontalStrut(8));

        JLabel labelTimeSeconds = new JLabel("sec");
        labelTimeSeconds.setFont(TextConstants.LARGE_FONT);
        panelTop.add(labelTimeSeconds);

        // Bottom - Emotiv input
        JPanel panelBottom = new JPanel();
        panelBottom.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.Y_AXIS));
        panelBottom.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Emostate Values",
                TitledBorder.LEADING, TitledBorder.TOP, TextConstants.DEFAULT_FONT, Color.black));
        this.add(panelBottom, BorderLayout.CENTER);

        // Eyes
        JPanel panelInputEyes = new JPanel();
        panelInputEyes.setOpaque(false);
        panelInputEyes.setLayout(new BoxLayout(panelInputEyes, BoxLayout.X_AXIS));
        panelInputEyes.setMaximumSize(new Dimension(4096, 64));
        panelInputEyes.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray), "Eyes",
                TitledBorder.LEADING, TitledBorder.TOP, TextConstants.DEFAULT_FONT, Color.black));
        panelBottom.add(panelInputEyes);

        comboExpressionFaceEyes = new JComboBox<>();
        comboExpressionFaceEyes.setModel(new DefaultComboBoxModel<>(expressionEyeValues));
        comboExpressionFaceEyes.setMaximumSize(new Dimension(128, 128));
        comboExpressionFaceEyes.addActionListener(event -> {
            // Reset previous value, if applicable
            if (previousExpressionEyes != null) {
                emostatePacketBuilder.setExpression(previousExpressionEyes, false);
            }

            checkboxEye.setSelected(emostatePacketBuilder.getExpressionBoolean((Expression) comboExpressionFaceEyes.getSelectedItem()));
            previousExpressionEyes = (Expression) comboExpressionFaceEyes.getSelectedItem();
        });
        panelInputEyes.add(comboExpressionFaceEyes, BorderLayout.WEST);

        panelInputEyes.add(Box.createHorizontalStrut(8));

        checkboxEye = new JCheckBox("Active");
        checkboxEye.setOpaque(false);
        checkboxEye.setFont(TextConstants.LARGE_FONT);
        checkboxEye.setVisible(!ServerModel.get().isPacketRepeatMode());
        checkboxEye.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emostatePacketBuilder.setExpression((Expression) comboExpressionFaceEyes.getSelectedItem(), checkboxEye.isSelected());
            }
        });
        panelInputEyes.add(checkboxEye);

        buttonEye = new JButton("Activate");
        buttonEye.setFont(TextConstants.LARGE_FONT);
        buttonEye.setBorder(null);
        buttonEye.setVisible(ServerModel.get().isPacketRepeatMode());
        buttonEye.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) {
                if(buttonEye.isVisible())
                    emostatePacketBuilder.setExpression((Expression) comboExpressionFaceEyes.getSelectedItem(), true);
            }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });
        panelInputEyes.add(buttonEye);

        panelBottom.add(Box.createVerticalStrut(16));
        
        //BodyMovement
        JPanel panelInputBodyMovement = new JPanel();
        panelInputBodyMovement.setOpaque(false);
        panelInputBodyMovement.setLayout(new BoxLayout(panelInputBodyMovement, BoxLayout.X_AXIS));
        panelInputBodyMovement.setMaximumSize(new Dimension(4096, 64));
        panelInputBodyMovement.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray), "Body Movement",
                TitledBorder.LEADING, TitledBorder.TOP, TextConstants.DEFAULT_FONT, Color.black));
        panelBottom.add(panelInputBodyMovement);
        
        comboExpressionBodyExpression = new JComboBox<>();
        comboExpressionBodyExpression.setModel(new DefaultComboBoxModel<>(bodyMovement));
        comboExpressionBodyExpression.setMaximumSize(new Dimension(128, 128));
        comboExpressionBodyExpression.addActionListener(event -> {
	        if (previousBodyMovement != null) {
	            emostatePacketBuilder.setExpression(previousBodyMovement, false);
	        }
	
	        checkboxEye.setSelected(emostatePacketBuilder.getExpressionBoolean((Expression) comboExpressionBodyExpression.getSelectedItem()));
	        previousExpressionEyes = (Expression) comboExpressionBodyExpression.getSelectedItem();
        });
        
        
        panelInputBodyMovement.add(comboExpressionBodyExpression, BorderLayout.WEST);

        panelInputBodyMovement.add(Box.createHorizontalStrut(8));
        
        spinnerBodyMovement = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 1d, 0.05d) );
        spinnerBodyMovement.setMaximumSize(new Dimension(128, 128));
        spinnerBodyMovement.setBorder(null);
        spinnerBodyMovement.setFont(TextConstants.LARGE_FONT);
        spinnerBodyMovement.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                handleComboExpressionChange(comboExpressionBodyExpression, spinnerBodyMovement);
            }
        });
        panelInputBodyMovement.add(spinnerBodyMovement, BorderLayout.EAST);

        panelBottom.add(Box.createVerticalStrut(16));
        
        
        // Upperface
        JPanel panelInputFaceUpper = new JPanel();
        panelInputFaceUpper.setOpaque(false);
        panelInputFaceUpper.setLayout(new BoxLayout(panelInputFaceUpper, BoxLayout.X_AXIS));
        panelInputFaceUpper.setMaximumSize(new Dimension(4096, 64));
        panelInputFaceUpper.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray), "Face - Upper",
                TitledBorder.LEADING, TitledBorder.TOP, TextConstants.DEFAULT_FONT, Color.black));
        panelBottom.add(panelInputFaceUpper);

        comboExpressionFaceUpper = new JComboBox<>();
        comboExpressionFaceUpper.setModel(new DefaultComboBoxModel<>(expressionFaceUpperValues));
        comboExpressionFaceUpper.setMaximumSize(new Dimension(128, 128));
        comboExpressionFaceUpper.addActionListener(event -> {
            // Reset previous value, if applicable
            if (previousExpressionFaceUpper != null) {
                emostatePacketBuilder.setExpression(previousExpressionFaceUpper, 0f);
            }
            spinnerUpperFace.setValue(emostatePacketBuilder.getExpressionFloating((Expression) comboExpressionFaceUpper.getSelectedItem()).doubleValue());
            previousExpressionFaceUpper = (Expression) comboExpressionFaceUpper.getSelectedItem();
        });
        panelInputFaceUpper.add(comboExpressionFaceUpper, BorderLayout.WEST);

        panelInputFaceUpper.add(Box.createHorizontalStrut(8));

        spinnerUpperFace = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 1d, 0.05d) );
        spinnerUpperFace.setMaximumSize(new Dimension(128, 128));
        spinnerUpperFace.setBorder(null);
        spinnerUpperFace.setFont(TextConstants.LARGE_FONT);
        spinnerUpperFace.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                handleComboExpressionChange(comboExpressionFaceUpper, spinnerUpperFace);
            }
        });
        panelInputFaceUpper.add(spinnerUpperFace, BorderLayout.EAST);

        panelBottom.add(Box.createVerticalStrut(16));

        // Lowerface
        JPanel panelInputFaceLower = new JPanel();
        panelInputFaceLower.setOpaque(false);
        panelInputFaceLower.setLayout(new BoxLayout(panelInputFaceLower, BoxLayout.X_AXIS));
        panelInputFaceLower.setMaximumSize(new Dimension(4096, 64));
        panelInputFaceLower.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray), "Face - Lower",
                TitledBorder.LEADING, TitledBorder.TOP, TextConstants.DEFAULT_FONT, Color.black));
        panelBottom.add(panelInputFaceLower);

        comboExpressionFaceLower = new JComboBox<>();
        comboExpressionFaceLower.setModel(new DefaultComboBoxModel<>(expressionFaceLowerValues));
        comboExpressionFaceLower.setMaximumSize(new Dimension(128, 128));
        comboExpressionFaceLower.addActionListener(event -> {
            // Reset previous value, if applicable
            if (previousExpressionFaceLower != null) {
                emostatePacketBuilder.setExpression(previousExpressionFaceLower, 0.0f);
            }

            spinnerLowerFace.setValue(emostatePacketBuilder.getExpression((Expression) comboExpressionFaceLower.getSelectedItem()).doubleValue());
            previousExpressionFaceLower = (Expression) comboExpressionFaceLower.getSelectedItem();
        });
        panelInputFaceLower.add(comboExpressionFaceLower, BorderLayout.WEST);

        panelInputFaceLower.add(Box.createHorizontalStrut(8));

        spinnerLowerFace = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 1d, 0.05d) );
        spinnerLowerFace.setMaximumSize(new Dimension(128, 128));
        spinnerLowerFace.setBorder(null);
        spinnerLowerFace.setFont(TextConstants.LARGE_FONT);
        spinnerLowerFace.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                handleComboExpressionChange(comboExpressionFaceLower, spinnerLowerFace);
            }
        });
        panelInputFaceLower.add(spinnerLowerFace, BorderLayout.EAST);

        panelBottom.add(Box.createVerticalStrut(16));

        // Emotion
        JPanel panelInputEmotion = new JPanel();
        panelInputEmotion.setOpaque(false);
        panelInputEmotion.setLayout(new BoxLayout(panelInputEmotion, BoxLayout.X_AXIS));
        panelInputEmotion.setMaximumSize(new Dimension(4096, 64));
        panelInputEmotion.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray), "Emotion",
                TitledBorder.LEADING, TitledBorder.TOP, TextConstants.DEFAULT_FONT, Color.black));
        panelBottom.add(panelInputEmotion);

        comboEmotion = new JComboBox<>();
        comboEmotion.setModel(new DefaultComboBoxModel<>(emotionValues));
        comboEmotion.setMaximumSize(new Dimension(128, 128));
        comboEmotion.addActionListener(event -> {
            spinnerEmotion.setValue(emostatePacketBuilder.getEmotion((Emotion) comboEmotion.getSelectedItem()).doubleValue());
        });
        panelInputEmotion.add(comboEmotion, BorderLayout.WEST);

        panelInputEmotion.add(Box.createHorizontalStrut(8));

        spinnerEmotion = new JSpinner( new SpinnerNumberModel(0.0d, 0.0d, 1d, 0.05d) );
        spinnerEmotion.setMaximumSize(new Dimension(128, 128));
        spinnerEmotion.setBorder(null);
        spinnerEmotion.setFont(TextConstants.LARGE_FONT);
        spinnerEmotion.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                handleComboEmotionChange(comboEmotion, spinnerEmotion);
            }
        });
        panelInputEmotion.add(spinnerEmotion, BorderLayout.EAST);
    }

    /**
     * Handles combo expression change to save to builder
     * @param combo Expression to save
     * @param spinner Value to save
     */
    private void handleComboExpressionChange(JComboBox combo, JSpinner spinner) {
        try {
            spinner.commitEdit();
            emostatePacketBuilder.setExpression((Expression) combo.getSelectedItem(), (float) ((double) spinner.getValue()));
        } catch(ParseException e) {
            Log.w("Failed to parse " + combo.getSelectedItem() + " value", ServerValuesView.class);
        }
    }

    /**
     * Handles combo emotion change to save to builder
     * @param combo Emotion to save
     * @param spinner Value to save
     */
    private void handleComboEmotionChange(JComboBox combo, JSpinner spinner) {
        try {
            spinner.commitEdit();
            emostatePacketBuilder.setEmotion((Emotion) combo.getSelectedItem(), (float) ((double) spinner.getValue()));
        } catch(ParseException e) {
            Log.w("Failed to parse " + combo.getSelectedItem() + " value", ServerValuesView.class);
        }
    }
}