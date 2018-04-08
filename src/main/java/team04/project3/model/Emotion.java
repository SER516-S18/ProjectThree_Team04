package team04.project3.model;

import com.google.gson.annotations.SerializedName;

public enum Emotion {
    @SerializedName("INTEREST") INTEREST("Interest"),
    @SerializedName("ENGAGEMENT") ENGAGEMENT("Engagement"),
    @SerializedName("STRESS") STRESS("Stress"),
    @SerializedName("RELAXATION") RELAXATION("Relaxation"),
    @SerializedName("EXCITEMENT") EXCITEMENT("Excitement"),
    @SerializedName("FOCUS") FOCUS("Focus");

    @SerializedName("NAME")
    public String NAME;

    private Emotion(String name) {
        this.NAME = name;
    }

    @Override
    public String toString() {
        return this.NAME;
    }
}
