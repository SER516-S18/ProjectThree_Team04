package team04.project3.model;

import com.google.gson.annotations.SerializedName;

/**
 * Enumeration representing all the valid expressions
 * @author  David Henderson (dchende2@asu.edu)
 */
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

    /**
     * Enumeration constructor for expression
     * @param name Human-readable name for expression
     * @param floating Whether the expression is a floating-value
     */
    Expression(String name, boolean floating) {
        this.NAME = name;
        this.IS_FLOATING = floating;
    }

    @SerializedName("NAME")
    public final String NAME;
    @SerializedName("ISFLOATING")
    private final boolean IS_FLOATING;

    /**
     * Gets if the expression is a floating value
     * @return Boolean if expression is floating
     */
    public boolean isFloating() {
        return IS_FLOATING;
    }

    /**
     * Gets if the expression is a binary value
     * @return Boolean if expression is floating
     */
    public boolean isBinary() {
        return !IS_FLOATING;
    }

    @Override
    public String toString() {
        return this.NAME;
    }
}
