package team04.project3.model.server;

import team04.project3.model.EmostatePacket;
import team04.project3.model.websocket.MessageDecoder;
import team04.project3.model.websocket.MessageEncoder;
import team04.project3.util.Log;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@javax.websocket.server.ServerEndpoint(
    value = "/emostate",
    decoders = MessageDecoder.class,
    encoders = MessageEncoder.class
)

/**
 * Endpoint for the server's websocket
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerWebsocketEndpoint {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    /**
     * Called when a client connects
     * @param session Session representing client
     * @throws IOException Upon IOException while opening
     */
    @javax.websocket.OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and WebSocket connection
        sessions.add(session);
        ServerModel.get().notifyClientConnected();
        Log.v("Client opened a new session (" + sessions.size() + " total open)", ServerWebsocketEndpoint.class);
    }

    /**
     * Called when a client sends a message
     * @param session Session representing client
     * @param message Message from client
     * @throws IOException Upon receiving message from client
     * @throws EncodeException Upon failing to decode message
     */
    @javax.websocket.OnMessage
    public void onMessage(Session session, EmostatePacket message) throws IOException, EncodeException {
        // Handle incoming messages
    }

    /**
     * Called when a client closes the connection
     * @param session Session representing client
     * @throws IOException Upon failing to close gracefully
     */
    @javax.websocket.OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
        sessions.remove(session);
        ServerModel.get().notifyClientDisconnected();
        Log.v("Client session closed (" + sessions.size() + " remain open)", ServerWebsocketEndpoint.class);
    }

    /**
     * Called when an error occurs in a session
     * @param session Session representing client
     * @param throwable Error encountered
     */
    @javax.websocket.OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        sessions.remove(session);
        Log.v("Client session threw an error (" + throwable.getMessage() + ")", ServerWebsocketEndpoint.class);
        throwable.printStackTrace();
    }

    /**
     * Sends a packet to all clients
     * @param packet Packet to send
     */
    public synchronized void send(EmostatePacket packet) {
        for(Session session : sessions) {
            try {
                session.getBasicRemote().sendObject(packet);
            } catch(EncodeException | IOException e) {
                Log.w("Failed to send packet to session (" + e.getMessage() + ")", ServerWebsocketEndpoint.class);
            }
        }

        if(sessions.size() > 0) {
            Log.v("Sent EmostatePacket to " + sessions.size() + " clients.", ServerWebsocketEndpoint.class);
        }
    }

    /**
     * Disconnects from all clients
     */
    public synchronized void disconnect() {
        for (Iterator<Session> i = sessions.iterator(); i.hasNext();) {
            Session session = i.next();
            try {
                session.close();
            } catch(IOException e) {
                Log.w("Failed to gracefully close session", ServerWebsocketEndpoint.class);
            }
        }

        sessions.clear();
    }

    /**
     * Gets the number of clients connected
     * @return Number of clients connected
     */
    public int getClientsCount() {
        return sessions.size();
    }
}
