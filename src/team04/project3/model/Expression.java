package team04.project3.model;

/**
 * Created by there on 2018/03/29.
 */
public enum Expression {
    BLINK("Blink", false),
    WINK_RIGHT("Right wink", false),
    WINK_LEFT("Left wink", false),
    LOOK_RIGHT("Look right", false),
    LOOK_LEFT("Look left", false),
    BROW_FURROW("Furrow brow", true),
    BROW_RAISE("Raise brow", true),
    SMILE("Smile", true),
    CLENCH("Clench", true),
    SMIRK_LEFT("Left smirk", true),
    SMIRK_RIGHT("Right smirk", true),
    LAUGH("Laugh", true);

    public String name;
    public boolean floating;

    private Expression(String name, boolean floating) {
        this.name = name;
        this.floating = floating;
    }
}
