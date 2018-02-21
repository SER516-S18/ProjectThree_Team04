package edu.asu.ser516.projecttwo.team04.ui;

import java.awt.*;
import javax.swing.*;

public class ServerStatusIcon extends JPanel {

    int xCenter = 0;
    int yCenter = 0;
    int radius = 0;
    Color color = Color.green;

    public ServerStatusIcon() {

    }
    public ServerStatusIcon(int x, int y, int rad, Color clr) {
        xCenter = x;
        yCenter = y;
        radius = rad;
        color = clr;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCircle(g, xCenter, yCenter, radius, color);
    }

    public void drawCircle(Graphics cg, int xCenter, int yCenter, int r, Color color) {
        cg.drawOval(xCenter - r, yCenter-r, 2*r, 2*r);
        cg.setColor(color);
    }

    public int getxCenter() {
        return xCenter;
    }

    public void setxCenter(int xCenter) {
        this.xCenter = xCenter;
    }

    public int getyCenter() {
        return yCenter;
    }

    public void setyCenter(int yCenter) {
        this.yCenter = yCenter;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}