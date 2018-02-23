package team04.project2.model.client;

import team04.project2.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClientWorker - Class that contains the input listener and handler, and the output handler
 */
public class ClientWorker {
    private static final ExecutorService executors = Executors.newCachedThreadPool();

    private ObjectInputStream streamIn;
    private ObjectOutputStream streamOut;

    private ClientOutputHandler outputHandler;
    private ClientInputListener inputListener;
    private ClientInputHandler inputHandler;

    /**
     * ClientWorker
     * @param socket A worker to handle a connection to the server
     */
    public ClientWorker(Socket socket) {
        try {
            // Open streams, start handlers and listeners
            streamOut = new ObjectOutputStream(socket.getOutputStream());
            streamIn = new ObjectInputStream(socket.getInputStream());

            // Create output handler (output to server)
            outputHandler = new ClientOutputHandler(this, streamOut);
            executors.submit(outputHandler);

            // Create input listener (input from server, at server's frequency)
            inputListener = new ClientInputListener(this, streamIn);
            executors.submit(inputListener);

            // Create input handler (handles input from listener, at client's frequency)
            inputHandler = new ClientInputHandler(this);
            executors.submit(inputHandler);

            Log.v("ClientWorker has initialized all three threads", ClientWorker.class);
        } catch (IOException e) {
            Log.w("Client failed to read input stream (" + e.getMessage() + ")", ClientWorker.class);
        }
    }

    /**
     * isRunning - Considered running if all three listeners/handlers are running
     * @return If ClientWorker is running
     */
    public boolean isRunning() {
        return (outputHandler != null && outputHandler.isRunning() &&
                inputListener != null && inputListener.isRunning() &&
                inputHandler != null && inputHandler.isRunning());
    }

    /**
     * getInputListener Gets the client's input listener
     * @return Client's input listener
     */
    public ClientInputListener getInputListener() {
        return inputListener;
    }

    /**
     * getOutputHandler Gets the client's output handler
     * @return Client's output handler
     */
    public ClientOutputHandler getOutputHandler() {
        return outputHandler;
    }
}

