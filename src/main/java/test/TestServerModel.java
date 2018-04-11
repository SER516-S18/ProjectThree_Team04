package test;

import org.junit.Test;
import team04.project3.model.server.ServerModel;

import static org.junit.Assert.assertEquals;

public class TestServerModel {

    @Test(expected = IllegalArgumentException.class)
    public void testStartException(){
        ServerModel.get().start();
        ServerModel.get().start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShutDownException(){
        ServerModel.get().start();
        ServerModel.get().shutdown();
        ServerModel.get().shutdown();
    }

    @Test
    public void TestIsRunning(){
        ServerModel.get().start();
        assertEquals(true, ServerModel.get().isRunning());
        ServerModel.get().shutdown();
        assertEquals(false, ServerModel.get().isRunning());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullListenerException(){
        ServerModel.get().addListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullPacketException(){
        ServerModel.get().setPacket(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeTickException(){
        ServerModel.get().setTick(-43);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativePortException(){
        ServerModel.get().setPort(-43);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeAutoRepeatIntervalException(){
        ServerModel.get().setAutoRepeatInterval(-43);
    }
}
