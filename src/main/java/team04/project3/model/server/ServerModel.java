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

    private int PORT;
    private long INTERVAL;
    private boolean isAutoRepeat;

    private ArrayList<ServerListener> listeners = new ArrayList<>();
    private ServerEndpoint endpoint;
    private Server server;
    private Runnable output;
    private EmostatePacketBuilder packet;
    private float tick = 0.0f;
    private boolean run = false;

    private ServerModel() {
        this(1726, 500);
    }

    private ServerModel(int port, long interval) {
        this.setPort(port);
        this.setInterval(interval);
        this.setPacket(EmostatePacketBuilder.getZeroedEmostatePacket());

        output = () -> {
            while(ServerModel.this.isRunning()) {
                try {
                    endpoint.send(packet.setTick(tick).build());
                    Thread.sleep(interval);
                    tick += (interval / 1000f);
                } catch(InterruptedException e) {
                    Log.w("Failed to wait interval between packets (" + e.getMessage() + ")", ServerModel.class);
                } catch (Exception e) {
                    if(ServerModel.this.isRunning()) {
                        Log.e("Model failed to send a packet, shutting down (" + e.getMessage() + ")", ServerModel.class);
                        this.shutdown();
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

    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        try {
            // Start server
            endpoint = new ServerEndpoint();
            server = new Server("localhost", PORT, "/ws", null, ServerEndpoint.class);
            server.start();

            // Start output
            executors.submit(output);

            this.run = true;
            this.notifyServerStarted();
        } catch(DeploymentException e) {
            Log.e("Failed to deploy web socket server (" + e.getMessage() + ")", ServerModel.class);
        }
    }

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

    public void setPacket(EmostatePacketBuilder packet) {
        if(packet == null)
            throw new IllegalArgumentException("Packet must be non-null");
        else
            this.packet = packet;
    }

    public void setInterval(long interval) {
        if(interval < 0L)
            throw new IllegalArgumentException("Interval must be greater than zero");
        else
            INTERVAL = interval;
    }

    public long getInterval() {
        return INTERVAL;
    }

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

    public boolean isAutoRepeat() {
        return isAutoRepeat;
    }

    public void setAutoRepeat(boolean autoRepeat) {
        isAutoRepeat = autoRepeat;
    }
}
