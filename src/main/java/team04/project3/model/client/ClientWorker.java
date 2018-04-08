package team04.project3.model.client;

import org.glassfish.tyrus.client.ClientManager;
import team04.project3.util.Log;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientWorker implements Runnable {
    private ClientModel model;
    private ClientManager client;
    private Session session;
    private boolean run = false;

    public ClientWorker(ClientModel model) {
        this.model = model;
    }

    @Override
    public void run() {
        run = true;

        synchronized (this) {
            try {
                // Start the client
                URI uri = new URI("ws://" + model.getHost().getHostAddress() + ":" + model.getPort() + "/ws/emostate");
                client = ClientManager.createClient();
                session = client.connectToServer(ClientWebsocketEndpoint.class, uri);

                // Run the client continuously
                wait();

                // Shutdown the client
                try {
                    session.close();
                    client.shutdown();
                } catch (IOException e) {
                    Log.w("Failed to shut down client gracefully", ClientModel.class);
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

    public boolean isRunning() {
        return run;
    }

    public void shutdown() {
        synchronized (this) {
            this.notify();
        }
    }
}
