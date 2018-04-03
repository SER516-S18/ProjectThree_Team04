package team04.project3.model;

import java.util.HashMap;

public class EmostatePacketBuilder {
    private HashMap<Expression, Float> expressions = new HashMap<>();
    private HashMap<Emotion, Float> emotions = new HashMap<>();
    private Float tick = null;

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

    public EmostatePacketBuilder() { }

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

    public EmostatePacketBuilder setExpression(Expression expression, boolean value) {
        if(expression == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(!expression.isBinary()) {
            throw new IllegalArgumentException("Expression \"" + expression.NAME + "\" cannot be set to a binary value");
        }

        this.expressions.put(expression, value ? 1.0f : 0.0f);
        return this;
    }

    public EmostatePacketBuilder setEmotion(Emotion emotion, float value) {
        if(emotion == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(value < 0.0f || value > 1.0f) {
            throw new IllegalArgumentException("Emotion \"" + emotion.NAME + "\" value must be between 0.0 and 1.0, inclusive");
        }

        this.emotions.put(emotion, value);
        return this;
    }

    public Float getExpression(Expression expression) {
        if(expression == null)
            throw new IllegalArgumentException("Expression must be non-null");
        else
            return this.expressions.getOrDefault(expression, null);
    }

    public Float getExpressionFloating(Expression expression) {
        return this.getExpression(expression);
    }

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

    public Float getEmotion(Emotion emotion) {
        if(emotion == null)
            throw new IllegalArgumentException("Emotion must be non-null");
        else
            return this.emotions.getOrDefault(emotion, null);
    }

    public EmostatePacketBuilder setTick(Float tick) {
        this.tick = tick;
        return this;
    }

    public EmostatePacket build() {
        return (tick == null ? new EmostatePacket(expressions, emotions) : new EmostatePacket(expressions, emotions, tick));
    }
}