package team04.project3.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import team04.project3.constants.ColorConstants;
import java.awt.*;

public class PerformanceMetricView extends JPanel {
	
	private PerformanceMetricGraphView graphView;
	private PerformanceMetricEmotionalStatesView emotionalStateView;
	
	public PerformanceMetricView(){
		
		JPanel panelBuffer = new JPanel(new GridLayout(1, 2, 8, 8));
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));
        
        graphView = new PerformanceMetricGraphView ();
        panelBuffer.add(graphView, BorderLayout.LINE_START);
        
        emotionalStateView = new PerformanceMetricEmotionalStatesView();
        panelBuffer.add(emotionalStateView, BorderLayout.LINE_END);
		
	}
	
}

