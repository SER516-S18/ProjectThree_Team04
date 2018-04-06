package team04.project3.util;

public interface Observer {

    void updateLowerFace(int smile, int clench, int leftSmirk, int rightSmirk, int laugh);
    void updateUpperFace(int raiseBrow, int furrowBrow);
    void updateEyes(int blink, int leftWink, int rightWink, double lookLeft, double lookRight);
    void updateMetrics(double interest, double engage, double stress, double relax, double excite, double focus);
}
