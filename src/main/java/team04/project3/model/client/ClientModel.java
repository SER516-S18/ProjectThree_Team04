package team04.project3.model.client;

import org.glassfish.tyrus.client.ClientManager;
import team04.project3.listeners.ClientListener;
import team04.project3.util.Log;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * The main model for client
 * @author  David Henderson (dchende2@asu.edu)
 */

public class ClientModel {
    private static InetAddress LOCALHOST;
    private static volatile ClientModel _instance;

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
        ClientModel result = _instance;
        if(result == null){
            synchronized (ClientModel.class) {
                result = _instance;
                if (result == null)
                    _instance = result = new ClientModel();
            }
        }
        return result;
    }

    private int PORT;
    private InetAddress HOST;

    private final ArrayList<ClientListener> listeners = new ArrayList<>();
    private ClientManager client;
    private Session session;
    private boolean run = false;

    /**
     * Default constructor, defaulting to port 1726 and LOCALHOST
     */
    private ClientModel() {
        this(1726, LOCALHOST);
    }

    /**
     * Full constructor
     * @param port Port to connect to
     * @param host Host to connect to
     */
    private ClientModel(int port, InetAddress host) {
        this.setHost(host);
        this.setPort(port);

        // Shutdown client on program shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(ClientModel.this.isRunning())
                ClientModel.this.shutdown();
        }));
    }

    /**
     * Called to start the client model and connect
     */
    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        try {
            client = ClientManager.createClient();
            session = client.connectToServer(ClientEndpoint.class, new URI("ws://" + HOST.getHostAddress() + ":" + PORT + "/ws"));

            this.run = true;
            this.notifyClientStarted();
        } catch(URISyntaxException e) {
            Log.w("Failed to connect to server (Invalid URI: " + e.getMessage() + ")", ClientModel.class);
        } catch (DeploymentException e) {
            Log.w("Failed to connect to server (Deployment error: " + e.getMessage() + ")", ClientModel.class);
        } catch (IOException e) {
            Log.w("Failed to connect to server (IOException: " + e.getMessage() + ")", ClientModel.class);
        }
    }

    /**
     * Called to disconnect the client from the server
     */
    public void shutdown() {
        if(!run)
            throw new IllegalArgumentException("Server is already stopped");

        try {
            session.close();
            client.shutdown();
        } catch(IOException e) {
            Log.w("Failed to shut down client gracefully", ClientModel.class);
        }

        session = null;
        client = null;
        run = false;
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
        Log.i("Client shutdown successfully", ClientModel.class);
        for(ClientListener listener : listeners) {
            listener.shutdown();
        }
    }

    /**
     * Method called when ClientModel is started up
     */
    private void notifyClientStarted() {
        Log.i("Client started and connected to server at " + HOST.getCanonicalHostName() + " port " + PORT, ClientModel.class);
        for(ClientListener listener : listeners) {
            listener.started();
        }
    }

    private void notifyValuesChanged() {
        for(ClientListener listener : listeners) {
            listener.valuesChanged();
        }
    }

    private void notifyValuesReset() {
        for(ClientListener listener : listeners) {
            listener.valuesReset();
        }
    }

    /**
     * Check if the client is running
     * @return boolean if client is running
     */
    public boolean isRunning() {
        return run;
    }

    /**
     * Setter for server's port
     * @param port Server's port
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
     * Changes the host (WILL shutdown if Client is running)
     * @param host The host to connect to
     */
    public void setHost(InetAddress host) {
        if(host == null)
            throw new IllegalArgumentException("Host must be non-null");
        else {
            // Shutdown if running
            if(this.isRunning())
                this.shutdown();

            HOST = host;
        }
    }

    /**
     * Sets the host to localhost
     */
    public void setHostToLocalhost() {
        this.setHost(LOCALHOST);
    }
}
