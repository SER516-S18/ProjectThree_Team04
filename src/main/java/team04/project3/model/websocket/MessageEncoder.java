package team04.project3.model.websocket;

import com.google.gson.Gson;
import team04.project3.model.EmostatePacket;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<EmostatePacket> {
    private static Gson gson = new Gson();

    @Override
    public String encode(final EmostatePacket message) {
        return gson.toJson(message);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization
    }

    @Override
    public void destroy() {
        // Close resources
    }
}