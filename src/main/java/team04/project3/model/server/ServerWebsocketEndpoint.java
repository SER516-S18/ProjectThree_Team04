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

public class ServerWebsocketEndpoint {
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @javax.websocket.OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and WebSocket connection
        sessions.add(session);
        Log.v("Client opened a new session (" + sessions.size() + " total open)", ServerWebsocketEndpoint.class);
    }

    @javax.websocket.OnMessage
    public void onMessage(Session session, EmostatePacket message) throws IOException, EncodeException {
        // Handle incoming messages
    }

    @javax.websocket.OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
        sessions.remove(session);
        Log.v("Client session closed (" + sessions.size() + " remain open)", ServerWebsocketEndpoint.class);
    }

    @javax.websocket.OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        sessions.remove(session);
        Log.v("Client session threw an error (" + throwable.getMessage() + ")", ServerWebsocketEndpoint.class);
        throwable.printStackTrace();
    }

    public void send(EmostatePacket packet) {
        for(Session session : sessions) {
            try {
                session.getBasicRemote().sendObject(packet);
            } catch(EncodeException | IOException e) {
                Log.w("Failed to send packet to session (" + e.getMessage() + ")", ServerWebsocketEndpoint.class);
            }
        }
    }

    public void disconnect() {
        Iterator<Session> iterator = sessions.iterator();
        for (Iterator<Session> i = sessions.iterator(); i.hasNext();) {
            Session session = iterator.next();
            try {
                session.close();
            } catch(IOException e) {
                Log.w("Failed to gracefully close session", ServerWebsocketEndpoint.class);
            }
            iterator.remove();
        }
    }
}
