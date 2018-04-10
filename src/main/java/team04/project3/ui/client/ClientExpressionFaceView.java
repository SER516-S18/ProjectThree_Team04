package team04.project3.ui.client;

import team04.project3.constants.ColorConstants;
import team04.project3.listeners.ClientListener;
import team04.project3.model.EmostatePacket;
import team04.project3.model.EmostatePacketBuilder;
import team04.project3.model.Expression;
import team04.project3.model.client.ClientModel;

import javax.swing.*;
import java.awt.*;

/**
 * UI for showing the face pictorially
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientExpressionFaceView extends JPanel {
    private static final float VALUE_THRESHOLD = 0.5f;
    
    private JPanel parent;
    private Dimension size;
    private Dimension origin;
    int scale;
    private EmostatePacket emostate;

    public ClientExpressionFaceView(ClientExpressionView parent) {
        this.parent = parent;
        // this.emostate = ClientModel.get().getNewestPacket();
        this.emostate = EmostatePacketBuilder.getZeroedEmostatePacket().build();

        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void valuesChanged() {
                emostate = ClientModel.get().getNewestPacket();
                ClientExpressionFaceView.this.repaint();
            }

            @Override
            public void valuesReset() {}

            @Override
            public void valuesAdded() { }

            @Override
            public void started() { }

            @Override
            public void shutdown() { }
        });
    }

    public void updateFace(Graphics g) {
        int i = 7;
        while(Math.pow(2, i) < parent.getHeight() && Math.pow(2,i) < parent.getWidth()) {
            i++;
        }
        size = new Dimension((int) Math.pow(2,i - 1), (int) Math.pow(2,i - 1));
        origin = new Dimension((int) Math.pow(2,i - 2), (int) Math.pow(2, i - 2));
        scale = (int) Math.pow(2,i -1) / 128;

        this.removeAll();
        this.validate();
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setBackground(ColorConstants.BACKGROUND_PINK);

        if(emostate == null)
            return;

        // Face
        g.drawOval(origin.width - (scale * 64), origin.height - (scale * 64), 128 * scale, 128 * scale);
        drawEyes(g);
    }

    private void drawEyes(Graphics g) {
        if(emostate.getExpression(Expression.WINK_LEFT) > VALUE_THRESHOLD || emostate.getExpression(Expression.BLINK) > VALUE_THRESHOLD)
            drawCircle(g, 96, -64, 16, 2);
        else
            drawCircle(g, 96, -64, 16, 16);

        if(emostate.getExpression(Expression.WINK_RIGHT) > VALUE_THRESHOLD || emostate.getExpression(Expression.BLINK) > VALUE_THRESHOLD)
            drawCircle(g, -96, -64, 16, 1);
        else
            drawCircle(g, -96, -64, 16, 16);
    }

    @Override
    protected void paintComponent(Graphics graph) {
        super.paintComponent(graph);
        updateFace(graph);
    }

    public void drawCircle(Graphics g, int x, int y, int width, int height) {
        if(emostate.getExpression(Expression.LOOK_LEFT) > VALUE_THRESHOLD)
            x += 32;
        if(emostate.getExpression(Expression.LOOK_RIGHT) > VALUE_THRESHOLD)
            x -= 32;
        
        g.drawOval(x + origin.width - (scale * width / 2), y + origin.height - (scale * height / 2), width * scale, height * scale);
    }
}
