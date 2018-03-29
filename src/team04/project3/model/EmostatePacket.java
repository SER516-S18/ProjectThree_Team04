package team04.project3.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EmostatePacket {
    private HashMap<Expression, Float> expressions;
    private HashMap<Emotion, Float> emotions;

    public EmostatePacket(HashMap<Expression, Float> expressions, HashMap<Emotion, Float> emotions) {
        if(expressions == null || emotions == null)
            throw new IllegalArgumentException("Arguments must be non-null");

        for(Expression expression : Expression.values()) {
            if(!expressions.containsKey(expression))
                throw new IllegalArgumentException("Expressions must contain all possible expression enumerations");
        }

        for(Emotion emotion : Emotion.values()) {
            if(!emotions.containsKey(emotion))
                throw new IllegalArgumentException("Emotions must contain all possible expression enumerations");
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
}
