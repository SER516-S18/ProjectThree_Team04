package team04.project3.model.server;

import org.glassfish.tyrus.server.Server;
import team04.project3.listeners.ServerListener;
import team04.project3.model.websocket.EmostatePacketBuilder;
import team04.project3.util.Log;

import javax.websocket.DeploymentException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The center model for the server
 * @author  David Henderson (dchende2@asu.edu)
 */

public class ServerModel {
    private static ServerModel _instance = null;
    private static final ExecutorService executors = Executors.newCachedThreadPool();

    /**
     * ServerModal singleton instance getter
     * @return ServerModel instance
     */
    public static ServerModel get() {
        if(_instance == null)
            _instance = new ServerModel();

        return _instance;
    }

    // Server - Settings
    private int PORT;
    private long INTERVAL;
    private boolean REPEAT;

    // Server - Websocket state
    private ArrayList<ServerListener> listeners = new ArrayList<>();
    private Server server;
    private ServerEndpoint endpoint;
    private boolean run = false;

    // Server - Packet state/behavior
    private EmostatePacketBuilder packet;
    private float tick = 0.0f;
    private Runnable output;
    private boolean repeating = false;

    private ServerModel() {
        this(1726, 500);
    }

    private ServerModel(int port, long interval) {
        this.setPort(port);
        this.setAutoRepeatInterval(interval);
        this.setPacket(EmostatePacketBuilder.getZeroedEmostatePacket());

        output = () -> {
            while(this.isRunning() && this.isPacketAutoRepeatMode() && this.isRepeatingPackets()) {
                try {
                    ServerModel.this.sendPacket();
                    Thread.sleep(INTERVAL);
                } catch(InterruptedException e) {
                    Log.w("Failed to wait interval between packets (" + e.getMessage() + ")", ServerModel.class);
                } catch (Exception e) {
                    if(ServerModel.this.isRunning()) {
                        Log.e("Model failed to send a packet, shutting down (" + e.getMessage() + ")", ServerModel.class);
                        ServerModel.this.shutdown();
                    }
                }
            }
        };

        // Shutdown server on program shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(ServerModel.this.isRunning())
                ServerModel.this.shutdown();
        }));
    }

    /**
     * Starts the server (note: this only opens the WebSocket server and does not send packets)
     */
    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        try {
            // Start server
            endpoint = new ServerEndpoint(this);
            server = new Server("localhost", PORT, "/ws", null, ServerEndpoint.class);
            server.start();

            this.run = true;
            this.notifyServerStarted();
        } catch(DeploymentException e) {
            Log.e("Failed to deploy web socket server (" + e.getMessage() + ")", ServerModel.class);
        }
    }

    /**
     * Stops the server (note: this shuts down the WebSocket server and
     * is not the method to stop repeatedly sending packets)
     */
    public void shutdown() {
        if(!run)
            throw new IllegalArgumentException("Server is already stopped");

        endpoint.disconnect();
        server.stop();

        tick = 0.0f;
        endpoint = null;
        server = null;
        run = false;
        this.notifyServerShutdown();
    }

    /**
     * Getter to check if server is running
     * @return run - If server is running
     */
    public boolean isRunning() {
        return run;
    }

    /**
     * Adds a ServerListener to notify
     * @param listener The listener to notify
     */
    public void addListener(ServerListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener must not be null");
        else
            listeners.add(listener);
    }

    /**
     * Notifies that the server is shutting down
     */
    private void notifyServerShutdown() {
        Log.i("Server shutdown successfully", ServerModel.class);
        for(ServerListener listener : listeners) {
            listener.shutdown();
        }
    }

    /**
     * Notifies the server is starting
     */
    private void notifyServerStarted() {
        Log.i("Server started successfully", ServerModel.class);
        for(ServerListener listener : listeners) {
            listener.started();
        }
    }

    /**
     * Send the packet to send
     * @param packet The packet to send to the client each tick
     */
    public void setPacket(EmostatePacketBuilder packet) {
        if(packet == null)
            throw new IllegalArgumentException("Packet must be non-null");
        else
            this.packet = packet;
    }

    /**
     * Sets which tick for the next packet sent (in seconds)
     * @param tick Tick in seconds for the next packet to start at
     */
    public void setTick(float tick) {
        if(tick < 0.0f)
            throw new IllegalArgumentException("Tick must be greater than zero");
        else
            this.tick = tick;
    }

    /**
     * Setter for server's port
     * @param port Port to connect to
     */
    public void setPort(int port) {
        if(port < 0)
            throw new IllegalArgumentException("Port must be greater than zero");
        else if(this.getPort() != port)
            PORT = port;
    }

    /**
     * Getter for server's port
     * @return port Server's port
     */
    public int getPort() {
        return PORT;
    }


    /**
     * Sets the interval for the auto-repeat
     * @param interval The interval in ms between packet transmissions
     */
    public void setAutoRepeatInterval(long interval) {
        if(interval < 0L)
            throw new IllegalArgumentException("Interval must be greater than zero");
        else
            INTERVAL = interval;
    }

    /**
     * Gets the interval for the auto-repeat
     * @return The interval in ms between packet transmissions
     */
    public long getAutoRepeatInterval() {
        return INTERVAL;
    }

    /**
     * Gets if the packets are set to auto-repeat, if on
     * @return If the packets auto-repeat, if on
     */
    public boolean isPacketAutoRepeatMode() {
        return REPEAT;
    }

    /**
     * Sets the model to auto-repeat mode
     * @param repeat If the model should auto-repeat
     */
    public void setPacketAutoRepeatMode(boolean repeat) {
        if(REPEAT == repeat)
            return;

        if(repeating)
            throw new IllegalStateException("Cannot turn off auto-repeating while repeater is running");

        REPEAT = repeat;
    }

    /**
     * Gets if the model is currently sending packets repeatedly
     * @return If the model is currently sending packets repeatedly
     */
    public boolean isRepeatingPackets() {
        return repeating;
    }

    /**
     * Method to start or stop sending packets (in auto-repeat mode)
     */
    public void sendPacketsToggle() {
        if(!this.isPacketAutoRepeatMode())
            throw new IllegalStateException("Cannot send repeating packets while model is not set to auto-repeat");
        if(!this.isRunning())
            throw new IllegalStateException("Cannot send packets while the model's websocket server is not running");

        if(this.isRepeatingPackets()) {
            repeating = false;
        } else {
            repeating = true;
            executors.submit(output);
        }
    }

    /**
     * Method to send an individual packet (if not in auto-repeat mode)
     */
    public void sendPacketIndividual() {
        if(this.isPacketAutoRepeatMode())
            throw new IllegalStateException("Cannot send individual packet while the model is set to auto-repeat");
        if(!this.isRunning())
            throw new IllegalStateException("Cannot send packets while the model's websocket server is not running");

        this.sendPacket();
    }

    /**
     * Called to send a packet to the endpoint
     */
    private void sendPacket() {
        endpoint.send(packet.setTick(tick).build());
        tick += (INTERVAL / 1000f);
    }
}
