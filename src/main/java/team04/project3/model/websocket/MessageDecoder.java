package team04.project3.model.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team04.project3.model.EmostatePacket;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Decodes EmostatePacket messages from websockets
 * @author  David Henderson (dchende2@asu.edu)
 */
public class MessageDecoder implements Decoder.Text<EmostatePacket> {
    private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

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
