package team04.project3.model.client;

import team04.project3.model.EmostatePacket;
import team04.project3.model.websocket.MessageDecoder;
import team04.project3.model.websocket.MessageEncoder;
import team04.project3.util.Log;

import javax.websocket.CloseReason;
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
        Log.v("Client session opened", ClientWebsocketEndpoint.class);
    }

    @javax.websocket.OnMessage
    public void onMessage(EmostatePacket message) {
        Log.v("Message received", ClientWebsocketEndpoint.class);
        model.addPacket(message);
    }

    @javax.websocket.OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Log.v("Client session closed (" + closeReason.toString() + ")", ClientWebsocketEndpoint.class);
    }

    @javax.websocket.OnError
    public void onError(Session session, Throwable throwable) {
        Log.v("Client session encountered an error (" + throwable.getMessage() + ")", ClientWebsocketEndpoint.class);
    }
}
