package team04.project3.model.client;

import team04.project3.model.websocket.Message;
import team04.project3.model.websocket.MessageDecoder;
import team04.project3.model.websocket.MessageEncoder;

import javax.websocket.OnMessage;
import java.text.SimpleDateFormat;

@javax.websocket.ClientEndpoint(
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class
)

public class ClientEndpoint {
    @OnMessage
    public void onMessage(Message message) {
        // Message
    }
}
