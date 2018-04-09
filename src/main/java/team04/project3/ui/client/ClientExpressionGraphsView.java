package team04.project3.ui.client;

import team04.project3.listeners.ClientListener;
import team04.project3.model.EmostatePacket;
import team04.project3.model.Expression;
import team04.project3.model.client.ClientModel;

import javax.swing.*;
import java.util.ArrayList;

public class ClientExpressionGraphsView extends JPanel {
    private ArrayList<ClientExpressionGraphView> graphs;

    public ClientExpressionGraphsView() {
        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void valuesChanged() {
                updateGraphs();
            }

            @Override
            public void valuesReset() {
                updateGraphValueLabels();
            }

            @Override
            public void valuesAdded() {
                updateGraphValueLabels();
            }

            @Override
            public void started() { }

            @Override
            public void shutdown() { }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.graphs = new ArrayList<>();

        // Add graphs
        for(Expression expression : Expression.values()) {
            ClientExpressionGraphView view = new ClientExpressionGraphView(expression);
            this.add(view);
            this.graphs.add(view);
        }

        // Populate with values
        updateGraphValueLabels();
    }

    private void updateGraphValueLabels() {
        EmostatePacket packet = ClientModel.get().getNewestPacket();
        for(ClientExpressionGraphView graphView : graphs) {
            graphView.setValue(packet == null ? 0.0f : packet.getExpression(graphView.getExpression()));
        }
    }

    private void updateGraphs() {
        for(ClientExpressionGraphView graphView : graphs) {
            graphView.updateGraphs();
        }
    }
}
