package team04.project3.model.server;

import team04.project3.listeners.ServerListener;
import team04.project3.util.Log;

import java.util.ArrayList;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;

/**
 * The center model for the server
 * @author  David Henderson (dchende2@asu.edu)
 */

public class ServerModel {
    private static ServerModel _instance = null;

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

    private ArrayList<ServerListener> listeners = new ArrayList<>();
    private ServerEndpoint endpoint;
    private Server server;
    private boolean run = false;

    private ServerModel() {
        this(1726);
    }

    private ServerModel(int port) {
        this.setPort(port);

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
            endpoint = new ServerEndpoint();
            server = new Server("localhost", PORT, "/ws", null, ServerEndpoint.class);
            server.start();

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
}
