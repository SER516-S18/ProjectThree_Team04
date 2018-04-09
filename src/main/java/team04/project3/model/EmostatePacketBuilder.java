package team04.project3.model;

import java.util.HashMap;

/**
 * Builder for a EmostatePacket
 * @author  David Henderson (dchende2@asu.edu)
 */
public class EmostatePacketBuilder {
    private final HashMap<Expression, Float> expressions = new HashMap<>();
    private final HashMap<Emotion, Float> emotions = new HashMap<>();
    private Float tick = null;

    /**
     * Get an emostate packet builder with all expressions and emotions set to 0
     * @return EmostatePacketBuilder with all expressions and emotions set to 0
     */
    public static EmostatePacketBuilder getZeroedEmostatePacket() {
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        for(Expression expression : Expression.values()) {
            builder.setExpression(expression, 0.0f);
        }

        for(Emotion emotion : Emotion.values()) {
            builder.setEmotion(emotion, 0.0f);
        }

        return builder;
    }

    /**
     * Default constructor for builder
     */
    public EmostatePacketBuilder() {}

    /**
     * Sets an expression to a value
     * @param expression Expression to set
     * @param value Value to set
     * @return Builder object
     */
    public EmostatePacketBuilder setExpression(Expression expression, float value) {
        if(expression == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(expression.isFloating() && (value < 0.0f || value > 1.0f)) {
            throw new IllegalArgumentException("Expression \"" + expression.NAME + "\" value must be between 0.0 and 1.0, inclusive");
        } else if(expression.isBinary() && value != 0.0f && value != 1.0f) {
            throw new IllegalArgumentException("Expression \"" + expression.NAME + "\" value must be exactly 0.0f or 1.0f");
        }

        this.expressions.put(expression, value);
        return this;
    }

    /**
     * Sets an expression to a boolean value (converts from true -> 1.0, false -> 0.0)
     * @param expression Expression to set
     * @param value Value to set
     * @return Builder object
     */
    public EmostatePacketBuilder setExpression(Expression expression, boolean value) {
        if(expression == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(!expression.isBinary()) {
            throw new IllegalArgumentException("Expression \"" + expression.NAME + "\" cannot be set to a binary value");
        }

        this.expressions.put(expression, value ? 1.0f : 0.0f);
        return this;
    }

    /**
     * Sets an emotion to a value
     * @param emotion Emotion to set
     * @param value Value to set
     * @return Builder object
     */
    public EmostatePacketBuilder setEmotion(Emotion emotion, float value) {
        if(emotion == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(value < 0.0f || value > 1.0f) {
            throw new IllegalArgumentException("Emotion \"" + emotion.NAME + "\" value must be between 0.0 and 1.0, inclusive");
        }

        this.emotions.put(emotion, value);
        return this;
    }

    /**
     * Gets the value of an expression
     * @param expression Expression to get
     * @return Float from 0.0 -> 1.0 inclusive
     */
    public Float getExpression(Expression expression) {
        if(expression == null)
            throw new IllegalArgumentException("Expression must be non-null");
        else
            return this.expressions.getOrDefault(expression, null);
    }

    /**
     * Gets the floating point value of an expression
     * @param expression Expression to get
     * @return Float from 0.0 -> 1.0 inclusive
     */
    public Float getExpressionFloating(Expression expression) {
        return this.getExpression(expression);
    }

    /**
     * Gets a boolean value of an expression
     * @param expression Expression to get
     * @return True/false from expression
     */
    public Boolean getExpressionBoolean(Expression expression) {
        if(expression == null)
            throw new IllegalArgumentException("Expression must be non-null");
        else if(!expression.isBinary())
            throw new IllegalArgumentException("Expression must be binary");
        else {
            if(this.expressions.containsKey(expression))
                return this.expressions.get(expression) == 1.0f;
            else
                return null;
        }
    }

    /**
     * Gets an emotion's value
     * @param emotion Emotion to get
     * @return Float from 0.0 -> 1.0 inclusive
     */
    public Float getEmotion(Emotion emotion) {
        if(emotion == null)
            throw new IllegalArgumentException("Emotion must be non-null");
        else
            return this.emotions.getOrDefault(emotion, null);
    }

    /**
     * Sets the tick of the builder to send
     * @param tick Tick to set
     * @return Builder object
     */
    public EmostatePacketBuilder setTick(Float tick) {
        this.tick = tick;
        return this;
    }

    /**
     * Construction method of the builder object
     * @return Valid EmostatePacket object
     */
    public EmostatePacket build() {
        return (tick == null ? new EmostatePacket(expressions, emotions) : new EmostatePacket(expressions, emotions, tick));
    }
}