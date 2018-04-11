package team04.project3.ui.client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import team04.project3.model.Expression;
import team04.project3.model.ValueTuple;
import team04.project3.model.client.ClientModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * UI for displaying a single expression's name, most recent value, and graph
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientExpressionGraphView extends ChartPanel {
    private final Expression expression;
    private XYSeriesCollection dataset;
    private JFreeChart chart;

    /**
     * Constructor for the ClientExpressionGraphView, showing the name label, graph, and most recent value for an expression
     * @param expression Expression to display
     */
    public ClientExpressionGraphView(Expression expression) {
        super(null, false);
        this.expression = expression;
        this.buildChart();
    }

    /**
     * Creates and adds the graph to the panel
     */
    private void buildChart() {
        // Create the chart
        dataset = new XYSeriesCollection();
        this.updateSeries();
        chart = ChartFactory.createXYLineChart("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlineVisible(false);
        chart.getPlot().setBackgroundAlpha(0);
        chart.getXYPlot().getRenderer().setSeriesStroke(0,
                new BasicStroke(4.0f)
        );

        ((XYPlot) chart.getPlot()).getDomainAxis().setVisible(false);
        ((XYPlot) chart.getPlot()).setDomainGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).setDomainMinorGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).getRangeAxis().setVisible(false);
        ((XYPlot) chart.getPlot()).setRangeGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).setRangeMinorGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).getRangeAxis().setRange(0.0d, 1.0d);

        this.setChart(chart);
        this.setDomainZoomable(false);
        this.setRangeZoomable(false);
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }

    /**
     * Called when the number of channels changes, to update the series (graph's lines)
     */
    private void updateSeries() {
        // Each series is a line, displaying a channel
        List<ValueTuple> packets = ClientModel.get().getExpressionPackets(expression);
        XYSeries series = new XYSeries(expression.NAME);
        for(ValueTuple tuple : packets) {
            series.add(tuple.TICK, tuple.VALUE);
        }
        dataset.addSeries(series);
    }

    /**
     * Called when new values need to be added to the graph
     */
    public void updateGraphs() {
        List<ValueTuple> packets = ClientModel.get().getExpressionPackets(expression);
        if(packets.size() > 0) {
            ValueTuple tuple = packets.get(packets.size() - 1);
            XYSeries series = dataset.getSeries(0);
            if (tuple != null) {
                // Put a point immediately after the previous one for a vertical line for binary values
                if(expression.isBinary() && packets.size() > 1)
                    series.add(packets.get(packets.size() - 2).TICK + 0.0001, tuple.VALUE);

                series.add(tuple.TICK, tuple.VALUE);
            }
        } else {
            dataset.getSeries(0).clear();
        }
    }

    /**
     * Returns which expression the panel represents
     * @return Expression the panel represents
     */
    public Expression getExpression() {
        return expression;
    }
}
