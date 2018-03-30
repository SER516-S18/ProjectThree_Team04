package team04.project3.model.server;

import team04.project3.model.EmostatePacket;
import team04.project3.model.websocket.MessageDecoder;
import team04.project3.model.websocket.MessageEncoder;
import team04.project3.util.Log;

import javax.websocket.*;
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
public class ServerEndpoint {
    static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and WebSocket connection
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, EmostatePacket message) throws IOException, EncodeException {
        // Handle new messages
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        sessions.remove(session);
    }

    public void disconnect() {
        Iterator<Session> iterator = sessions.iterator();
        for (Iterator<Session> i = sessions.iterator(); i.hasNext();) {
            Session session = iterator.next();
            try {
                session.close();
            } catch(IOException e) {
                Log.w("Failed to gracefully close session", ServerEndpoint.class);
            }
            iterator.remove();
        }
    }
}
