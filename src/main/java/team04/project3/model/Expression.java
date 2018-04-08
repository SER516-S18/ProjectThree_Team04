package team04.project3.model;

import com.google.gson.annotations.SerializedName;

public enum Expression {
    @SerializedName("BLINK") BLINK("Blink", false),
    @SerializedName("WINK_RIGHT") WINK_RIGHT("Right wink", false),
    @SerializedName("WINK_LEFT") WINK_LEFT("Left wink", false),
    @SerializedName("LOOK_RIGHT") LOOK_RIGHT("Look right", false),
    @SerializedName("LOOK_LEFT") LOOK_LEFT("Look left", false),
    @SerializedName("BROW_FURROW") BROW_FURROW("Furrow brow", true),
    @SerializedName("BROW_RAISE") BROW_RAISE("Raise brow", true),
    @SerializedName("SMILE") SMILE("Smile", true),
    @SerializedName("CLENCH") CLENCH("Clench", true),
    @SerializedName("SMIRK_LEFT") SMIRK_LEFT("Left smirk", true),
    @SerializedName("SMIRK_RIGHT") SMIRK_RIGHT("Right smirk", true),
    @SerializedName("LAUGH") LAUGH("Laugh", true);

    Expression(String name, boolean floating) {
        this.NAME = name;
        this.floating = floating;
    }

    public String NAME;
    private boolean floating;

    public boolean isFloating() {
        return floating;
    }

    public boolean isBinary() {
        return !floating;
    }

    @Override
    public String toString() {
        return this.NAME;
    }
}
