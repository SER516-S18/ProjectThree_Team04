package team04.project3.model.client;

import team04.project3.model.EmostatePacket;
import team04.project3.model.websocket.MessageDecoder;
import team04.project3.model.websocket.MessageEncoder;
import team04.project3.util.Log;

import javax.websocket.OnMessage;

@javax.websocket.ClientEndpoint(
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class
)

public class ClientEndpoint {
    private ClientModel model;

    public ClientEndpoint(ClientModel model) {
        this.model = model;
    }

    @OnMessage
    public void onMessage(EmostatePacket message) {
        Log.v("Message received", ClientEndpoint.class);
        model.addPacket(message);
    }
}
