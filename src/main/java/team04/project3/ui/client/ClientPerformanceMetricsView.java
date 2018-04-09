package team04.project3.ui.client;

import javax.swing.*;
import java.awt.*;

public class ClientPerformanceMetricsView extends JPanel{
    public ClientPerformanceMetricsView() {
        this.setLayout(new BorderLayout());

        PerformanceMetricGraphView graphView;
        PerformanceMetricEmotionalStatesView emotionalStateView;

        graphView = new PerformanceMetricGraphView ();
        this.add(graphView, BorderLayout.LINE_START);

        emotionalStateView = new PerformanceMetricEmotionalStatesView();
        this.add(emotionalStateView, BorderLayout.LINE_END);
    }
}
