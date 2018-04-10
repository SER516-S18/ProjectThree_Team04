package team04.project3.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A packet for transmission containing all values (expression/emotion key-value pairs)
 * @author  David Henderson (dchende2@asu.edu)
 */
public class EmostatePacket {
    private final HashMap<Expression, Float> expressions;
    private final HashMap<Emotion, Float> emotions;
    private Float tick = null;

    /**
     * Creates an EmostatePacket including a tick
     * @param expressions Expression key-values
     * @param emotions Emotion key-values
     * @param tick Tick
     */
    public EmostatePacket(HashMap<Expression, Float> expressions, HashMap<Emotion, Float> emotions, float tick) {
        this(expressions, emotions);
        this.tick = tick;
    }

    /**
     * Creates an EmostatePacket
     * @param expressions Expression key-values
     * @param emotions Emotion key-values
     */
    public EmostatePacket(HashMap<Expression, Float> expressions, HashMap<Emotion, Float> emotions) {
        // Data validation, must have values for every expression and emotion
        if(expressions == null || emotions == null)
            throw new IllegalArgumentException("Arguments must be non-null");

        for(Expression expression : Expression.values()) {
            if(!expressions.containsKey(expression))
                throw new IllegalArgumentException("Expressions must contain all possible expression enumerations");
            else {
                Float value = expressions.get(expression);
                if(value == null) {
                    throw new IllegalArgumentException("Expression \"" + expression.NAME + "\" value must not be null");
                } else if(expression.isFloating() && (value < 0.0f || value > 1.0f)) {
                    throw new IllegalArgumentException("Expression \"" + expression.NAME + "\" value must be between 0.0 and 1.0, inclusive");
                } else if(expression.isBinary() && value != 0.0f && value != 1.0f) {
                    throw new IllegalArgumentException("Expression \"" + expression.NAME + "\" value must be exactly 0.0f or 1.0f");
                }
            }
        }

        for(Emotion emotion : Emotion.values()) {
            if(!emotions.containsKey(emotion)) {
                throw new IllegalArgumentException("Emotions must contain all possible expression enumerations");
            } else {
                Float value = emotions.get(emotion);
                if(value == null) {
                    throw new IllegalArgumentException("Emotion \"" + emotion.NAME + "\" value must not be null");
                } else if(value < 0.0f || value > 1.0f) {
                    throw new IllegalArgumentException("Emotion \"" + emotion.NAME + "\" value must be between 0.0 and 1.0, inclusive");
                }
            }
        }

        this.expressions = expressions;
        this.emotions = emotions;
    }

    /**
     * Gets an expression's value
     * @param expression Expression to get
     * @return Expression's value
     */
    public Float getExpression(Expression expression) {
        if(expression == null)
            throw new IllegalArgumentException("Expression must be non-null");

        return expressions.get(expression);
    }

    /**
     * Gets an unmodifiable map of all expression key-values
     * @return An unmodifiable map of all expression key-values
     */
    public Map<Expression, Float> getExpressions() {
        return Collections.unmodifiableMap(expressions);
    }

    /**
     * Gets an emotion's value
     * @param emotion Emotion to get
     * @return Emotion's value
     */
    public Float getEmotion(Emotion emotion) {
        if(emotion == null)
            throw new IllegalArgumentException("Emotion must be non-null");

        return emotions.get(emotion);
    }

    /**
     * Gets an unmodifiable map of all emotion key-values
     * @return An unmodifiable map of all emotion key-values
     */
    public Map<Emotion, Float> getEmotions() {
        return Collections.unmodifiableMap(emotions);
    }

    /**
     * Sets the tick of the packet, only can be set if not already set
     * @param tick Tick value
     */
    public void setTick(float tick) {
        if(this.tick == null)
            this.tick = tick;
        else
            throw new IllegalStateException("Cannot set tick after it has already been set");
    }

    /**
     * Gets the tick of the packet
     * @return Tick of the packet
     */
    public Float getTick() {
        return this.tick;
    }
}
