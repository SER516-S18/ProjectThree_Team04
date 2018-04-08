package team04.project3.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import team04.project3.constants.ColorConstants;
import team04.project3.constants.PerformanceMetricConstants;
import team04.project3.constants.TextConstants;
import team04.project3.model.client.PerformanceMetricModel;
import java.awt.*;
import java.awt.event.ActionEvent;
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

		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(8, 8, 8, 8));
		this.setOpaque(false);
		this.performanceMetricModel = new PerformanceMetricModel();

		panelBuffer = new JPanel(new GridLayout(6, 2, 8, 8));
        panelBuffer.setBackground(ColorConstants.BACKGROUND_PINK);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));
        getEmotionPanel();

        add(panelBuffer);
	}
        
	
	private void getEmotionPanel() {

		interestButton = getEmotionButton(PerformanceMetricConstants.INTEREST, performanceMetricModel.getInterestColor());
		interestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				interestButton.setBackground(newColor);

			}
		});
		panelBuffer.add(interestButton);

        engagementButton = getEmotionButton(PerformanceMetricConstants.ENGAGEMENT,performanceMetricModel.getEngagementColor());
		engagementButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				engagementButton.setBackground(newColor);

			}
		});
        panelBuffer.add(engagementButton);

        stressButton = getEmotionButton(PerformanceMetricConstants.STRESS,performanceMetricModel.getStressColor());
		stressButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				stressButton.setBackground(newColor);

			}
		});
        panelBuffer.add(stressButton);

        relaxationButton = getEmotionButton(PerformanceMetricConstants.RELAXATION,performanceMetricModel.getRelaxationColor());
		relaxationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				relaxationButton.setBackground(newColor);

			}
		});
        panelBuffer.add(relaxationButton);

        excitementButton = getEmotionButton(PerformanceMetricConstants.EXCITEMENT,performanceMetricModel.getExcitementColor());
		excitementButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				excitementButton.setBackground(newColor);

			}
		});
        panelBuffer.add(excitementButton);

        focusButton = getEmotionButton(PerformanceMetricConstants.FOCUS,performanceMetricModel.getFocusColor());
		focusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				focusButton.setBackground(newColor);

			}
		});
        panelBuffer.add(focusButton);

		JPanel displayPanel = new JPanel(new GridBagLayout());
		displayPanel.setBackground(ColorConstants.BACKGROUND_PINK);
		displayPanel.setBorder(new LineBorder(Color.black));
		displayPanel.setBackground(new Color(220,220,220));
		JLabel DisplayLength = new JLabel("Display Length  ");
		DisplayLength.setFont(TextConstants.DEFAULT_FONT);
		displayPanel.add(DisplayLength);

		panelBuffer.add(displayPanel);
		panelBuffer.add(new JTextField(10));

		panelBuffer.add(new Panel());
		panelBuffer.add(new Panel());
		panelBuffer.add(new Panel());
		panelBuffer.add(new Panel());
	}
	
	private JButton getEmotionButton(String emotion, Color color)
	{
		JButton emotionButton = new JButton(emotion);
		emotionButton.setBackground(color);
		emotionButton.setOpaque(true);
		emotionButton.setHorizontalTextPosition(SwingConstants.CENTER);
		emotionButton.setBorder(new LineBorder(Color.black));
		emotionButton.setHorizontalAlignment(SwingConstants.CENTER);
		emotionButton.setFont(TextConstants.DEFAULT_FONT);
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

