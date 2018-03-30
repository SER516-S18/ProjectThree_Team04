package team04.project3.model.server;

import team04.project3.listeners.ServerListener;
import team04.project3.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

    private ArrayList<ServerListener> listeners = new ArrayList<>();
    private boolean run = false;

    private ServerModel() {
        // Shutdown server on program shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(ServerModel.this.isRunning())
                ServerModel.this.shutdown();
        }));
    }

    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        run = true;
        this.notifyServerStarted();
    }

    public void shutdown() {
        if(!run)
            throw new IllegalArgumentException("Server is already stopped");

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
}
