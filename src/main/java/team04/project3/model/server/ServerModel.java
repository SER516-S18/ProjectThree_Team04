package team04.project3.model.server;

import team04.project3.listeners.ServerListener;
import team04.project3.model.EmostatePacketBuilder;
import team04.project3.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static team04.project3.util.Util.DEFAULT_PORT;

/**
 * The center model for the server
 * @author  David Henderson (dchende2@asu.edu)
 */

public class ServerModel {
    private static ServerModel instance = null;
    private static final ExecutorService executors = Executors.newCachedThreadPool();

    /**
     * ServerModal singleton instance getter
     * @return ServerModel instance
     */
    public static ServerModel get() {
        ServerModel result = instance;
        if(result == null){
            synchronized (ServerModel.class) {
                result = instance;
                if (result == null)
                    instance = result = new ServerModel();
            }
        }
        return result;
    }

    // Server - Settings
    private int PORT;
    private long INTERVAL;
    private boolean REPEAT;

    // Server - Websocket state
    private ArrayList<ServerListener> listeners = new ArrayList<>();
    private ServerWorker worker;

    // Server - Packet state/behavior
    private EmostatePacketBuilder packet;
    private float tick = 0.0f;
    private Runnable output;
    private boolean repeating = false;

    /**
     * Private constructor for singleton, defaulting to default port with repeat interval
     */
    private ServerModel() {
        this(DEFAULT_PORT, 500);
    }

    /**
     * Full private constructor for singleton
     * @param port Port to host at
     * @param interval Interval for repeating packets
     */
    private ServerModel(int port, long interval) {
        this.setPort(port);
        this.setAutoRepeatInterval(interval);
        this.setPacket(EmostatePacketBuilder.getZeroedEmostatePacket());

        output = () -> {
            while(this.isRunning() && this.isPacketRepeatMode() && this.isRepeatingPackets()) {
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

        // Shutdown server on program disconnect
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(ServerModel.this.isRunning())
                ServerModel.this.shutdown();
        }));
    }

    /**
     * Starts the server (note: this only opens the WebSocket server and does not send packets)
     */
    public void start() {
        if(this.isRunning())
            throw new IllegalArgumentException("Server is already running");

        worker = new ServerWorker(this);
        new Thread(worker).start();
        this.notifyServerStarted();
    }

    /**
     * Stops the server (note: this shuts down the WebSocket server and
     * is not the method to stop repeatedly sending packets)
     */
    public void shutdown() {
        if(worker == null)
            throw new IllegalArgumentException("Server is already stopped");

        worker.shutdown();
        worker = null;
        tick = 0.0f;
        this.notifyServerShutdown();
    }

    /**
     * Getter to check if server is running
     * @return run - If server is running
     */
    public boolean isRunning() {
        return worker != null && worker.isRunning();
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
        Log.i("Server disconnect successfully", ServerModel.class);
        for(ServerListener listener : listeners) {
            listener.shutdown();
        }
    }

    /**
     * Notifies the server is starting
     */
    private void notifyServerStarted() {
        Log.i("Server started successfully on port " + ServerModel.this.PORT, ServerModel.class);
        for(ServerListener listener : listeners) {
            listener.started();
        }
    }

    /**
     * Notifies a client connected
     */
    public void notifyClientConnected() {
        for(ServerListener listener : listeners) {
            listener.clientConnected();
        }
    }

    /**
     * Notifies a client disconnected
     */
    public void notifyClientDisconnected() {
        for(ServerListener listener : listeners) {
            listener.clientDisconnected();
        }
    }

    /**
     * Notifies a packet has been sent
     */
    private void notifyPacketSent() {
        for(ServerListener listener : listeners) {
            listener.packetSent();
        }
    }

    /**
     * Notifies repeating packets have started or stopped
     */
    private void notifyPacketRepeatingToggled() {
        for(ServerListener listener : listeners) {
            listener.packetRepeatingToggled();
        }
    }

    /**
     * Notifies the setting to repeat packets or not has changed
     */
    public void notifyPacketRepeatModeChanged() {
        for(ServerListener listener : listeners) {
            listener.packetRepeatingModeChanged();
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
     * @param tick Tick in seconds for the next packet to connect at
     */
    public void setTick(float tick) {
        if(tick < 0.0f)
            throw new IllegalArgumentException("Tick must be greater than zero");
        else
            this.tick = tick;
    }

    /**
     * Returns the current tick
     * @return Current time tick
     */
    public float getTick() {
        return tick;
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
    public boolean isPacketRepeatMode() {
        return REPEAT;
    }

    /**
     * Sets the model to auto-repeat mode
     * @param repeat If the model should auto-repeat
     */
    public void setPacketRepeatMode(boolean repeat) {
        if(REPEAT == repeat)
            return;

        if(repeating)
            throw new IllegalStateException("Cannot turn off auto-repeating while repeater is running");

        REPEAT = repeat;
        notifyPacketRepeatModeChanged();
    }

    /**
     * Gets if the model is currently sending packets repeatedly
     * @return If the model is currently sending packets repeatedly
     */
    public boolean isRepeatingPackets() {
        return repeating;
    }

    /**
     * Method to connect or stop sending packets (in auto-repeat mode)
     */
    public void sendPacketsToggle() {
        if(!this.isPacketRepeatMode())
            throw new IllegalStateException("Cannot send repeating packets while model is not set to auto-repeat");
        if(!this.isRunning())
            throw new IllegalStateException("Cannot send packets while the model's websocket server is not running");

        if(this.isRepeatingPackets()) {
            repeating = false;
        } else {
            repeating = true;
            executors.submit(output);
        }

        this.notifyPacketRepeatingToggled();
    }

    /**
     * Method to send an individual packet (if not in auto-repeat mode)
     */
    public void sendPacketIndividual() {
        if(this.isPacketRepeatMode())
            throw new IllegalStateException("Cannot send individual packet while the model is set to auto-repeat");
        if(!this.isRunning())
            throw new IllegalStateException("Cannot send packets while the model's websocket server is not running");

        this.sendPacket();
    }

    /**
     * Called to send a packet to the endpoint
     */
    private void sendPacket() {
        worker.send(packet.setTick(tick).build());
        tick += (INTERVAL / 1000f);
        this.notifyPacketSent();
    }

    /**
     * Gets number of clients connected
     * @return Number of clients connected
     */
    public int getClientsCount() {
        return (worker == null ? 0 : worker.getClientsCount());
    }
}
