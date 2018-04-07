package team04.project3.model.websocket;

import com.google.gson.Gson;
import team04.project3.model.EmostatePacket;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<EmostatePacket> {
    private static Gson gson = new Gson();

    @Override
    public EmostatePacket decode(String s) {
        return gson.fromJson(s, EmostatePacket.class);
    }

    @Override
    public boolean willDecode(String s) {
        return true;
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
