package test;

import org.junit.Test;
import team04.project3.model.EmostatePacket;
import team04.project3.model.Emotion;
import team04.project3.model.Expression;

import java.util.HashMap;

public class TestEmostatePacket {

    private static HashMap<Expression, Float> expressions = new HashMap<>();
    private static HashMap<Emotion, Float> emotions = new HashMap<>();

    static {
        for (Expression expression : Expression.values())
            expressions.put(expression,0.0f);
        for (Emotion emotion : Emotion.values())
            emotions.put(emotion,0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullExpressionException(){
        EmostatePacket emostatePacket = new EmostatePacket(expressions,emotions);
        emostatePacket.getExpression(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullEmotionException(){
        EmostatePacket emostatePacket = new EmostatePacket(expressions,emotions);
        emostatePacket.getEmotion(null);
    }

    @Test(expected = IllegalStateException.class)
    public void TestSetTickException(){
        EmostatePacket emostatePacket = new EmostatePacket(expressions,emotions);
        emostatePacket.setTick(1f);
        emostatePacket.setTick(1f);
    }
}
