package team04.project3.model;

import com.google.gson.annotations.SerializedName;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Emotion {
    @SerializedName("INTEREST") INTEREST("Interest"),
    @SerializedName("ENGAGEMENT") ENGAGEMENT("Engagement"),
    @SerializedName("STRESS") STRESS("Stress"),
    @SerializedName("RELAXATION") RELAXATION("Relaxation"),
    @SerializedName("EXCITEMENT") EXCITEMENT("Excitement"),
    @SerializedName("FOCUS") FOCUS("Focus");

    public static Map<String, Emotion> emotionMap = new HashMap<>();

    static {

        EnumSet.allOf(Emotion.class)
                .forEach(ex -> emotionMap.put(ex.name, ex));
    }

    public String name;

    private Emotion(String name) {
        this.name = name;
    }
}
