package team04.project3.ui;

import javax.swing.*;
import javax.swing.border.Border;
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
	private JLabel displayLength;
	private JLabel seconds;
	private JTextField length;
	private JPanel panelBuffer;
	
	public PerformanceMetricEmotionalStatesView() {

		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(15, 15, 15, 15));
		this.setOpaque(false);
		this.performanceMetricModel = new PerformanceMetricModel();

		panelBuffer = new JPanel(new GridBagLayout());
		panelBuffer.setBackground(ColorConstants.BACKGROUND_PINK);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));
        getEmotionPanel();


	}
        
	
	private void getEmotionPanel() {

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.ipadx = 50;
		gridBagConstraints.ipady = 50;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);


		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		interestButton = getEmotionButton(PerformanceMetricConstants.INTEREST, performanceMetricModel.getInterestColor());
		interestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				interestButton.setBackground(newColor);

			}
		});
		panelBuffer.add(interestButton,gridBagConstraints);


		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		engagementButton = getEmotionButton(PerformanceMetricConstants.ENGAGEMENT,performanceMetricModel.getEngagementColor());
		engagementButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				engagementButton.setBackground(newColor);

			}
		});
        panelBuffer.add(engagementButton,gridBagConstraints);

		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
        stressButton = getEmotionButton(PerformanceMetricConstants.STRESS,performanceMetricModel.getStressColor());
		stressButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				stressButton.setBackground(newColor);

			}
		});
        panelBuffer.add(stressButton,gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
        relaxationButton = getEmotionButton(PerformanceMetricConstants.RELAXATION,performanceMetricModel.getRelaxationColor());
		relaxationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				relaxationButton.setBackground(newColor);

			}
		});
        panelBuffer.add(relaxationButton,gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
        excitementButton = getEmotionButton(PerformanceMetricConstants.EXCITEMENT,performanceMetricModel.getExcitementColor());
		excitementButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				excitementButton.setBackground(newColor);

			}
		});
        panelBuffer.add(excitementButton,gridBagConstraints);

		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
        focusButton = getEmotionButton(PerformanceMetricConstants.FOCUS,performanceMetricModel.getFocusColor());
		focusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
				focusButton.setBackground(newColor);

			}
		});
        panelBuffer.add(focusButton,gridBagConstraints);

		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

		gridBagConstraints.insets = new Insets(10, 0, 0, 10);
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridx = 1;
		displayLength= new JLabel(PerformanceMetricConstants.DISPLAY_LENGTH,SwingConstants.CENTER);
		displayLength.setFont (displayLength.getFont ().deriveFont (16.0f));
		panelBuffer.add(displayLength,gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		length=new JTextField(5);
		displayLength.setLabelFor(length);
		length.setForeground(Color.WHITE);
		length.setFont (length.getFont ().deriveFont (16.0f));
		length.setBorder(border);
		length.setBackground(Color.LIGHT_GRAY);
		length.setOpaque(true);
		panelBuffer.add(length,gridBagConstraints);

		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 4;
		seconds = new JLabel(PerformanceMetricConstants.SECONDS,SwingConstants.CENTER);
		seconds.setForeground(Color.WHITE);
		seconds.setFont (seconds.getFont ().deriveFont (16.0f));
		seconds.setBorder(border);
		seconds.setBackground(Color.LIGHT_GRAY);
		seconds.setOpaque(true);
		panelBuffer.add(seconds,gridBagConstraints);

		add(panelBuffer,BorderLayout.NORTH);


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

