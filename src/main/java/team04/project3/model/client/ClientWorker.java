package team04.project3.model.client;

import org.glassfish.tyrus.client.ClientManager;
import team04.project3.util.Log;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Worker runnable for the ClientModel, keeping the websocket open
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientWorker implements Runnable {
    private ClientModel model;
    private ClientManager client;
    private ClientWebsocketEndpoint endpoint;
    private Session session;
    private boolean run = false;
    private boolean connected = false;

    /**
     * Creates a ClientWorker for the ClientModel
     * @param model Model (parent) to create for
     */
    public ClientWorker(ClientModel model) {
        this.model = model;
    }

    @Override
    public void run() {
        connected = false;
        run = true;

        synchronized (this) {
            try {
                // Start the client
                URI uri = new URI("ws://" + model.getHost().getHostAddress() + ":" + model.getPort() + "/ws/emostate");
                client = ClientManager.createClient();
                endpoint = new ClientWebsocketEndpoint();
                session = client.connectToServer(endpoint, uri);
                connected = true;

                // Run the client continuously
                wait();

                // Shutdown the client
                try {
                    session.close();
                    client.shutdown();
                } catch (IOException e) {
                    Log.w("Failed to shut down client gracefully", ClientModel.class);
                } finally {
                    connected = false;
                }
            } catch (URISyntaxException e) {
                Log.w("Failed to connect to server (Invalid URI: " + e.getMessage() + ")", ClientWorker.class);
            } catch (DeploymentException e) {
                Log.w("Failed to connect to server (Deployment error: " + e.getMessage() + ")", ClientWorker.class);
            } catch (InterruptedException e) {
                Log.w("Failed to wait due to interruption (" + e.getMessage() + ")", ClientWorker.class);
            } catch (IOException e) {
                Log.w("Failed to connect to server (IOException: " + e.getMessage() + ")", ClientWorker.class);
            }
        }

        run = false;
    }

    /**
     * Returns if the worker is running
     * @return If the worker is running
     */
    public boolean isRunning() {
        return run;
    }

    /**
     * Returns if client is connected successfully to the server
     * @return If the client is connected successfully
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Shuts down the worker
     */
    public void shutdown() {
        synchronized (this) {
            this.notify();
        }
    }
}
