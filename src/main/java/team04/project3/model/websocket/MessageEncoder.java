package team04.project3.model.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team04.project3.model.EmostatePacket;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<EmostatePacket> {
    private static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

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