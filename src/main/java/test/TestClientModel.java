package test;

import org.junit.Test;
import team04.project3.model.client.ClientModel;

import static org.junit.Assert.assertEquals;

public class TestClientModel {

    @Test(expected = IllegalArgumentException.class)
    public void testStartException(){
        ClientModel.get().start();
        ClientModel.get().start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisconnectException(){
        ClientModel.get().start();
        ClientModel.get().disconnect();
        ClientModel.get().disconnect();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullListenerException(){
        ClientModel.get().addListener(null);
    }

    @Test
    public void TestIsRunning(){
        ClientModel.get().start();
        assertEquals(true, ClientModel.get().isRunning());
        ClientModel.get().disconnect();
        assertEquals(false, ClientModel.get().isRunning());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativePortException(){
        ClientModel.get().setPort(-43);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullHostException(){
        ClientModel.get().setHost(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullPacketException(){
        ClientModel.get().addPacket(null);
    }
}
