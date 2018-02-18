package edu.asu.ser516.projecttwo.team04;

import edu.asu.ser516.projecttwo.team04.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientModel {
    private static InetAddress LOCALHOST;
    static {
        try {
            LOCALHOST = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOCALHOST = null;
            Log.e("Failed to get the default localhost (" + e.getMessage() + ")", ClientModel.class);
        }
    }

    private int PORT;
    private InetAddress HOST;

    private Socket socket;
    private boolean run = false;

    public ClientModel() {
        this(1516, LOCALHOST);
    }

    public ClientModel(int port, InetAddress host) {
        this.setHost(host);
        this.setPort(port);

        this.start();
    }

    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        try {
            run = true;
            socket = new Socket(HOST, PORT);
            new Thread(new ClientWorker(socket)).start();
            Log.i("Client started and connected to server at " + HOST.getCanonicalHostName() + " port " + PORT, ClientModel.class);
        } catch (IOException e) {
            Log.e("Failed to connect to server at " + HOST.getCanonicalHostName() + " port " + PORT + "(" + e.getMessage() + ")", ClientModel.class);
        }
    }

    public void shutdown() {
        if(!run)
            throw new IllegalArgumentException("Server is already stopped");

        try {
            socket.close();
        } catch (IOException e) {
            Log.w("Failed to close socket to server (" + e.getMessage() + ")", ServerModel.class);
        }

        run = false;
        socket = null;
        Log.i("Client shutdown successfully", ClientModel.class);
    }

    /**
     * Setter for server's port
     * @param port Server's port
     */
    public void setPort(int port) {
        if(port < 0)
            throw new IllegalArgumentException("Port must be greater than zero");
        PORT = port;
    }

    /**
     * Getter for server's port
     * @return port Server's port
     */
    public int getPort() {
        return PORT;
    }

    public void setHost(InetAddress host) {
        if(host == null)
            throw new IllegalArgumentException("Host must be non-null");
        else
            HOST = host;
    }

    /**
     * Sets the host to localhost
     */
    public void setHostToLocalhost() {
        this.setHost(LOCALHOST);
    }

    private class ClientWorker implements Runnable {
        private Scanner scannerIn;

        private ClientWorker(Socket socket) {
            try {
                scannerIn = new Scanner(socket.getInputStream());
            } catch (IOException e) {
                Log.w("Client failed to read input stream (" + e.getMessage() + ")", ClientModel.class);
            }
        }

        @Override
        public void run() {
            while(ClientModel.this.run && scannerIn.hasNext()) {
                int val = scannerIn.nextInt();
                Log.v("Client got " + val, ClientModel.class);
            }
        }
    }
}
