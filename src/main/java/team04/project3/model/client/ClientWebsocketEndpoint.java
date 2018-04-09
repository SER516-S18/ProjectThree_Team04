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

/**
 * Endpoint for the client's websocket
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientWebsocketEndpoint {
    /**
     * Called when a client opens a connection to the server
     * @param session Session representing client
     */
    @javax.websocket.OnOpen
    public void onOpen(Session session) {
        Log.v("Client session opened", ClientWebsocketEndpoint.class);
    }

    /**
     * Called when the server sends a message to the client
     * @param message Message from the server
     */
    @javax.websocket.OnMessage
    public void onMessage(EmostatePacket message) {
        ClientModel.get().addPacket(message);
    }

    /**
     * Called when the client closes the connection to the server
     * @param session Session representing client
     * @param closeReason Reason for disconnect
     */
    @javax.websocket.OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Log.v("Client session closed (" + closeReason.toString() + ")", ClientWebsocketEndpoint.class);
        if(ClientModel.get().isRunning())
            ClientModel.get().disconnect();
    }

    /**
     * Called when the client session encounters an error
     * @param session Session representing client
     * @param throwable Error encountered
     */
    @javax.websocket.OnError
    public void onError(Session session, Throwable throwable) {
        Log.v("Client session encountered an error (" + throwable.getMessage() + ")", ClientWebsocketEndpoint.class);
        if(ClientModel.get().isRunning())
            ClientModel.get().disconnect();
    }
}
