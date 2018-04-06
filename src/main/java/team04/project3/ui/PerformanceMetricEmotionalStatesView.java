package team04.project3.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import team04.project3.constants.ColorConstants;
import team04.project3.constants.PerformanceMetricConstants;
import team04.project3.model.client.PerformanceMetricModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;


public class PerformanceMetricEmotionalStatesView extends JPanel {
	
	
	private PerformanceMetricModel performanceMetricModel;
	
	private JButton interestButton;
	private JButton engagementButton;
	private JButton stressButton;
	private JButton relaxationButton;
	private JButton excitementButton;
	private JButton focusButton;
	private JPanel panelBuffer;
	
	public PerformanceMetricEmotionalStatesView() {
		
		this.performanceMetricModel = performanceMetricModel;
		
        
        JPanel panelBuffer = new JPanel(new GridBagLayout());
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));
        Component panel = getEmotionPanel();
	}
        
	
	private Component getEmotionPanel() {
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.ipadx = 50;
		gridBagConstraints.ipady = 50;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		interestButton = getEmotionButton(PerformanceMetricConstants.INTEREST, performanceMetricModel.getInterestColor());
		panelBuffer.add(interestButton, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
        engagementButton = getEmotionButton(PerformanceMetricConstants.ENGAGEMENT,performanceMetricModel.getEngagementColor());
        panelBuffer.add(engagementButton, gridBagConstraints);
        
        gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
        stressButton = getEmotionButton(PerformanceMetricConstants.STRESS,performanceMetricModel.getStressColor());
        panelBuffer.add(stressButton, gridBagConstraints);
        
        gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
        relaxationButton = getEmotionButton(PerformanceMetricConstants.RELAXATION,performanceMetricModel.getRelaxationColor());
        panelBuffer.add(relaxationButton, gridBagConstraints);
        
        gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
        excitementButton = getEmotionButton(PerformanceMetricConstants.EXCITEMENT,performanceMetricModel.getExcitementColor());
        panelBuffer.add(excitementButton, gridBagConstraints);
        
        gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
        focusButton = getEmotionButton(PerformanceMetricConstants.FOCUS,performanceMetricModel.getFocusColor());
        panelBuffer.add(focusButton, gridBagConstraints);
        
        
        this.add(panelBuffer, BorderLayout.CENTER);
        
        return panelBuffer;
        
	
	}
	
	private JButton getEmotionButton(String emotion, Color color)
	{
		JButton emotionButton = new JButton(emotion);
		emotionButton.setBackground(color);
		emotionButton.setOpaque(true);
		emotionButton.setHorizontalTextPosition(SwingConstants.CENTER);
		emotionButton.setBorder(new LineBorder(Color.black));
		emotionButton.setHorizontalAlignment(SwingConstants.CENTER);
		emotionButton.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));
		return emotionButton;
	}
	
	public void addEmotionButtonsListener(ActionListener actionListener) {
		interestButton.addActionListener(actionListener);
		engagementButton.addActionListener(actionListener);
		stressButton.addActionListener(actionListener);
		relaxationButton.addActionListener(actionListener);
		excitementButton.addActionListener(actionListener);
		focusButton.addActionListener(actionListener);
	}
	
	
	public void updatePerformanceMetricView(PerformanceMetricModel performanceMetricModel) {
		this.performanceMetricModel = performanceMetricModel;
		interestButton.setBackground(this.performanceMetricModel.getInterestColor());
		engagementButton.setBackground(this.performanceMetricModel.getEngagementColor());
		stressButton.setBackground(this.performanceMetricModel.getStressColor());
		relaxationButton.setBackground(this.performanceMetricModel.getRelaxationColor());
		excitementButton.setBackground(this.performanceMetricModel.getExcitementColor());
		focusButton.setBackground(this.performanceMetricModel.getFocusColor());
		
	}
	

}

