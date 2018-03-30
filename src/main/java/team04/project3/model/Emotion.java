package team04.project3.model;

import com.google.gson.annotations.SerializedName;

public enum Emotion {
    @SerializedName("INTEREST") INTEREST("Interest"),
    @SerializedName("ENGAGEMENT") ENGAGEMENT("Engagement"),
    @SerializedName("STRESS") STRESS("Stress"),
    @SerializedName("RELAXATION") RELAXATION("Relaxation"),
    @SerializedName("EXCITEMENT") EXCITEMENT("Excitement"),
    @SerializedName("FOCUS") FOCUS("Focus");

    public String name;

    private Emotion(String name) {
        this.name = name;
    }
}
