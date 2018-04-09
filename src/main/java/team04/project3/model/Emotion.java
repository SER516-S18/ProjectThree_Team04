package team04.project3.model;

import com.google.gson.annotations.SerializedName;

/**
 * Enumeration representing all the emotions
 * @author  David Henderson (dchende2@asu.edu)
 */
public enum Emotion {
    @SerializedName("INTEREST") INTEREST("Interest"),
    @SerializedName("ENGAGEMENT") ENGAGEMENT("Engagement"),
    @SerializedName("STRESS") STRESS("Stress"),
    @SerializedName("RELAXATION") RELAXATION("Relaxation"),
    @SerializedName("EXCITEMENT") EXCITEMENT("Excitement"),
    @SerializedName("FOCUS") FOCUS("Focus");

    @SerializedName("NAME")
    public final String NAME;

    /**
     * Enumeration constructor for Emotion
     * @param name Human-readable name to set
     */
    private Emotion(String name) {
        this.NAME = name;
    }

    @Override
    public String toString() {
        return this.NAME;
    }
}
