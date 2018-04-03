package team04.project3.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EmostatePacket {
    private final HashMap<Expression, Float> expressions;
    private final HashMap<Emotion, Float> emotions;
    private Float tick = null;

    public EmostatePacket(HashMap<Expression, Float> expressions, HashMap<Emotion, Float> emotions, float tick) {
        this(expressions, emotions);
        this.tick = tick;
    }

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

    public Float getExpression(Expression expression) {
        if(expression == null)
            throw new IllegalArgumentException("Expression must be non-null");

        return expressions.get(expression);
    }

    public Map<Expression, Float> getExpressions() {
        return Collections.unmodifiableMap(expressions);
    }

    public Float getEmotion(Emotion emotion) {
        if(emotion == null)
            throw new IllegalArgumentException("Emotion must be non-null");

        return emotions.get(emotion);
    }

    public Map<Emotion, Float> getEmotions() {
        return Collections.unmodifiableMap(emotions);
    }

    public void setTick(float tick) {
        if(this.tick == null)
            this.tick = tick;
        else
            throw new IllegalStateException("Cannot set tick after it has already been set");
    }

    public Float getTick() {
        return this.tick;
    }
}
