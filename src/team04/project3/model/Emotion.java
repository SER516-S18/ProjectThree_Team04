package team04.project3.model;

/**
 * Created by there on 2018/03/29.
 */
public enum Emotion {
    INTEREST("Interest"),
    ENGAGEMENT("Engagement"),
    STRESS("Stress"),
    RELAXATION("Relaxation"),
    EXCITEMENT("Excitement"),
    FOCUS("Focus");

    public String name;

    private Emotion(String name) {
        this.name = name;
    }
}
