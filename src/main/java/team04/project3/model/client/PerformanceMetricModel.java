package team04.project3.model.client;


import java.awt.*;

/**
 * The color of each emotion is represented.
 *
 * @author Shirisha Kakarla
 *
 */
public class PerformanceMetricModel {
  private Color interestColor;
  private Color engagementColor;
  private Color stressColor;
  private Color relaxationColor;
  private Color excitementColor;
  private Color focusColor;

  private int displayLength;

  public PerformanceMetricModel() {
    interestColor = Color.RED;
    engagementColor = Color.GREEN;
    stressColor = Color.BLUE;
    relaxationColor = Color.YELLOW;
    excitementColor = Color.ORANGE;
    focusColor = Color.MAGENTA;
    displayLength = 30;
  }

 
  public void setInterestColor(Color interestColor) {
    this.interestColor = interestColor;
  }

  public Color getInterestColor() {
    return interestColor;
  }

  public void setEngagementColor(Color engagementColor) {
    this.engagementColor = engagementColor;
  }

  public Color getEngagementColor() {
    return engagementColor;
  }

  public void setStressColor(Color stressColor) {
    this.stressColor = stressColor;
  }

  public Color getStressColor() {
    return stressColor;
  }

  
  public void setRelaxationColor(Color relaxationColor) {
    this.relaxationColor = relaxationColor;
  }

  
  public Color getRelaxationColor() {
    return relaxationColor;
  }

  public void setExcitementColor(Color excitementColor) {
    this.excitementColor = excitementColor;
  }

  
  public Color getExcitementColor() {
    return excitementColor;
  }

  public void setFocusColor(Color focusColor) {
    this.focusColor = focusColor;
  }

  public Color getFocusColor() {
    return focusColor;
  }

  public void setDisplayLength(int displayLength) {
    this.displayLength = displayLength;
  }

  public int getDisplayLength() {
    return displayLength;
  }
}