package team04.project3.model.client;

import team04.project3.listeners.ClientListener;
import team04.project3.model.EmostatePacket;
import team04.project3.model.Emotion;
import team04.project3.model.Expression;
import team04.project3.model.ValueTuple;
import team04.project3.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static team04.project3.util.Util.DEFAULT_PORT;

/**
 * The main model for client
 * @author  David Henderson (dchende2@asu.edu)
 */

public class ClientModel {
    private static InetAddress LOCALHOST;
    private static volatile ClientModel instance;

    static {
        // Helper function to save the localhost as a default
        try {
            LOCALHOST = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOCALHOST = null;
            Log.e("Failed to get the default localhost (" + e.getMessage() + ")", ClientModel.class);
        }
    }

    /**
     * Singleton ClientModel getter
     * @return ClientModel instance
     */
    public static ClientModel get() {
        ClientModel result = instance;
        if(result == null){
            synchronized (ClientModel.class) {
                result = instance;
                if (result == null)
                    instance = result = new ClientModel();
            }
        }
        return result;
    }

    private int PORT;
    private InetAddress HOST;

    private final ArrayList<ClientListener> listeners = new ArrayList<>();
    private ClientWorker worker;
    private LinkedList<EmostatePacket> packets;
    private HashMap<Expression, LinkedList<ValueTuple>> expressionPackets;
    private HashMap<Emotion, LinkedList<ValueTuple>> emotionPackets;
    private EmostatePacket packetNewest;

    /**
     * Default constructor, defaulting to port 1726 and LOCALHOST
     */
    private ClientModel() {
        this(DEFAULT_PORT, LOCALHOST);
    }

