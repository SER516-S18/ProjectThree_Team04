package test;

import org.junit.Test;
import team04.project3.model.EmostatePacketBuilder;
import team04.project3.model.Emotion;
import team04.project3.model.Expression;

public class TestEmostatePacketBuilder {

    @Test(expected = IllegalArgumentException.class)
    public void testSetExpressionNullException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setExpression(null,0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExpressionHighFloatException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setExpression(Expression.BLINK,2f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExpressionLowFloatException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setExpression(Expression.BLINK,-2f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExpressionFloatInBetweenException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setExpression(Expression.BLINK,0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExpressionNullException2(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setExpression(null,false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetBinaryExpressionException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setExpression(Expression.BROW_RAISE,false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmotionNullException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setEmotion(null,0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmotionHighFloatException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setEmotion(Emotion.ENGAGEMENT,2f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmotionLowFloatException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.setEmotion(Emotion.ENGAGEMENT,-2f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExpressionNullException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.getExpression(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExpressionBooleanNullException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.getExpressionBoolean(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExpressionBooleanBinaryException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.getExpressionBoolean(Expression.BROW_RAISE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEmotionNullException(){
        EmostatePacketBuilder builder = new EmostatePacketBuilder();
        builder.getEmotion(null);
    }
}
