package team04.project3.ui.client;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.DimensionConstants;
import team04.project3.listeners.ClientListener;
import team04.project3.model.EmostatePacket;
import team04.project3.model.EmostatePacketBuilder;
import team04.project3.model.Expression;
import team04.project3.model.client.ClientModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

/**
 * Class to draw a face and represent the facial expressions.
 *
 * @SER516 ProjectThree_Team04
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ClientExpressionFaceView extends JPanel {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    private double SCALE_X_FACTOR, SCALE_Y_FACTOR;
    private int ORIGIN_X_COORD = 0;
    private int ORIGIN_Y_COORD = 0;
    private EmostatePacket emostatePacket;

    public ClientExpressionFaceView() {
        emostatePacket = ClientModel.get().getNewestPacket();

        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void valuesChanged() {
                emostatePacket = ClientModel.get().getNewestPacket();
            }

            @Override
            public void valuesReset() { }

            @Override
            public void valuesAdded() { }

            @Override
            public void started() { }

            @Override
            public void shutdown() { }
        });
    }


    /**
     * Draws the face with the required expressions
     * @param g Is a Graphics Object
     * @param height Specifies the height of the window
     * @param width Specifies the width of the window
     */

    public void drawFace(Graphics g, int height, int width) {
        if (emostatePacket == null)
            emostatePacket = EmostatePacketBuilder.getZeroedEmostatePacket().build();

        SCALE_X_FACTOR = width / 100.0;
        SCALE_Y_FACTOR = height / 100.0;
        
        String eyeDirection = "";
        boolean leftEyeBlink = false;
        boolean rightEyeBlink = false;

        float raiseBrowVal = emostatePacket.getExpression(Expression.BROW_RAISE);
        float furrowBrowVal = emostatePacket.getExpression(Expression.BROW_FURROW);

        float smile = emostatePacket.getExpression(Expression.SMILE);
        float clench = emostatePacket.getExpression(Expression.CLENCH);
        float smirkLeft = emostatePacket.getExpression(Expression.SMIRK_LEFT);
        float smirkRight = emostatePacket.getExpression(Expression.SMIRK_RIGHT);
        float laugh = emostatePacket.getExpression(Expression.LAUGH);


        if (emostatePacket.getExpression(Expression.BLINK) > 0) {
            leftEyeBlink = true;
            rightEyeBlink = true;
        } else if (emostatePacket.getExpression(Expression.WINK_LEFT) > 0) {
            leftEyeBlink = true;
        } else if (emostatePacket.getExpression(Expression.WINK_RIGHT) > 0) {
            rightEyeBlink = true;
        }

        if (emostatePacket.getExpression(Expression.LOOK_LEFT) > 0) {
            eyeDirection = Expression.LOOK_LEFT.NAME;
        } else if (emostatePacket.getExpression(Expression.LOOK_RIGHT) > 0) {
            eyeDirection = Expression.LOOK_RIGHT.NAME;
        }
        
        renderFace(g);
        renderEyes(g, eyeDirection, leftEyeBlink, rightEyeBlink);
        renderEyeBrows(g, raiseBrowVal, furrowBrowVal);
        renderMouth(g, smile, clench, smirkLeft, smirkRight, laugh);

        this.repaint();
    }

    /**
     * Makes outer structure of the face
     * @param g Is a Graphics Object
     */
    private void renderFace(Graphics g) {
        g.setColor(ColorConstants.BACKGROUND_PINK);
        createCircle(g, DimensionConstants.FACE_X_COORDINATE, DimensionConstants.FACE_Y_COORDINATE, DimensionConstants.HEAD_RADIUS);
        g.setColor(Color.BLACK);
        renderNose(g);
    }

    /**
     * Makes the eyes along with the various expressions needed
     */
    private void renderEyes(Graphics g, String eyeDirection, boolean isLeftBlink, boolean isRightBlink) {
        if (isLeftBlink && isRightBlink) {
            renderLine(g, DimensionConstants.LEFT_EYE_X_POSITION + DimensionConstants.EYE_RADIUS,  DimensionConstants.EYE_Y_POSITION,
                    DimensionConstants.LEFT_EYE_X_POSITION - DimensionConstants.EYE_RADIUS,  DimensionConstants.EYE_Y_POSITION);
            renderLine(g, DimensionConstants.RIGHT_EYE_X_POSITION - DimensionConstants.EYE_RADIUS, DimensionConstants.EYE_Y_POSITION,
                    DimensionConstants.RIGHT_EYE_X_POSITION + DimensionConstants.EYE_RADIUS, DimensionConstants.EYE_Y_POSITION);
        } else if (isLeftBlink) {
            g.setColor(Color.WHITE);
            createOval(g, DimensionConstants.LEFT_EYE_X_POSITION, DimensionConstants.EYE_Y_POSITION, DimensionConstants.EYE_RADIUS,
                    DimensionConstants.EYE_RADIUS);
            g.setColor(Color.BLACK);
            renderLine(g, DimensionConstants.RIGHT_EYE_X_POSITION - DimensionConstants.EYE_RADIUS,
                    DimensionConstants.EYE_Y_POSITION, DimensionConstants.RIGHT_EYE_X_POSITION + DimensionConstants.EYE_RADIUS,
                    DimensionConstants.EYE_Y_POSITION);
        } else if (isRightBlink) {
            renderLine(g, DimensionConstants.LEFT_EYE_X_POSITION + DimensionConstants.EYE_RADIUS,
                    DimensionConstants.EYE_Y_POSITION, DimensionConstants.LEFT_EYE_X_POSITION - DimensionConstants.EYE_RADIUS,
                    DimensionConstants.EYE_Y_POSITION);
            g.setColor(Color.WHITE);
            createOval(g, DimensionConstants.RIGHT_EYE_X_POSITION, DimensionConstants.EYE_Y_POSITION,
                    DimensionConstants.EYE_RADIUS, DimensionConstants.EYE_RADIUS);
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
            createOval(g, DimensionConstants.LEFT_EYE_X_POSITION, DimensionConstants.EYE_Y_POSITION,
                    DimensionConstants.EYE_RADIUS, DimensionConstants.EYE_RADIUS);
            createOval(g, DimensionConstants.RIGHT_EYE_X_POSITION, DimensionConstants.EYE_Y_POSITION,
                    DimensionConstants.EYE_RADIUS, DimensionConstants.EYE_RADIUS);
            g.setColor(Color.BLACK);
        }

        renderPupils(g, eyeDirection, isLeftBlink, isRightBlink);
    }

    /**
     * Makes the pupils along with the various expressions needed.
     * @param g Is a Graphics Object
     * @param eyeDirection
     */
    private void renderPupils(Graphics g, String eyeDirection, boolean isBlinkLeft, boolean isBlinkRight) {
        int correctEYE = 2;
        if (eyeDirection.equals(Expression.LOOK_RIGHT.NAME)) {
            fillOval(g, DimensionConstants.LEFT_EYE_X_POSITION + correctEYE - DimensionConstants.LOOK_LEFT_RIGHT_VAL,
                    DimensionConstants.EYE_Y_POSITION, DimensionConstants.PUPIL_SIZE, DimensionConstants.PUPIL_SIZE);
            fillOval(g, DimensionConstants.RIGHT_EYE_X_POSITION + correctEYE - DimensionConstants.LOOK_LEFT_RIGHT_VAL,
                    DimensionConstants.EYE_Y_POSITION, DimensionConstants.PUPIL_SIZE, DimensionConstants.PUPIL_SIZE);
        } else if (eyeDirection.equals(Expression.LOOK_LEFT.NAME)) {
            fillOval(g, DimensionConstants.LEFT_EYE_X_POSITION + correctEYE + DimensionConstants.LOOK_LEFT_RIGHT_VAL,
                    DimensionConstants.EYE_Y_POSITION, DimensionConstants.PUPIL_SIZE, DimensionConstants.PUPIL_SIZE);
            fillOval(g, DimensionConstants.RIGHT_EYE_X_POSITION + correctEYE + DimensionConstants.LOOK_LEFT_RIGHT_VAL,
                    DimensionConstants.EYE_Y_POSITION, DimensionConstants.PUPIL_SIZE, DimensionConstants.PUPIL_SIZE);
        } else {
            if (isBlinkLeft && !isBlinkRight) {
                fillOval(g, DimensionConstants.LEFT_EYE_X_POSITION + correctEYE, DimensionConstants.EYE_Y_POSITION,
                        DimensionConstants.PUPIL_SIZE, DimensionConstants.PUPIL_SIZE);
            } else if (isBlinkRight && !isBlinkLeft) {
                fillOval(g, DimensionConstants.RIGHT_EYE_X_POSITION + correctEYE, DimensionConstants.EYE_Y_POSITION,
                        DimensionConstants.PUPIL_SIZE, DimensionConstants.PUPIL_SIZE);

            } else if (!isBlinkLeft) {
                fillOval(g, DimensionConstants.LEFT_EYE_X_POSITION + correctEYE , DimensionConstants.EYE_Y_POSITION,
                        DimensionConstants.PUPIL_SIZE, DimensionConstants.PUPIL_SIZE);
                fillOval(g, DimensionConstants.RIGHT_EYE_X_POSITION + correctEYE, DimensionConstants.EYE_Y_POSITION,
                        DimensionConstants.PUPIL_SIZE, DimensionConstants.PUPIL_SIZE);
            }
        }
    }

    /**
     * Makes the eyebrows along with the various expressions needed.
     * @param g Is a Graphics Object
     */
    private void renderEyeBrows(Graphics g, double raiseBrowVal, double furrowBrowVal) {
        int y1, y2;
        if (raiseBrowVal > 0.0) {
            y1 = DimensionConstants.EYE_BROW_Y_POSITION + (int) (raiseBrowVal * SCALE_X_FACTOR);
            y2 = DimensionConstants.EYE_BROW_Y_POSITION - (int) (raiseBrowVal * SCALE_X_FACTOR);
        } else if (furrowBrowVal > 0.0) {
            y1 = DimensionConstants.EYE_BROW_Y_POSITION - (int) (furrowBrowVal * SCALE_X_FACTOR);
            y2 = DimensionConstants.EYE_BROW_Y_POSITION + (int) (furrowBrowVal * SCALE_X_FACTOR );
        } else {
            y1 = DimensionConstants.EYE_BROW_Y_POSITION;
            y2 = DimensionConstants.EYE_BROW_Y_POSITION;
        }
        renderLine(g, DimensionConstants.EYE_BROW_LEFT_X1, y1, DimensionConstants.EYE_BROW_LEFT_X2, y2);
        renderLine(g, DimensionConstants.EYE_BROW_RIGHT_X1, y2, DimensionConstants.EYE_BROW_RIGHT_X2, y1);
    }

    /**
     * Make nose.
     * @param g Is a Graphics Object
     */
    public void renderNose(Graphics g) {
        renderLine(g, DimensionConstants.NOSE_X_POSITION, DimensionConstants.NOSE_Y1_POSITION, DimensionConstants.NOSE_X_POSITION ,
                DimensionConstants.NOSE_Y1_POSITION + DimensionConstants.NOSE_WIDTH);
    }

    /**
     * Makes the mouth along with the expressions needed.
     * @param g Is a Graphics Object
     */
    private void renderMouth(Graphics g, double smile, double clench, double smirkLeft, double smirkRight, double laugh) {
        double x1 = 40;
        double y1 = DimensionConstants.MOUTH_Y_POSITION;
        double x2 = 60;
        double y2 = DimensionConstants.MOUTH_Y_POSITION;
        double x3 = ((x2 - x1) / 2) + x1;
        double y3 = DimensionConstants.MOUTH_Y_POSITION;
        double y4 = DimensionConstants.MOUTH_Y_POSITION;

        double[] data;

        if (smile > 0.0) {
            data = renderSmile(x1, x2, x3, y1, y2, y3, y4, smile);
        } else if (clench > 0.0) {
            data = renderClench(x1, x2, x3, y1, y2, y3, y4, clench);
        } else if (smirkLeft > 0.0) {
            data = renderSmirkLeft(x1, x2, x3, y1, y2, y3, y4, smirkLeft);
        } else if (smirkRight > 0.0) {
            data = renderSmirkRight(x1, x2, x3, y1, y2, y3, y4, smirkRight);
        } else if (laugh > 0.0) {
            data = renderLaugh(x1, x2, x3, y1, y2, y3, y4, laugh);
        } else {
            data = renderDefault(x1, x2, x3, y1, y2, y3, y4);
        }

        g.setColor(Color.RED);
        renderLips(g, data[0], data[3], data[1], data[4], data[2], data[5]);
        renderLips(g, data[0],  data[3], data[1], data[4], data[2], data[6]);
        g.setColor(Color.BLACK);
    }

    private double[] renderSmile(double x1, double x2,double x3, double y1, double y2, double y3, double y4, double smile) {
        x1 = x1 - (smile * 5);
        x2 = x2 + (smile * 5);
        y3 = y3 + ((smile / 2.0) * 10);
        y4 = y3;

        double[] data = new double[7];
        data[0] = x1;
        data[1] = x2;
        data[2] = x3;
        data[3] = y1;
        data[4] = y2;
        data[5] = y3;
        data[6] = y4;

        return data;
    }

    private double[] renderClench(double x1, double x2,double x3, double y1, double y2, double y3, double y4, double clench) {

        x1 = x1 - (clench * 5);
        x2 = x2 + (clench * 5);
        y3 = y3 - ((clench / 2.0) * 10);
        y4 = y4 + ((clench / 2.0) * 10);

        double[] data = new double[7];
        data[0] = x1;
        data[1] = x2;
        data[2] = x3;
        data[3] = y1;
        data[4] = y2;
        data[5] = y3;
        data[6] = y4;

        return data;
    }

    private double[] renderSmirkLeft(double x1, double x2,double x3, double y1, double y2, double y3, double y4, double smirkLeft) {
        x1 = x1 + (smirkLeft * 3);
        x2 = x2 + (smirkLeft * 3);
        y2 = y2 - (smirkLeft * 5);

        double[] data = new double[7];
        data[0] = x1;
        data[1] = x2;
        data[2] = x3;
        data[3] = y1;
        data[4] = y2;
        data[5] = y3;
        data[6] = y4;

        return data;
    }

    private double[] renderSmirkRight(double x1, double x2,double x3, double y1, double y2, double y3, double y4, double smirkRight) {
        x1 = x1 - (smirkRight * 3);
        x2 = x2 - (smirkRight * 3);
        y1 = y1 - (smirkRight * 5);

        double[] data = new double[7];
        data[0] = x1;
        data[1] = x2;
        data[2] = x3;
        data[3] = y1;
        data[4] = y2;
        data[5] = y3;
        data[6] = y4;

        return data;
    }

    private double[] renderLaugh(double x1, double x2,double x3, double y1, double y2, double y3, double y4, double laugh) {
        x1 = x1 - (laugh * 5);
        x2 = x2 + (laugh * 5);
        y4 = y4 + ((laugh / 2.0) * 10);

        double[] data = new double[7];
        data[0] = x1;
        data[1] = x2;
        data[2] = x3;
        data[3] = y1;
        data[4] = y2;
        data[5] = y3;
        data[6] = y4;

        return data;
    }

    private double[] renderDefault(double x1, double x2,double x3, double y1, double y2, double y3, double y4) {

        double[] data = new double[7];
        data[0] = x1;
        data[1] = x2;
        data[2] = x3;
        data[3] = y1;
        data[4] = y2;
        data[5] = y3;
        data[6] = y4;

        return data;
    }


    /**
     * Make lips
     */
    private void renderLips(Graphics g, double x1, double y1, double x2, double y2, double x3, double y3) {
        int i, new_x, new_y, last_x, last_y;
        double denom = (Math.pow(x1, 2) * (x2 - x3)) + (x1 * (Math.pow(x3, 2) - Math.pow(x2, 2)))
                + (Math.pow(x2, 2) * x3) + -(Math.pow(x3, 2) * x2);

        double a = ((y1 * (x2 - x3)) + (x1 * (y3 - y2)) + (y2 * x3) + -(y3 * x2)) / denom;

        double bb = ((Math.pow(x1, 2) * (y2 - y3)) + (y1 * (Math.pow(x3, 2) - Math.pow(x2, 2))) + (Math.pow(x2, 2) * y3)
                + -(Math.pow(x3, 2) * y2)) / denom;

        double c = ((Math.pow(x1, 2) * ((x2 * y3) - (x3 * y2)))
                + (x1 * ((Math.pow(x3, 2) * y2) - (Math.pow(x2, 2) * y3)))
                + (y1 * ((Math.pow(x2, 2) * x3) - (Math.pow(x3, 2) * x2)))) / denom;

        for (i = (int) x1, last_x = (int) x1, last_y = (int) y1; i <= x2; i++) {
            new_x = i;
            new_y = (int) ((a * Math.pow(i, 2)) + (bb * i) + c);
            renderLine(g, last_x, last_y, new_x, new_y);
            last_x = new_x;
            last_y = new_y;
        }
    }

    /**
     * Creates a Circle
     */
    private void createCircle(Graphics g, int x, int y, int radius) {
        g.fillOval(xScale(x - radius) + ORIGIN_X_COORD, yScale(y - radius) + ORIGIN_Y_COORD, xScale(radius * 3),
                yScale(radius * 2));
    }

    /**
     * Creates an Oval
     */
    private void createOval(Graphics g, int x, int y, int width, int height) {
        g.fillOval(xScale(x - width) + ORIGIN_X_COORD, yScale(y - height) + ORIGIN_Y_COORD, xScale(width * 3),
                yScale(height * 2));
    }

    /**
     * Used to fill the Oval
     */
    private void fillOval(Graphics g, int x, int y, int height, int width) {
        g.fillOval(xScale(x - width) + ORIGIN_X_COORD, yScale(y - height) + ORIGIN_Y_COORD, xScale(width * 2),
                yScale(height * 2));
    }

    /**
     * Used to create a line
     */
    private void renderLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(xScale(x1) + ORIGIN_X_COORD, yScale(y1) + ORIGIN_X_COORD, xScale(x2) + ORIGIN_X_COORD, yScale(y2) + ORIGIN_X_COORD);
    }

    private int xScale(int x) {
        return (int) (x * SCALE_X_FACTOR);
    }


    private int yScale(int y) {
        return (int) (y * SCALE_Y_FACTOR);
    }

    /**
     * Paints the desired face.
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawFace(g, getHeight(), getWidth());
    }

    /**
     * Gets the dimension of the outer panel.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

}