    /**
     * Full constructor
     * @param port Port to connect to
     * @param host Host to connect to
     */
    private ClientModel(int port, InetAddress host) {
        this.setHost(host);
        this.setPort(port);

        packets = new LinkedList<>();
        expressionPackets = new HashMap<>();
        for(Expression expression : Expression.values()) {
            expressionPackets.put(expression, new LinkedList<>());
        }
        emotionPackets = new HashMap<>();
        for(Emotion emotion : Emotion.values()) {
            emotionPackets.put(emotion, new LinkedList<>());
        }

        // Shutdown client on program disconnect
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(ClientModel.this.isConnected())
                ClientModel.this.disconnect();
        }));
    }

    /**
     * Called to connect the client model and connect
     */
    public void start() {
        if(this.isRunning())
            throw new IllegalArgumentException("Client is already running");

        worker = new ClientWorker(this);
        new Thread(worker).start();
        this.notifyClientStarted();
    }

    /**
     * Called to disconnect the client from the server
     */
    public void disconnect() {
        if(worker == null)
            throw new IllegalArgumentException("Client is already stopped");

        worker.shutdown();
        worker = null;
        this.notifyClientShutdown();
    }

    /**
     * Add ClientListeners to ClientModel
     * @param listener ClientListener to inform
     */
    public void addListener(ClientListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener must not be null");
        else
            listeners.add(listener);
    }

    /**
     * Method called when ClientModel is shut down
     */
    private void notifyClientShutdown() {
        Log.i("Client disconnect successfully", ClientModel.class);
        for(ClientListener listener : listeners) {
            listener.shutdown();
        }
    }

    /**
     * Method called when ClientModel is started up
     */
    private void notifyClientStarted() {
        Log.i("Client started and is attempting to connect to server at " + HOST.getCanonicalHostName() + " port " + PORT, ClientModel.class);
        for(ClientListener listener : listeners) {
            listener.started();
        }
    }

    /**
     * Notifies Emostate values were changed (added/modified/deleted)
     */
    private void notifyValuesChanged() {
        for(ClientListener listener : listeners) {
            listener.valuesChanged();
        }
    }

    /**
     * Notifies Emostate values were strictly added
     */
    private void notifyValuesAdded() {
        for(ClientListener listener : listeners) {
            listener.valuesAdded();
        }

        this.notifyValuesChanged();
    }

    /**
     * Notifies Emostate value were set
     */
    private void notifyValuesReset() {
        for(ClientListener listener : listeners) {
            listener.valuesReset();
        }

        this.notifyValuesChanged();
    }

    /**
     * Check if the client is running
     * @return boolean if client is running
     */
    public boolean isConnected() {
        return worker != null && worker.isConnected();
    }

    /**
     * Check if the client is running (not necessarily connected)
     * @return boolean if client is running
     */
    public boolean isRunning() {
        return worker != null && worker.isRunning();
    }

    /**
     * Setter for server's port to connect to
     * @param port Server's port to connect to
     */
    public void setPort(int port) {
        if(port < 0)
            throw new IllegalArgumentException("Port must be greater than zero");
        else if(this.getPort() != port)
            PORT = port;
    }

    /**
     * Getter for server's port to connect to
     * @return port Server's port to connect to
     */
    public int getPort() {
        return PORT;
    }

    /**
     * Changes the host (WILL disconnect if Client is running)
     * @param host The host to connect to
     */
    public void setHost(InetAddress host) {
        if(host == null)
            throw new IllegalArgumentException("Host must be non-null");
        else {
            if(this.isRunning()) {
                // Reconnect if running
                this.disconnect();

                try {
                    long timeout = System.currentTimeMillis() + 1000L;
                    while (this.isRunning() && System.currentTimeMillis() < timeout) {
                        Thread.sleep(100L);
                    }
                } catch(InterruptedException e) {
                    Log.w("Failed to wait while disconnecting to change host (" + e.getMessage() + ")", ClientModel.class);
                }

                HOST = host;

                this.start();
            } else {
                // Otherwise just set the new host
                HOST = host;
            }
        }
    }

    /**
     * Sets the host to localhost
     */
    public void setHostToLocalhost() {
        this.setHost(LOCALHOST);
    }

    /**
     * Returns the host to connect to
     * @return The host to connect to
     */
    public InetAddress getHost() {
        return HOST;
    }

    /**
     * Adds a packet to the model (from the server)
     * @param packet Packet to add
     */
    public void addPacket(EmostatePacket packet) {
        if(packet == null)
            throw new IllegalArgumentException("Packet must be non-null");

        // Reset if the server sends a packet earlier in time
        if(packetNewest != null && packet.getTick() < packetNewest.getTick())
            resetPackets();

        packetNewest = packet;
        packets.add(packet);
        for(Expression expression : Expression.values()) {
            expressionPackets.get(expression).add(new ValueTuple(packet.getExpression(expression), packet.getTick()));
        }
        for(Emotion emotion : Emotion.values()) {
            emotionPackets.get(emotion).add(new ValueTuple(packet.getEmotion(emotion), packet.getTick()));
        }

        this.notifyValuesAdded();
    }

    /**
     * Resets and remove/clears all previous packets
     */
    public void resetPackets() {
        packetNewest = null;
        packets.clear();
        for(Expression expression : Expression.values())
            expressionPackets.get(expression).clear();
        for(Emotion emotion : Emotion.values())
            emotionPackets.get(emotion).clear();

        this.notifyValuesReset();
    }

    /**
     * Returns an unmodifiable list of all packets
     * @return Unmodifiable list of all packets
     */
    public List<EmostatePacket> getPackets() {
        return Collections.unmodifiableList(packets);
    }

    /**
     * Gets a list of all packet values for a specific expression
     * @param expression Expression to get
     * @return Packet values
     */
    public List<ValueTuple> getExpressionPackets(Expression expression) {
        return Collections.unmodifiableList(expressionPackets.get(expression));
    }

    /**
     * Gets a list of all packet values for a specific emotion
     * @param emotion Emotion to get
     * @return Packet values
     */
    public List<ValueTuple> getEmotionPackets(Emotion emotion) {
        return Collections.unmodifiableList(emotionPackets.get(emotion));
    }

    /**
     * Returns the newest received packet, or null if none
     * @return Newest receieved packet, or null if none
     */
    public EmostatePacket getNewestPacket() {
        return packetNewest;
    }

    /**
     * Gets the number of packets received
     * @return Number of packets received
     */
    public int getPacketsCount() {
        return packets.size();
    }
}
