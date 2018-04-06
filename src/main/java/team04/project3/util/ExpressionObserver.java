package team04.project3.util;

public class ExpressionObserver implements Observer {

    private int observerID;
    private static int observerIDTracker = 0;

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

    public ExpressionObserver(Subject featureGrabber) {

        this.observerID = ++observerIDTracker;
        featureGrabber.register(this);
    }

    @Override
    public void updateLowerFace(int smile, int clench, int leftSmirk, int rightSmirk, int laugh) {

        this.smile = smile;
        this.clench = clench;
        this.leftSmirk = leftSmirk;
        this.rightSmirk = rightSmirk;
        this.laugh = laugh;
    }

    @Override
    public void updateUpperFace(int raiseBrow, int furrowBrow) {
        this.raiseBrow = raiseBrow;
        this.furrowBrow = furrowBrow;
    }

    @Override
    public void updateEyes(int blink, int leftWink, int rightWink, double lookLeft, double lookRight) {

        this.blink = blink;
        this.leftWink = leftWink;
        this.rightWink = rightWink;
        this.lookLeft = lookLeft;
        this.lookRight = lookRight;
    }

    @Override
    public void updateMetrics(double interest, double engage, double stress, double relax, double excite, double focus) {

        this.interest = interest;
        this.engage = engage;
        this.stress = stress;
        this.relax = relax;
        this.excite = excite;
        this.focus = focus;
        printValue();
    }

    public void printValue() {

        System.out.println("ObserverID: " + observerID +

                "\nClench Value: " + clench +
                "\nSmile Value: " + smile +
                "\nLeft Smirk Value: " + leftSmirk +
                "\nRight Smirk Value: " + rightSmirk +
                "\nLaugh Value: " + laugh +
                "\nBlink Value: " + blink +
                "\nLeft Wink Value: " + leftWink +
                "\nRight Wink Value: " + rightWink +
                "\nLook Left Value: " + lookLeft +
                "\nLook Right Value: " + lookRight +
                "\nInterest Value: " + interest +
                "\nEngage Value: " + engage +
                "\nStress Value: " + stress +
                "\nRelax Value: " + relax +
                "\nExcite Value: " + excite +
                "\nFocus Value: " + focus +
                "\nRaised brow Value: " + raiseBrow +
                "\nFurrow brow Value: " + furrowBrow


        );
        System.out.println("\n\n");
    }
}
