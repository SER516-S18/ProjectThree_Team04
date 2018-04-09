package team04.project3.model.server;

import org.glassfish.tyrus.server.Server;
import team04.project3.model.EmostatePacket;
import team04.project3.util.Log;

import javax.websocket.DeploymentException;

public class ServerWorker implements Runnable {
    private ServerModel model;
    private Server server;
    private ServerWebsocketEndpoint endpoint;
    private boolean run = false;

    public ServerWorker(ServerModel model) {
        this.model = model;
    }

    @Override
    public void run() {
        run = true;

        synchronized (this) {
            try {
                // Start server
                endpoint = new ServerWebsocketEndpoint();
                server = new Server("localhost", model.getPort(), "/ws", null, ServerWebsocketEndpoint.class);
                server.start();

                // Run the server continuously until notify
                wait();

                // Shut down the server
                endpoint.disconnect();
                long timeout = System.currentTimeMillis() + 1000L;
                while(endpoint.getClientsCount() > 0 && System.currentTimeMillis() < timeout) {
                    Thread.sleep(100L);
                }
                server.stop();
            } catch (DeploymentException e) {
                Log.e("Failed to deploy web socket server, shutting down (" + e.getMessage() + ")", ServerWorker.class);
                ServerModel.get().shutdown();
            } catch (InterruptedException e) {
                Log.e("Failed to wait due to interruption (" + e.getMessage() + ")", ServerWorker.class);
            }
        }

        run = false;
    }

    public boolean isRunning() {
        return run;
    }

    public void send(EmostatePacket packet) {
        if(packet == null)
            throw new IllegalArgumentException("Packet must be non-null");

        if(!this.isRunning() || endpoint == null)
            throw new RuntimeException("Worker is not running");

        endpoint.send(packet);
    }

    public void shutdown() {
        synchronized (this) {
            this.notify();
        }
    }

    public int getClientsCount() {
        return (endpoint == null ? 0 : endpoint.getClientsCount());
    }
}
