package team04.project3.util;

import java.util.ArrayList;

public class Expression implements Subject {

    private ArrayList<Observer> observers;

    private int smile;
    private int clench;
    private int leftSmirk;
    private int rightSmirk;
    private int laugh;
    private int blink;
    private int leftWink;
    private int rightWink;
    private double lookLeft;
    private double lookRight;
    private double interest;
    private double engage;
    private double stress;
    private double relax;
    private double excite;
    private double focus;
    private int raiseBrow;
    private int furrowBrow;

    public Expression() {
        observers = new ArrayList<>();
    }

    @Override
    public void register(Observer newObserver) {
        observers.add(newObserver);


    }
    @Override
    public void unregister(Observer deleteObserver) {

        int observerIndex = observers.indexOf(deleteObserver);
        System.out.println("deleted");
        observers.remove(observerIndex);
    }

    @Override
    public void notifyObserver() {

        for (Observer observer : observers) {
            observer.updateUpperFace(this.raiseBrow, this.furrowBrow);
            observer.updateLowerFace(this.smile, this.clench, this.leftSmirk,this.rightSmirk, this.laugh);
            observer.updateEyes(this.blink, this.leftWink, this.rightWink, this.lookLeft, this.lookRight);
            observer.updateMetrics(this.interest, this.engage, this.stress, this.relax, this.excite, this.focus);
        }
    }

    private void setRaiseBrow(int newBrowValue) {
        this.raiseBrow = newBrowValue;
        notifyObserver();
    }

    public void setFurrowBrow(int newBrowValue) {

        this.furrowBrow = newBrowValue;
        notifyObserver();
    }

    public void setInterest(double newInterestValue) {

        this.interest = newInterestValue;
        notifyObserver();
    }

    public void setEngage(double newEngageValue) {

        this.engage = newEngageValue;
        notifyObserver();
    }

    public void setStress(double newStressValue) {

        this.stress = newStressValue;
        notifyObserver();
    }

    public void setRelax(double newRelaxValue) {
        this.relax = newRelaxValue;
        notifyObserver();
    }

    public void setExcite(double newExciteValue) {
        this.excite = newExciteValue;
        notifyObserver();
    }

    public void setFocus(double newFocusValue) {
        this.focus = newFocusValue;
        notifyObserver();
    }

    public void setBlink(int newBlinkValue) {
        this.blink = newBlinkValue;
        notifyObserver();
    }

    public void setLeftWink(int newLeftWinkValue) {
        this.leftWink = newLeftWinkValue;
        notifyObserver();
    }

    public void setRightWink(int newRightWinkValue) {

        this.rightWink = newRightWinkValue;
        notifyObserver();
    }


    public void setLookLeft(double newLookLeftValue) {
        this.lookLeft = newLookLeftValue;
        notifyObserver();
    }

    public void setLookRight(double newLookRightValue) {
        this.lookRight = newLookRightValue;
        notifyObserver();
    }

    public void setSmile(int newSmileValue) {

        this.smile = newSmileValue;
        notifyObserver();
    }

    public void setClench(int newClenchValue) {

        this.clench = newClenchValue;
        notifyObserver();
    }

    public void setLeftSmirk(int newLeftSmirkValue) {

        this.leftSmirk = newLeftSmirkValue;
        notifyObserver();
    }

    public void setRightSmirk(int newRightSmirkValue) {

        this.rightSmirk = newRightSmirkValue;
        notifyObserver();
    }

    public void setLaugh(int newLaughValue) {

        this.laugh = newLaughValue;
        notifyObserver();
    }
}
