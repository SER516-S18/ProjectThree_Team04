package team04.project3.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EmostatePacket {
    private HashMap<Expression, Float> expressions;
    private HashMap<Emotion, Float> emotions;

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
                    throw new IllegalArgumentException("Expression \"" + expression.name + "\" value must not be null");
                } else if(expression.isFloating() && (value < 0.0f || value > 1.0f)) {
                    throw new IllegalArgumentException("Expression \"" + expression.name + "\" value must be between 0.0 and 1.0, inclusive");
                } else if(expression.isBinary() && value != 0.0f && value != 1.0f) {
                    throw new IllegalArgumentException("Expression \"" + expression.name + "\" value must be exactly 0.0f or 1.0f");
                }
            }
        }

        for(Emotion emotion : Emotion.values()) {
            if(!emotions.containsKey(emotion)) {
                throw new IllegalArgumentException("Emotions must contain all possible expression enumerations");
            } else {
                Float value = emotions.get(emotion);
                if(value == null) {
                    throw new IllegalArgumentException("Emotion \"" + emotion.name + "\" value must not be null");
                } else if(value < 0.0f || value > 1.0f) {
                    throw new IllegalArgumentException("Emotion \"" + emotion.name + "\" value must be between 0.0 and 1.0, inclusive");
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

    public static class EmostatePacketBuilder {
        private HashMap<Expression, Float> expressions = new HashMap<>();
        private HashMap<Emotion, Float> emotions = new HashMap<>();

        public static EmostatePacket getZeroedEmostatePacket() {
            EmostatePacketBuilder builder = new EmostatePacketBuilder();
            for(Expression expression : Expression.values()) {
                builder.setExpression(expression, 0.0f);
            }

            for(Emotion emotion : Emotion.values()) {
                builder.setEmotion(emotion, 0.0f);
            }

            return builder.build();
        }

        public EmostatePacketBuilder() { }

        public EmostatePacketBuilder setExpression(Expression expression, float value) {
            if(expression == null) {
                throw new IllegalArgumentException("Arguments must not be null");
            } else if(expression.isFloating() && (value < 0.0f || value > 1.0f)) {
                throw new IllegalArgumentException("Expression \"" + expression.name + "\" value must be between 0.0 and 1.0, inclusive");
            } else if(expression.isBinary() && value != 0.0f && value != 1.0f) {
                throw new IllegalArgumentException("Expression \"" + expression.name + "\" value must be exactly 0.0f or 1.0f");
            }

            this.expressions.put(expression, value);
            return this;
        }

        public EmostatePacketBuilder setExpression(Expression expression, boolean value) {
            if(expression == null) {
                throw new IllegalArgumentException("Arguments must not be null");
            } else if(!expression.isBinary()) {
                throw new IllegalArgumentException("Expression \"" + expression.name + "\" cannot be set to a binary value");
            }

            this.expressions.put(expression, value ? 1.0f : 0.0f);
            return this;
        }

        public EmostatePacketBuilder setEmotion(Emotion emotion, float value) {
            if(emotion == null) {
                throw new IllegalArgumentException("Arguments must not be null");
            } else if(value < 0.0f || value > 1.0f) {
                throw new IllegalArgumentException("Emotion \"" + emotion.name + "\" value must be between 0.0 and 1.0, inclusive");
            }

            this.emotions.put(emotion, value);
            return this;
        }

        public EmostatePacket build() {
            return new EmostatePacket(expressions, emotions);
        }
    }
}
