package team04.project3.model.client;

import team04.project3.model.EmostatePacket;
import team04.project3.model.server.ServerWebsocketEndpoint;
import team04.project3.model.websocket.MessageDecoder;
import team04.project3.model.websocket.MessageEncoder;
import team04.project3.util.Log;

import javax.websocket.Session;

@javax.websocket.ClientEndpoint(
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class
)

public class ClientWebsocketEndpoint {
    private ClientModel model;

    public ClientWebsocketEndpoint(ClientModel model) {
        this.model = model;
    }

    @javax.websocket.OnOpen
    public void onOpen(Session session) {
        Log.v("Client opened a new session", ClientWebsocketEndpoint.class);
    }

    @javax.websocket.OnMessage
    public void onMessage(EmostatePacket message) {
        Log.v("Message received", ClientWebsocketEndpoint.class);
        model.addPacket(message);
    }
}
